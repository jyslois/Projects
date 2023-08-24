package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.DuplicateCheckResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfoRemote.CheckNickNameDuplicateUseCase
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
class CheckNickNameDuplicateUseCaseTest {
    private lateinit var checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val nickname = "로이스"

    @Before
    fun setUp() {
        checkNickNameDuplicateUseCase = CheckNickNameDuplicateUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_SuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DuplicateCheckResponse(code = 1002, "사용 가능한 닉네임입니다")
        coEvery { mockMemberRepository.checkNickName(nickname) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = checkNickNameDuplicateUseCase(nickname)

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.checkNickName(nickname) }
    }

    @Test
    fun invoke_UnsuccssfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DuplicateCheckResponse(code = 1003, "이미 사용 중인 닉네임입니다")
        coEvery { mockMemberRepository.checkNickName(nickname) } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = checkNickNameDuplicateUseCase(nickname)

        // Then
        actualResponseFlow.collect {
            assertTrue((it.isFailure))
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(expectedResponse.msg, exception.message, "반환된 예외 메세지(${exception.message})가 예상 예외 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.checkNickName(nickname) }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "닉네임 중복 체크 실패. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.checkNickName(nickname) } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = checkNickNameDuplicateUseCase(nickname)

        // Then
        actualResponseFlow.collect {
            assertTrue((it.isFailure))
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 에러 메세지(${exception.message})가 예상 에러 메시지(${errorMsg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.checkNickName(nickname) }
    }

}