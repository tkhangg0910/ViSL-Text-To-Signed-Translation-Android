package com.example.final_project.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_project.data.repository.ReplayRepository
import com.example.final_project.domain.model.HistoryItem
import com.example.final_project.domain.model.ReplayState
import com.example.final_project.domain.usecase.GetHistoryUseCase
import com.example.final_project.domain.usecase.ManageHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

sealed class HistoryUiState {

    data object Loading : HistoryUiState()

    data class Loaded(
        val groupedItems:  Map<String, List<HistoryItem>>,
        val searchQuery:   String  = "",
        val totalCount:    Int     = 0,
        val pendingDelete: Long?   = null,
    ) : HistoryUiState() {
        val isEmpty: Boolean get() = groupedItems.values.all { it.isEmpty() }
    }
}

sealed class HistoryEvent {
    data class ShowMessage(val message: String)   : HistoryEvent()
    data class NavigateToTranslate(val text: String) : HistoryEvent()
}


@OptIn(
    kotlinx.coroutines.FlowPreview::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class
)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val manageHistoryUseCase: ManageHistoryUseCase,
    private val replayRepository: ReplayRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _events = MutableStateFlow<HistoryEvent?>(null)
    val events: StateFlow<HistoryEvent?> = _events

    private val _showStarredOnly = MutableStateFlow(false)
    val showStarredOnly: StateFlow<Boolean> = _showStarredOnly
    val count: StateFlow<Int> = manageHistoryUseCase.count()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun onToggleStarFilter() {
        _showStarredOnly.value = !_showStarredOnly.value
    }

    val uiState: StateFlow<HistoryUiState> = combine(
        _searchQuery.debounce(300L),
        _showStarredOnly
    ) { query, starredOnly ->
        Pair(query, starredOnly)
    }.flatMapLatest { (query, starredOnly) ->
        if (starredOnly) {
            getHistoryUseCase.getStarred().map { items ->
                val filtered = if (query.isBlank()) items
                else items.filter { it.inputText.contains(query, ignoreCase = true) }
                HistoryUiState.Loaded(
                    groupedItems = filtered.groupByDateLabel(),
                    searchQuery  = query,
                    totalCount   = filtered.size,
                )
            }
        } else {
            getHistoryUseCase.search(query).map { groupedItems ->
                HistoryUiState.Loaded(
                    groupedItems = groupedItems,
                    searchQuery  = query,
                    totalCount   = groupedItems.values.sumOf { it.size },
                )
            }
        }
    }.stateIn(
        scope        = viewModelScope,
        started      = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HistoryUiState.Loading,
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onClearSearch() {
        _searchQuery.value = ""
    }

    fun onToggleStar(item: HistoryItem) {
        viewModelScope.launch {
            manageHistoryUseCase.toggleStar(
                id               = item.id,
                currentlyStarred = item.isStarred,
            )
            val msg = if (item.isStarred) "unstared" else "Stared"
            sendEvent(HistoryEvent.ShowMessage(msg))
        }
    }


    fun onDeleteItem(item: HistoryItem) {
        viewModelScope.launch {
            manageHistoryUseCase.deleteItem(item.id)
            sendEvent(HistoryEvent.ShowMessage("Deleted"))
        }
    }
    fun onDeleteAll() {
        viewModelScope.launch {
            manageHistoryUseCase.deleteAll()
            sendEvent(HistoryEvent.ShowMessage("Deleted All"))
        }
    }


    fun onPlayAgain(item: HistoryItem) {
        replayRepository.setReplay(
            ReplayState(
                uriPath = item.videoPath,
                glossTokens = item.glossTokens,
                dialect = item.dialect,
                inputText = item.inputText,
            )
        )
        sendEvent(HistoryEvent.NavigateToTranslate(item.inputText))
    }
    fun onEventConsumed() {
        _events.value = null
    }

    private fun sendEvent(event: HistoryEvent) {
        _events.value = event
    }
    private fun List<HistoryItem>.groupByDateLabel(): Map<String, List<HistoryItem>> {
        val now       = Calendar.getInstance()
        val today     = now.toDateOnly()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.toDateOnly()
        val weekAgo   = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }.toDateOnly()

        return groupBy { item ->
            val itemDate = Calendar.getInstance()
                .apply { timeInMillis = item.createdAt }
                .toDateOnly()
            when {
                itemDate == today     -> "Today"
                itemDate == yesterday -> "Yesterday"
                itemDate >= weekAgo   -> "This Week"
                else                  -> "Before"
            }
        }
    }

    private fun Calendar.toDateOnly(): Calendar = (clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }


}