package com.android.mymindnotes.hilt.modules.api

import com.android.mymindnotes.data.retrofit.api.user.CheckEmailApi
import com.android.mymindnotes.data.retrofit.api.user.CheckNickNameApi
import com.android.mymindnotes.core.network.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DuplicateCheckApiModule {
    @Provides
    @Singleton
    fun provideCheckEmailApi (
        retrofitService: RetrofitService
    ): CheckEmailApi {
        return retrofitService.retrofit.create(CheckEmailApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCheckNickNameApi (
        retrofitService: RetrofitService
    ): CheckNickNameApi {
        return retrofitService.retrofit.create(CheckNickNameApi::class.java)
    }
}
