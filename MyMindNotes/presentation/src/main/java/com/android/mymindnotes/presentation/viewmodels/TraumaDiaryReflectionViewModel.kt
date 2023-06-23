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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
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

    sealed class TraumaDiaryReflectionUiState {
        data class Success(val reflectionResult: String?, val saveDiaryResult: Map<String, Object>?): TraumaDiaryReflectionUiState()
        data class Error(val error: Boolean): TraumaDiaryReflectionUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TraumaDiaryReflectionUiState>()
    val uiState: SharedFlow<TraumaDiaryReflectionUiState> = _uiState


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

    // Clear Methods
    suspend fun clearTraumaDiaryTempRecords() {
        clearTraumaDiaryTempRecordsUseCase()
    }


    // (서버) 일기 저장 함수 호출
    suspend fun saveDiary() {
        saveDiaryUseCase().collect {
            _uiState.emit(TraumaDiaryReflectionUiState.Success(null, it))
        }
    }

    init {
        viewModelScope.launch {

            val getReflectionFlow = getTraumaDiaryReflectionUseCase().map { TraumaDiaryReflectionUiState.Success(it, null) }
            val saveDiaryErrorFlow = saveDiaryUseCase.error.map { TraumaDiaryReflectionUiState.Error(it) }

            merge(getReflectionFlow, saveDiaryErrorFlow).collect {
                _uiState.emit(it)
            }
        }
    }


}