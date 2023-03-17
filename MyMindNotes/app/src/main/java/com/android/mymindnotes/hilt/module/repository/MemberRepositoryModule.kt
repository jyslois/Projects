package com.android.mymindnotes.hilt.module.repository

import com.android.mymindnotes.data.repositoryImpl.MemberRepositoryImpl
import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // // 결합의 범위는 결합이 설치된 구성요소의 범위와 일치해야 하므로 SingletonComponent에 설치해야 한다
abstract class MemberRepositoryModule {
    @Singleton
    @Binds // Repository 인스턴스를 생성할 때 같은 주소를 가르키도록 설정하여 중복 생성을 막기 위해서 사용
    abstract fun bindMemberRepository (
        memberRepositoryImpl: MemberRepositoryImpl
    ) : MemberRepository
}