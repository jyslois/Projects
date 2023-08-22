package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JoinUseCase @Inject constructor(
    private val memberRepository: MemberRepository,

    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val saveFirstTimeStateUseCase: SaveFirstTimeStateUseCase,
    private val saveUserIndexUseCase: SaveUserIndexUseCase,
    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
) {

    suspend operator fun invoke(email: String, nickname: String, password: String, birthyear: Int): Flow<Result<String?>> {

        return flow {
            try {
                val response = memberRepository.join(email, nickname, password, birthyear).first() // JoinResponse

                when (response.code) {
                    2001 -> emit(Result.failure(RuntimeException(response.msg)))
                    2000 -> {
                        // 회원 번호 저장
                        response.userIndex?.let { saveUserIndexUseCase(it) }

                        // 아이디와 비밀번호 저장
                        saveIdAndPasswordUseCase(email, password)

                        // 아이디/비밀번호 저장 체크 박스 상태를 true로 저장, 자동 로그인 설정
                        saveAutoSaveStateUseCase(true)
                        saveAutoLoginStateUseCase(true)

                        // 회원가입 후 최초 로그인시 알람 설정 다이얼로그를 띄우기 위한 sharedPreferences
                        saveFirstTimeStateUseCase(true)

                        emit(Result.success(response.msg))
                    }
                }
            } catch (e: Exception) {
                emit(Result.failure(RuntimeException(e.message ?: "회원가입에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }

}