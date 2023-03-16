package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.DuplicateCheckUseCase
import com.android.mymindnotes.hilt.module.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val duplicateCheckUseCase: DuplicateCheckUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    // SharedFlow
    // 이메일 중복 체크 버튼 클릭 감지 플로우
    private val _clickEmailCheck = MutableSharedFlow<Boolean>()
    val clickEmailCheck = _clickEmailCheck.asSharedFlow()

    // Function
    // 이메일 중복 체크 버튼 클릭 감지
    suspend fun clickEmailCheckButton() {
        _clickEmailCheck.emit(true)
    }

    // SharedFlow
    // 이메일 중복 체크 결과 저장 플로우
    private val _emailCheckResult = MutableSharedFlow<Map<String, Object>>()
    val emailCheckResult = _emailCheckResult.asSharedFlow()

    // 이메일 중복 체크 함수 호출
    suspend fun checkEmail(emailInput: String) {
      duplicateCheckUseCase.checkEmail(emailInput)
    }

    // collect & emit
    init {
        viewModelScope.launch(ioDispatcher) {
            launch {
                // 이메일 중복 체크 결과 플로우 구독
                duplicateCheckUseCase.emailCheckResult.collect {
                    _emailCheckResult.emit(it)
                }
            }
        }
    }

}