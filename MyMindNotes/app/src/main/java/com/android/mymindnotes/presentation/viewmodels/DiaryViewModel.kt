package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecase.GetDiaryListUseCase
import com.bumptech.glide.Glide.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
  private val getDiaryListUseCase: GetDiaryListUseCase
): ViewModel() {

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    // 일기 리스트 가져오기
    // getDiaryList() 결과값 저장 플로우
    private val _diaryList = MutableSharedFlow<Map<String, Object>>()
    val diaryList = _diaryList.asSharedFlow()

    // getDiaryList() 함수 호출
    suspend fun getDiaryList() {
        getDiaryListUseCase.getDiaryList().collect {
            _diaryList.emit(it)
        }
    }

    // 정렬 버튼 감지
    // 날짜별 최신순/오래된순 정렬
    private val _sortDateButton = MutableSharedFlow<Boolean>()
    val sortDateButton = _sortDateButton.asSharedFlow()

    suspend fun clickSortDateButton() {
        _sortDateButton.emit(true)
    }

    // 마음 일기 모음 버튼
    private val _sortEmotionDiaryButton = MutableSharedFlow<Boolean>()
    val sortEmotionDiaryButton = _sortEmotionDiaryButton.asSharedFlow()

    suspend fun clickSortEmotionDiaryButton() {
        _sortEmotionDiaryButton.emit(true)
    }

    // 트라우마 일기 모음 버튼
    private val _sortTraumaButton = MutableSharedFlow<Boolean>()
    val sortTraumaButton = _sortTraumaButton.asSharedFlow()

    suspend fun clickSortTraumaButton() {
        _sortTraumaButton.emit(true)
    }

    // 감정별 정렬 버튼
    private val _sortEmotionButton = MutableSharedFlow<Boolean>()
    val sortEmotionButton = _sortEmotionButton.asSharedFlow()

    suspend fun clickSortEmotionButton() {
        _sortEmotionButton.emit(true)
    }

    init {
        viewModelScope.launch {
            // getDiaryList() 결과값 collect & emit - 생성 시 자동
            getDiaryListUseCase.getDiaryList().collect {
                _diaryList.emit(it)
            }

            // error collect & emit
            getDiaryListUseCase.getDiaryListError.collect {
                _error.emit(it)
            }
        }
    }

}