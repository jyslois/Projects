package com.android.mymindnotes.hilt.modules.repository

import com.android.mymindnotes.data.repositoryImpls.TraumaDiaryLocalRepositoryImpl
import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryLocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TraumaDiarySharedPreferencesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTraumaDiarySharedPreferencesRepository(
        sharedPreferencesRepositoryImpl: TraumaDiaryLocalRepositoryImpl
    ): TraumaDiaryLocalRepository
}