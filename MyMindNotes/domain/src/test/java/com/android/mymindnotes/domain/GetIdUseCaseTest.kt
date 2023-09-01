package com.android.mymindnotes.domain

import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfo.GetIdUseCase
import io.mockk.coEvery
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GetIdUseCaseTest {
    private lateinit var getIdUseCase: GetIdUseCase
    private val mockMemberRepository = mockk<MemberRepository>()

    // 테스트 데이터
    private val testId = "seolois@hotmail.com"

    @Before
    fun setUp() {
        getIdUseCase = GetIdUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {
        clearMocks(mockMemberRepository)
    }

    @Test
    fun invoke_callingGetId() = runTest {
        // Given
        coEvery { mockMemberRepository.getId() } returns flowOf(testId)

        // When
        val resultFlow = getIdUseCase()

        // Then
        resultFlow.collect { id ->
            assertEquals(testId, id)
        }
        coVerify { mockMemberRepository.getId() }
    }
}