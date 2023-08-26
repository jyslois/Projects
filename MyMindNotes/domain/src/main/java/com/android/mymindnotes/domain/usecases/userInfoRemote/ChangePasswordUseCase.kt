package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val savePasswordUseCase: SavePasswordUseCase
) {
    suspend operator fun invoke(password: String, originalPassword: String): Flow<Result<String?>> {

        return flow {
            try {
                val response = memberRepository.changePassword(password, originalPassword).first()
                when (response.code) {
                    3005, 3003 -> emit(Result.failure(RuntimeException(response.msg)))
                    3002 -> {
                        savePasswordUseCase(password)
                        emit(Result.success(response.msg))
                    }
                }
            } catch(e: Exception) {
                    emit(Result.failure(RuntimeException("비밀번호 변경 실패. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }

}

