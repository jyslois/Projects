package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryThoughtUseCase
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
class SaveTodayDiaryThoughtUseCaseTest {
    private lateinit var saveTodayDiaryThoughtUseCaseTest: SaveTodayDiaryThoughtUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    // 테스트 데이터
    private val testThought = "테스트 생각"

    @Before
    fun setUp() {
        saveTodayDiaryThoughtUseCaseTest = SaveTodayDiaryThoughtUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryThought() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.saveThought(any()) } just Runs

        // Then
        saveTodayDiaryThoughtUseCaseTest(thought = testThought)

        // When
        coEvery { mockTodayDiaryRepository.saveThought(testThought) }
    }
}