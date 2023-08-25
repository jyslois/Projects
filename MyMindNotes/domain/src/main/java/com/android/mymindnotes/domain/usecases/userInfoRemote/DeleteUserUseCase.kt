package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ClearLoginStatesUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.RuntimeException

class DeleteUserUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val clearAlarmSettingsUseCase: ClearAlarmSettingsUseCase,
    private val clearTimeSettingsUseCase: ClearTimeSettingsUseCase,
    private val clearLoginStatesUseCase: ClearLoginStatesUseCase,
    private val stopAlarmUseCase: StopAlarmUseCase
) {

    suspend operator fun invoke(): Flow<Result<String?>> {

        return flow {
            try {
                val response = memberRepository.deleteUser().first()
                // 알람 삭제
                stopAlarmUseCase()
                // 알람 설정 해제
                clearAlarmSettingsUseCase()
                // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
                clearTimeSettingsUseCase()
                // 모든 상태 저장 설정 지우기
                clearLoginStatesUseCase()

                emit(Result.success(response.msg))
            } catch(e: Exception) {
                emit(Result.failure(RuntimeException("회원 탈퇴 실패. 인터넷 연결을 확인해 주세요.")))
            }
        }
    }
}

