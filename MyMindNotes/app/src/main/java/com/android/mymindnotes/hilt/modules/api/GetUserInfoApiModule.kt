package com.android.mymindnotes.hilt.modules.api

import com.android.mymindnotes.core.network.RetrofitService
import com.android.mymindnotes.data.retrofit.api.user.GetUserInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetUserInfoApiModule {
    @Provides
    @Singleton
    fun provideGetUserInfoApi(
        retrofitService: RetrofitService
    ): GetUserInfoApi {
        return retrofitService.retrofit.create(GetUserInfoApi::class.java)
    }
}