package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ClearLoginStatesUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.RuntimeException
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val memberRemoteRepository: MemberRemoteRepository,
    private val clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase,
    private val clearTimeSettingsUseCase: ClearTimeSettingsUseCase,
    private val clearLoginStatesUseCase: ClearLoginStatesUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase
) {

//    // 회원탈퇴 함수 콜
//    suspend fun deleteUser(): Flow<Map<String, Object>> {
//        return memberRemoteRepository.deleteUser()
//    }

    suspend operator fun invoke(): Flow<Result<String>> {

        return memberRemoteRepository.deleteUser().onEach { // onEach(): Flow가 방출하는 각 항목(즉, 회원 탈퇴 결과)에 대해 지정된 작업을 수행
            // onEach를 사용한 이유는 deleteUser() 함수가 반환한 Flow의 각 항목에 대해 아래의 작업을 수행하기 위함이다. 즉, 회원 탈퇴가 성공적으로 수행될 때마다 아래의 작업들이 실행될 것이다.
            // 알람 삭제
            stopAlarmUseCase()
            // 알람 설정 해제
            clearAlarmSettingsUseCase()
            // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
            clearTimeSettingsUseCase()
            // 모든 상태 저장 설정 지우기
            clearLoginStatesUseCase()

        }.map {
            Result.success("Success")
        }.catch {
            emit(Result.failure(RuntimeException("회원 탈퇴 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }
}

