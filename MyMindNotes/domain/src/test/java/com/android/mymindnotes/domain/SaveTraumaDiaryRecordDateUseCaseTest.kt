package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDateUseCase
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
class SaveTraumaDiaryRecordDateUseCaseTest {
    private lateinit var saveTraumaDiaryRecordDateUseCase: SaveTraumaDiaryRecordDateUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testDate = "2023-09-03"

    @Before
    fun setUp() {
        saveTraumaDiaryRecordDateUseCase = SaveTraumaDiaryRecordDateUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryRecordDate() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveDate(any()) } just Runs

        // When
        saveTraumaDiaryRecordDateUseCase(date = testDate)

        // Then
        coVerify { mockTraumaDiaryRepository.saveDate(testDate) }
    }
}
