package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.TodayDiaryRepository
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@ExperimentalCoroutinesApi
class ClearTodayDiaryTempRecordsUseCaseTest {
    private lateinit var clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
    private val mockTodayDiaryRepository = mockk<TodayDiaryRepository>()

    @Before
    fun setUp() {
        clearTodayDiaryTempRecordsUseCase = ClearTodayDiaryTempRecordsUseCase(mockTodayDiaryRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockTodayDiaryRepository)
    }

    @Test
    fun invoke_clearingTodayDiaryTempRecords() = runTest {
        // Given
        coEvery { mockTodayDiaryRepository.clearTodayDiaryTempRecords() } just Runs

        // When
        clearTodayDiaryTempRecordsUseCase()

        // Then
        coVerify { mockTodayDiaryRepository.clearTodayDiaryTempRecords() }
    }
}
