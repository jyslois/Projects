package com.android.mymindnotes.domain.usecases.userInfoRemote

import com.android.mymindnotes.data.repositoryInterfaces.MemberRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChangeNickNameUseCase @Inject constructor(
    private val repository: MemberRemoteRepository
) {

//    // 닉네임 바꾸기
//    suspend fun changeNickName(nickName: String): Flow<Map<String, Object>> {
//        return repository.changeNickName(nickName)
//    }
    suspend operator fun invoke(nickName: String): Flow<Result<String>> {
        return repository.changeNickName(nickName).map { response ->
            when (response["code"].toString().toDouble()) {
                3001.0 -> Result.failure(RuntimeException(response["msg"] as String))
                3000.0 -> Result.success("Success")
                else -> Result.failure(RuntimeException("닉네임 변경 중 오류 발생."))
            }
        }.catch {
            emit(Result.failure(RuntimeException("닉네임 변경 실패. 인터넷 연결을 확인해 주세요.")))
        }
    }

}
