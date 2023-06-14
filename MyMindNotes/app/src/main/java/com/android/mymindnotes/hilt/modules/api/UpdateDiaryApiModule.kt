package com.android.mymindnotes.hilt.modules.api

import com.android.mymindnotes.core.network.RetrofitService
import com.android.mymindnotes.data.retrofit.api.diary.UpdateDiaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpdateDiaryApiModule {
    @Provides
    @Singleton
    fun provideUpdateDiaryApi(
        retrofitService: RetrofitService
    ): UpdateDiaryApi {
        return retrofitService.retrofit.create(UpdateDiaryApi::class.java)
    }
}