package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.TodayDiaryRemoteDataSource
import com.android.mymindnotes.data.dataSources.TodayDiaryRemoteDataSourceInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDiaryRemoteDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindTodayDiaryRemoteDataSource(
        todayDiaryRemoteDataSource: TodayDiaryRemoteDataSource
    ): TodayDiaryRemoteDataSourceInterface
}