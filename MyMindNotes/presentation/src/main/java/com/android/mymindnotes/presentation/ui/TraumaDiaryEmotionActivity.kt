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
import com.android.mymindnotes.presentation.databinding.ActivityTraumaDiaryEmotionBinding
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryEmotionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TraumaDiaryEmotionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTraumaDiaryEmotionBinding

    // 뷰모델 객체 주입
    private val viewModel: TraumaDiaryEmotionViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 감정 변수
    private var chosenEmotionId = 0

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getEmotionTempRecords()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraumaDiaryEmotionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is TraumaDiaryEmotionViewModel.TraumaDiaryEmotionUiState.Loading -> {
                            setUpEmotionSelection()
                        }

                        is TraumaDiaryEmotionViewModel.TraumaDiaryEmotionUiState.Success -> {

                            // 만약 감정이 저장된 상태라면, 화면으로 다시 돌아왔을 때 체크 표시가 돼 있게 뿌리기
                            updateEmotion(uiState.emotion)

                            // 만약 감정 text가 저장된 상태라면, 화면으로 다시 돌아왔을 때 그대로 뿌리기
                            updateEmotionText(uiState.emotionText)

                        }
                    }
                }

            }
        }


        // 버튼 클릭
        // 감정 설명서 보기 버튼 클릭
        binding.RecordEmotionHelpButton.setOnClickListener {
            val intent = Intent(applicationContext, EmotionInstructions::class.java)
            startActivity(intent)

        }

        // 어떤 감정을 느꼈나요? 팁 버튼 클릭
        binding.RecordEmotionTips.setOnClickListener {
            tipDialog()
        }

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener {
            if (chosenEmotionId == 0) {
                dialog("감정을 선택해 주세요.")
            } else {
                val emotionText = binding.RecordEmotionUserInput.text.toString()
                // 감정 및 감정 설명 저장
                viewModel.nextOrPreviousButtonClickedOrPaused(chosenEmotionId, emotionText)

                // 다음 화면으로 이동
                val intent = Intent(applicationContext, TraumaDiaryReflectionActivity::class.java)
                startActivity(intent)
            }

        }

        // 이전 버튼 클릭
        binding.RecordPreviousButton.setOnClickListener {

            val emotionText = binding.RecordEmotionUserInput.text.toString()
            // 감정 및 감정 설명 저장
            viewModel.nextOrPreviousButtonClickedOrPaused(chosenEmotionId, emotionText)

            // 이전 화면으로 이동
            finish()

        }

    }

    private fun updateEmotion(emotion: String?) {
        emotion?.let {
            when (it) {
                "기쁨" -> binding.happinessButton.isChecked = true
                "기대" -> binding.anticipationButton.isChecked = true
                "신뢰" -> binding.trustButton.isChecked = true
                "놀람" -> binding.surpriseButton.isChecked = true
                "슬픔" -> binding.sadnessButton.isChecked = true
                "혐오" -> binding.disgustButton.isChecked = true
                "공포" -> binding.fearButton.isChecked = true
                "분노" -> binding.angerButton.isChecked = true
            }
        }
    }

    private fun updateEmotionText(emotionText: String?) {
        emotionText?.let {
            binding.RecordEmotionUserInput.setText(it)
            binding.RecordEmotionUserInput.setSelection(it.length) // 커서를 문자열 끝으로 이동
        }
    }


    private fun setUpEmotionSelection() {
        val emotionGroup1 = binding.emotions1
        val emotionGroup2 = binding.emotions2

        var isChangingCheckedState = false

        emotionGroup1.setOnCheckedChangeListener { _, checkedId ->
            if (!isChangingCheckedState) {
                isChangingCheckedState = true
                emotionGroup2.clearCheck()
                chosenEmotionId = checkedId
                isChangingCheckedState = false
            }
        }

        emotionGroup2.setOnCheckedChangeListener { _, checkedId ->
            if (!isChangingCheckedState) {
                isChangingCheckedState = true
                emotionGroup1.clearCheck()
                chosenEmotionId = checkedId
                isChangingCheckedState = false
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
        builder.setIcon(R.drawable.tips)
        builder.setTitle("감정 작성 Tips")
        builder.setMessage(R.string.emotionTips)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val emotionText = binding.RecordEmotionUserInput.text.toString()
        // 감정 및 감정 설명 저장
        viewModel.nextOrPreviousButtonClickedOrPaused(chosenEmotionId, emotionText)

        // 이전 화면으로 이동
        finish()
    }

    override fun onPause() {
        super.onPause()

        val emotionText = binding.RecordEmotionUserInput.text.toString()
        // 감정 및 감정 설명 저장
        viewModel.nextOrPreviousButtonClickedOrPaused(chosenEmotionId, emotionText)

    }

}