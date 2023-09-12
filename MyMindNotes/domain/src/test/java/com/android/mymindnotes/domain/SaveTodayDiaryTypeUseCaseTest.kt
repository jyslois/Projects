package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryTypeUseCase
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveTodayDiaryTypeUseCaseTest {
    private lateinit var saveTodayDiaryTypeUseCase: SaveTodayDiaryTypeUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testType = "오늘의 마음 일기"

    @Before
    fun setUp() {
        saveTodayDiaryTypeUseCase = SaveTodayDiaryTypeUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryType() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveType(any()) } just Runs

        // When
        saveTodayDiaryTypeUseCase(type = testType)

        // Then
        coEvery { mockTodayDiaryRepository.saveType(testType) }
    }

}