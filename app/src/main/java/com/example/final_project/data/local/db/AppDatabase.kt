package com.example.final_project.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TranslationEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}

