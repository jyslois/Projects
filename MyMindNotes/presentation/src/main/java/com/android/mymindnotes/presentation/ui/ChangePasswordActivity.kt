package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.widget.Toast
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityChangePasswordBinding
import com.android.mymindnotes.presentation.viewmodels.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background)

        // 액션바 설정
        setActionBar()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is ChangePasswordViewModel.ChangePasswordUiState.Loading -> {
                            binding.originalPasswordInput.hint = "기존 비밀번호"
                            binding.passwordInput.hint = "영문+숫자 조합 6자 이상"
                            binding.passwordReypeInput.hint = "영문+숫자 조합 6자 이상"
                        }

                        // 비밀번호 변경 구독
                        is ChangePasswordViewModel.ChangePasswordUiState.Success -> {

                            Toast.makeText(
                                applicationContext,
                                "비밀번호가 변경되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // 화면 전환
                            val intent = Intent(applicationContext, MainPageActivity::class.java)
                            startActivity(intent)
                        }


                        // 애러 구독
                        is ChangePasswordViewModel.ChangePasswordUiState.Error -> {
                            dialog(uiState.error)
                        }
                    }
                }

            }
        }

        // 비밀번호 형식 체크
        val passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}"

        // 버튼 클릭
        // 비밀번호 변경 클릭
        binding.changePasswordButton.setOnClickListener {
            lifecycleScope.launch {
                val passwordInput = binding.passwordInput.text.toString()
                val passwordRetypeInput = binding.passwordReypeInput.text.toString()
                val originalPasswordInput = binding.originalPasswordInput.text.toString()
                if (originalPasswordInput == "" || passwordInput == "" || passwordRetypeInput == "") {
                    dialog("비밀번호를 입력해 주세요.")
                } else if (passwordInput != passwordRetypeInput) {
                    dialog("새로운 비밀번호가 일치하지 않습니다.")
                } else if (!Pattern.matches(passwordPattern, passwordInput)) {
                    dialog("영문+숫자 조합 6자~20자여야 합니다.")
                    // 비밀번호와 비밀번호 확인란이 일치하지 않으면
                } else {
                    // 네트워크 통신, 비밀번호 변경
                    viewModel.changePasswordButtonClicked(passwordInput, originalPasswordInput)
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

    // 액션바 설정
    private fun setActionBar() {
        // 액션 바 타이틀
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 기본 타이틀 사용 안함
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM // 커스텀 사용
        supportActionBar!!.setCustomView(R.layout.changepassword_actionbartext) // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
