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

    sealed class NickNameChangeResult {
        object NickNameChanged : NickNameChangeResult()
        data class Error(val message: String) : NickNameChangeResult()
    }

    suspend operator fun invoke(nickName: String): Flow<NickNameChangeResult> {
        return repository.changeNickName(nickName).map { response ->
            when (response["code"].toString().toDouble()) {
                3001.0 -> NickNameChangeResult.Error(response["msg"] as String)
                3000.0 -> NickNameChangeResult.NickNameChanged
                else -> NickNameChangeResult.Error("닉네임 변경 중 오류 발생")
            }
        }.catch {
            emit(NickNameChangeResult.Error("닉네임 변경 실패. 인터넷 연결을 확인해 주세요."))
        }
    }
}
