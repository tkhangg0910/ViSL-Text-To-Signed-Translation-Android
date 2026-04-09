package com.example.final_project.data.local.db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translations",
    indices = [
        Index(value = ["created_at"]),
        Index(value = ["input_text"]),
    ],
)
data class TranslationEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    /** Original Input sentence. */
    @ColumnInfo(name = "input_text")
    val inputText: String,

    /**
     * String raw Dialect ("north" | "central" | "south").
     */
    @ColumnInfo(name = "dialect")
    val dialect: String,

    /**
     * Gloss dict from BE serialize to JSON String.
     * {"S":"tôi","V":"ra","O":"ủy ban","TIME":"chiều nay"}
     */
    @ColumnInfo(name = "gloss_json")
    val glossJson: String,

    /**
     * List of token after word segmentation.
     */
    @ColumnInfo(name = "tokens_json")
    val tokensJson: String,

    @ColumnInfo(name = "video_path")
    val videoPath: String?,


    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "is_starred")
    val isStarred: Boolean = false,
)