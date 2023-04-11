package com.android.mymindnotes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.mymindnotes.data.retrofit.model.diary.UserDiary
import com.android.mymindnotes.EmotionFragment
import com.android.mymindnotes.databinding.ActivityEmotionFragmentBinding
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
        binding.contentEmotionDescription.text = arguments?.getString("emotionText")
    }

    fun refreshData(diary: UserDiary) {
        binding.contentEmotion.text = diary.emotion
        binding.contentEmotionDescription.text = diary.emotionDescription
    }

//    companion object {
//        fun newInstance(bundle: Bundle?): EmotionFragment {
//            val fragment = EmotionFragment()
//            fragment.arguments = bundle
//            return fragment
//        }
//    }
}