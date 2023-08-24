package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.DeleteUserResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.loginStates.ClearLoginStatesUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfoRemote.DeleteUserUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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
class DeleteUserUseCaseTest {
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val mockMemberRepository = mockk<MemberRepository>()
    private val mockClearAlarmSettingsUseCase = mockk<ClearAlarmSettingsUseCase>()
    private val mockClearTimeSettingsUseCase = mockk<ClearTimeSettingsUseCase>()
    private val mockClearLoginStateUseCase = mockk<ClearLoginStatesUseCase>()
    private val mockStopAlarmUseCase = mockk<StopAlarmUseCase>()

    @Before
    fun setUp() {
        deleteUserUseCase = DeleteUserUseCase(mockMemberRepository, mockClearAlarmSettingsUseCase, mockClearTimeSettingsUseCase, mockClearLoginStateUseCase, mockStopAlarmUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository, mockClearAlarmSettingsUseCase, mockClearTimeSettingsUseCase, mockClearLoginStateUseCase, mockStopAlarmUseCase)
    }

    @Test
    fun invoke_ReturnsSuccessfulResponse() = runTest(testDispatcher) {
        // Given
        val expectedResponse = DeleteUserResponse(code = 4000, msg = "회원 탈퇴 완료")
        coEvery { mockMemberRepository.deleteUser() } returns flowOf(expectedResponse)
        coEvery { mockStopAlarmUseCase() } just Runs
        coEvery { mockClearAlarmSettingsUseCase() } just Runs
        coEvery { mockClearTimeSettingsUseCase() } just Runs
        coEvery { mockClearLoginStateUseCase() } just Runs

        // When
        val actualResponseFlow = deleteUserUseCase()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val msg = it.getOrNull()
            assertNotNull(msg)
            assertEquals(expectedResponse.msg, msg, "반환된 메세지($msg)가 예상 메시지(${expectedResponse.msg})와 다릅니다.")
        }
        coVerify { mockMemberRepository.deleteUser() }
        coVerify { mockStopAlarmUseCase() }
        coVerify { mockClearAlarmSettingsUseCase() }
        coVerify { mockClearTimeSettingsUseCase() }
        coVerify { mockClearLoginStateUseCase() }
    }

    @Test
    fun invoke_ThrowsRuntimeException() = runTest(testDispatcher) {
        // Given
        val errorMsg = "회원 탈퇴 실패. 인터넷 연결을 확인해 주세요."
        coEvery { mockMemberRepository.deleteUser() } throws RuntimeException(errorMsg)

        // When
        val actualResponseFlow = deleteUserUseCase()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isFailure)
            val exception = it.exceptionOrNull()
            assertNotNull(exception)
            assertTrue(exception is RuntimeException)
            assertEquals(errorMsg, exception.message, "반환된 예외 메시지(${exception.message})가 예상 예외 메세지(${errorMsg})와 다릅니다")
        }
        coVerify { mockMemberRepository.deleteUser() }
    }
}