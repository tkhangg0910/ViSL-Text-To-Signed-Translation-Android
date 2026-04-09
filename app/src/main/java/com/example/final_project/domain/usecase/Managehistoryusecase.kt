package com.example.final_project.domain.usecase

import com.example.final_project.data.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
) {

    suspend fun toggleStar(id: Long, currentlyStarred: Boolean) {
        repository.toggleStar(id = id, currentlyStarred = currentlyStarred)
    }

    suspend fun deleteItem(id: Long) {
        repository.deleteById(id)
    }


    suspend fun deleteAll() {
        repository.deleteAll()
    }
    fun count(): Flow<Int> = repository.countFlow()
}