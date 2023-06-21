package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryThoughtUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryThoughtUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryThoughtViewModel @Inject constructor(
    private val traumaDiaryThoughtUseCase: SaveTraumaDiaryThoughtUseCase,
    private val getTraumaDiaryThoughtUseCase: GetTraumaDiaryThoughtUseCase
): ViewModel() {
    // 버튼 클릭
    // 팁 버튼 클릭 감지
    private val _recordTips = MutableSharedFlow<Boolean>()
    val recordTips = _recordTips.asSharedFlow()

    // 팁 버튼 클릭
    suspend fun clickTips() {
        _recordTips.emit(true)
    }

    // 다음 버튼 클릭 감지
    private val _recordNextButton = MutableSharedFlow<Boolean>()
    val recordNextButton = _recordNextButton.asSharedFlow()

    // 다음 버튼 클릭
    suspend fun clickRecordNextButton() {
        _recordNextButton.emit(true)
    }

    // 이전 버튼 클릭 감지
    private val _recordPreviousButton = MutableSharedFlow<Boolean>()
    val recordPreviousButton = _recordPreviousButton.asSharedFlow()

    // 이전 버튼 클릭
    suspend fun clickRecordPreviousButton() {
        _recordPreviousButton.emit(true)
    }

    // Save Method
    suspend fun saveThought(thought: String) {
        traumaDiaryThoughtUseCase(thought)
    }

    // Get Method
    private val _thought = MutableSharedFlow<String?>()
    val thought = _thought.asSharedFlow()

    suspend fun getThought() {
        getTraumaDiaryThoughtUseCase().collect {
            _thought.emit(it)
        }
    }
}