package com.example.final_project.di

import com.example.final_project.data.repository.HistoryRepository
import com.example.final_project.data.repository.HistoryRepositoryImpl
import com.example.final_project.data.repository.ReplayRepository
import com.example.final_project.data.repository.ReplayRepositoryImpl
import com.example.final_project.data.repository.TranslationRepository
import com.example.final_project.data.repository.TranslationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTranslationRepository(
        impl: TranslationRepositoryImpl,
    ): TranslationRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        impl: HistoryRepositoryImpl,
    ): HistoryRepository

    @Binds @Singleton
    abstract fun bindReplayRepository(impl: ReplayRepositoryImpl): ReplayRepository

}
