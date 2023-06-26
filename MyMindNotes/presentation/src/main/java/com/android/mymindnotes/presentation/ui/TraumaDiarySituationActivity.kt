package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import android.content.DialogInterface
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityTraumaDiarySituationBinding
import com.android.mymindnotes.presentation.viewmodels.TraumaDiarySituationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TraumaDiarySituationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTraumaDiarySituationBinding

    // 뷰모델 객체 주입
    private val viewModel: TraumaDiarySituationViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraumaDiarySituationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground1).into(binding.background)


        // 팁 버튼 클릭
        binding.RecordSituationTips.setOnClickListener {
            tipDialog()
        }

        // 다음 버튼 클릭
        binding.RecordNextButton.setOnClickListener {
            lifecycleScope.launch {
                val situation = binding.RecordSituationUserInput.text.toString()
                if (situation == "") {
                    dialog("상황을 작성해 주세요.")
                } else {
                    // 상황 저장
                    viewModel.saveSituation(situation)
                    // 다음 화면으로 이동
                    val intent = Intent(applicationContext, TraumaDiaryThoughtActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        // 만약 상황이 저장된 상태라면 화면에 뿌리기
                        is TraumaDiarySituationViewModel.TraumaDiarySituationUiState.Success -> {
                            uiState.situationResult?.let {
                                if (it != "") {
                                    binding.RecordSituationUserInput.setText(it)
                                }
                            }
                        }
                    }
                }


            }

        }
    }


    // 뒤로 가기 버튼 누를 시, 알람창 띄우기
    private var dialogListener =
        DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                // 기록 삭제
                lifecycleScope.launch {
                    viewModel.clearTraumaDiaryTempRecords()
                }
                finish()
            }
        }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("입력한 기록이 사라져요. 정말 종료하시겠어요?")
        builder.setNegativeButton("종료", dialogListener)
        builder.setPositiveButton("계속 작성", null)
        alertDialog = builder.show()
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
        builder.setTitle("상황 작성 Tips")
        builder.setMessage(R.string.situationTips)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }
}