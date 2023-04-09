package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class EmotionSortingPopupViewModel @Inject constructor(

): ViewModel() {

    // emotionGroup 클릭 감지 저장 플로우
    private val _emotionGroup = MutableSharedFlow<Int>()
    val emotionGroup = _emotionGroup.asSharedFlow()

    suspend fun clickEmotionGroup(checkedId: Int) {
        _emotionGroup.emit(checkedId)
    }

}