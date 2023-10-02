package com.android.mymindnotes.domain

import com.android.mymindnotes.domain.usecases.alarm.ChangeAlarmSwitchUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmHourUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmMinuteUseCase
import com.android.mymindnotes.domain.usecases.alarm.GetAlarmTimeUseCase
import com.android.mymindnotes.domain.usecases.alarm.SaveAlarmStateUseCase
import com.android.mymindnotes.domain.usecases.alarm.StopAlarmUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearAlarmSettingsUseCase
import com.android.mymindnotes.domain.usecases.userInfo.ClearTimeSettingsUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ChangeAlarmSwitchUseCaseTest {
    private lateinit var changeAlarmSwitchUseCase: ChangeAlarmSwitchUseCase
    private val mockClearAlarmSettingsUseCase = mockk<ClearAlarmSettingsUseCase>()
    private val mockClearTimeSettingsUseCase = mockk<ClearTimeSettingsUseCase>()
    private val mockSaveAlarmStateUseCase = mockk<SaveAlarmStateUseCase>()
    private val mockGetAlarmTimeUseCase = mockk<GetAlarmTimeUseCase>()
    private val mockGetAlarmHourUseCase = mockk<GetAlarmHourUseCase>()
    private val mockGetAlarmMinuteUseCase = mockk<GetAlarmMinuteUseCase>()
    private val mockStopAlarmUseCase = mockk<StopAlarmUseCase>()

    @Before
    fun setUp() {
        changeAlarmSwitchUseCase = ChangeAlarmSwitchUseCase(mockClearAlarmSettingsUseCase, mockClearTimeSettingsUseCase, mockSaveAlarmStateUseCase, mockGetAlarmTimeUseCase, mockGetAlarmHourUseCase, mockGetAlarmMinuteUseCase, mockStopAlarmUseCase)
    }

    @After
    fun tearDown() {
        clearMocks(mockClearAlarmSettingsUseCase, mockClearTimeSettingsUseCase, mockSaveAlarmStateUseCase, mockGetAlarmTimeUseCase, mockGetAlarmHourUseCase, mockGetAlarmMinuteUseCase, mockStopAlarmUseCase)
    }

    @Test
    fun invoke_switchedOn_noAlarmTime() = runTest {
        // Given
        coEvery { mockGetAlarmTimeUseCase() } returns flowOf(null)
        coEvery { mockGetAlarmHourUseCase() } returns flowOf(0)
        coEvery { mockGetAlarmMinuteUseCase() } returns flowOf(0)
        coEvery { mockSaveAlarmStateUseCase(any()) } just Runs

        // When
        val resultState = changeAlarmSwitchUseCase(isChecked = true).first()

        // Then
        assertEquals(ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOn, resultState)
        coVerify { mockGetAlarmTimeUseCase() }
        coVerify { mockGetAlarmHourUseCase() }
        coVerify { mockGetAlarmMinuteUseCase() }
        coVerify { mockSaveAlarmStateUseCase(true) }
    }

    @Test
    fun invoke_switchedOn_alarmTimeSet() = runTest {
        // Given
        val time = "22:10"
        val hour = 22
        val minute = 10
        coEvery { mockGetAlarmTimeUseCase() } returns flowOf(time)
        coEvery { mockGetAlarmHourUseCase() } returns flowOf(hour)
        coEvery { mockGetAlarmMinuteUseCase() } returns flowOf(minute)
        coEvery { mockSaveAlarmStateUseCase(any()) } just Runs

        // When
        val resultState = changeAlarmSwitchUseCase(isChecked = true).first()

        // Then
        assertEquals(ChangeAlarmSwitchUseCase.SuccessResult.AlarmTime(time, hour, minute), resultState)
        coVerify { mockGetAlarmTimeUseCase() }
        coVerify { mockGetAlarmHourUseCase() }
        coVerify { mockGetAlarmMinuteUseCase() }
        coVerify { mockSaveAlarmStateUseCase(true) }
    }

    @Test
    fun invoke_switchedOff() = runTest {
        // Given
        coEvery { mockGetAlarmTimeUseCase() } returns flowOf(null)
        coEvery { mockGetAlarmHourUseCase() } returns flowOf(0)
        coEvery { mockGetAlarmMinuteUseCase() } returns flowOf(0)
        coEvery { mockClearAlarmSettingsUseCase() } just Runs
        coEvery { mockClearTimeSettingsUseCase() } just Runs
        coEvery { mockStopAlarmUseCase() } just Runs

        // When
        val resultState = changeAlarmSwitchUseCase(isChecked = false).first()

        // Then
        assertEquals(ChangeAlarmSwitchUseCase.SuccessResult.AlarmSwitchedOff, resultState)
        coVerify { mockGetAlarmTimeUseCase() }
        coVerify { mockGetAlarmHourUseCase() }
        coVerify { mockGetAlarmMinuteUseCase() }
        coVerify { mockClearAlarmSettingsUseCase() }
        coVerify { mockClearTimeSettingsUseCase() }
        coVerify { mockStopAlarmUseCase() }
    }

}