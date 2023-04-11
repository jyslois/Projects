package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.diary.DeleteDiaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DeleteDiaryApiModule {
    @Provides
    @Singleton
    fun provideDeleteDiaryApi(
        retrofitService: RetrofitService
    ): DeleteDiaryApi {
        return retrofitService.retrofit.create(DeleteDiaryApi::class.java)
    }
}