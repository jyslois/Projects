package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.GetUserInfoUseCase
import com.android.mymindnotes.domain.usecase.UseMemberSharedPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val useSharedPreferencesUseCase: UseMemberSharedPreferencesUseCase
) : ViewModel() {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 최초 접속 알람 설정 다이얼로그
    // 최초 접속 값을 저장하는 플로우 (생성 시 자동 emit)
    private val _firstTime = MutableSharedFlow<Boolean>()
    val firstTime = _firstTime.asSharedFlow()

    // 닉네임 세팅
    // 회원 정보를 저장하는 플로우 (생성 시 자동 emit)
    private val _userInfo = MutableStateFlow<Map<String, Object>>(emptyMap())
    val userInfo = _userInfo.asStateFlow()

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
                // 최초 접속 여부 collect & emit
                useSharedPreferencesUseCase.getFirstTime().collect {
                    _firstTime.emit(it)
                }
            }

            launch {
                // 회원정보 값 collect & emit
                getUserInfoUseCase.getUserInfo().collect {
                    _userInfo.emit(it)
                }
            }

            launch {
                // error collect & emit
                getUserInfoUseCase.error.collect {
                    _error.emit(it)
                }
            }

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
