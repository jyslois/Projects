package com.android.mymindnotes.hilt.module.repository

import com.android.mymindnotes.data.repositoryImpl.TraumaDiarySharedPreferencesRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.TraumaDiarySharedPreferencesRepository
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
        sharedPreferencesRepositoryImpl: TraumaDiarySharedPreferencesRepositoryImpl
    ): TraumaDiarySharedPreferencesRepository
}