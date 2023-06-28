package com.android.mymindnotes.presentation.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.EditText
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityLoginBinding
import com.android.mymindnotes.presentation.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var alertDialog: AlertDialog
    lateinit var autoSave: CheckBox
    lateinit var autoLogin: CheckBox
    lateinit var email: EditText
    lateinit var password: EditText

    // 뷰모델 객체 주입
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 백그라운드 이미지
        Glide.with(this@LoginActivity).load(R.drawable.mainbackground).into(binding.background)

        // 로그인 정보 저장 & 자동 로그인 구현 관련
        val loginButton = binding.loginButton
        val findPasswordButton = binding.findPasswordButton
        autoSave = binding.autoSaveButton
        autoLogin = binding.autoLoginButton
        email = binding.email
        password = binding.password


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is LoginViewModel.LoginUiState.Loading -> {
                            binding.email.hint = "이메일"
                            binding.password.hint = "비밀번호"
                        }

                        // 저장된 데이터 화면에 뿌리기
                        is LoginViewModel.LoginUiState.Succeed -> {
                            autoSave.isChecked = uiState.autoSaveState

                            if (uiState.autoLoginState) {
                                // 체크박스 상태도 체크로 표시 (아이디 비밀번호 저장, 로그인 상태 유지 둘 다 체크)
                                autoLogin.isChecked = true
                                autoSave.isChecked = true
                            } else {
                                autoLogin.isChecked = false
                            }

                            email.setText(uiState.id)
                            password.setText(uiState.password)


                        }

                        // 로그인 성공 시
                        is LoginViewModel.LoginUiState.LoginSucceed -> {
                            // 화면 전환
                            startActivity<MainPageActivity>()
                        }

                        // 로그인 애러 감지
                        is LoginViewModel.LoginUiState.Error -> {
                            dialog(uiState.error)
                        }


                    }
                }
            }
        }

        // 클릭 이벤트

        // 아이디/비밀번호 저장 체크 박스 클릭 시
        autoSave.setOnClickListener {
            lifecycleScope.launch {

                viewModel.saveAutoSaveCheck(autoSave.isChecked, autoLogin.isChecked, email.text.toString(), password.text.toString())

            }
        }

        // 자동 로그인 체크 박스 클릭 시
        autoLogin.setOnClickListener {
            lifecycleScope.launch {
                // 만약 체크 박스가 체크되어 있다면
                if (autoLogin.isChecked) {
                    // autoSave 박스도 자동으로 체크해 주기
                    autoSave.isChecked = true
                    viewModel.saveAutoSaveCheck(autoSave.isChecked, autoLogin.isChecked, email.text.toString(), password.text.toString())
                    // 체크되어 있지 않다면 상태 저장
                    viewModel.saveAutoLoginCheck(false)
                }
            }
        }

        // 비밀번호 찾기 버튼 클릭시
        findPasswordButton.setOnClickListener {
            startActivity<FindPasswordActivity>()
        }

        // 로그인 버튼 클릭 시
        loginButton.setOnClickListener {
            lifecycleScope.launch {
                val emailInput = email.text.toString()
                val passwordInput = password.text.toString()

                // 만약 이메일/페스워드를 적지 않았다면
                if (emailInput.isBlank()) {
                    dialog("아이디를 입력하세요")
                } else if (passwordInput.isBlank()) {
                    dialog("비밀번호를 입력하세요")
                    // 바른 이메일 형식을 입력하지 않았다면ㄴ
                } else if (!emailInput.contains("@")) {
                    dialog("올바른 이메일 형식으로 입력해 주세요")
                    // 비밀번호가 6자리 이하라면
                } else if (binding.password.text.toString().length < 6) {
                    dialog("비밀번호가 틀렸습니다")
                } else {
                    // 형식 통과
                    if (autoLogin.isChecked) {
                        autoSave.isChecked = true // 자동 로그인 체크가 되었다면, 자동 저장 체크도 자동으로 해 주기
                    }

                    // 로그인 시도
                    viewModel.login(
                        emailInput,
                        passwordInput,
                        autoLogin.isChecked,
                        autoSave.isChecked
                    )

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

    inline fun <reified T> Context.startActivity() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }


    override fun onStop() {
        super.onStop()

        lifecycleScope.launch {
            // 만약 체크 박스가 체크되어 있다면
            if (autoSave.isChecked) {
                // 상태 저장
                viewModel.saveAutoSaveCheck(autoSave.isChecked, autoLogin.isChecked, email.text.toString(), password.text.toString())
            } else {
                // 체크되어 있지 않다면 아이디와 비밀번호 값을 null 저장
                viewModel.saveAutoSaveCheck(false, false, null, null)
            }

        }

    }


}