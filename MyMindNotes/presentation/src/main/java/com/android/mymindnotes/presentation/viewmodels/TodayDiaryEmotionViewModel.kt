package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionPartsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionPartsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryEmotionViewModel @Inject constructor(
    private val saveTodayDiaryEmotionPartsUseCase: SaveTodayDiaryEmotionPartsUseCase,
    private val getTodayDiaryEmotionPartsUseCase: GetTodayDiaryEmotionPartsUseCase,
    private val clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
): ViewModel() {


    sealed class TodayDiaryEmotionUiState {
        data class Success(val emotion: String?, val emotionText: String?): TodayDiaryEmotionUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TodayDiaryEmotionUiState>()
    val uiState: SharedFlow<TodayDiaryEmotionUiState> = _uiState


    // Save Methods
    suspend fun saveEmotionColor(color: Int) {
        saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(color)
    }

    suspend fun saveEmotion(emotion: String) {
        saveTodayDiaryEmotionPartsUseCase.saveEmotion(emotion)
    }

    suspend fun saveEmotionText(emotionText: String?) {
        saveTodayDiaryEmotionPartsUseCase.saveEmotionText(emotionText)
    }

    // Clear Methods
    suspend fun clearTodayDiaryTempRecords() {
        clearTodayDiaryTempRecordsUseCase()
    }

    init {
        viewModelScope.launch {
            combine(
                getTodayDiaryEmotionPartsUseCase.getEmotion(),
                getTodayDiaryEmotionPartsUseCase.getEmotionText()
            ) { emotion, emotionText ->
                TodayDiaryEmotionUiState.Success(emotion, emotionText)
            }.collect {
                _uiState.emit(it)
            }
        }
    }


}