package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.domain.usecases.diary.trauma.ClearTraumaDiaryTempRecordsUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.GetTraumaDiarySituationUseCase
import com.android.mymindnotes.domain.usecases.diary.trauma.SaveTraumaDiarySituationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class TraumaDiarySituationViewModel @Inject constructor(
    private val saveTraumaDiarySituationUseCase: SaveTraumaDiarySituationUseCase,
    private val getTraumaDiarySituationUseCase: GetTraumaDiarySituationUseCase,
    private val clearTraumaDiaryTempRecordsUseCase: ClearTraumaDiaryTempRecordsUseCase
): ViewModel() {

    // 버튼 클릭
    // 팁 버튼 클릭 감지
    private val _recordTips = MutableSharedFlow<Boolean>()
    val recordTips = _recordTips.asSharedFlow()

    // 팁 버튼 클릭
    suspend fun clickTips() {
        _recordTips.emit(true)
    }

    // 다음 버튼 클릭 감지
    private val _recordNextButton = MutableSharedFlow<Boolean>()
    val recordNextButton = _recordNextButton.asSharedFlow()

    // 다음 버튼 클릭
    suspend fun clickRecordNextButton() {
        _recordNextButton.emit(true)
    }

    // Save Method
    suspend fun saveSituation(situation: String) {
        saveTraumaDiarySituationUseCase(situation)
    }

    // Get Method
    // Situation Result Flow
    private val _situation = MutableSharedFlow<String?>()
    val situation = _situation.asSharedFlow()

    suspend fun getSituation() {
        getTraumaDiarySituationUseCase().collect {
            _situation.emit(it)
        }
    }

    // Clear Methods
    suspend fun clearTraumaDiaryTempRecords() {
        clearTraumaDiaryTempRecordsUseCase()
    }

}