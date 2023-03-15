package com.android.mymindnotes.hilt.module

import com.android.mymindnotes.data.retrofit.LoginApi
import com.android.mymindnotes.data.retrofit.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LogInApiModule {
    @Provides
    @Singleton
    fun provideLogInApi(
        retrofitService: RetrofitService
    ): LoginApi {
        return retrofitService.retrofit.create(LoginApi::class.java)
    }
}