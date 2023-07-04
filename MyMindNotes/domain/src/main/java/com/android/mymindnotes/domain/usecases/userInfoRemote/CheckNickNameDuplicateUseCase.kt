package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckNickNameDuplicateUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {

//    // 닉네임
//    // (서버) 닉네임 중복 체크 함수 호출
//    suspend fun checkNickName(nickNameInput: String): Flow<Map<String, Object>> {
//        return repository.checkNickName(nickNameInput)
//    }

    sealed class NickNameCheckResult {
        object NotDuplicate : NickNameCheckResult()
        data class Error(val message: String) : NickNameCheckResult()
    }

    suspend operator fun invoke(nickNameInput: String): Flow<NickNameCheckResult> {
        return repository.checkNickName(nickNameInput).map { response ->
            when(response["code"].toString().toDouble()) {
                1003.0 -> NickNameCheckResult.Error(response["msg"] as String)
                1002.0 -> NickNameCheckResult.NotDuplicate
                else -> NickNameCheckResult.Error("닉네임 체크 중 오류 발생")
            }
        }.catch {
            emit(NickNameCheckResult.Error("닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요."))
        }
    }
}