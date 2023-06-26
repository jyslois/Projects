package com.android.mymindnotes.presentation.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import android.widget.Toast
import android.content.DialogInterface
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityDiaryResultEditBinding
import com.android.mymindnotes.presentation.viewmodels.DiaryResultEditViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryResultEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryResultEditBinding

    // 데이터 저장을 위한 변수
    var type: String? = null
    var situation: String? = null
    var thought: String? = null
    var emotion: String? = null
    var emotionDescription: String? = null
    var reflection: String? = null
    var date: String? = null
    var diaryNumber = 0
    var alertDialog: AlertDialog? = null

    // viewModel 객체 주입
    private val viewModel: DiaryResultEditViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryResultEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground4).into(binding.background)

        // 화면 세팅
        // 데이터 세팅
        val intent = intent
        type = intent.getStringExtra("type")
        situation = intent.getStringExtra("situation")
        thought = intent.getStringExtra("thought")
        emotion = intent.getStringExtra("emotion")
        emotionDescription = intent.getStringExtra("emotionDescription")
        reflection = intent.getStringExtra("reflection")
        diaryNumber = intent.getIntExtra("diaryNumber", 0)
        date = intent.getStringExtra("date")

        // 화면에 뿌리기
        binding.date.text = "$date "
        binding.type.text = type
        binding.editSituation.setText(situation)
        binding.editThought.setText(thought)
        binding.editEmotion.setText(emotion)
        binding.editEmotionText.setText(emotionDescription)
        binding.editReflection.setText(reflection)

        // 타입에 따라 회고 입력란 힌트 다르게 설정
        if (type == "오늘의 마음 일기") {
            binding.editReflection.hint = "(선택) 나는 왜 그런 마음이 들었을까요?"
        } else if (type == "트라우마 일기") {
            binding.editReflection.hint = "지금의 내게 어떤 영향을 미치고 있나요?"
        }

        // 버튼 클릭 이벤트
        // 감정 설명서 페이지 클릭
        binding.emotionHelp.setOnClickListener {
            // 감정 설명서 페이지로 이동
            val intentToEmotionInstructions =
                Intent(applicationContext, EmotionInstructions::class.java)
            startActivity(intentToEmotionInstructions)
        }

        // 수정 버튼 클릭
        binding.editButton.setOnClickListener {
            lifecycleScope.launch {
                val situation = binding.editSituation.text.toString()
                val thought = binding.editThought.text.toString()
                val emotion = binding.editEmotion.text.toString()
                val emotionDescription = binding.editEmotionText.text.toString()
                val reflection = binding.editReflection.text.toString()

                // 일기 수정 네트워크 통신
                if (type == "트라우마 일기" && reflection.isEmpty()) {
                    Toast.makeText(this@DiaryResultEditActivity, "회고를 입력해 주세요", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.updateDiary(
                        diaryNumber,
                        situation,
                        thought,
                        emotion,
                        emotionDescription,
                        reflection
                    )
                }

            }
        }

        // 감지
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when(uiState) {

                        // 수정 결과 구독
                        is DiaryResultEditViewModel.DiaryResultEditUiState.Success -> {
                           uiState.updateDiaryResult?.let {
                               if (it["code"].toString().toDouble() == 8001.0) {
                                   val toast = Toast.makeText(
                                       this@DiaryResultEditActivity,
                                       it["msg"] as String?,
                                       Toast.LENGTH_SHORT
                                   )
                                   toast.show()
                               } else if (it["code"].toString().toDouble() == 8000.0) {
                                   finish()
                               }
                           }
                        }

                        // 애러 구독
                        is DiaryResultEditViewModel.DiaryResultEditUiState.Error -> {
                            if (uiState.error) {
                                val toast = Toast.makeText(
                                    this@DiaryResultEditActivity,
                                    "서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 다시 시도해 주세요.",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
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
                // 취소면 Activity.RESULT_CANCEL 로 처리
                finish()
            }
        }

    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("수정한 기록이 사라져요. 정말 돌아가시겠어요?")
        builder.setNegativeButton("종료", dialogListener)
        builder.setPositiveButton("계속 수정", null)
        alertDialog = builder.create()
        alertDialog!!.show()
    }

    override fun finish() {
        setResult(RESULT_OK)
        super.finish()
    }
}