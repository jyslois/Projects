package com.android.mymindnotes.hilt.modules.dataSource

import com.android.mymindnotes.data.dataSources.TraumaDiaryRemoteDataSource
import com.android.mymindnotes.data.dataSources.TraumaDiaryRemoteDataSourceInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TraumaDiaryRemoteDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindTraumaDiaryRemoteDataSource (
        traumaDiaryRemoteDataSource: TraumaDiaryRemoteDataSource
    ): TraumaDiaryRemoteDataSourceInterface
}