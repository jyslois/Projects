package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.DeleteDiaryUseCase
import com.android.mymindnotes.domain.usecases.GetDiaryListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryResultViewModel @Inject constructor(
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getDiaryListUseCase: GetDiaryListUseCase
): ViewModel() {

    // 다이어리 리스트 불러오기
    private val _diaryList = MutableSharedFlow<Map<String, Object>>()
    val diaryList = _diaryList.asSharedFlow()

    suspend fun getDiaryList() {
        getDiaryListUseCase.getDiaryList().collect {
            _diaryList.emit(it)
        }
    }

    // 에러
    private val _getDiaryListError = MutableSharedFlow<Boolean>()
    val getDiaryListError = _getDiaryListError.asSharedFlow()

    private val _deleteDiaryError = MutableSharedFlow<Boolean>()
    val deleteDiaryError = _deleteDiaryError.asSharedFlow()

    // 버튼 클릭 감지
    // 돌아가기 버튼 클릭 감지 플로우
    private val _backToListButton = MutableSharedFlow<Boolean>()
    val backToListButton = _backToListButton.asSharedFlow()

    suspend fun clickBackToListButton() {
        _backToListButton.emit(true)
    }

    // 수정하기 버튼 클릭 감지 플로우
    private val _editButton = MutableSharedFlow<Boolean>()
    val editButton = _editButton.asSharedFlow()

    suspend fun clickEditButton() {
        _editButton.emit(true)
    }

    // 삭제하기 버튼 클릭 감지 플로우
    private val _deleteButton = MutableSharedFlow<Boolean>()
    val deleteButton = _deleteButton.asSharedFlow()

    suspend fun clickDeleteButton() {
        _deleteButton.emit(true)
    }

    // 일기 삭제하기
    // 일기 삭제하기 저장 플로우
    private val _deleteDiaryResult = MutableSharedFlow<Map<String, Object>>()
    val deleteDiaryResult = _deleteDiaryResult.asSharedFlow()

    // 일기 삭제하기
    suspend fun deleteDiary(diaryNumber: Int) {
        deleteDiaryUseCase.deleteDiary(diaryNumber).collect {
            _deleteDiaryResult.emit(it)
        }
    }

    init {
        viewModelScope.launch {
            // error collect & emit
            getDiaryListUseCase.getDiaryListError.collect {
                _getDiaryListError.emit(it)
            }
        }

        viewModelScope.launch {
            deleteDiaryUseCase.deleteDiaryError.collect {
                _deleteDiaryError.emit(it)
            }
        }
    }

}