package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.domain.usecases.diary.today.ClearTodayDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.today.GetTodayDiaryEmotionUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionColorUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionTextUseCase
import com.android.mymindnotes.domain.usecases.diary.today.SaveTodayDiaryEmotionUseCase
import com.android.mymindnotes.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayDiaryEmotionViewModel @Inject constructor(
    private val saveTodayDiaryEmotionUseCase: SaveTodayDiaryEmotionUseCase,
    private val saveTodayDiaryEmotionColorUseCase: SaveTodayDiaryEmotionColorUseCase,
    private val saveTodayDiaryEmotionTextUseCase: SaveTodayDiaryEmotionTextUseCase,
    private val getTodayDiaryEmotionUseCase: GetTodayDiaryEmotionUseCase,
    private val getTodayDiaryEmotionTextUseCase: GetTodayDiaryEmotionTextUseCase,
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

            saveTodayDiaryEmotionTextUseCase(emotionText)

            when (chosenEmotionId) {
                R.id.happinessButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.orange_happiness)
                    saveTodayDiaryEmotionUseCase("기쁨")
                }

                R.id.anticipationButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.green_anticipation)
                    saveTodayDiaryEmotionUseCase("기대")
                }

                R.id.trustButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.darkblue_trust)
                    saveTodayDiaryEmotionUseCase("신뢰")
                }

                R.id.surpriseButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.yellow_surprise)
                    saveTodayDiaryEmotionUseCase("놀람")
                }

                R.id.sadnessButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.grey_sadness)
                    saveTodayDiaryEmotionUseCase("슬픔")
                }

                R.id.disgustButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.brown_disgust)
                    saveTodayDiaryEmotionUseCase("혐오")
                }

                R.id.fearButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.black_fear)
                    saveTodayDiaryEmotionUseCase("공포")
                }

                R.id.angerButton -> {
                    saveTodayDiaryEmotionColorUseCase(R.drawable.red_anger)
                    saveTodayDiaryEmotionUseCase("분노")
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
            getTodayDiaryEmotionUseCase(),
            getTodayDiaryEmotionTextUseCase()
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