package com.android.mymindnotes.hilt.module

import com.android.mymindnotes.data.repositoryImpl.SharedPreferencesRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.SharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // // 결합의 범위는 결합이 설치된 구성요소의 범위와 일치해야 하므로 SingletonComponent에 설치해야 한다.
abstract class SharedPreferencesRepositoryModule {
    @Singleton // Repository 인스턴스를 생성할 때 같은 주소를 가르키도록 설정하여 중복 생성을 막기 위해서 사용
    @Binds
    abstract fun bindDiaryRepository(
        sharedPreferencesRepositoryImpl: SharedPreferencesRepositoryImpl
    ): SharedPreferencesRepository
}