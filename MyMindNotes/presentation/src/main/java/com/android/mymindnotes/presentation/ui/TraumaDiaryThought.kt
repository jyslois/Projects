package com.android.mymindnotes.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.databinding.ActivityTraumaDiaryThoughtBinding
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryThoughtViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TraumaDiaryThought : AppCompatActivity() {
    private lateinit var binding: ActivityTraumaDiaryThoughtBinding

    // 뷰모델 객체 주입
    private val viewModel: TraumaDiaryThoughtViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraumaDiaryThoughtBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(com.android.mymindnotes.presentation.R.drawable.diarybackground1).into(binding.recordbackground)


        // 팁 버튼 클릭
        binding.RecordThoughtTips.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickTips()
            }
        }

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordNextButton()
            }
        }

        // 이전 버튼 클릭
        binding.RecordPreviousButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordPreviousButton()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 만약 생각이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
                launch {
                    // getThought() Result 구독
                    viewModel.thought.collect {
                        if (it != "") {
                            binding.RecordThoughtUserInput.setText(it)
                        }
                    }
                }

                launch {
                    viewModel.getThought()
                }

                // 버튼 클릭 감지
                launch {
                    // 팁 버튼 클릭 감지
                    viewModel.recordTips.collect {
                        tipDialog()
                    }
                }

                launch {
                    // 다음 버튼 클릭 감지
                    viewModel.recordNextButton.collect {
                        val thought = binding.RecordThoughtUserInput.text.toString()
                        if (thought == "") {
                            dialog("생각을 작성해 주세요.")
                        } else {
                            // 상황 저장
                            viewModel.saveThought(thought)
                            // 다음 화면으로 이동
                            val intent = Intent(applicationContext, TraumaDiaryEmotion::class.java)
                            startActivity(intent)
                        }
                    }
                }

                launch {
                    // 이전 버튼 감지
                    viewModel.recordPreviousButton.collect {
                        val thought = binding.RecordThoughtUserInput.text.toString()
                        if (thought != "") {
                            // 상황 저장
                            viewModel.saveThought(thought)
                        }
                        // 이전 화면으로 이동
                        finish()
                    }
                }
            }
        }
    }

    // 알림 dialogue
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // 팁 dialogue
    private fun tipDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(com.android.mymindnotes.presentation.R.drawable.tips)
        builder.setTitle("생각 작성 Tips")
        builder.setMessage(com.android.mymindnotes.presentation.R.string.traumaThoughtTips)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // backprssed 시 생각 저장 후 뒤로가기
    override fun onBackPressed() {
        // 생각 저장
        lifecycleScope.launch {
            val thought = binding.RecordThoughtUserInput.text.toString()
            if (thought != "") {
                // 생각 저장
                viewModel.saveThought(thought)
            }
            // 이전 화면으로 이동
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            val thought = binding.RecordThoughtUserInput.text.toString()
            if (thought != "") {
                // 생각 저장
                viewModel.saveThought(thought)
            }
        }
    }
}