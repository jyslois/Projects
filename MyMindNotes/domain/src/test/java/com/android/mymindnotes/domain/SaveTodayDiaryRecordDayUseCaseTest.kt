package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDayUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveTodayDiaryRecordDayUseCaseTest {
    private lateinit var saveTodayDiaryRecordSDayUseCase: SaveTodayDiaryRecordDayUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    @Before
    fun setUp() {
        saveTodayDiaryRecordSDayUseCase = SaveTodayDiaryRecordDayUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    // 테스트 데이터
    private val testDay = "토요일"

    @Test
    fun invoke_SavingDiaryDay() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveDay(any()) } just Runs

        // When
        saveTodayDiaryRecordSDayUseCase(testDay)

        // Then
        coVerify { mockTodayDiaryRepository.saveDay(testDay) }
    }

}