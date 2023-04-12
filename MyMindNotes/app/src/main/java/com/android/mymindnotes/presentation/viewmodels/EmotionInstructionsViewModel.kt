package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class EmotionInstructionsViewModel @Inject constructor(

): ViewModel() {

    // 버튼 클릭 감지
    // 감정 정렬 버튼
    private val _sortButton = MutableSharedFlow<Boolean>()
    val sortButton = _sortButton.asSharedFlow()

    // 버튼 클릭
    suspend fun clickSortButton() {
        _sortButton.emit(true)
    }


}