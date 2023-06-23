package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityTraumaDiaryReflectionBinding
import com.android.mymindnotes.presentation.viewmodels.TraumaDiaryReflectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TraumaDiaryReflection : AppCompatActivity() {
    private lateinit var binding: ActivityTraumaDiaryReflectionBinding

    // 뷰모델 객체 주입
    private val viewModel: TraumaDiaryReflectionViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraumaDiaryReflectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.recordbackground)


        // 버튼 클릭
        // 감정 설명서 보기 버튼 클릭
        binding.RecordEmotionHelpButton.setOnClickListener {
            val intent = Intent(applicationContext, EmotionInstructions::class.java)
            startActivity(intent)

        }

        // 팁 버튼 클릭
        binding.RecordReflectionTips.setOnClickListener {
            tipDialog()
        }

        // 이전 버튼 클릭
        binding.RecordPreviousButton.setOnClickListener {
            lifecycleScope.launch {
                val reflection = binding.RecordReflectionUserInput.text.toString()
                if (reflection != "") {
                    // 회고 저장
                    viewModel.saveReflection(reflection)
                }
                // 이전 화면으로 이동
                finish()
            }
        }

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener {
            lifecycleScope.launch {
                val reflection = binding.RecordReflectionUserInput.text.toString()

                if (reflection == "") {
                    dialog("회고를 작성해 주세요.")
                } else {
                    // 회고 저장
                    viewModel.saveReflection(reflection)
                    // 타입 저장
                    viewModel.saveType("트라우마 일기")
                    // 날짜 저장
                    val now = System.currentTimeMillis()
                    val getDate = Date(now)
                    val mFormat = SimpleDateFormat("yyyy-MM-dd")
                    val date = mFormat.format(getDate)
                    viewModel.saveDate(date)
                    // 요일 저장
                    val DAY = arrayOf("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
                    val today = Calendar.getInstance()
                    val day = DAY[today[Calendar.DAY_OF_WEEK]]
                    viewModel.saveDay(day)

                    // 서버에 일기 저장
                    viewModel.saveDiary()
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Success -> {
                            // reflection 값 구독
                            uiState.reflectionResult?.let {
                                if (it != "") {
                                    // 만약 회고가 저장된 상태라면 화면에 뿌리기
                                    binding.RecordReflectionUserInput.setText(it)
                                }
                            }

                            // 일기 저장 결과 구독
                            uiState.saveDiaryResult?.let {
                                if (it["code"].toString().toDouble() == 6001.0) {
                                    dialog(it["msg"] as String)
                                } else if (it["code"].toString().toDouble() == 6000.0) {
                                    // 저장한 것 삭제
                                    viewModel.clearTraumaDiaryTempRecords()

                                    // 성공 토스트 띄우기
                                    Toast.makeText(
                                        applicationContext,
                                        R.string.successfulRecord,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // 메인으로 화면 전환
                                    val intent = Intent(applicationContext, MainPage::class.java)
                                    startActivity(intent)
                                }
                            }
                        }

                        // 애러 구독
                        is TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Error -> {
                            if (uiState.error) {
                                dialog("서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요.")
                            }
                        }
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
        builder.setIcon(R.drawable.tips)
        builder.setTitle("회고 작성 Tips")
        builder.setMessage(R.string.traumaReflectionTips)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }


    // backprssed 시 회고 저장 후 뒤로가기
    override fun onBackPressed() {
        lifecycleScope.launch {
            val reflection = binding.RecordReflectionUserInput.text.toString()
            if (reflection != "") {
                // 회고 저장
                viewModel.saveReflection(reflection)
            }
            // 이전 화면으로 이동
            finish()
        }
    }
}