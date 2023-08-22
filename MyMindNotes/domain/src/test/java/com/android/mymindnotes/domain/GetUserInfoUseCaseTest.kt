package com.android.mymindnotes.domain

import com.android.mymindnotes.core.dto.GetUserInfoResponse
import com.android.mymindnotes.data.repositoryInterfaces.MemberRepository
import com.android.mymindnotes.domain.usecases.userInfoRemote.GetUserInfoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class GetUserInfoUseCaseTest {
    private val mockMemberRepository = mockk<MemberRepository>()
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
         getUserInfoUseCase = GetUserInfoUseCase(mockMemberRepository)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun invoke_NormalResponse() = runTest(testDispatcher) {
        // Given
        val nickname = "로이스"
        val email = "seolois@hotmail.com"
        val birthyear = 1991
        val expectedResponse = GetUserInfoResponse(birthyear = birthyear, email = email, nickname = nickname)
        coEvery { mockMemberRepository.getUserInfo() } returns flowOf(expectedResponse)

        // When
        val actualResponseFlow = getUserInfoUseCase.invoke()

        // Then
        actualResponseFlow.collect {
            assertTrue(it.isSuccess)
            val userInfo = it.getOrNull()
            assertNotNull(userInfo)
            assertEquals(birthyear, userInfo.birthyear)
            assertEquals(email, userInfo.email)
            assertEquals(nickname, userInfo.nickname)
        }
    }
}