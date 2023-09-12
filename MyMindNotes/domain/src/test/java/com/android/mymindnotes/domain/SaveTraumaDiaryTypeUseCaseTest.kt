package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryTypeUseCase
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
class SaveTraumaDiaryTypeUseCaseTest {
    private lateinit var saveTraumaDiaryTypeUseCase: SaveTraumaDiaryTypeUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testType = "트라우마 일기"

    @Before
    fun setUp() {
        saveTraumaDiaryTypeUseCase = SaveTraumaDiaryTypeUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryType() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveType(any()) } just Runs

        // When
        saveTraumaDiaryTypeUseCase(type = testType)

        // Then
        coVerify { mockTraumaDiaryRepository.saveType(testType) }
    }
}
