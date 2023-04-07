package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.GetDiaryListApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetDiaryListApiModule {
    @Provides
    @Singleton
    fun provideGetDiaryListApi(
        retrofitService: RetrofitService
    ): GetDiaryListApi {
        return retrofitService.retrofit.create(GetDiaryListApi::class.java)
    }
}