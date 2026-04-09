package com.example.final_project.di

import android.content.Context
import androidx.room.Room
import com.example.final_project.data.local.db.AppDatabase
import com.example.final_project.data.local.db.TranslationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "visl_database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DB_NAME,
    )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    @Singleton
    fun provideTranslationDao(db: AppDatabase): TranslationDao = db.translationDao()
}

