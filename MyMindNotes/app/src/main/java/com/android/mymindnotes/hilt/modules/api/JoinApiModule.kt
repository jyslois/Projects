package com.android.mymindnotes.hilt.modules.api

import com.android.mymindnotes.data.retrofit.api.user.JoinApi
import com.android.mymindnotes.core.network.RetrofitService
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