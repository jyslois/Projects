package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityEmotionSortingPopupBinding
import com.android.mymindnotes.presentation.viewmodels.EmotionPopupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmotionSortingPopup : AppCompatActivity() {
    private lateinit var binding: ActivityEmotionSortingPopupBinding

    // 뷰모델 객체 주입
    private val viewModel: EmotionPopupViewModel by viewModels()

    // 데이터 보내기
    private fun sendIntent(emotion: String?) {
        val intent = Intent()
        intent.putExtra("emotion", emotion)
        setResult(RESULT_OK, intent)
        //액티비티(팝업) 닫기
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmotionSortingPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var emotionGroup = binding.emotionsGroup

        emotionGroup.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
            lifecycleScope.launch {
                viewModel.clickEmotion(checkedId)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.emotion.collect {
                    when (it) {
                        R.id.allEmotionsButton -> sendIntent("모든 감정")
                        R.id.happinessButton -> sendIntent("기쁨")
                        R.id.anticipationButton -> sendIntent("기대")
                        R.id.trustButton -> sendIntent("신뢰")
                        R.id.surpriseButton -> sendIntent("놀람")
                        R.id.sadnessButton -> sendIntent("슬픔")
                        R.id.disgustButton -> sendIntent("혐오")
                        R.id.fearButton -> sendIntent("공포")
                        R.id.angerButton -> sendIntent("분노")
                    }
                }
            }
        }
    }
}