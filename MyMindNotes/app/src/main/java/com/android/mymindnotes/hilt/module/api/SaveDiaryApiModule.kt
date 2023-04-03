package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.diary.SaveDiaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SaveDiaryApiModule {
    @Provides
    @Singleton
    fun provideSaveDiaryApi(
        retrofitService: RetrofitService
    ): SaveDiaryApi {
        return retrofitService.retrofit.create(SaveDiaryApi::class.java)
    }
}