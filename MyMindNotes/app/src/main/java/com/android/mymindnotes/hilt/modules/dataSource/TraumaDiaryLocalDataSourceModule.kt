package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.TraumaDiaryLocalDataSource
import com.android.mymindnotes.data.dataSources.TraumaDiaryLocalDataSourceInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TraumaDiaryLocalDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindTraumaDiaryLocalDataSource (
        traumaDiaryLocalDataSource: TraumaDiaryLocalDataSource
    ): TraumaDiaryLocalDataSourceInterface
}