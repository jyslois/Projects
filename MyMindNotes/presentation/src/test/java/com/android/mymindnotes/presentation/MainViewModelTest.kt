package com.android.mymindnotes.presentation

import com.android.mymindnotes.domain.usecases.loginStates.GetAutoLoginStateUseCase
import com.android.mymindnotes.presentation.viewmodels.MainViewModel
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private lateinit var mainViewModel: MainViewModel
    private val mockGetAutoLoginStateUseCase = mockk<GetAutoLoginStateUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mockGetAutoLoginStateUseCase)
        Dispatchers.setMain(testDispatcher) // Dispatchers.Main을 주어진 testDispatcher로 대체하도록 설정, 이렇게 하면 테스트 중인 코드는 이 testDispatcher를 메인 스레드로 인식하게 되어 정상적으로 작동
    }

    @After
    fun tearDown() {
        clearMocks(mockGetAutoLoginStateUseCase)
        Dispatchers.resetMain() // main dispatcher를 기본값으로 재설정
    }

    // GetAutoLoginStateUseCase가 true를 반환할 때 MainUiState.AutoLogin 상태가 발생하는지 확인
    @Test
    fun checkAndUpdateAutoLoginState_EmitsAutoLoginState() = runTest(testDispatcher) {
        // Given
        coEvery { mockGetAutoLoginStateUseCase() } returns flowOf(true)

        // When
        mainViewModel.checkAndUpdateAutoLoginState()
        // Ensure all coroutines have completed,  현재 테스트 디스패처의 작업 큐에 있는, 즉 실행 대기 중인 모든 코루틴 작업들이 실행되고 완료될 때까지 진행을 일시 중단
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(MainViewModel.MainUiState.AutoLogin, mainViewModel.uiState.value)
        coVerify { mockGetAutoLoginStateUseCase() }
    }

    // GetAutoLoginStateUseCase가 false를 반환할 때 기본 상태인 MainUiState.Loading을 유지하는지 확인
    @Test
    fun checkAndUpdateAutoLoginState_RetainsLoadingState() = runTest(testDispatcher) {
        // Given
        coEvery { mockGetAutoLoginStateUseCase() } returns flowOf(false)

        // When
        mainViewModel.checkAndUpdateAutoLoginState()
        // Ensure all coroutines have completed,  현재 테스트 디스패처의 작업 큐에 있는, 즉 실행 대기 중인 모든 코루틴 작업들이 실행되고 완료될 때까지 진행을 일시 중단
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(MainViewModel.MainUiState.Loading, mainViewModel.uiState.value) // mainViewModel의 uiState의 현재 값이 MainViewModel.MainUiState.Loading과 동일한지 확인
        coVerify { mockGetAutoLoginStateUseCase() }
    }

}