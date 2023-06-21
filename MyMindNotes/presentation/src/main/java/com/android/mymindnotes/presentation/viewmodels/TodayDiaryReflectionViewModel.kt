package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.SaveTodayDiaryUseCase
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryReflectionViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveTodayDiaryUseCase,

    private val saveTodayDiaryReflectionUseCase: SaveTodayDiaryReflectionUseCase,
    private val saveTodayDiaryTypeUseCase: SaveTodayDiaryTypeUseCase,
    private val saveTodayDiaryRecordDateUseCase: SaveTodayDiaryRecordDateUseCase,
    private val saveTodayDiaryRecordDayUseCase: SaveTodayDiaryRecordDayUseCase,
    private val getTodayDiaryReflectionUseCase: GetTodayDiaryReflectionUseCase,
    private val clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
): ViewModel() {

    // 버튼 클릭
    // 감정 설명서 버튼 클릭 감지
    private val _recordEmotionHelpButton = MutableSharedFlow<Boolean>()
    val recordEmotionHelpButton = _recordEmotionHelpButton.asSharedFlow()

    // 감정 설명서 버튼 클릭
    suspend fun clickRecordEmotionHelpButton() {
        _recordEmotionHelpButton.emit(true)
    }

    // 팁 버튼 클릭 감지
    private val _recordTips = MutableSharedFlow<Boolean>()
    val recordTips = _recordTips.asSharedFlow()

    // 팁 버튼 클릭
    suspend fun clickRecordEmotionTips() {
        _recordTips.emit(true)
    }

    // 이전 버튼 클릭 감지
    private val _recordPreviousButton = MutableSharedFlow<Boolean>()
    val recordPreviousButton = _recordPreviousButton.asSharedFlow()

    // 이전 버튼 클릭
    suspend fun clickRecordPreviousButton() {
        _recordPreviousButton.emit(true)
    }

    // 저장 버튼 클릭 감지
    private val _recordSaveButton = MutableSharedFlow<Boolean>()
    val recordSaveButton = _recordSaveButton.asSharedFlow()

    // 저장 버튼 클릭
    suspend fun clickRecordSaveButton() {
        _recordSaveButton.emit(true)
    }

    // Get Method
    // Reflection Result Flow
    private val _reflection = MutableSharedFlow<String?>()
    val reflection = _reflection.asSharedFlow()

    suspend fun getReflection() {
        getTodayDiaryReflectionUseCase().collect {
            _reflection.emit(it)
        }
    }

    // Save Methods
    suspend fun saveReflection(reflection: String?) {
        saveTodayDiaryReflectionUseCase(reflection)
    }

    suspend fun saveType(type: String) {
        saveTodayDiaryTypeUseCase(type)
    }

    suspend fun saveDate(date: String) {
        saveTodayDiaryRecordDateUseCase(date)
    }

    suspend fun saveDay(day: String) {
        saveTodayDiaryRecordDayUseCase(day)
    }

    // 일기 저장 결과 플로우
    private val _saveDiary = MutableSharedFlow<Map<String, Object>>()
    val saveDiary = _saveDiary.asSharedFlow()

    // (서버) 일기 저장 함수 호출
    suspend fun saveDiary() {
        saveDiaryUseCase().collect {
            _saveDiary.emit(it)
        }
    }

    // 에러 메시지
    private val _error = MutableSharedFlow<Boolean>()
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            // 일기 저장 에러 값 구독
            launch {
                saveDiaryUseCase.error.collect {
                    _error.emit(it)
                }
            }
        }
    }

    // Clear Methods
    suspend fun clearTodayDiaryTempRecords() {
        clearTodayDiaryTempRecordsUseCase()
    }
}