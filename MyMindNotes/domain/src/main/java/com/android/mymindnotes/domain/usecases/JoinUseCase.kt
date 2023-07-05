package com.android.mymindnotes.domain.usecases

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoLoginStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveFirstTimeStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveUserIndexUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JoinUseCase @Inject constructor(
    private val repository: MemberRemoteRepository,

    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val saveFirstTimeStateUseCase: SaveFirstTimeStateUseCase,
    private val saveUserIndexUseCase: SaveUserIndexUseCase,

    private val saveAutoLoginStateUseCase: SaveAutoLoginStateUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
) {

//    // (서버) 회원 가입 함수 호출
//    suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<Map<String, Object>> {
//        return repository.join(email, nickname, password, birthyear)
//    }

    suspend operator fun invoke(email: String, nickname: String, password: String, birthyear: Int): Flow<JoinResult> {
        return repository.join(email, nickname, password, birthyear).map { response ->
            when (response["code"].toString().toDouble()) {
                2001.0 -> JoinResult.Error(response["msg"] as String)
                2000.0 -> {
                    // 회원 번호 저장
                    saveUserIndexUseCase(response["user_index"].toString().toDouble().toInt())

                    // 아이디와 비밀번호 저장
                    saveIdAndPasswordUseCase(email, password)

                    // 아이디/비밀번호 저장 체크 박스 상태를 true로 저장, 자동 로그인 설정
                    saveAutoSaveStateUseCase(true)
                    saveAutoLoginStateUseCase(true)

                    // 회원가입 후 최초 로그인시 알람 설정 다이얼로그를 띄우기 위한 sharedPreferences
                    saveFirstTimeStateUseCase(true)

                    JoinResult.Success
                }
                else -> JoinResult.Error("회원가입 중 오류 발생")

            }
        }.catch {
            emit(JoinResult.Error("회원가입에 실패했습니다. 인터넷 연결을 확인해 주세요."))
        }
    }

    sealed class JoinResult {
        object Success: JoinResult()
        data class Error(val message: String): JoinResult()
    }


}