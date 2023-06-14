package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordMindChoiceViewModel @Inject constructor (
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    // 버튼 클릭 이벤트
    // 버튼 클릭 플로우
    private val _clickTodayEmotionButton = MutableSharedFlow<Boolean>()
    val clickTodayEmotionButton = _clickTodayEmotionButton.asSharedFlow()

    private val _clickTraumaButton = MutableSharedFlow<Boolean>()
    val clickTraumaButton = _clickTraumaButton.asSharedFlow()

    // 회원 정보를 저장하는 플로우 (생성 시 자동 emit)
    private val _userInfo = MutableSharedFlow<Map<String, Object>>()
    val userInfo = _userInfo.asSharedFlow()

    // 오늘의 마음 일기 버튼 클릭
    suspend fun clickTodayEmotionButton() {
        _clickTodayEmotionButton.emit(true)
    }

    // 트라우마 일기 버튼 클릭
    suspend fun clickTraumaButton() {
        _clickTraumaButton.emit(true)
    }

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            launch {
                // 회원정보 값 collect& emit
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

}