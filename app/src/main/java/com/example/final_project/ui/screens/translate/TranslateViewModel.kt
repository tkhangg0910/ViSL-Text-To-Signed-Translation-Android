package com.example.final_project.ui.screens.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.repository.ReplayRepository
import com.example.final_project.domain.model.Dialect
import com.example.final_project.domain.model.PipelineState
import com.example.final_project.domain.model.PipelineStep
import com.example.final_project.domain.model.ReplayState
import com.example.final_project.domain.model.StepStatus
import com.example.final_project.domain.model.TranslationResult
import com.example.final_project.domain.usecase.TranslateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val replayRepository: ReplayRepository,
) : ViewModel() {
    private val _pipelineState = MutableStateFlow<PipelineState>(PipelineState.Idle)
    val pipelineState: StateFlow<PipelineState> = _pipelineState.asStateFlow()
    private val _replayInputText = MutableStateFlow<String?>(null)
    val replayInputText: StateFlow<String?> = _replayInputText.asStateFlow()

    private val _replayDialect = MutableStateFlow<Dialect?>(null)
    val replayDialect: StateFlow<Dialect?> = _replayDialect.asStateFlow()
    private var _cachedReplay: ReplayState? = null

    fun checkPendingReplay() {
        val replay = replayRepository.consumeReplay() ?: return
        _cachedReplay = replay
        _replayInputText.value = replay.inputText
        _replayDialect.value   = replay.dialect
    }
    fun consumeReplayFull(): ReplayState? {
        return _cachedReplay
    }
    fun onReplayConsumed() {
        _cachedReplay          = null
        _replayInputText.value = null
        _replayDialect.value   = null
    }


    fun translate(text: String, dialect: Dialect, topK: Int) {
        viewModelScope.launch {
            translateUseCase(text = text, dialect = dialect, topK = topK)
                .collect { state ->
                    _pipelineState.value = state
                }
        }
    }
    // TranslateViewModel.kt

    fun fakeTranslate() {
        viewModelScope.launch {
            val steps = listOf(
                PipelineStep(1, "Receive input",
                    StepStatus.WAITING),
                PipelineStep(2, "Normalize sentence",
                    StepStatus.WAITING),
                PipelineStep(3, "Gloss translation",
                    StepStatus.WAITING),
                PipelineStep(4, "Word segmentation",
                    StepStatus.WAITING),
                PipelineStep(5, "Embedding + Vector DB",
                    StepStatus.WAITING),
                PipelineStep(6, "Pose generation & render",
                    StepStatus.WAITING),
            )
            val mutable = steps.toMutableList()
            _pipelineState.value = PipelineState.Loading(mutable.toList())

            for (i in mutable.indices) {
                delay(600)
                mutable[i] = mutable[i].copy(status = StepStatus.RUNNING)
                _pipelineState.value = PipelineState.Loading(mutable.toList())

                delay(800)
                mutable[i] = mutable[i].copy(status = StepStatus.DONE)
                _pipelineState.value = PipelineState.Loading(mutable.toList())
            }

            _pipelineState.value = PipelineState.Success(
                result = TranslationResult(
                    id = -1,
                    inputText = "debug",
                    dialect = Dialect.NORTH,
                    glossTokens = emptyList(),
                    tokens = emptyList(),
                    videoPath = null,
                )
            )
        }
    }
}