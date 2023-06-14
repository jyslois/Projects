package com.android.mymindnotes.hilt.modules.repository

import com.android.mymindnotes.data.repositoryImpls.DiaryRepositoryImpl
import com.android.mymindnotes.data.repositoryInterfaces.DiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiaryRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindDiaryRepository (
        diaryRepositoryImpl: DiaryRepositoryImpl
    ): DiaryRepository
}