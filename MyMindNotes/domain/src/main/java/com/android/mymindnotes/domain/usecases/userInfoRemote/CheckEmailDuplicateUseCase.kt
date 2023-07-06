package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckEmailDuplicateUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {

//    // 이메일
//    // (서버) 이메일 중복 체크 함수 호출
//    suspend fun checkEmail(emailInput: String): Flow<Map<String, Object>> {
//        return repository.checkEmail(emailInput)
//    }

    suspend operator fun invoke(emailInput: String): Flow<Result<String>> {
        return repository.checkEmail(emailInput).map { response ->
            when (response["code"].toString().toDouble()) {
                1001.0 -> Result.failure(RuntimeException(response["msg"] as String))
                1000.0 -> Result.success("Success")
                else -> Result.failure(RuntimeException("이메일 중복 체크 중 오류 발생"))
            }
        }.catch {
            emit(Result.failure(RuntimeException("이메일 중복 체크에 실패했습니다. 인터넷 연결을 확인해 주세요.")))
        }
    }

}