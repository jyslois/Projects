package com.android.mymindnotes.hilt.module.repository

import com.android.mymindnotes.data.repositoryImpl.TodayDiaryRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.TodayDiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDiaryRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTodayDiaryRepository (
        todayDiaryRepositoryImpl: TodayDiaryRepositoryImpl
    ): TodayDiaryRepository
}