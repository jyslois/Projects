package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionPartsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionPartsUseCase
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
class TraumaDiaryEmotionViewModel @Inject constructor(
    private val saveTraumaDiaryEmotionPartsUseCase: SaveTraumaDiaryEmotionPartsUseCase,
    private val getTraumaDiaryEmotionPartsUseCase: GetTraumaDiaryEmotionPartsUseCase
): ViewModel() {

    sealed class TraumaDiaryEmotionUiState {
        data class Success(val emotion: String?, val emotionText: String?): TraumaDiaryEmotionUiState()
    }

    // ui상태
    private val _uiState = MutableSharedFlow<TraumaDiaryEmotionUiState>()
    val uiState: SharedFlow<TraumaDiaryEmotionUiState> = _uiState

    // Save Methods
    suspend fun saveEmotionColor(color: Int) {
        saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(color)
    }

    suspend fun saveEmotion(emotion: String?) {
        saveTraumaDiaryEmotionPartsUseCase.saveEmotion(emotion)
    }

    suspend fun saveEmotionText(emotionText: String?) {
        saveTraumaDiaryEmotionPartsUseCase.saveEmotionText(emotionText)
    }

    init {
        viewModelScope.launch {
            combine(
                getTraumaDiaryEmotionPartsUseCase.getEmotion(),
                getTraumaDiaryEmotionPartsUseCase.getEmotionText()
            ) { emotion, emotionText ->
               TraumaDiaryEmotionUiState.Success(emotion, emotionText)
            }.collect {
                _uiState.emit(it)
            }
        }
    }

}