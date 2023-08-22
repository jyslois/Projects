package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val saveUserIndexUseCase: SaveUserIndexUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
) {


    suspend operator fun invoke(email: String, password: String, isAutoLoginChecked: Boolean, isAutoSaveChecked: Boolean): Flow<Result<String?>> {

        return flow {
            try {
                val response = memberRepository.login(email, password).first()

                when (response.code) {
                    5001, 5003, 5005 -> emit(Result.failure(RuntimeException(response.msg)))
                    5000 -> {
                        // 로그인 성공
                        if (isAutoLoginChecked) {
                            // MainActivity에서의 자동 로그인을 위한 상태 저장
                            saveAutoLoginStateUseCase(true)
                        }

                        if (isAutoSaveChecked) {
                            saveAutoSaveStateUseCase(true)
                            saveIdAndPasswordUseCase(email, password)
                        } else {
                            saveAutoSaveStateUseCase(false)
                            saveIdAndPasswordUseCase(null, null)
                        }

                        // 회원 번호 저장
                        response.userIndex?.let { saveUserIndexUseCase(it) }
                        emit(Result.success(response.msg))
                    }
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException(e.message ?: "로그인에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }

}