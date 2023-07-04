package com.android.mymindnotes.hilt.modules.repository

import com.android.mymindnotes.data.repositoryImpls.TraumaDiaryRemoteRepositoryImpl
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TraumaDiaryRemoteRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTraumaDiaryRemoteRepository (
        traumaDiaryRemoteRepositoryImpl: TraumaDiaryRemoteRepositoryImpl
    ): TraumaDiaryRemoteRepository
}