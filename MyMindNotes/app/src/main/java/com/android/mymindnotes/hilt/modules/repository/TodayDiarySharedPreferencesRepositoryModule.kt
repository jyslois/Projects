package com.android.mymindnotes.hilt.modules.repository

import com.android.mymindnotes.data.repositoryImpls.TodayDiaryLocalRepositoryImpl
import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryLocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayDiarySharedPreferencesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindDiarySharedPreferencesRepository(
        sharedPreferencesRepositoryImpl: TodayDiaryLocalRepositoryImpl
    ): TodayDiaryLocalRepository
}