package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.core.hilt.coroutineModules.MainDispatcherCoroutineScope
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ClearLoginStatesUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase,
    private val clearTimeSettingsUseCase: ClearTimeSettingsUseCase,
    private val clearLoginStatesUseCase: ClearLoginStatesUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase
) {

//    // 회원탈퇴 함수 콜
//    suspend fun deleteUser(): Flow<Map<String, Object>> {
//        return memberRepository.deleteUser()
//    }

    suspend operator fun invoke(): String {

        var resultState = ""

        try {
            memberRepository.deleteUser().collect {
                if (it["code"].toString().toDouble() == 4000.0) {
                    // 알람 삭제
                    stopAlarmUseCase()
                    // 알람 설정 해제
                    clearAlarmSettingsUseCase()
                    // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
                    clearTimeSettingsUseCase()
                    // 모든 상태 저장 설정 지우기
                    clearLoginStatesUseCase()

                    resultState = "Success"
                }
            }
        } catch(e: Exception) {
            resultState = "Error"
        }

        return resultState
    }


}