package com.android.mymindnotes.hilt.modules.repository

import com.android.mymindnotes.data.repositoryImpls.TodayDiaryRemoteRepositoryImpl
import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDiaryRemoteRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTodayDiaryRemoteRepository (
        todayDiaryRemoteRepositoryImpl: TodayDiaryRemoteRepositoryImpl
    ): TodayDiaryRemoteRepository
}