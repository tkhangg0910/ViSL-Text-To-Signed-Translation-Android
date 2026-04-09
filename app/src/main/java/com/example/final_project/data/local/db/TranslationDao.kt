package com.example.final_project.data.local.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {

    // Insert
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: TranslationEntity): Long

    // Read
    @Query("SELECT * FROM translations ORDER BY created_at DESC")
    fun getAllOrderByDate(): Flow<List<TranslationEntity>>

    @Query(
        """
        SELECT * FROM translations
        WHERE input_text LIKE '%' || :query || '%'
        ORDER BY created_at DESC
        """,
    )
    fun searchByText(query: String): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE is_starred = 1 ORDER BY created_at DESC")
    fun getStarred(): Flow<List<TranslationEntity>>

    @Query("SELECT * FROM translations WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TranslationEntity?

    // Update
    @Query("UPDATE translations SET is_starred = :starred WHERE id = :id")
    suspend fun updateStarred(id: Long, starred: Boolean)


    @Query("UPDATE translations SET video_path = :path WHERE id = :id")
    suspend fun updateVideoPath(id: Long, path: String)

    // Delete
    @Query("DELETE FROM translations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM translations")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM translations")
    fun countFlow(): Flow<Int>
}
