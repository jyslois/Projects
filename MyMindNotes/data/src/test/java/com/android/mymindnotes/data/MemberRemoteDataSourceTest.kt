package com.android.mymindnotes.data

import com.android.mymindnotes.core.dto.ChangeNicknameResponse
import com.android.mymindnotes.core.dto.ChangePasswordResponse
import com.android.mymindnotes.core.dto.ChangeToTemporaryPassword
import com.android.mymindnotes.core.dto.ChangeToTemporaryPasswordResponse
import com.android.mymindnotes.core.dto.ChangeUserNickname
import com.android.mymindnotes.core.dto.ChangeUserPassword
import com.android.mymindnotes.core.dto.DeleteUserResponse
import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import com.android.mymindnotes.core.dto.GetUserInfoResponse
import com.android.mymindnotes.core.dto.JoinResponse
import com.android.mymindnotes.core.dto.LoginResponse
import com.android.mymindnotes.core.dto.UserInfo
import com.android.mymindnotes.core.dto.UserInfoLogin
import com.android.mymindnotes.data.dataSources.MemberRemoteDataSource
import com.android.mymindnotes.data.retrofit.api.user.ChangeNicknameApi
import com.android.mymindnotes.data.retrofit.api.user.ChangePasswordApi
import com.android.mymindnotes.data.retrofit.api.user.ChangeToTempPasswordApi
import com.android.mymindnotes.data.retrofit.api.user.CheckEmailApi
import com.android.mymindnotes.data.retrofit.api.user.CheckNickNameApi
import com.android.mymindnotes.data.retrofit.api.user.DeleteUserApi
import com.android.mymindnotes.data.retrofit.api.user.GetUserInfoApi
import com.android.mymindnotes.data.retrofit.api.user.JoinApi
import com.android.mymindnotes.data.retrofit.api.user.LoginApi
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class MemberRemoteDataSourceTest {
    private lateinit var memberRemoteDataSource: MemberRemoteDataSource
    private val mockLoginApi = mockk<LoginApi>()
    private val mockCheckEmailApi = mockk<CheckEmailApi>()
    private val mockCheckNickNameApi = mockk<CheckNickNameApi>()
    private val mockJoinApi = mockk<JoinApi>()
    private val mockGetUserInfoApi = mockk<GetUserInfoApi>()
    private val mockDeleteUserApi = mockk<DeleteUserApi>()
    private val mockChangeNicknameApi = mockk<ChangeNicknameApi>()
    private val mockChangePasswordApi = mockk<ChangePasswordApi>()
    private val mockChangeToTempPasswordApi = mockk<ChangeToTempPasswordApi>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        memberRemoteDataSource = MemberRemoteDataSource(
            loginApi = mockLoginApi,
            checkEmailApi = mockCheckEmailApi,
            checkNickNameApi = mockCheckNickNameApi,
            joinApi = mockJoinApi,
            getUserInfoApi = mockGetUserInfoApi,
            deleteUserApi = mockDeleteUserApi,
            changeNicknameApi = mockChangeNicknameApi,
            changePasswordApi = mockChangePasswordApi,
            changeToTempPasswordApi = mockChangeToTempPasswordApi,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        clearMocks(mockLoginApi, mockCheckEmailApi, mockCheckNickNameApi, mockJoinApi, mockGetUserInfoApi, mockDeleteUserApi, mockChangeNicknameApi, mockChangePasswordApi, mockChangeToTempPasswordApi)
    }

    // getUserInfo
    @Test
    fun getUserInfo_ReturnsExpectedUserInfo() = runTest(testDispatcher) {
        // Given
        val expectedResponse = GetUserInfoResponse(birthyear = 1991, email = "seolois@hotmail.com", nickname = "로이스")
        coEvery { mockGetUserInfoApi.getUserInfo(1) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.getUserInfo(1)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 유저 정보($it)가, 예상된 유저 정보(${expectedResponse})와 다릅니다", expectedResponse, it)
        }
        coVerify { mockGetUserInfoApi.getUserInfo(1) }
    }

    @Test
    fun getUserInfo_ThrowsRuntimeException(): Unit = runTest(testDispatcher) {
        // Given
        coEvery { mockGetUserInfoApi.getUserInfo(1) } throws RuntimeException("회원정보 불러오던 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.getUserInfo(1)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockGetUserInfoApi.getUserInfo(1) }
    }

    // login
    @Test
    fun login_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = LoginResponse(code = 5000, msg = "로그인에 성공했습니다.", nickname = "로이스", userIndex = 1)
        val userInfoLogin = UserInfoLogin("test@test.com", "password11")
        coEvery { mockLoginApi.login(userInfoLogin) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.login(userInfoLogin)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockLoginApi.login(userInfoLogin) }
    }

    @Test
    fun login_ReturnsUnsuccessfulLoginCode() = runTest(testDispatcher) {
        // Given
        val expectedResponse = LoginResponse(code = 5001, msg = "가입되지 않은 이메일입니다.")
        val userInfoLogin = UserInfoLogin("non@test.com", "wrongPassword")
        coEvery { mockLoginApi.login(userInfoLogin) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.login(userInfoLogin)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockLoginApi.login(userInfoLogin) }
    }

    @Test
    fun login_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val userInfoLogin = UserInfoLogin("test@test.com", "password11")
        coEvery { mockLoginApi.login(userInfoLogin) } throws RuntimeException("로그인 시도 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.login(userInfoLogin)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockLoginApi.login(userInfoLogin) }
    }

    // emailDuplicateCheck
    @Test
    fun emailDuplicateCheck_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DuplicateCheckResponse(code = 1000, msg = "사용 가능한 이메일입니다")
        val emailInput = "test@test.com"
        coEvery { mockCheckEmailApi.checkEmail(emailInput) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.emailDuplicateCheck(emailInput)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockCheckEmailApi.checkEmail(emailInput) }
    }

    @Test
    fun emailDuplicateCheck_ReturnsUnsuccessfulDuplicateCheckCode() = runTest(testDispatcher) {
        // Given
        val unsuccessfulResponse = DuplicateCheckResponse(code = 1001, msg = "이미 가입된 이메일입니다")
        val emailInput = "test@test.com"
        coEvery { mockCheckEmailApi.checkEmail(emailInput) } returns unsuccessfulResponse

        // When
        val returnedResponse = memberRemoteDataSource.emailDuplicateCheck(emailInput)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${unsuccessfulResponse.code})과 다릅니다.", unsuccessfulResponse, it)
        }
        coVerify { mockCheckEmailApi.checkEmail(emailInput) }
    }

    @Test
    fun emailDuplicateCheck_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val emailInput = "test@test.com"
        coEvery { mockCheckEmailApi.checkEmail(emailInput) } throws RuntimeException("이메일 중복 체크 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.emailDuplicateCheck(emailInput)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockCheckEmailApi.checkEmail(emailInput) }
    }


    // nickNameDuplicateCheck
    @Test
    fun nickNameDuplicateCheck_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DuplicateCheckResponse(code = 1002, msg = "사용 가능한 닉네임입니다")
        val nicknameInput = "로이스"
        coEvery { mockCheckNickNameApi.checkNickname(nicknameInput) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.nickNameDuplicateCheck(nicknameInput)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockCheckNickNameApi.checkNickname(nicknameInput) }
    }

    @Test
    fun nickNameDuplicateCheck_ReturnsUnsuccessfulDuplicateCheckCode() = runTest(testDispatcher) {
        // Given
        val unsuccessfulResponse = DuplicateCheckResponse(code = 1003, msg = "이미 사용 중인 닉네임입니다")
        val nicknameInput = "로이스"
        coEvery { mockCheckNickNameApi.checkNickname(nicknameInput) } returns unsuccessfulResponse

        // When
        val returnedResponse = memberRemoteDataSource.nickNameDuplicateCheck(nicknameInput)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${unsuccessfulResponse.code})과 다릅니다.", unsuccessfulResponse, it)
        }
        coVerify { mockCheckNickNameApi.checkNickname(nicknameInput) }
    }

    @Test
    fun nickNameDuplicateCheck_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val nicknameInput = "로이스"
        coEvery { mockCheckNickNameApi.checkNickname(nicknameInput) } throws RuntimeException("닉네임 중복 체크 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.nickNameDuplicateCheck(nicknameInput)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockCheckNickNameApi.checkNickname(nicknameInput) }
    }

    // join
    @Test
    fun join_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = JoinResponse(code = 2000, msg = "회원 가입 성공! 환영합니다", userIndex = 1)
        val userInfo = UserInfo("test@test.com", "로이스", "password11", 1991)
        coEvery { mockJoinApi.addUser(userInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.join(userInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 결과(${it})가 예상(${expectedResponse})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockJoinApi.addUser(userInfo) }
    }

    @Test
    fun join_ReturnsUnsuccessfulJoinCode() = runTest(testDispatcher) {
        // Given
        val expectedResponse = JoinResponse(code = 2001, msg = "회원 가입 양식을 다시 한 번 확인해 주세요", userIndex = 1)
        val userInfo = UserInfo("test@test.com", "로이스", "0", 1991)
        coEvery { mockJoinApi.addUser(userInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.join(userInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockJoinApi.addUser(userInfo) }
    }

    @Test
    fun join_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val userInfo = UserInfo("test@test.com", "로이스", "password11", 1991)
        coEvery { mockJoinApi.addUser(userInfo) } throws RuntimeException("회원 가입 시도 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.join(userInfo)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockJoinApi.addUser(userInfo) }
    }

    // deleteUser
    @Test
    fun deleteUser_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DeleteUserResponse(code = 4000, msg = "회원 탈퇴 완료")
        val userIndex = 1
        coEvery { mockDeleteUserApi.deleteUser(userIndex) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.deleteUser(userIndex)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 결과(${it})가 예상(${expectedResponse})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockDeleteUserApi.deleteUser(userIndex) }
    }

    @Test
    fun deleteUser_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val userIndex = 1
        coEvery { mockDeleteUserApi.deleteUser(userIndex) } throws RuntimeException("회원 탈퇴 시도 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.deleteUser(userIndex)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockDeleteUserApi.deleteUser(userIndex) }
    }

    // changeNickName
    @Test
    fun changeNickName_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangeNicknameResponse(code = 3000, msg = "닉네임이 변경되었습니다")
        val userIndex = 1
        val newNickName = "로이"
        val nicknameInfo = ChangeUserNickname(userIndex, newNickName)
        coEvery { mockChangeNicknameApi.updateUserNickname(nicknameInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.changeNickName(nicknameInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 결과(${it})가 예상(${expectedResponse})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockChangeNicknameApi.updateUserNickname(nicknameInfo) }
    }

    @Test
    fun changeNickName_ReturnsUnsuccessfulCode() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangeNicknameResponse(code = 3001, msg = "닉네임 양식을 다시 한 번 확인해 주세요.")
        val userIndex = 1
        val newNickName = "@_@"
        val nicknameInfo = ChangeUserNickname(userIndex, newNickName)
        coEvery { mockChangeNicknameApi.updateUserNickname(nicknameInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.changeNickName(nicknameInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockChangeNicknameApi.updateUserNickname(nicknameInfo) }
    }

    @Test
    fun changeNickName_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val userIndex = 1
        val newNickName = "로이스"
        val nicknameInfo = ChangeUserNickname(userIndex, newNickName)
        coEvery { mockChangeNicknameApi.updateUserNickname(nicknameInfo) } throws RuntimeException("닉네임 변경 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.changeNickName(nicknameInfo)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockChangeNicknameApi.updateUserNickname(nicknameInfo) }
    }

    // changePassword
    @Test
    fun changePassword_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangePasswordResponse(code = 3002, msg = "비밀번호가 변경되었습니다")
        val userIndex = 1
        val newPassword = "newPassword123"
        val originalPassword = "password11"
        val passwordInfo = ChangeUserPassword(userIndex, newPassword, originalPassword)
        coEvery { mockChangePasswordApi.updateUserPassword(passwordInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.changePassword(passwordInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 결과(${it})가 예상(${expectedResponse})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockChangePasswordApi.updateUserPassword(passwordInfo) }
    }

    @Test
    fun changePassword_ReturnsUnsuccessfulCode() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangePasswordResponse(code = 3005, msg = "기존 비밀번호를 확인해 주세요")
        val userIndex = 1
        val newPassword = "newPassword123"
        val originalPassword = "password"
        val passwordInfo = ChangeUserPassword(userIndex, newPassword, originalPassword)
        coEvery { mockChangePasswordApi.updateUserPassword(passwordInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.changePassword(passwordInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockChangePasswordApi.updateUserPassword(passwordInfo) }
    }

    @Test
    fun changePassword_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val userIndex = 1
        val newPassword = "newPassword123"
        val originalPassword = "password11"
        val passwordInfo = ChangeUserPassword(userIndex, newPassword, originalPassword)
        coEvery { mockChangePasswordApi.updateUserPassword(passwordInfo) } throws RuntimeException("비밀번호 변경 시도 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.changePassword(passwordInfo)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockChangePasswordApi.updateUserPassword(passwordInfo) }
    }

    // changeToTemporaryPassword
    @Test
    fun changeToTemporaryPassword_ReturnsExpectedResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangeToTemporaryPasswordResponse(code = 3006, msg = "임시 비밀번호로 비밀번호 변경 완료")
        val email = "test@test.com"
        val randomPassword = "RandomTempPassword123"
        val temporaryPasswordInfo = ChangeToTemporaryPassword(email, randomPassword)
        coEvery { mockChangeToTempPasswordApi.toTemPassword(temporaryPasswordInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.changeToTemporaryPassword(temporaryPasswordInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 결과(${it})가 예상(${expectedResponse})과 다릅니다.", expectedResponse, it)
        }
        coVerify { mockChangeToTempPasswordApi.toTemPassword(temporaryPasswordInfo) }
    }

    @Test
    fun changeToTemporaryPassword_ReturnsUnsuccessfulCode() = runTest(testDispatcher) {
        // Given
        val expectedResponse = ChangeToTemporaryPasswordResponse(code = 3007, msg = "가입되어 있지 않은 이메일입니다")
        val email = "notexisting@test.com"
        val randomPassword = "RandomTempPassword123"
        val temporaryPasswordInfo = ChangeToTemporaryPassword(email, randomPassword)
        coEvery { mockChangeToTempPasswordApi.toTemPassword(temporaryPasswordInfo) } returns expectedResponse

        // When
        val returnedResponse = memberRemoteDataSource.changeToTemporaryPassword(temporaryPasswordInfo)

        // Then
        returnedResponse.collect {
            assertEquals("반환된 코드(${it.code})가 예상(${expectedResponse.code})과 다릅니다.", expectedResponse.code, it.code)
        }
        coVerify { mockChangeToTempPasswordApi.toTemPassword(temporaryPasswordInfo) }
    }

    @Test
    fun changeToTemporaryPassword_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val email = "test@test.com"
        val randomPassword = "RandomTempPassword123"
        val temporaryPasswordInfo = ChangeToTemporaryPassword(email, randomPassword)
        coEvery { mockChangeToTempPasswordApi.toTemPassword(temporaryPasswordInfo) } throws RuntimeException("임시 비밀번호로 비밀번호 변경 중 오류 발생")

        // When
        val returnedResponse = memberRemoteDataSource.changeToTemporaryPassword(temporaryPasswordInfo)

        // Then
        assertFailsWith<RuntimeException> {
            returnedResponse.first()
        }
        coVerify { mockChangeToTempPasswordApi.toTemPassword(temporaryPasswordInfo) }
    }

}