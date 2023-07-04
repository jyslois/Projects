package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.TodayDiaryLocalDataSource
import com.android.mymindnotes.data.dataSources.TodayDiaryLocalDataSourceInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDiaryLocalDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindTodayDiaryLocalDataSource (
        todayDiaryLocalDataSource: TodayDiaryLocalDataSource
    ): TodayDiaryLocalDataSourceInterface
}