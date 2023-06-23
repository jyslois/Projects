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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
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

    sealed class TodayDiaryReflectionUiState {
        data class Success(val reflectionResult: String?, val saveDiaryResult: Map<String, Object>?): TodayDiaryReflectionUiState()
        data class Error(val error: Boolean): TodayDiaryReflectionUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TodayDiaryReflectionUiState>()
    val uiState: SharedFlow<TodayDiaryReflectionUiState> = _uiState

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

    // Clear Methods
    suspend fun clearTodayDiaryTempRecords() {
        clearTodayDiaryTempRecordsUseCase()
    }

    // (서버) 일기 저장 함수 호출
    suspend fun saveDiary() {
        saveDiaryUseCase().collect {
            _uiState.emit(TodayDiaryReflectionUiState.Success(null, it))
        }
    }

    init {
        viewModelScope.launch {

            val getReflectionFlow = getTodayDiaryReflectionUseCase().map { TodayDiaryReflectionUiState.Success(it, null) }
            val saveDiaryErrorFlow = saveDiaryUseCase.error.map { TodayDiaryReflectionUiState.Error(it) }

            merge(getReflectionFlow, saveDiaryErrorFlow).collect {
                _uiState.emit(it)
            }
        }
    }

}