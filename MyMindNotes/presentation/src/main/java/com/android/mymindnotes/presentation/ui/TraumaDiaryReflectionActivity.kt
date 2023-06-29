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
class TraumaDiaryReflectionActivity : AppCompatActivity() {
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


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Loading -> {

                        }

                        is TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Success -> {
                            // reflection 값 구독
                            uiState.reflection?.let {
                                if (it != "") {
                                    // 만약 회고가 저장된 상태라면 화면에 뿌리기
                                    binding.RecordReflectionUserInput.setText(it)
                                }
                            }
                        }

                        is TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.DiarySaved -> {
                            // 성공 토스트 띄우기
                            Toast.makeText(
                                applicationContext,
                                R.string.successfulRecord,
                                Toast.LENGTH_SHORT
                            ).show()

                            // 메인으로 화면 전환
                            val intent = Intent(applicationContext, MainPageActivity::class.java)
                            startActivity(intent)
                        }

                        // 애러 구독
                        is TraumaDiaryReflectionViewModel.TraumaDiaryReflectionUiState.Error -> {
                            dialog(uiState.error)
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
                    viewModel.previousButtonClickedOrBackPressed(reflection)
                }
                // 이전 화면으로 이동
                finish()
            }
        }

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener {
            lifecycleScope.launch {
                // 회고
                val reflection = binding.RecordReflectionUserInput.text.toString()

                if (reflection == "") {
                    dialog("회고를 작성해 주세요.")
                } else {

                    // 날짜
                    val now = System.currentTimeMillis()
                    val getDate = Date(now)
                    val mFormat = SimpleDateFormat("yyyy-MM-dd")
                    val date = mFormat.format(getDate)

                    // 요일
                    val dayOfWeek = arrayOf("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일")
                    val today = Calendar.getInstance()
                    val day = dayOfWeek[today[Calendar.DAY_OF_WEEK]]


                    // 서버에 일기 저장
                    viewModel.saveDiaryButtonClicked(reflection, "트라우마 일기", date, day)
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
                viewModel.previousButtonClickedOrBackPressed(reflection)
            }
            // 이전 화면으로 이동
            finish()
        }
    }
}