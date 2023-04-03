package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import android.content.Intent
import com.android.mymindnotes.EmotionInstructions
import com.android.mymindnotes.data.retrofit.RetrofitService
import com.android.mymindnotes.data.retrofit.api.diary.SaveDiaryApi
import com.android.mymindnotes.data.retrofit.model.diary.UserDiary
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.databinding.ActivityTodayDiaryReflectionBinding
import com.android.mymindnotes.presentation.viewmodels.TodayDiaryReflectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TodayDiaryReflection : AppCompatActivity() {
    private lateinit var binding: ActivityTodayDiaryReflectionBinding

    // 뷰모델 객체 주입
    private val viewModel: TodayDiaryReflectionViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayDiaryReflectionBinding.inflate(layoutInflater)
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

        // 팁 버튼 클릭
        binding.RecordReflectionTips.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordEmotionTips()
            }
        }

        // 이전 버튼 클릭
        binding.RecordPreviousButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordPreviousButton()
            }
        }

        // 저장 버튼 클릭
        binding.RecordSaveButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickRecordSaveButton()
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // 만약 상황이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
                launch {
                    // getEmotion Result 구독
                    viewModel.reflection.collect {
                        if (it != "") {
                            binding.RecordReflectionUserInput.setText(it)
                        }
                    }
                }

                launch {
                    viewModel.getReflection()
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
                    // 팁 버튼 클릭 감지
                    viewModel.recordTips.collect {
                        tipDialog()
                    }
                }


                launch {
                    // 이전 버튼 감지
                    viewModel.recordPreviousButton.collect {
                        val reflection = binding.RecordReflectionUserInput.text.toString()
                        if (reflection != "") {
                            // 회고 저장
                            viewModel.saveReflection(reflection)
                        }
                        // 이전 화면으로 이동
                        finish()
                    }
                }

                launch {
                    // 일기 저장 버튼 클릭 감지
                    viewModel.recordSaveButton.collect {
                        // 회고 저장
                        val reflection = binding.RecordReflectionUserInput.text.toString()
                        viewModel.saveReflection(reflection)
                        // 타입 저장
                        viewModel.saveType("오늘의 마음 일기")
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


                launch {
                    // 일기 저장 결과 플로우 구독
                    viewModel.saveDiary.collect {
                        if (it["code"].toString().toDouble() == 6001.0) {
                            dialog(it["msg"] as String)
                        } else if (it["code"].toString().toDouble() == 6000.0) {
                            // 저장한 것 삭제
                            viewModel.clearReflectionSharedPreferences()
                            viewModel.clearEmotionColorSharedPreferences()
                            viewModel.clearEmotionSharedPreferences()
                            viewModel.clearEmotionTextSharedPreferences()
                            viewModel.clearSituationSharedPreferences()
                            viewModel.clearThoughtSharedPreferences()
                            viewModel.clearTypeSharedPreferences()
                            viewModel.clearDateSharedPreferences()
                            viewModel.clearDaySharedPreferences()

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

                // 에러 감지
                // 에러 값 구독
                launch {
                    viewModel.error.collect {
                        if (it) {
                            dialog("서버와의 통신에 실패했습니다. 인터넷 연결을 확인해 주세요.")
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
        builder.setMessage(R.string.reflectionTips)
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