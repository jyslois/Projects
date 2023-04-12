package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class EmotionPopupViewModel @Inject constructor(

): ViewModel() {

    // emotion 클릭 감지 저장 플로우
    private val _emotion = MutableSharedFlow<Int>()
    val emotion = _emotion.asSharedFlow()

    suspend fun clickEmotion(checkedId: Int) {
        _emotion.emit(checkedId)
    }

}