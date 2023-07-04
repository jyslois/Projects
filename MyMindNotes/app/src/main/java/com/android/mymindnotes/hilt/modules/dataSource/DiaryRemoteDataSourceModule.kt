package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.DiaryRemoteDataSourceInterface
import com.android.mymindnotes.data.dataSources.DiaryRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiaryRemoteDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindDiaryRemoteDataSource (
        diaryRemoteDataSource: DiaryRemoteDataSource
    ) : DiaryRemoteDataSourceInterface
}