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
class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding!!.background)

        // 액션바 설정
        // 액션 바 타이틀
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 기본 타이틀 사용 안함
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM // 커스텀 사용
        supportActionBar!!.setCustomView(R.layout.changepassword_actionbartext) // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // 비밀번호 형식 체크
        val passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}"

        // 버튼 클릭
        // 비밀번호 변경 클릭
        binding.changePasswordButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickChangePasswordButton()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    // 비밀번호 변경 버튼이 눌러졌으면
                    viewModel.changePasswordButton.collect {
                        val passwordInput = binding!!.passwordInput.text.toString()
                        val passwordRetypeInput = binding!!.passwordReypeInput.text.toString()
                        val originalPasswordInput = binding!!.originalPasswordInput.text.toString()
                        if (originalPasswordInput == "" || passwordInput == "" || passwordRetypeInput == "") {
                            dialog("비밀번호를 입력해 주세요.")
                        } else if (passwordInput != passwordRetypeInput) {
                            dialog("새로운 비밀번호가 일치하지 않습니다.")
                        } else if (!Pattern.matches(passwordPattern, passwordInput)) {
                            dialog("영문+숫자 조합 6자~20자여야 합니다.")
                            // 비밀번호와 비밀번호 확인란이 일치하지 않으면
                        } else {
                            // 네트워크 통신, 비밀번호 변경
                            viewModel.changePassword(passwordInput, originalPasswordInput)
                        }
                    }
                }

                launch {
                    // 비밀번호 변경 결과 구독
                    viewModel.changePasswordResult.collect {
                        if (it["code"].toString().toDouble() == 3005.0 || it["code"].toString().toDouble() == 3003.0) {
                            dialog(it["msg"] as String?)
                        } else if (it["code"].toString().toDouble() == 3002.0) {
                            // 비밀번호 재저장
                            val passwordInput = binding.passwordInput.text.toString()
                            viewModel.savePassword(passwordInput)
                            Toast.makeText(applicationContext, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                            // 화면 전환
                            val intent = Intent(applicationContext, MainPage::class.java)
                            startActivity(intent)
                        }
                    }
                }

                launch {
                    // 에러 구독
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
}
