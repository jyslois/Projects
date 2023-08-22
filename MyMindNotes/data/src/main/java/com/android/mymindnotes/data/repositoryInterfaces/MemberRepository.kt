package com.android.mymindnotes.data.repositoryInterfaces

import com.android.mymindnotes.core.dto.ChangeNicknameResponse
import com.android.mymindnotes.core.dto.ChangePasswordResponse
import com.android.mymindnotes.core.dto.ChangeToTemporaryPassword
import com.android.mymindnotes.core.dto.ChangeToTemporaryPasswordResponse
import com.android.mymindnotes.core.dto.DeleteUserResponse
import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import com.android.mymindnotes.core.dto.GetUserInfoResponse
import com.android.mymindnotes.core.dto.JoinResponse
import com.android.mymindnotes.core.dto.LoginResponse
import kotlinx.coroutines.flow.*

interface MemberRepository {

    // Local
    // get Methods
    suspend fun getAutoLoginCheck(): Flow<Boolean>
    suspend fun getAutoSaveCheck(): Flow<Boolean>
    suspend fun getId(): Flow<String?>
    suspend fun getPassword(): Flow<String?>
    suspend fun getFirstTime(): Flow<Boolean>
    suspend fun getAlarmState(): Flow<Boolean>
    suspend fun getTime(): Flow<String?>
    suspend fun getHour(): Flow<Int>
    suspend fun getMinute(): Flow<Int>
    suspend fun getRebootTime(): Flow<Long>


    // save Methods
    suspend fun saveAutoLoginCheck(state: Boolean)
    suspend fun saveAutoSaveCheck(state: Boolean)
    suspend fun saveIdAndPassword(id: String?, password: String?)
    suspend fun savePassword(password: String?)
    suspend fun saveUserIndex(index: Int)
    suspend fun saveFirstTime(state: Boolean)
    suspend fun saveAlarmState(state: Boolean)
    suspend fun saveTime(time: String)
    suspend fun saveHour(hour: Int)
    suspend fun saveMinute(minute: Int)
    suspend fun saveRebootTime(time: Long)

    // clear SharedPreferences
    suspend fun clearLoginStatesRelatedKeys()
    suspend fun clearAlarmRelatedKeys()
    suspend fun clearRebootTimeKey()


    // Remote
    // Get User Info
    suspend fun getUserInfo(): Flow<GetUserInfoResponse>

    // Log in & Log out
    suspend fun login(email: String, password: String): Flow<LoginResponse>

    // Email & Nickname Duplicate Check
    suspend fun checkEmail(emailInput: String): Flow<DuplicateCheckResponse>
    suspend fun checkNickName(nickNameInput: String): Flow<DuplicateCheckResponse>

    // Join
    suspend fun join(email: String, nickname: String, password: String, birthyear: Int): Flow<JoinResponse>

    // Delete
    suspend fun deleteUser(): Flow<DeleteUserResponse>

    // change NickName
    suspend fun changeNickName(nickName: String): Flow<ChangeNicknameResponse>

    // change Password
    suspend fun changePassword(password: String, originalPassword: String): Flow<ChangePasswordResponse>

    // change to TemporaryPassword
    suspend fun changeToTemporaryPassword(temporaryPasswordInfo: ChangeToTemporaryPassword): Flow<ChangeToTemporaryPasswordResponse>

}
