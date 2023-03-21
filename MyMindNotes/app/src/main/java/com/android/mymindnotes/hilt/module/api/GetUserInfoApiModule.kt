package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.GetUserInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetUserInfoApiModule {
    @Provides
    @Singleton
    fun providegetUserInfoApi(
        retrofitService: RetrofitService
    ): GetUserInfoApi {
        return retrofitService.retrofit.create(GetUserInfoApi::class.java)
    }
}