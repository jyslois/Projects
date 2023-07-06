package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionUseCase
import com.android.mymindnotes.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraumaDiaryEmotionViewModel @Inject constructor(
    private val saveTraumaDiaryEmotionUseCase: SaveTraumaDiaryEmotionUseCase,
    private val getTraumaDiaryEmotionUseCase: GetTraumaDiaryEmotionUseCase,
    private val getTraumaDiaryEmotionTextUseCase: GetTraumaDiaryEmotionTextUseCase
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

            lateinit var emotion: String
            var emotionColor = 0

            when (chosenEmotionId) {
                R.id.happinessButton -> {
                    emotionColor = R.drawable.orange_happiness
                    emotion = "기쁨"
                }

                R.id.anticipationButton -> {
                    emotionColor = R.drawable.green_anticipation
                    emotion = "기대"
                }

                R.id.trustButton -> {
                    emotionColor = R.drawable.darkblue_trust
                    emotion = "신뢰"
                }

                R.id.surpriseButton -> {
                    emotionColor = R.drawable.yellow_surprise
                    emotion = "놀람"
                }

                R.id.sadnessButton -> {
                    emotionColor = R.drawable.grey_sadness
                    emotion = "슬픔"
                }

                R.id.disgustButton -> {
                    emotionColor = R.drawable.brown_disgust
                    emotion = "혐오"
                }

                R.id.fearButton -> {
                    emotionColor = R.drawable.black_fear
                    emotion = "공포"
                }

                R.id.angerButton -> {
                    emotionColor = R.drawable.red_anger
                    emotion = "분노"
                }
            }

            saveTraumaDiaryEmotionUseCase(emotion, emotionColor, emotionText)

        }
    }


    suspend fun getEmotionTempRecords() {
        combine(
            getTraumaDiaryEmotionUseCase(),
            getTraumaDiaryEmotionTextUseCase()
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