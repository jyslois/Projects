package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionPartsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionPartsUseCase
import com.android.mymindnotes.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryEmotionViewModel @Inject constructor(
    private val saveTraumaDiaryEmotionPartsUseCase: SaveTraumaDiaryEmotionPartsUseCase,
    private val getTraumaDiaryEmotionPartsUseCase: GetTraumaDiaryEmotionPartsUseCase
): ViewModel() {

    sealed class TraumaDiaryEmotionUiState {
        object Loading : TraumaDiaryEmotionUiState()
        data class Success(val emotion: String?, val emotionText: String?): TraumaDiaryEmotionUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<TraumaDiaryEmotionUiState>(TraumaDiaryEmotionUiState.Loading)
    val uiState: StateFlow<TraumaDiaryEmotionUiState> = _uiState

    fun nextOrPreviousButtonClickedOrPaused(chosenEmotionId: Int, emotionText: String?) {

        viewModelScope.launch {

            saveTraumaDiaryEmotionPartsUseCase.saveEmotionText(emotionText)

            when (chosenEmotionId) {
                R.id.happinessButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.orange_happiness)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("기쁨")
                }

                R.id.anticipationButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.green_anticipation)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("기대")
                }

                R.id.trustButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.darkblue_trust)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("신뢰")
                }

                R.id.surpriseButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.yellow_surprise)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("놀람")
                }

                R.id.sadnessButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.grey_sadness)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("슬픔")
                }

                R.id.disgustButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.brown_disgust)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("혐오")
                }

                R.id.fearButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.black_fear)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("공포")
                }

                R.id.angerButton -> {
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.red_anger)
                    saveTraumaDiaryEmotionPartsUseCase.saveEmotion("분노")
                }
            }
        }
    }


    suspend fun getEmotionTempRecords() {
        combine(
            getTraumaDiaryEmotionPartsUseCase.getEmotion(),
            getTraumaDiaryEmotionPartsUseCase.getEmotionText()
        ) { emotion, emotionText ->
            TraumaDiaryEmotionUiState.Success(emotion, emotionText)
        }.collect {
            _uiState.emit(it)
        }
    }

    init {
        viewModelScope.launch {
            getEmotionTempRecords()
        }
    }

}