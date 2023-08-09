package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.SavePasswordUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val savePasswordUseCase: SavePasswordUseCase
) {

//    // 비밀번호 바꾸기
//    suspend fun changePassword(password: String, originalPassword: String): Flow<Map<String, Object>> {
//        return repository.changePassword(password, originalPassword)
//    }

    suspend operator fun invoke(password: String, originalPassword: String): Flow<Result<String?>> {
        return memberRepository.changePassword(password, originalPassword).map { response ->
            when (response.code) {
                3005, 3003 -> Result.failure(RuntimeException(response.msg))
                3002 -> {
                    savePasswordUseCase(password)
                    Result.success(response.msg)
                }
                else -> Result.failure(RuntimeException("비밀번호 변경 중 오류 발생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("비밀번호 변경 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }

}

