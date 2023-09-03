package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
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
class SaveTraumaDiaryReflectionUseCaseTest {
    private lateinit var saveTraumaDiaryReflectionUseCase: SaveTraumaDiaryReflectionUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testReflection = "테스트 회고"

    @Before
    fun setUp() {
        saveTraumaDiaryReflectionUseCase = SaveTraumaDiaryReflectionUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryReflection() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveReflection(any()) } just Runs

        // When
        saveTraumaDiaryReflectionUseCase.invoke(reflection = testReflection)

        // Then
        coVerify { mockTraumaDiaryRepository.saveReflection(testReflection) }
    }
}
