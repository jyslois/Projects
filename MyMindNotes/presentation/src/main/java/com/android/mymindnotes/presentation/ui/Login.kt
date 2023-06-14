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
class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var alertDialog: AlertDialog
    lateinit var autoSave: CheckBox
    lateinit var autoLogin: CheckBox
    lateinit var email: EditText
    lateinit var password: EditText

    // 뷰모델 객체 주입
    private val viewModel: LoginViewModel by viewModels()

    // 알림 dialoguee
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 백그라운드 이미지
        Glide.with(this@Login).load(R.drawable.mainbackground).into(binding.background)

        // 로그인 정보 저장 & 자동 로그인 구현 관련
        var loginButton = binding.loginButton
        var findPasswordButton = binding.findPasswordButton
        autoSave = binding.autoSaveButton
        autoLogin = binding.autoLoginButton
        email = binding.email
        password = binding.password


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // AutoSaveCheck 값 구독 (시작할 때)
                launch {
                    viewModel.autoSaveCheck.collect {
                        autoSave.isChecked = it
                    }
                }

                // AutoLoginCheck 값 구독 (시작할 때)
                launch {
                    viewModel.autoLoginCheck.collect {
                        // 만약 autoLogin 값이 true로 저장되어 있다면,
                        if (it) {
                            // 체크박스 상태도 체크로 표시 (아이디 비밀번호 저장, 로그인 상태 유지 둘 다 체크)
                            autoLogin.isChecked = true
                            autoSave.isChecked = true
                        } else {
                            autoLogin.isChecked = false
                        }
                    }
                }

                // id 값 구독 (시작할 때)
                launch {
                    viewModel.id.collect {
                        // 아이디 뷰에 뿌리기
                        email.setText(it)
                    }
                }

                // password 값 구독 (시작할 때)
                launch {
                    viewModel.password.collect {
                        // 패스워드 뷰에 뿌리기
                        password.setText(it)
                    }
                }

                // 오토세이브 체크박스 클릭 이벤트 감지
                launch {
                    viewModel.autoSaveButton.collect {
                        // 만약 체크 박스가 체크표시되어 있거나, 로그인 상태 유지 체크 박스가 체크되어 있다면,
                        if (autoSave.isChecked || autoLogin.isChecked) {
                            // 상태 저장
                            viewModel.saveAutoSaveCheck(true)
                            // 아이디와 비밀번호 저장
                            viewModel.saveIdAndPassword(
                                email.text.toString(),
                                password.text.toString()
                            )
                        } else {
                            // 체크되어 있지 않다면,
                            // 상태 저장, 아이디와 비밀번호 값을 null로 저장
                            viewModel.saveAutoSaveCheck(false)
                            viewModel.saveIdAndPassword(null, null)
                        }
                    }
                }

                // 오토로그인 체크박스 클릭 이벤트 감지
                launch {
                    viewModel.autoLoginButton.collect {
                        // 만약 체크 박스가 체크되어 있다면
                        if (autoLogin.isChecked) {
                            // autoSave 박스도 자동으로 체크해 주기
                            autoSave.isChecked = true
                            viewModel.saveAutoSaveCheck(true) // The onClickListener is only triggered when the button is clicked by the user. Changing the state of the button programmatically does not count as a user click, so the onClickListener will not be triggered.
                        } else {
                            // 체크되어 있지 않다면 상태 저장
                            viewModel.saveAutoLoginCheck(false)
                        }
                    }
                }

                // 비밀번호 찾기 버튼 클릭 이벤트 감지
                launch {
                    viewModel.findPasswordButton.collect {
                        startActivity<FindPassword>()
                    }
                }

                // 로그인 버튼 클릭 이벤트 감지
                launch {
                    viewModel.loginButton.collect {
                        val emailInput = email.text.toString()
                        val passwordInput = password.text.toString()

                        // 만약 이메일/페스워드를 적지 않았다면
                        if (emailInput == "") {
                            dialog("아이디를 입력하세요")
                        } else if (passwordInput == "") {
                            dialog("비밀번호를 입력하세요")
                            // 바른 이메일 형식을 입력하지 않았다면ㄴ
                        } else if (!emailInput.contains("@")) {
                            dialog("올바른 이메일 형식으로 입력해 주세요")
                            // 비밀번호가 6자리 이하라면
                        } else if (binding.password.text.toString().length < 6) {
                            dialog("비밀번호가 틀렸습니다")
                        } else {
                            // 형식 통과
                            // 아이디/비밀번호 저장 버튼이 체크되어 있다면
                            if (autoSave.isChecked) {
                                // 체크박스 상태 저장
                                viewModel.saveAutoSaveCheck(true)
                                // 아이디와 비밀번호 저장
                                viewModel.saveIdAndPassword(emailInput, passwordInput)
                            } else {
                                viewModel.saveAutoSaveCheck(false)
                                viewModel.saveIdAndPassword(null, null)
                            }

                            viewModel.login(emailInput, passwordInput) // 로그인 하기

                        }
                    }
                }

                // 로그인 결과 구독
                launch {
                    viewModel.logInResult.collect {

                        if (it["code"].toString().toDouble() == 5001.0 || it["code"].toString().toDouble() == 5003.0
                            || it["code"].toString().toDouble() == 5005.0
                        ) {
                            // 형식 오류/아이디와 비번 불일치로 인한 메시지
                            dialog(it["msg"] as String)
                        } else if (it["code"].toString().toDouble() == 5000.0) {
                            // 로그인 성공
                            if (autoLogin.isChecked) {
                                // MainActivity에서의 자동 로그인을 위한 상태 저장
                                autoSave.isChecked = true
                                viewModel.saveAutoLoginCheck(true)
                            }

                            // 회원 번호 저장
                            viewModel.saveUserIndex(it["user_index"].toString().toDouble().toInt())

                            // 화면 전환
                            startActivity<MainPage>()
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


        // 클릭 이벤트

        // 아이디/비밀번호 저장 체크 박스 클릭 시
        autoSave.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickAutoSaveBox()
            }
        }

        // 자동 로그인 체크 박스 클릭 시
        autoLogin.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickAutoLoginBox()
            }
        }

        // 비밀번호 찾기 버튼 클릭시
        findPasswordButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickFindPasswordButton()
            }
        }

        // 로그인 버튼 클릭 시
        loginButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickLoginButton()
            }
        }

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
                viewModel.saveAutoSaveCheck(true)
                // 아이디와 비밀번호 저장
                viewModel.saveIdAndPassword(email.text.toString(), password.text.toString())
            } else {
                // 체크되어 있지 않다면 아이디와 비밀번호 값을 null 저장
                viewModel.saveAutoSaveCheck(false)
                viewModel.saveIdAndPassword(null, null)
            }
        }

    }


}