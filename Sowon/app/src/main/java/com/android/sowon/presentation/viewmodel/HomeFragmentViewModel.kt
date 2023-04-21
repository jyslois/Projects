package com.android.sowon.presentation.viewmodel

import android.widget.TextView
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(

): ViewModel() {

    // 버튼 클릭 이벤트 감지
    // 전체 강의 보기 버튼
    private val _allSortingButton = MutableSharedFlow<TextView>()
    val allSortingButton = _allSortingButton.asSharedFlow()

    // 클릭 이벤트 처리
    suspend fun clickAllSortingButton(textView: TextView) {
        _allSortingButton.emit(textView)
    }

    // 기초 강의 보기 버튼
    private val _basicsSortingButton = MutableSharedFlow<TextView>()
    val basicsSortingButton = _basicsSortingButton.asSharedFlow()

    // 클릭 이벤트 처리
    suspend fun clickBasicsSortingButton(textView: TextView) {
        _basicsSortingButton.emit(textView)
    }

    // 카카오톡 강의 보기 버튼
    private val _kakaoTalkSortingButton = MutableSharedFlow<TextView>()
    val kakaoTalkSortingButton = _kakaoTalkSortingButton.asSharedFlow()

    // 클릭 이벤트 처리
    suspend fun clickKakaoTalkSortingButton(textView: TextView) {
        _kakaoTalkSortingButton.emit(textView)
    }


    // 배민 강의 보기 버튼
    private val _baeminSortingButton = MutableSharedFlow<TextView>()
    val baeminSortingButton = _baeminSortingButton.asSharedFlow()

    // 클릭 이벤트 처리
    suspend fun clickBaeminSortingButton(textView: TextView) {
        _baeminSortingButton.emit(textView)
    }




}