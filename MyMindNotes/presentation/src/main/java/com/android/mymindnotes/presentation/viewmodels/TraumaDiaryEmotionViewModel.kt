package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiaryEmotionUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionColorUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiaryEmotionTextUseCase
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
    private val saveTraumaDiaryEmotionColorUseCase: SaveTraumaDiaryEmotionColorUseCase,
    private val saveTraumaDiaryEmotionTextUseCase: SaveTraumaDiaryEmotionTextUseCase,
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

            saveTraumaDiaryEmotionTextUseCase(emotionText)

            when (chosenEmotionId) {
                R.id.happinessButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.orange_happiness)
                    saveTraumaDiaryEmotionUseCase("기쁨")
                }

                R.id.anticipationButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.green_anticipation)
                    saveTraumaDiaryEmotionUseCase("기대")
                }

                R.id.trustButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.darkblue_trust)
                    saveTraumaDiaryEmotionUseCase("신뢰")
                }

                R.id.surpriseButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.yellow_surprise)
                    saveTraumaDiaryEmotionUseCase("놀람")
                }

                R.id.sadnessButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.grey_sadness)
                    saveTraumaDiaryEmotionUseCase("슬픔")
                }

                R.id.disgustButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.brown_disgust)
                    saveTraumaDiaryEmotionUseCase("혐오")
                }

                R.id.fearButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.black_fear)
                    saveTraumaDiaryEmotionUseCase("공포")
                }

                R.id.angerButton -> {
                    saveTraumaDiaryEmotionColorUseCase(R.drawable.red_anger)
                    saveTraumaDiaryEmotionUseCase("분노")
                }
            }
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