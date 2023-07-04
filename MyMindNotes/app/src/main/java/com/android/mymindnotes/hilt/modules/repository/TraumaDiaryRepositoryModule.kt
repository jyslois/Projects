package com.android.mymindnotes.hilt.modules.repository

import com.android.mymindnotes.data.repositoryImpls.TraumaDiaryRemoteRemoteRepositoryImpl
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRemoteRepository
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
        traumaDiaryRemoteRepositoryImpl: TraumaDiaryRemoteRemoteRepositoryImpl
    ): TraumaDiaryRemoteRepository
}