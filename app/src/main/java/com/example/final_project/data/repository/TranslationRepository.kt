package com.example.final_project.data.repository

import android.util.Base64
import android.util.Log
import com.example.final_project.data.local.db.TranslationDao
import com.example.final_project.data.local.db.TranslationEntity
import com.example.final_project.data.local.storage.VideoStorage
import com.example.final_project.data.remote.api.ViSLApiService
import com.example.final_project.data.remote.model.SseEvent
import com.example.final_project.data.remote.model.TranslateRequestDto
import com.example.final_project.domain.model.Dialect
import com.example.final_project.domain.model.GlossToken
import com.example.final_project.domain.model.GlossRole
import com.example.final_project.domain.model.PipelineState
import com.example.final_project.domain.model.PipelineStep
import com.example.final_project.domain.model.StepStatus
import com.example.final_project.domain.model.TranslationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val TAG = "TranslationRepo"

interface TranslationRepository {
    fun translate(
        text: String,
        dialect: Dialect,
        topK: Int = 5,
    ): Flow<PipelineState>
}

// Implementation

class TranslationRepositoryImpl @Inject constructor(
    private val apiService:     ViSLApiService,
    private val translationDao: TranslationDao,
    private val videoStorage:   VideoStorage,
    private val json:           Json,
) : TranslationRepository {

    override fun translate(
        text:    String,
        dialect: Dialect,
        topK:    Int,
    ): Flow<PipelineState> = flow {

        val steps = buildInitialSteps()
        emit(PipelineState.Loading(steps = steps))

        val request = TranslateRequestDto(
            text    = text,
            dialect = dialect.apiValue,
            topK    = topK,
        )

        val mutableSteps = steps.toMutableList()

        runCatching {
            apiService.translateStream(request).collect { event ->
                when (event) {

                    // step_start: mark step is RUNNING
                    is SseEvent.StepStart -> {
                        mutableSteps.updateStatus(event.step, StepStatus.RUNNING)
                        emit(PipelineState.Loading(steps = mutableSteps.toList()))
                        Log.d(TAG, "Step ${event.step} started: ${event.name}")
                    }

                    // step_done: mark step DONE
                    is SseEvent.StepDone -> {
                        mutableSteps.updateStatus(event.step, StepStatus.DONE)
                        emit(PipelineState.Loading(steps = mutableSteps.toList()))
                        Log.d(TAG, "Step ${event.step} done: ${event.name}")
                    }

                    // step_error: mark ERROR, stop streaming
                    is SseEvent.StepError -> {
                        mutableSteps.updateStatus(event.step, StepStatus.ERROR)
                        emit(
                            PipelineState.Error(
                                message = "Lỗi bước ${event.step} (${event.name}): ${event.error}",
                                steps   = mutableSteps.toList(),
                            )
                        )
                        Log.e(TAG, "Step ${event.step} error: ${event.error}")
                        return@collect
                    }

                    // complete: save video + DB, emit Success
                    is SseEvent.Complete -> {
                        val result = handleComplete(
                            event   = event,
                            text    = text,
                            dialect = dialect,
                        )
                        emit(PipelineState.Success(result = result))
                        Log.d(TAG, "Pipeline complete, videoPath=${result.videoPath}")
                    }

                    is SseEvent.Unknown -> {
                        Log.w(TAG, "Unknown SSE event: ${event.rawEvent}")
                    }
                }
            }
        }.onFailure { throwable ->
            Log.e(TAG, "Pipeline failed", throwable)
            emit(
                PipelineState.Error(
                    message = throwable.message ?: "Undefined Connection Error",
                    steps   = mutableSteps.toList(),
                )
            )
        }
    }

    // Private helpers

    private suspend fun handleComplete(
        event:   SseEvent.Complete,
        text:    String,
        dialect: Dialect,
    ): TranslationResult {

        val videoBytes = Base64.decode(event.videoB64, Base64.DEFAULT)

        val videoPath = videoStorage.saveVideoToDownloads(videoBytes)
        if (videoPath == null) {
            Log.w(TAG, "Video save failed — result will have null videoPath")
        }

        val glossTokens = buildGlossTokens(event)

        val glossJson = json.encodeToString(
            mapOf(
                "S"     to event.gloss.subject,
                "V"     to event.gloss.verb,
                "O"     to event.gloss.obj,
                "TIME"  to event.gloss.time,
                "PLACE" to event.gloss.place,
            )
                .filterValues { it != null }
                .mapValues { it.value!! }
        )
        val tokensJson = json.encodeToString(event.tokens)

        val entity = TranslationEntity(
            inputText  = text,
            dialect    = dialect.apiValue,
            glossJson  = glossJson,
            tokensJson = tokensJson,
            videoPath  = videoPath,
        )
        val insertedId = translationDao.insert(entity)
        Log.d(TAG, "Inserted translation id=$insertedId")

        return TranslationResult(
            id          = insertedId,
            inputText   = text,
            dialect     = dialect,
            glossTokens = glossTokens,
            tokens      = event.tokens,
            videoPath   = videoPath,
        )
    }


    private fun buildGlossTokens(event: SseEvent.Complete): List<GlossToken> =
        buildList {
            event.gloss.time?.let    { add(GlossToken(role = GlossRole.TIME,  label = it)) }
            event.gloss.subject?.let { add(GlossToken(role = GlossRole.S,     label = it)) }
            event.gloss.obj?.let     { add(GlossToken(role = GlossRole.O,     label = it)) }
            event.gloss.verb?.let    { add(GlossToken(role = GlossRole.V,     label = it)) }
            event.gloss.place?.let   { add(GlossToken(role = GlossRole.PLACE, label = it)) }
        }


    private fun buildInitialSteps(): List<PipelineStep> = listOf(
        PipelineStep(index = 1, name = "Receive input",               status = StepStatus.WAITING),
        PipelineStep(index = 2, name = "Normalize sentence",        status = StepStatus.WAITING),
        PipelineStep(index = 3, name = "Gloss translation",         status = StepStatus.WAITING),
        PipelineStep(index = 4, name = "Word segmentation",         status = StepStatus.WAITING),
        PipelineStep(index = 5, name = "Embedding + Vector DB",     status = StepStatus.WAITING),
        PipelineStep(index = 6, name = "Pose generation & render",  status = StepStatus.WAITING),
    )
}

// Extension

private fun MutableList<PipelineStep>.updateStatus(stepIndex: Int, status: StepStatus) {
    val i = indexOfFirst { it.index == stepIndex }
    if (i >= 0) this[i] = this[i].copy(status = status)
}
