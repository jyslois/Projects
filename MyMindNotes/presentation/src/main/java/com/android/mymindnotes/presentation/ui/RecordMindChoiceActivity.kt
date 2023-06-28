package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityRecordMindChoiceBinding
import com.android.mymindnotes.presentation.viewmodels.RecordMindChoiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordMindChoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordMindChoiceBinding
    // 뷰모델 객체 주입
    private val viewModel: RecordMindChoiceViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onResume() {
        super.onResume()
        viewModel.getNickNameFromUserInfo()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecordMindChoiceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground2).into(binding.choicebackground)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is RecordMindChoiceViewModel.RecordMindChoiceUiState.Loading -> {
                            binding.recordText.text = "오늘은 어떤 마음에 대해 알아 볼까요?"
                        }

                        // 닉네임 가져오기
                        is RecordMindChoiceViewModel.RecordMindChoiceUiState.Success -> {
                            binding.nickNameText.text = "${uiState.nickName} 님,"
                        }

                        // 애러 구독
                        is RecordMindChoiceViewModel.RecordMindChoiceUiState.Error -> {
                            dialog(uiState.error)
                        }

                    }
                }
            }
        }


        // 버튼 클릭 이벤트
        // 오늘의 마음 일기 버튼 클릭
        binding.todayEmotionButton.setOnClickListener {
            val intent = Intent(applicationContext, TodayDiaryEmotionActivity::class.java)
            startActivity(intent)
        }

        // 트라우마 일기 버튼 클릭
        binding.traumaButton.setOnClickListener {
            val intent = Intent(applicationContext, TraumaDiarySituationActivity::class.java)
            startActivity(intent)
        }

    }

    // error dialogue
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }
}