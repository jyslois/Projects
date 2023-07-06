package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChangeToTemporaryPasswordUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {


//    // 임시 비밀번호로 비밀번호 변경하기
//    suspend fun changeToTemporaryPassword(email: String, randomPassword: String): Flow<Map<String, Object>> {
//        return repository.changeToTemporaryPassword(email, randomPassword)
//    }

    suspend operator fun invoke(
        email: String,
        randomPassword: String
    ): Flow<Result<String>> {
        return repository.changeToTemporaryPassword(email, randomPassword).map { response ->
            when (response["code"].toString().toDouble()) {
                3007.0 -> Result.failure(RuntimeException(response["msg"] as String))
                3006.0 -> Result.success(response["msg"] as String)
                else -> Result.failure(RuntimeException("임시 비밀번호 발송 중 오류 방생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("임시 비밀번호 발송에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
        }

    }
}


