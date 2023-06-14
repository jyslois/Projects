package com.android.mymindnotes.presentation.ui

import android.os.Bundle
import dagger.assisted.AssistedFactory

@AssistedFactory
interface DiaryResultFragmentFactoryAssistedFactory {
    fun create(diaryInfo: Bundle?): DiaryResultFragmentFactory
}