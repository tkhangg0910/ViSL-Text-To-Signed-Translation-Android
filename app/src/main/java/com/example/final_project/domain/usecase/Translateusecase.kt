package com.example.final_project.domain.usecase

import com.example.final_project.data.repository.TranslationRepository
import com.example.final_project.domain.model.Dialect
import com.example.final_project.domain.model.PipelineState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val repository: TranslationRepository,
) {
    operator fun invoke(
        text:    String,
        dialect: Dialect,
        topK:    Int = DEFAULT_TOP_K,
    ): Flow<PipelineState> {
        val trimmed = text.trim()

        // Validation
        if (trimmed.isEmpty()) {
            return flow {
                emit(PipelineState.Error(message = "Please input something."))
            }
        }

        if (trimmed.length > MAX_LENGTH) {
            return flow {
                emit(
                    PipelineState.Error(
                        message = "Sentence is too long. Max Length $MAX_LENGTH (Now ${trimmed.length})."
                    )
                )
            }
        }

        // Delegate
        return repository.translate(
            text    = trimmed,
            dialect = dialect,
            topK    = topK,
        )
    }

    companion object {
        const val MAX_LENGTH     = 500
        const val DEFAULT_TOP_K  = 5
    }
}
