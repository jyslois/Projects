package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TraumaDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDayUseCase
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
class SaveTraumaDiaryRecordDayUseCaseTest {
    private lateinit var saveTraumaDiaryRecordDayUseCase: SaveTraumaDiaryRecordDayUseCase
    private val mockTraumaDiaryRepository = mockk<TraumaDiaryRepository>()

    // 테스트 데이터
    private val testDay = "일요일"

    @Before
    fun setUp() {
        saveTraumaDiaryRecordDayUseCase = SaveTraumaDiaryRecordDayUseCase(mockTraumaDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTraumaDiaryRepository)
    }

    @Test
    fun invoke_savingDiaryRecordDay() = runTest {
        // Given
        coEvery { mockTraumaDiaryRepository.saveDay(any()) } just Runs

        // When
        saveTraumaDiaryRecordDayUseCase.invoke(day = testDay)

        // Then
        coVerify { mockTraumaDiaryRepository.saveDay(testDay) }
    }
}
