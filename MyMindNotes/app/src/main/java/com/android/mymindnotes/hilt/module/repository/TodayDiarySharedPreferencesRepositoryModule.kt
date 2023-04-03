package com.android.mymindnotes.hilt.module.repository

import com.android.mymindnotes.data.repositoryImpl.TodayDiarySharedPreferencesRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.TodayDiarySharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDiarySharedPreferencesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindDiarySharedPreferencesRepository(
        sharedPreferencesRepositoryImpl: TodayDiarySharedPreferencesRepositoryImpl
    ): TodayDiarySharedPreferencesRepository
}