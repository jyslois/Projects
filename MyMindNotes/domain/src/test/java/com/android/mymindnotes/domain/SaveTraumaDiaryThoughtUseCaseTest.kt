package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryThoughtUseCase
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
class SaveTraumaDiaryThoughtUseCaseTest {
    private lateinit var saveTraumaDiaryThoughtUseCase: SaveTraumaDiaryThoughtUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testThought = "테스트 생각"

    @Before
    fun setUp() {
        saveTraumaDiaryThoughtUseCase = SaveTraumaDiaryThoughtUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryThought() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveThought(any()) } just Runs

        // When
        saveTraumaDiaryThoughtUseCase.invoke(thought = testThought)

        // Then
        coVerify { mockTraumaDiaryRepository.saveThought(testThought) }
    }
}