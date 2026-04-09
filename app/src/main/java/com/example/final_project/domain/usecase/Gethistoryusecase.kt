package com.example.final_project.domain.usecase

import com.example.final_project.data.repository.HistoryRepository
import com.example.final_project.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
) {
    fun getAllGrouped(): Flow<Map<String, List<HistoryItem>>> =
        repository.getAll().map { items ->
            items.groupByDateLabel()
        }

    fun search(query: String): Flow<Map<String, List<HistoryItem>>> {
        val trimmed = query.trim()
        return if (trimmed.isEmpty()) {
            getAllGrouped()
        } else {
            repository.search(trimmed).map { items ->
                items.groupByDateLabel()
            }
        }
    }


    fun getStarred(): Flow<List<HistoryItem>> = repository.getStarred()

    // Private grouping logic

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