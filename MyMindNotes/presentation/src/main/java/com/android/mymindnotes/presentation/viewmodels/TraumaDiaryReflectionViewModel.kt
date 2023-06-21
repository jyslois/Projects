package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.SaveTraumaDiaryUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDateUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryRecordDayUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryReflectionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryReflectionViewModel @Inject constructor(
    private val saveDiaryUseCase: SaveTraumaDiaryUseCase,
    private val saveTraumaDiaryReflectionUseCase: SaveTraumaDiaryReflectionUseCase,
    private val saveTraumaDiaryTypeUseCase: SaveTraumaDiaryTypeUseCase,
    private val saveTraumaDiaryRecordDateUseCase: SaveTraumaDiaryRecordDateUseCase,
    private val saveTraumaDiaryRecordDayUseCase: SaveTraumaDiaryRecordDayUseCase,
    private val getTraumaDiaryReflectionUseCase: GetTraumaDiaryReflectionUseCase,
    private val clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
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
        getTraumaDiaryReflectionUseCase().collect {
            _reflection.emit(it)
        }
    }

    // Save Methods
    suspend fun saveReflection(reflection: String?) {
        saveTraumaDiaryReflectionUseCase(reflection)
    }

    suspend fun saveType(type: String) {
        saveTraumaDiaryTypeUseCase(type)
    }

    suspend fun saveDate(date: String) {
        saveTraumaDiaryRecordDateUseCase(date)
    }

    suspend fun saveDay(day: String) {
        saveTraumaDiaryRecordDayUseCase(day)
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
    suspend fun clearTraumaDiaryTempRecords() {
        clearTraumaDiaryTempRecordsUseCase()
    }
}