package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import android.content.Intent
import com.android.mymindnotes.EmotionInstructions
import com.android.mymindnotes.Old_Reflection
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.databinding.ActivityTraumaDiaryEmotionBinding
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryEmotionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TraumaDiaryEmotion : AppCompatActivity() {
    private lateinit var binding: ActivityTraumaDiaryEmotionBinding

    // 뷰모델 객체 주입
    private val viewModel: TraumaDiaryEmotionViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 감정 선택 체크 변수
    private var isChecking = true
    private var chosenEmotionId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraumaDiaryEmotionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground)

        // 버튼 클릭
        // 감정 설명서 보기 버튼 클릭
        binding.RecordEmotionHelpButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordEmotionHelpButton()
            }

        }

        // 어떤 감정을 느꼈나요? 팁 버튼 클릭
        binding.RecordEmotionTips.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordEmotionTips()
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


        // 감정 선택
        val emotionGroup1 = binding.emotions1
        val emotionGroup2 = binding.emotions2

        // 감정 선택 시 radiogroup 별로 선택 해제되기 && 선택시 이벤트
        emotionGroup1.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                emotionGroup2.clearCheck()
                chosenEmotionId = checkedId
            }
            isChecking = true
        }


        emotionGroup2.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            if (checkedId != -1 && isChecking) {
                isChecking = false
                emotionGroup1.clearCheck()
                chosenEmotionId = checkedId
            }
            isChecking = true
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {


                // 만약 감정이 저장된 상태라면, 화면으로 다시 돌아왔을 때 체크 표시가 돼 있게 뿌리기
                launch {
                    // getEmotion Result 구독
                    viewModel.emotion.collect {
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

                launch {
                    viewModel.getEmotion()
                }

                // 만약 감정 text가 저장된 상태라면, 화면으로 다시 돌아왔을 때 그대로 뿌리기
                launch {
                    // getEmotionText Result 구독
                    viewModel.emotionText.collect {
                        if (it != "") {
                            binding.RecordEmotionUserInput.setText(it)
                        }
                    }
                }

                launch {
                    viewModel.getEmotionText()
                }

                // 버튼 클릭 감지
                launch {
                    // 감정 설명서 보기 버튼 클릭 감지
                    viewModel.recordEmotionHelpButton.collect {
                        val intent = Intent(applicationContext, EmotionInstructions::class.java)
                        startActivity(intent)
                    }
                }

                launch {
                    // 어떤 감정을 느꼈나요? 팁 버튼 클릭 감지
                    viewModel.recordEmotionTips.collect {
                        tipDialog()
                    }
                }

                launch {
                    // 다음 버튼 클릭 감지
                    viewModel.recordNextButton.collect {
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

                            // 다음 화면으로 이동
                            val intent = Intent(applicationContext, Old_Reflection::class.java)
                            startActivity(intent)
                        }
                    }
                }

                launch {
                    // 이전 버튼 클릭 감지
                    viewModel.recordPreviousButton.collect {
                        // 저장 후 이전 화면으로 이동
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

                        // 이전 화면으로 이동
                        finish()
                    }
                }

            }
        }
    }


    // 알림 dialoguee
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // 팁 dialogue
    fun tipDialog() {
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

        lifecycleScope.launch {
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

            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
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
        }
    }

}