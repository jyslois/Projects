package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.CheckEmailApi
import com.android.mymindnotes.data.retrofit.JoinApi
import com.android.mymindnotes.data.retrofit.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JoinApiModule {
    @Provides
    @Singleton
    fun provideJoinApi (
        retrofitService: RetrofitService
    ): JoinApi {
        return retrofitService.retrofit.create(JoinApi::class.java)
    }
}