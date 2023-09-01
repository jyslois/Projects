package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.alarm.ChangeAlarmSwitchUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.SetAlarmDialogueUseCase
import com.android.mymindnotes.presentation.viewmodels.AlarmSettingViewModel
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class AlarmSettingViewModelTest {
    private lateinit var alarmSettingViewModel: AlarmSettingViewModel
    private val mockChangeAlarmSwitchUseCase = mockk<ChangeAlarmSwitchUseCase>()
    private val mockSetAlarmDialogueUseCase = mockk<SetAlarmDialogueUseCase>()
    private val mockGetAlarmStateUseCase = mockk<GetAlarmStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    // Test data
    private val time = "오후 10:09"
    private val hourOfDay = 22
    private val hour = 10
    private val minute = 9
    private val min = Integer.parseInt("09")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        alarmSettingViewModel = AlarmSettingViewModel(mockChangeAlarmSwitchUseCase, mockSetAlarmDialogueUseCase, mockGetAlarmStateUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockChangeAlarmSwitchUseCase, mockSetAlarmDialogueUseCase, mockGetAlarmStateUseCase)
        Dispatchers.resetMain()
    }

    @Test
    fun alarmSwitchChanged_UiStateChangedToAlarmSwitchedOn() = runTest {
        // Given
        val expectedResponse = ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOn
        coEvery { mockChangeAlarmSwitchUseCase(true) } returns flowOf(expectedResponse)

        // When
        alarmSettingViewModel.alarmSwitchChanged(true)

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOn, alarmSettingViewModel.uiState.value)
        coVerify { mockChangeAlarmSwitchUseCase(true) }
    }

    @Test
    fun alarmSwitchChanged_UiStateChangedToAlarmSwitchedOnWithTime() = runTest {
        // Given
        val expectedResponse = ChangeAlarmSwitchUseCase.SuccessResult.AlarmTime(time = time, hour = hour, minute = minute)
        coEvery { mockChangeAlarmSwitchUseCase(true) } returns flowOf(expectedResponse)

        // When
        alarmSettingViewModel.alarmSwitchChanged(true)

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOnWithTime(time, hour, minute), alarmSettingViewModel.uiState.value)
        coVerify { mockChangeAlarmSwitchUseCase(true) }
    }

    @Test
    fun alarmSwitchChanged_UiStateChangedToAlarmSwitchedOff() = runTest {
        // Given
        val expectedResponse = ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOff
        coEvery { mockChangeAlarmSwitchUseCase(false) } returns flowOf(expectedResponse)

        // When
        alarmSettingViewModel.alarmSwitchChanged(false)

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOff, alarmSettingViewModel.uiState.value)
        coVerify { mockChangeAlarmSwitchUseCase(false) }
    }

    @Test
    fun alarmDialogueSet_UiStateChangedToAlarmSwitchedOnWithTime() = runTest {
        // Given
        coEvery { mockSetAlarmDialogueUseCase(any(), any(), any(), any()) } just Runs

        // When
        alarmSettingViewModel.alarmDialogueSet(time, hourOfDay, min, minute)

        // wait unitl all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOnWithTime(time, hourOfDay, minute), alarmSettingViewModel.uiState.value)
        coVerify { mockSetAlarmDialogueUseCase(time, hourOfDay, min, minute) }
    }


    @Test
    fun `init_if alarm has just been switched on, should change UiState to AlarmSwitchedOn`() = runTest {
        // Given
        coEvery { mockGetAlarmStateUseCase() } returns flowOf(true)
        coEvery { mockChangeAlarmSwitchUseCase(true) } returns flowOf(ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOn)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        alarmSettingViewModel = AlarmSettingViewModel(
            mockChangeAlarmSwitchUseCase, mockSetAlarmDialogueUseCase, mockGetAlarmStateUseCase
        )

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOn, alarmSettingViewModel.uiState.value)
        coVerify { mockGetAlarmStateUseCase() }
        coVerify { mockChangeAlarmSwitchUseCase(true) }
    }


    @Test
    fun `init_if alarm has been set, should change UiState to AlarmSwitchedOnWithTime`() = runTest {
        // Given
        coEvery { mockGetAlarmStateUseCase() } returns flowOf(true)
        coEvery { mockChangeAlarmSwitchUseCase(true) } returns flowOf(ChangeAlarmSwitchUseCase.SuccessResult.AlarmTime(time, hour, minute))

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        alarmSettingViewModel = AlarmSettingViewModel(
            mockChangeAlarmSwitchUseCase, mockSetAlarmDialogueUseCase, mockGetAlarmStateUseCase
        )

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOnWithTime(time, hour, minute), alarmSettingViewModel.uiState.value)
        coVerify { mockGetAlarmStateUseCase() }
        coVerify { mockChangeAlarmSwitchUseCase(true) }
    }


    @Test
    fun `init_if alarm has not been switched on, UiState should stay as Loading`() = runTest {
        // Given
        coEvery { mockGetAlarmStateUseCase() } returns flowOf(false)

        // When: Initialize the ViewModel to trigger the init block with the mock setup
        alarmSettingViewModel = AlarmSettingViewModel(
            mockChangeAlarmSwitchUseCase, mockSetAlarmDialogueUseCase, mockGetAlarmStateUseCase
        )

        // Wait until all coroutine tasks finish
        advanceUntilIdle()

        // Then
        assertEquals(AlarmSettingViewModel.AlarmSettingUiState.Loading, alarmSettingViewModel.uiState.value)
        coVerify { mockGetAlarmStateUseCase() }
    }

}