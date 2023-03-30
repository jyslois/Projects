package com.android.mymindnotes

import androidx.appcompat.app.AppCompatActivity
import android.widget.RadioGroup
import android.content.SharedPreferences
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import android.content.Intent
import com.android.mymindnotes.EmotionInstructions
import android.app.Activity
import com.android.mymindnotes.New_Situation
import android.content.DialogInterface
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.databinding.ActivityNewEmotionBinding
import com.android.mymindnotes.presentation.viewmodels.NewEmotionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class New_Emotion : AppCompatActivity() {
    private lateinit var binding: ActivityNewEmotionBinding

    // 뷰모델 객체 주입
    private val viewModel: NewEmotionViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 감정 선택 체크 변수
    var isChecking = true
    var chosenEmotionId = 0

    // 임시
    var emotion: SharedPreferences? = null
    var emotionEdit: SharedPreferences.Editor? = null
    var emotionText: SharedPreferences? = null
    var emotionTextEdit: SharedPreferences.Editor? = null
    var situation: SharedPreferences? = null
    var situationEdit: SharedPreferences.Editor? = null
    var thought: SharedPreferences? = null
    var thoughtEdit: SharedPreferences.Editor? = null
    var reflection: SharedPreferences? = null
    var reflectionEdit: SharedPreferences.Editor? = null
    var type: SharedPreferences? = null
    var typeEdit: SharedPreferences.Editor? = null
    var emotionColor: SharedPreferences? = null
    var emotionColorEdit: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEmotionBinding.inflate(layoutInflater)
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
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.orange_happiness)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "기쁨")
                                    emotionEdit?.commit()
                                }
                                R.id.anticipationButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.green_anticipation)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "기대")
                                    emotionEdit?.commit()
                                }
                                R.id.trustButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.darkblue_trust)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "신뢰")
                                    emotionEdit?.commit()
                                }
                                R.id.surpriseButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.yellow_surprise)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "놀람")
                                    emotionEdit?.commit()
                                }
                                R.id.sadnessButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.grey_sadness)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "슬픔")
                                    emotionEdit?.commit()
                                }
                                R.id.disgustButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.brown_disgust)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "혐오")
                                    emotionEdit?.commit()
                                }
                                R.id.fearButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.black_fear)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "공포")
                                    emotionEdit?.commit()
                                }
                                R.id.angerButton -> {
                                    emotionColorEdit?.putInt("emotionColor", R.drawable.red_anger)
                                    emotionColorEdit?.commit()
                                    emotionEdit?.putString("emotion", "분노")
                                    emotionEdit?.commit()
                                }
                            }
                            // 감정Text 저장
                            emotionTextEdit?.putString(
                                "emotionText",
                                binding.RecordEmotionUserInput.text.toString()
                            )
                            emotionTextEdit?.commit()
                            val intent = Intent(applicationContext, New_Situation::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }


        emotion = getSharedPreferences("emotion", MODE_PRIVATE)
        emotionEdit = emotion?.edit()
        emotionText = getSharedPreferences("emotionText", MODE_PRIVATE)
        emotionTextEdit = emotionText?.edit()
        situation = getSharedPreferences("situation", MODE_PRIVATE)
        situationEdit = situation?.edit()
        thought = getSharedPreferences("thought", MODE_PRIVATE)
        thoughtEdit = thought?.edit()
        reflection = getSharedPreferences("reflection", MODE_PRIVATE)
        reflectionEdit = reflection?.edit()
        type = getSharedPreferences("type", MODE_PRIVATE)
        typeEdit = type?.edit()
        emotionColor = getSharedPreferences("emotionColor", MODE_PRIVATE)
        emotionColorEdit = emotionColor?.edit()



        // 만약 감정이 저장된 상태라면, 화면으로 다시 돌아왔을 때 체크 표시가 돼 있게 뿌리기
        val emo = emotion?.getString("emotion", "")
        when (emo) {
            "기쁨" -> binding.happinessButton.isChecked = true
            "기대" -> binding.anticipationButton.isChecked = true
            "신뢰" -> binding.trustButton.isChecked = true
            "놀람" -> binding.surpriseButton.isChecked = true
            "슬픔" -> binding.sadnessButton.isChecked = true
            "혐오" -> binding.disgustButton.isChecked = true
            "공포" -> binding.fearButton.isChecked = true
            "분노" -> binding.angerButton.isChecked = true
        }

        // 만약 감정 text가 저장된 상태라면, 화면으로 다시 돌아왔을 때 그대로 뿌리기
        val emoText = emotionText?.getString("emotionText", "")
        if (emoText != "") {
            binding.RecordEmotionUserInput.setText(emoText)
        }
    }

    var dialogListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            // 기록 삭제
            emotionColorEdit!!.clear()
            emotionColorEdit!!.commit()
            emotionEdit!!.clear()
            emotionEdit!!.commit()
            emotionTextEdit!!.clear()
            emotionTextEdit!!.commit()
            situationEdit!!.clear()
            situationEdit!!.commit()
            thoughtEdit!!.clear()
            thoughtEdit!!.commit()
            reflectionEdit!!.clear()
            reflectionEdit!!.commit()
            typeEdit!!.clear()
            typeEdit!!.commit()
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
    fun tipDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.tips)
        builder.setTitle("감정 작성 Tips")
        builder.setMessage(R.string.emotionTips)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }
}