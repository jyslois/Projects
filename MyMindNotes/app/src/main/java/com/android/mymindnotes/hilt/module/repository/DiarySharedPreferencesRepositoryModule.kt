package com.android.mymindnotes.hilt.module.repository

import com.android.mymindnotes.data.repositoryImpl.DiarySharedPreferencesRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.DiarySharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiarySharedPreferencesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindDiarySharedPreferencesRepository(
        sharedPreferencesRepositoryImpl: DiarySharedPreferencesRepositoryImpl
    ): DiarySharedPreferencesRepository
}