package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mymindnotes.core.model.UserDiary
import com.android.mymindnotes.domain.usecases.diary.GetDiaryListUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
  private val getDiaryListUseCase: GetDiaryListUseCase
): ViewModel() {

    sealed class DiaryUiState {
        object Loading: DiaryUiState()
        data class Success(val diaryList: ArrayList<UserDiary>?): DiaryUiState()
        data class Error(val error: String): DiaryUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryUiState>(DiaryUiState.Loading)
    val uiState: StateFlow<DiaryUiState> = _uiState


    // getDiaryList() 함수 호출
    suspend fun getDiaryList() {
        try {
            getDiaryListUseCase().collect {
                if (it["code"].toString().toDouble() == 7000.0) {
                    // 서버로부터 리스트 받아와서 저장하기 , https://ppizil.tistory.com/4
                    val gson = Gson()
                    val type = object : TypeToken<ArrayList<UserDiary>?>() {}.type
                    val jsonResult = gson.toJson(it["diaryList"])
                    val diaryList: ArrayList<UserDiary>? = gson.fromJson(jsonResult, type)
                    _uiState.value = DiaryUiState.Success(diaryList)
                }
            }
        } catch (e: Exception) {
            _uiState.value = DiaryUiState.Error("일기 목록 가져오기에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")
        }

    }

    init {
        viewModelScope.launch {
            getDiaryList()
        }
    }

}