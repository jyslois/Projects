package com.android.mymindnotes.domain

import com.android.mymindnotes.domain.usecases.loginStates.ChangeAutoSaveBoxStateUseCase
import com.android.mymindnotes.domain.usecases.loginStates.SaveAutoSaveStateUseCase
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ChangeAutoSaveBoxStateUseCaseTest {
    private lateinit var changeAutoSaveBoxStateUseCase: ChangeAutoSaveBoxStateUseCase
    private val mockSaveIdAndPasswordUseCase = mockk<SaveIdAndPasswordUseCase>()
    private val mockSaveAutoSaveStateUseCase = mockk<SaveAutoSaveStateUseCase>()

    // 테스트 데이터
    private val testId = "seolois@hotmail.com"
    private val testPassword = "testPassword111"

    @Before
    fun setUp() {
        changeAutoSaveBoxStateUseCase = ChangeAutoSaveBoxStateUseCase(mockSaveIdAndPasswordUseCase, mockSaveAutoSaveStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockSaveIdAndPasswordUseCase, mockSaveAutoSaveStateUseCase)
    }

    @Test
    fun `invoke_when either isAutoSaveChecked or isAutoLoginChecked is true, change AutoSaveState to true and save Id and Password`() = runTest {
        // Given
        coEvery { mockSaveIdAndPasswordUseCase(any(), any()) } just Runs
        coEvery { mockSaveAutoSaveStateUseCase(any()) } just Runs

        // When
        changeAutoSaveBoxStateUseCase(isAutoSaveChecked = true, isAutoLoginChecked = true, id = testId, password = testPassword)

        // Then
        coVerify {
            mockSaveAutoSaveStateUseCase(true)
            mockSaveIdAndPasswordUseCase(id = testId, password = testPassword)
        }
    }

    @Test
    fun `invoke_when neither isAutoSaveChecked nor isAutoLoginChecked is true, change saveAutoSaveState to false and make Id and Password null`() = runTest {
        // Given
        coEvery { mockSaveIdAndPasswordUseCase(any(), any()) } just Runs
        coEvery { mockSaveAutoSaveStateUseCase(any()) } just Runs

        // When
        changeAutoSaveBoxStateUseCase(isAutoSaveChecked = false, isAutoLoginChecked = false, id = testId, password = testPassword)

        // Then
        coVerify {
            mockSaveAutoSaveStateUseCase(false)
            mockSaveIdAndPasswordUseCase(id = null, password = null)
        }
    }
}
