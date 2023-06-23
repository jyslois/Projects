package com.android.mymindnotes.presentation.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityTodayDiaryEmotionBinding
import com.android.mymindnotes.presentation.viewmodels.TodayDiaryEmotionViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodayDiaryEmotion : AppCompatActivity() {
    private lateinit var binding: ActivityTodayDiaryEmotionBinding

    // 뷰모델 객체 주입
    private val viewModel: TodayDiaryEmotionViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 감정 변수
    var chosenEmotionId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayDiaryEmotionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground)

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
            lifecycleScope.launch {
                // emotion 저장
                if (chosenEmotionId == 0) {
                    dialog("감정을 선택해 주세요.")
                } else {
                    when (chosenEmotionId) {
                        R.id.happinessButton -> {
                            viewModel.saveEmotionColor(R.drawable.orange_happiness)
                            viewModel.saveEmotion("기쁨")
                        }
                        R.id.anticipationButton -> {
                            viewModel.saveEmotionColor(R.drawable.green_anticipation)
                            viewModel.saveEmotion("기대")
                        }
                        R.id.trustButton -> {
                            viewModel.saveEmotionColor(R.drawable.darkblue_trust)
                            viewModel.saveEmotion("신뢰")
                        }
                        R.id.surpriseButton -> {
                            viewModel.saveEmotionColor(R.drawable.yellow_surprise)
                            viewModel.saveEmotion("놀람")
                        }
                        R.id.sadnessButton -> {
                            viewModel.saveEmotionColor(R.drawable.grey_sadness)
                            viewModel.saveEmotion("슬픔")
                        }
                        R.id.disgustButton -> {
                            viewModel.saveEmotionColor(R.drawable.brown_disgust)
                            viewModel.saveEmotion("혐오")
                        }
                        R.id.fearButton -> {
                            viewModel.saveEmotionColor(R.drawable.black_fear)
                            viewModel.saveEmotion("공포")
                        }
                        R.id.angerButton -> {
                            viewModel.saveEmotionColor(R.drawable.red_anger)
                            viewModel.saveEmotion("분노")
                        }
                    }
                    // 감정Text 저장
                    val emotionText = binding.RecordEmotionUserInput.text.toString()
                    viewModel.saveEmotionText(emotionText)

                    val intent = Intent(applicationContext, TodayDiarySituation::class.java)
                    startActivity(intent)
                }
            }
        }

        // 감정 선택
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


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is TodayDiaryEmotionViewModel.TodayDiaryEmotionUiState.Success -> {

                            // 만약 감정이 저장된 상태라면, 화면으로 다시 돌아왔을 때 체크 표시가 돼 있게 뿌리기
                            uiState.emotion?.let {
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

                            // 만약 감정 text가 저장된 상태라면, 화면으로 다시 돌아왔을 때 그대로 뿌리기
                            uiState.emotionText?.let {
                                if (it != "") {
                                    binding.RecordEmotionUserInput.setText(it)
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    private var dialogListener =
        DialogInterface.OnClickListener { _: DialogInterface?, which: Int ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                // 기록 삭제
                lifecycleScope.launch {
                    viewModel.clearTodayDiaryTempRecords()
                }
                finish()
            }
        }

    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("입력한 기록이 사라져요. 정말 종료하시겠어요?")
        builder.setNegativeButton("종료", dialogListener)
        builder.setPositiveButton("계속 작성", null)
        val alertDialog = builder.show()
        alertDialog.show()
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

}