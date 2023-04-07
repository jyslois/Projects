package com.android.mymindnotes.domain.usecase

import android.util.Log
import com.android.mymindnotes.domain.repositoryinterfaces.MemberRepository
import com.android.mymindnotes.hilt.module.MainDispatcherCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    @MainDispatcherCoroutineScope private val mainDispatcherCoroutineScope: CoroutineScope
) {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>(replay = 1)
    val error = _error.asSharedFlow()


    // 회원 정보 불러오기
    suspend fun getUserInfo(): Flow<Map<String, Object>> = memberRepository.getUserInfo()


    init {
        mainDispatcherCoroutineScope.launch {
            launch {
                // error collect & emit
                memberRepository.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

}