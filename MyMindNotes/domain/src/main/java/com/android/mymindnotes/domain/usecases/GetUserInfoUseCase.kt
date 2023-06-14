package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
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