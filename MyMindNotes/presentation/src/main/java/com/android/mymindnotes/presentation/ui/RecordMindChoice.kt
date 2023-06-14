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
class RecordMindChoice : AppCompatActivity() {
    private lateinit var binding: ActivityRecordMindChoiceBinding
    // 뷰모델 객체 주입
    private val viewModel: RecordMindChoiceViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecordMindChoiceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground2).into(binding.choicebackground)

        // 버튼 클릭 이벤트
        // 오늘의 마음 일기 버튼 클릭
        binding.todayEmotionButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickTodayEmotionButton()
            }
        }

        // 트라우마 일기 버튼 클릭
        binding.traumaButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickTraumaButton()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 닉네임 세팅 ㅡ 회원정보 값 구독
                launch {
                    viewModel.userInfo.collect {
                        val nick = it["nickname"] as String?
                        if (nick != null) {
                            binding.nickNameText.text = "$nick 님,"
                        }
                    }
                }

                // 클릭 이벤트 감지
                // 오늘의 마음 일기 버튼 클릭 감지
                launch {
                    viewModel.clickTodayEmotionButton.collect {
                        if (it) {
                            val intent = Intent(applicationContext, TodayDiaryEmotion::class.java)
                            startActivity(intent)
                        }
                    }
                }

                // 트라우마 일기 버튼 클릭 감지
                launch {
                    viewModel.clickTraumaButton.collect {
                        if (it) {
                            val intent = Intent(applicationContext, TraumaDiarySituation::class.java)
                            startActivity(intent)
                        }
                    }
                }

                // 에러 감지
                // 에러 값 구독
                launch {
                    viewModel.error.collect {
                        if (it) {
                            dialog("서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 앱을 다시 시작해주세요.")
                        }
                    }
                }
            }
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