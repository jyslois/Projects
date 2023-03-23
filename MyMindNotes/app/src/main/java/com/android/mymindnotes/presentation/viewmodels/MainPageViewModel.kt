package com.android.mymindnotes.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecase.UseSharedPreferencesUseCase
import com.android.mymindnotes.hilt.module.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val useSharedPreferencesUseCase: UseSharedPreferencesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    // 최초 접속 알람 설정 다이얼로그
    // 최초 접속 값을 저장하는 플로우 (생성 시 자동 emit)
    private val _firstTime = MutableSharedFlow<Boolean>(replay = 1)
    val firstTime = _firstTime.asSharedFlow()

    // 닉네임 세팅
    // 회원 정보를 저장하는 플로우 (생성 시 자동 emit)
    private val _userInfo = MutableSharedFlow<Map<String, Object>>()
    val userInfo = _userInfo.asSharedFlow()

    // 클릭 이벤트 반응 플로우
    // 일기 쓰기 버튼
    private val _clickAddRecordButton = MutableSharedFlow<Boolean>()
    val clickAddRecordButton = _clickAddRecordButton.asSharedFlow()

    // 메뉴 버튼
    private val _clickMainMenuButton = MutableSharedFlow<Boolean>()
    val clickMainMenuButton = _clickMainMenuButton.asSharedFlow()


    init {

        viewModelScope.launch {
            launch {
                Log.e("FirstTimeCheck", "viewModel - launch 안에 들어옴")
                Log.e("FirstTimeCheck", "firstTime - ${useSharedPreferencesUseCase.firstTime}")
                // 최초 접속 여부 값 collect & emit
                useSharedPreferencesUseCase.firstTime.collect {
                    _firstTime.emit(it)
                }

//                useSharedPreferencesUseCase.getFirstTime().collect {
//                    _firstTime.emit(it)
//                    Log.e("FirstTimeCheck", "viewModel - emit")
//                }
            }

            launch {
                // 회원정보 값 collect & emit
                getUserInfoUseCase.getUserInfo().collect {
                    _userInfo.emit(it)
                }
            }

//            launch(ioDispatcher) {
//                launch {
//                    // (서버) 최초 접속 여부 불러오는 함수 호출
//                    useSharedPreferencesUseCase.getFirstTime()
//                    Log.e("FirstTimeCheck", "함수 호출 -> ViewModel")
//                }
//
//                launch {
//                    // (서버) 닉네임 불러오기 위해 회원정보 불러오는 함수 호출
//                    getUserInfoUseCase.getUserInfo()
//                }
//            }

        }
    }

    // 최초 접속 값을 바꾸기 위한 함수 호출
    suspend fun saveFirstTime(boolean: Boolean) {
        useSharedPreferencesUseCase.saveFirstTime(boolean)
    }

    // 클릭 이벤트 처리
    suspend fun clickAddRecordButton() {
        _clickAddRecordButton.emit(true)
    }

    suspend fun clickMainMenuButton() {
        _clickMainMenuButton.emit(true)
    }

}
