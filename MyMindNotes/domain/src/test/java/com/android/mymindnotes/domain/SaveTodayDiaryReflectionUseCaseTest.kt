package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
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
class SaveTodayDiaryReflectionUseCaseTest {
    private lateinit var saveTodayDiaryReflectionUseCase: SaveTodayDiaryReflectionUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    @Before
    fun setUp() {
        saveTodayDiaryReflectionUseCase = SaveTodayDiaryReflectionUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    // 테스트 데이터
    private val testReflection = "테스트 회고"

    @Test
    fun invoke_savingDiaryReflection() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveReflection(any()) } just Runs

        // When
        saveTodayDiaryReflectionUseCase(reflection = testReflection)

        // Then
        coVerify { mockTodayDiaryRepository.saveReflection(testReflection) }
    }
}