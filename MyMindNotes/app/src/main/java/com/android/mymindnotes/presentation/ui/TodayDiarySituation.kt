package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.databinding.ActivityTodayDiarySituationBinding
import com.android.mymindnotes.presentation.viewmodels.TodayDiarySituationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodayDiarySituation : AppCompatActivity() {
    private lateinit var binding: ActivityTodayDiarySituationBinding

    // 뷰모델 객체 주입
    private val viewModel: TodayDiarySituationViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodayDiarySituationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.background)

        // 팁 버튼 클릭
        binding.RecordSituationTips.setOnClickListener {
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

                // 만약 상황이 저장된 상태라면 다시 돌아왔을 때 화면에 뿌리기
                launch {
                    // getSituation() Result 구독
                    viewModel.situation.collect {
                        if (it != "") {
                            binding.RecordSituationUserInput.setText(it)
                        }
                    }
                }

                launch {
                    viewModel.getSituation()
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
                        val situation = binding.RecordSituationUserInput.text.toString()
                        if (situation == "") {
                            dialog("상황을 작성해 주세요.")
                        } else {
                            // 상황 저장
                            viewModel.saveSituation(situation)
                            // 다음 화면으로 이동
                            val intent = Intent(applicationContext, TodayDiaryThought::class.java)
                            startActivity(intent)
                        }
                    }
                }

                launch {
                    // 이전 버튼 감지
                    viewModel.recordPreviousButton.collect {
                        val situation = binding.RecordSituationUserInput.text.toString()
                        if (situation != "") {
                            // 상황 저장
                            viewModel.saveSituation(situation)
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
        builder.setIcon(R.drawable.tips)
        builder.setTitle("상황 작성 Tips")
        builder.setMessage(R.string.situationTips)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // 백 클릭시 상황 저장 후 이전 화면으로
    override fun onBackPressed() {
        lifecycleScope.launch {
            val situation = binding.RecordSituationUserInput.text.toString()
            if (situation != "") {
                // 상황 저장
                viewModel.saveSituation(situation)
            }
            // 이전 화면으로 이동
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            val situation = binding.RecordSituationUserInput.text.toString()
            if (situation != "") {
                // 상황 저장
                viewModel.saveSituation(situation)
            }
        }
    }
}