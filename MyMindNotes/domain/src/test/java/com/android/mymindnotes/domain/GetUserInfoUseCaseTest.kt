package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.GetUserInfoResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetUserInfoUseCaseTest {
    private val mockMemberRepository = mockk<MemberRepository>()
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
         getUserInfoUseCase = GetUserInfoUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    // 테스트의 목적: GetUserInfoUseCase가 정상적인 응답을 받았을 때 올바르게 동작하는지 테스트
    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) { // invoke_NormalResponse 함수는 runTest를 사용하여 테스트 코루틴을 동기적으로 실행하고 그 결과 (성공 또는 실패)를 리턴한다
        // Given
        val nickname = "로이스"
        val email = "seolois@hotmail.com"
        val birthyear = 1991
        val expectedResponse = GetUserInfoResponse(birthyear = birthyear, email = email, nickname = nickname)
        coEvery { mockMemberRepository.getUserInfo() } returns flowOf(expectedResponse) // mock 객체가 getUserInfo()를 호출할 때 expectedResponse를 담은 flow를 반환하도록 설정

        // When
        val actualResponseFlow = getUserInfoUseCase()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val userInfo = it.getOrNull()
            assertNotNull(userInfo)
            assertEquals(birthyear, userInfo.birthyear, "반환된 생년(${userInfo.birthyear})이 예상 생년($birthyear)과 다릅니다")
            assertEquals(email, userInfo.email, "반환된 이메일(${userInfo.email})이 예상 이메일($email)과 다릅니다.")
            assertEquals(nickname, userInfo.nickname, "반환된 닉네임(${userInfo.nickname})이 예상 닉네임($nickname)과 다릅니다.")
        }
        coVerify { mockMemberRepository.getUserInfo() } // mock 객체가 getUserInfo()를 한 번 호출했는지 확인
    }

    // getUserInfo() 호출이 RuntimeException을 발생시킬 때 GetUserInfoUseCase가 예상대로 동작하는지 테스트
    @Test
    fun invoke_ThrowsRuntimeException() = runTest {
        // Given
        val errorMessage = "회원정보를 불러오지 못했습니다. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.getUserInfo() } throws RuntimeException(errorMessage)

        // When
        val actualResponseFlow = getUserInfoUseCase()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure) // 실패한 결과인지 확인
            val exception = it.exceptionOrNull()
            assertNotNull(exception) // 예외가 null이 아닌지 확인
            assertTrue(exception is RuntimeException) // 발생한 예외가 RuntimeException인지 확인
            assertEquals(errorMessage, exception.message, "반환된 에러 메시지(${exception.message})가 예상 에러 메세지(${errorMessage})와 다릅니다") // 예외 메시지가 기대한 메시지와 동일한지 확인
        }
        coVerify { mockMemberRepository.getUserInfo() } // mock 객체가 getUserInfo()를 한 번 호출했는지 확인
    }
}