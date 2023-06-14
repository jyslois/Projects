package com.android.mymindnotes.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class DiaryResultFragmentFactory @AssistedInject constructor (
    @Assisted private val diaryInfo: Bundle?
) : FragmentFactory() {

    // 인자를 받는 생성자 사용
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            SituationFragment::class.java.name -> SituationFragment().apply {
                arguments = diaryInfo
            }
            ThoughtFragment::class.java.name -> ThoughtFragment().apply {
                arguments = diaryInfo
            }
            EmotionFragment::class.java.name -> EmotionFragment().apply {
                arguments = diaryInfo
            }
            ReflectionFragment::class.java.name -> ReflectionFragment().apply {
                arguments = diaryInfo
            }
            else -> super.instantiate(classLoader, className)
        }
    }

}