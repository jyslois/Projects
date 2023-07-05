package com.android.mymindnotes.domain.usecases.loginStates
import com.android.mymindnotes.domain.usecases.userInfo.SaveIdAndPasswordUseCase
import javax.inject.Inject

class ChangeAutoSaveBoxStateUseCase @Inject constructor(
    private val saveIdAndPasswordUseCase: SaveIdAndPasswordUseCase,
    private val saveAutoSaveStateUseCase: SaveAutoSaveStateUseCase
) {

    suspend operator fun invoke(isAutoSaveChecked: Boolean, isAutoLoginChecked: Boolean, id: String?, password: String?) {
        if (isAutoSaveChecked || isAutoLoginChecked) {
            saveAutoSaveStateUseCase(true)
            saveIdAndPasswordUseCase(id, password)
        } else {
            saveAutoSaveStateUseCase(false)
            saveIdAndPasswordUseCase(null, null)
        }
    }
}