package com.android.mymindnotes.hilt.module.api

import com.android.mymindnotes.data.retrofit.CheckEmailApi
import com.android.mymindnotes.data.retrofit.CheckNickNameApi
import com.android.mymindnotes.data.retrofit.RetrofitService
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
