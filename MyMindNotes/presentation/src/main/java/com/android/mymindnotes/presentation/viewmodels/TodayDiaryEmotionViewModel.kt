package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionPartsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionPartsUseCase
import com.android.mymindnotes.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryEmotionViewModel @Inject constructor(
    private val saveTodayDiaryEmotionPartsUseCase: SaveTodayDiaryEmotionPartsUseCase,
    private val getTodayDiaryEmotionPartsUseCase: GetTodayDiaryEmotionPartsUseCase,
    private val clearTodayDiaryTempRecordsUseCase: ClearTodayDiaryTempRecordsUseCase
) : ViewModel() {


    sealed class TodayDiaryEmotionUiState {
        object Loading : TodayDiaryEmotionUiState()
        data class Success(val emotion: String?, val emotionText: String?) :
            TodayDiaryEmotionUiState()
    }

    // ui상태
    private val _uiState =
        MutableStateFlow<TodayDiaryEmotionUiState>(TodayDiaryEmotionUiState.Loading)
    val uiState: StateFlow<TodayDiaryEmotionUiState> = _uiState

    fun nextButtonClicked(chosenEmotionId: Int, emotionText: String?) {

        viewModelScope.launch {

            saveTodayDiaryEmotionPartsUseCase.saveEmotionText(emotionText)

            when (chosenEmotionId) {
                R.id.happinessButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.orange_happiness)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("기쁨")
                }

                R.id.anticipationButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.green_anticipation)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("기대")
                }

                R.id.trustButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.darkblue_trust)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("신뢰")
                }

                R.id.surpriseButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.yellow_surprise)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("놀람")
                }

                R.id.sadnessButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.grey_sadness)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("슬픔")
                }

                R.id.disgustButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.brown_disgust)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("혐오")
                }

                R.id.fearButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.black_fear)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("공포")
                }

                R.id.angerButton -> {
                    saveTodayDiaryEmotionPartsUseCase.saveEmotionColor(R.drawable.red_anger)
                    saveTodayDiaryEmotionPartsUseCase.saveEmotion("분노")
                }
            }
        }


    }

    fun finishButtonClicked() {
        viewModelScope.launch {
            clearTodayDiaryTempRecordsUseCase()
        }
    }

    suspend fun getEmotionTempRecords() {
        combine(
            getTodayDiaryEmotionPartsUseCase.getEmotion(),
            getTodayDiaryEmotionPartsUseCase.getEmotionText()
        ) { emotion, emotionText ->
            TodayDiaryEmotionUiState.Success(emotion, emotionText)
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