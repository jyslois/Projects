package com.android.mymindnotes.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.android.mymindnotes.core.model.UserDiary
import com.android.mymindnotes.domain.usecases.diary.DeleteDiaryUseCase
import com.android.mymindnotes.domain.usecases.diary.GetDiaryListUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DiaryResultViewModel @Inject constructor(
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val getDiaryListUseCase: GetDiaryListUseCase
) : ViewModel() {

    sealed class DiaryResultUiState {
        object Loading : DiaryResultUiState()
        object Finish : DiaryResultUiState()
        data class Success(val diaryList: ArrayList<UserDiary>?) : DiaryResultUiState()
        data class Error(val error: String) : DiaryResultUiState()
    }

    // ui상태
    private val _uiState = MutableStateFlow<DiaryResultUiState>(DiaryResultUiState.Loading)
    val uiState: StateFlow<DiaryResultUiState> = _uiState


    // 다이어리 리스트 불러오기
    suspend fun getDiaryList() {

        getDiaryListUseCase().collect {
            when {
                it.isSuccess -> {
                    val gson = Gson()
                    val type = object : TypeToken<List<UserDiary?>?>() {}.type
                    val jsonResult = gson.toJson(it.getOrNull())
                    val diaryList: ArrayList<UserDiary>? = gson.fromJson(jsonResult, type)
                    _uiState.value = DiaryResultUiState.Success(diaryList)
                }

                it.isFailure -> {
                    _uiState.value = DiaryResultUiState.Error(it.exceptionOrNull()?.message ?: "일기를 불러오는 데 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")

                    _uiState.value = DiaryResultUiState.Loading
                }
            }
        }

    }

    // 일기 삭제하기
    suspend fun deleteDiaryButtonClicked(diaryNumber: Int) {

        deleteDiaryUseCase(diaryNumber).collect {
            when {
                it.isSuccess -> _uiState.value = DiaryResultUiState.Finish

                it.isFailure -> {
                    _uiState.value = DiaryResultUiState.Error(it.exceptionOrNull()?.message ?: "일기 삭제에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.")

                    _uiState.value = DiaryResultUiState.Loading
                }
            }
        }

    }

}