package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDateUseCase
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
class SaveTodayDiaryRecordDateUseCaseTest {
    private lateinit var saveTodayDiaryRecordDateUseCase: SaveTodayDiaryRecordDateUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testDate = "2023-09-16"

    @Before
    fun setUp() {
        saveTodayDiaryRecordDateUseCase = SaveTodayDiaryRecordDateUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryRecordDate() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveDate(any()) } just Runs

        // When
        saveTodayDiaryRecordDateUseCase(date = testDate)

        // Then
        coVerify { mockTodayDiaryRepository.saveDate(testDate) }
    }
}