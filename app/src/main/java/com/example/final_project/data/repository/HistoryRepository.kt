package com.example.final_project.data.repository

import android.util.Log
import androidx.room.Query
import com.example.final_project.data.local.db.TranslationDao
import com.example.final_project.data.local.db.TranslationEntity
import com.example.final_project.data.local.storage.VideoStorage
import com.example.final_project.domain.model.Dialect
import com.example.final_project.domain.model.GlossRole
import com.example.final_project.domain.model.GlossToken
import com.example.final_project.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

private const val TAG = "HistoryRepo"

interface HistoryRepository {

    fun getAll(): Flow<List<HistoryItem>>

    fun search(query: String): Flow<List<HistoryItem>>

    fun getStarred(): Flow<List<HistoryItem>>

    fun countFlow(): Flow<Int>
    suspend fun toggleStar(id: Long, currentlyStarred: Boolean)

    suspend fun deleteById(id: Long)

    suspend fun deleteAll()
}

// Implementation

class HistoryRepositoryImpl @Inject constructor(
    private val translationDao: TranslationDao,
    private val videoStorage:   VideoStorage,
    private val json:           Json,
) : HistoryRepository {

    override fun getAll(): Flow<List<HistoryItem>> =
        translationDao.getAllOrderByDate().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun search(query: String): Flow<List<HistoryItem>> =
        translationDao.searchByText(query).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getStarred(): Flow<List<HistoryItem>> =
        translationDao.getStarred().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun countFlow(): Flow<Int> = translationDao.countFlow()

    override suspend fun toggleStar(id: Long, currentlyStarred: Boolean) {
        translationDao.updateStarred(id = id, starred = !currentlyStarred)
    }

    override suspend fun deleteById(id: Long) {
        val entity = translationDao.getById(id) ?: return

        entity.videoPath?.let { path ->
            val deleted = videoStorage.deleteVideo(path)
            if (!deleted) Log.w(TAG, "Could not delete video: $path")
        }
        translationDao.deleteById(id)
        Log.d(TAG, "Deleted translation id=$id")
    }

    override suspend fun deleteAll() {
        val count = videoStorage.deleteAll()
        Log.d(TAG, "Deleted $count video files")
        translationDao.deleteAll()
        Log.d(TAG, "Cleared all translations")
    }

    // Mapping

    private fun TranslationEntity.toDomain(): HistoryItem {
        val glossTokens = parseGlossJson(glossJson)
        val tokens      = parseTokensJson(tokensJson)
        val dialect     = Dialect.fromApiValue(dialect) ?: Dialect.CENTRAL

        return HistoryItem(
            id          = id,
            inputText   = inputText,
            dialect     = dialect,
            glossTokens = glossTokens,
            tokens      = tokens,
            videoPath   = videoPath,
            createdAt   = createdAt,
            isStarred   = isStarred,
        )
    }

    private fun parseGlossJson(glossJson: String): List<GlossToken> =
        runCatching {
            val obj = json.parseToJsonElement(glossJson).jsonObject
            buildList {
                obj["TIME"]?.jsonPrimitive?.content?.let  { add(GlossToken(GlossRole.TIME,  it)) }
                obj["S"]?.jsonPrimitive?.content?.let     { add(GlossToken(GlossRole.S,     it)) }
                obj["O"]?.jsonPrimitive?.content?.let     { add(GlossToken(GlossRole.O,     it)) }
                obj["V"]?.jsonPrimitive?.content?.let     { add(GlossToken(GlossRole.V,     it)) }
                obj["PLACE"]?.jsonPrimitive?.content?.let { add(GlossToken(GlossRole.PLACE, it)) }
            }
        }.getOrElse {
            Log.e(TAG, "Failed to parse glossJson: $glossJson", it)
            emptyList()
        }
    private fun parseTokensJson(tokensJson: String): List<String> =
        runCatching {
            json.parseToJsonElement(tokensJson)
                .jsonArray
                .map { it.jsonPrimitive.content }
        }.getOrElse {
            Log.e(TAG, "Failed to parse tokensJson: $tokensJson", it)
            emptyList()
        }
}
