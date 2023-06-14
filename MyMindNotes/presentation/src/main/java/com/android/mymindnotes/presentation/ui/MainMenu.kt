package com.android.mymindnotes.presentation.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityMainMenuBinding
import com.android.mymindnotes.presentation.viewmodels.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private val viewModel: MainMenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainmenubackground).into(binding.background)

        // 버튼 클릭 이벤트
        binding.recordDiaryButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordDiaryButton()
            }
        }

        binding.diaryButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickDiaryButton()
            }
        }

        binding.emotionInstructionButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickEmotionInstructionButton()
            }
        }

        binding.accountSettingButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickAccountSettingButton()
            }
        }

        binding.alarmSettingButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickAlarmSettingButton()
            }
        }

        // 버튼 클릭 이벤트 감지
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recordDiaryButton.collect {
                        startActivity<RecordMindChoice>()
                    }
                }

                launch {
                    viewModel.diaryButton.collect {
                        startActivity<Diary>()
                    }
                }

                launch {
                    viewModel.emotionInstructionButton.collect {
                        startActivity<EmotionInstructions>()
                    }
                }

                launch {
                    viewModel.accountSettingButton.collect {
                        startActivity<com.android.mymindnotes.presentation.ui.AccountSetting>()
                    }
                }

                launch {
                    viewModel.alarmSettingButton.collect {
                        startActivity<AlarmSetting>()
                    }
                }
            }
        }

    }

    inline fun <reified T> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

}