package com.android.mymindnotes.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mymindnotes.core.dto.Diary
import com.android.mymindnotes.presentation.databinding.ActivityEmotionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EmotionFragment @Inject constructor(

) : Fragment() {
    private lateinit var binding: ActivityEmotionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityEmotionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contentEmotion.text = arguments?.getString("emotion")
        binding.contentEmotionDescription.text = arguments?.getString("emotionDescription")
    }

    fun refreshData(diary: Diary) {
        binding.contentEmotion.text = diary.emotion
        binding.contentEmotionDescription.text = diary.emotionDescription
    }

}