package com.android.mymindnotes.hilt.module.repository

import com.android.mymindnotes.data.repositoryImpl.TraumaDiaryRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.TraumaDiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TraumaDiaryRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTraumaDiaryRepository (
        traumaDiaryRepositoryImpl: TraumaDiaryRepositoryImpl
    ): TraumaDiaryRepository
}