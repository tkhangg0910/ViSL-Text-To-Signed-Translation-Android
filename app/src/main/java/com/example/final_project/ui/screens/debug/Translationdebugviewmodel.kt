package com.example.final_project.ui.screens.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.domain.model.Dialect
import com.example.final_project.domain.model.PipelineState
import com.example.final_project.domain.usecase.TranslateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationDebugViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
) : ViewModel() {

    private val _pipelineState = MutableStateFlow<PipelineState>(PipelineState.Idle)
    val pipelineState: StateFlow<PipelineState> = _pipelineState.asStateFlow()

    fun translate(text: String, dialect: Dialect, topK: Int) {
        viewModelScope.launch {
            translateUseCase(text = text, dialect = dialect, topK = topK)
                .collect { state ->
                    _pipelineState.value = state
                }
        }
    }
}