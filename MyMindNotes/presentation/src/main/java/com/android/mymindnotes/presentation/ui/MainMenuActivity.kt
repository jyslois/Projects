package com.android.mymindnotes.presentation.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityMainMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainmenubackground).into(binding.background)

        // 버튼 클릭 이벤트
        binding.recordDiaryButton.setOnClickListener {
            startActivity<RecordMindChoiceActivity>()
        }

        binding.diaryButton.setOnClickListener {
            startActivity<DiaryActivity>()
        }

        binding.emotionInstructionButton.setOnClickListener {
            startActivity<EmotionInstructions>()
        }

        binding.accountSettingButton.setOnClickListener {
            startActivity<AccountSettingActivity>()
        }

        binding.alarmSettingButton.setOnClickListener {
            startActivity<AlarmSettingActivity>()
        }



    }

    inline fun <reified T> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

}