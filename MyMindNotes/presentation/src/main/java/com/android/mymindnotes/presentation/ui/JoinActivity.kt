package com.android.mymindnotes.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.text.TextWatcher
import android.text.Editable
import android.graphics.Color
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityJoinBinding
import com.android.mymindnotes.presentation.viewmodels.JoinViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoinBinding

    // 중복 확인을 했는지에 대한 변수
    var emailCheck: Boolean = false
    var nicknameCheck: Boolean = false

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 뷰모델 객체 주입
    private val viewModel: JoinViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background)

        // ViewModel SharedFlow 구독
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is JoinViewModel.JoinUiState.Loading -> {
                            binding.emailInput.hint = "이메일 입력"
                            binding.passwordInput.hint = "영문+숫자 조합 6자 이상"
                            binding.passwordRetypeInput.hint = "비밀번호 확인"
                            binding.nickNameInput.hint = "특수문자 제외 2자 이상"
                            binding.birthyearInput.hint = "태어난 년도"
                        }

                        // 이메일 중복 체크 결과 플로우 구독
                        is JoinViewModel.JoinUiState.EmailDuplicateCheckSucceed -> {
                            confirmEmailDialog()
                        }

                        // 닉네임 중복 체크 결과 플로우 구독
                        is JoinViewModel.JoinUiState.NicknameDuplicateCheckSucceed -> {
                            confirmNicknameDialog()
                        }

                        // 회원가입 결과 플로우 구독
                        is JoinViewModel.JoinUiState.JoinSucceed -> {
                            // 메인 화면 전환
                            val intent = Intent(applicationContext, MainPageActivity::class.java)
                            startActivity(intent)
                        }

                        is JoinViewModel.JoinUiState.Error -> {
                            dialog(uiState.error)
                        }
                    }
                }
            }
        }

        // 형식 체크
        val emailPattern = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"
        val passwordPattern = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}"
        val nicknamePattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$"

        // 버튼 클릭 이벤트
        // 이메일 중복 확인 버튼
        // 이메일 중복 체크
        binding.emailCheckButton.setOnClickListener {
            lifecycleScope.launch {
                val emailInput = binding.emailInput.text.toString()
                if (emailInput == "") {
                    dialog("이메일을 입력해 주세요")
                    // 이메일 형식 체크
                } else if (!Pattern.matches(emailPattern, emailInput)) {
                    dialog("올바른 이메일 형식으로 입력해 주세요")
                } else {
                    if (!emailCheck) {
                        // 네트워크 통신(이메일 중복됐는지 체크)
                        viewModel.checkEmailButtonClicked(emailInput)
                    }
                }
            }
        }

        // 닉네임 중복 확인 버튼
        binding.nickNameCheckButton.setOnClickListener {
            lifecycleScope.launch {
                val nickNameInput = binding.nickNameInput.text.toString()
                if (nickNameInput == "") {
                    dialog("닉네임을 입력해 주세요")
                    // 이메일 형식 체크
                } else if (!Pattern.matches(nicknamePattern, nickNameInput)) {
                    dialog("닉네임은 특수문자를 제외한 2~10자여야 합니다")
                } else {
                    if (!nicknameCheck) {
                        // 네트워크 통신(닉네임이 중복됐는지 체크)
                        viewModel.checkNickNameButtonClicked(nickNameInput)
                    }
                }
            }
        }

        // 회원가입 버튼
        binding.joinButton.setOnClickListener {
            lifecycleScope.launch {
                val nickNameInput = binding.nickNameInput.text.toString()
                val emailInput = binding.emailInput.text.toString()
                val passwordInput = binding.passwordInput.text.toString()
                val passwordReTypeInput = binding.passwordRetypeInput.text.toString()
                val birthyearInput = binding.birthyearInput.text.toString()

                // 형식 체크
                // 만약 이메일이나 페스워드를 적지 않았다면
                if (emailInput == "" || passwordInput == "") {
                    dialog("이메일과 비밀번호를 입력해 주세요")
                    // 비밀 번호 형식이 잘못되었다면
                } else if (!Pattern.matches(passwordPattern, passwordInput)) {
                    dialog("비밀번호는 영문+숫자 조합 6자~20자여야 합니다")
                    // 비밀번호와 비밀번호 확인란이 일치하지 않으면
                } else if (passwordInput != passwordReTypeInput) {
                    dialog("비밀번호가 일치하지 않습니다")
                    // 닉네임을 적지 않았다면
                } else if (nickNameInput == "") {
                    dialog("닉네임을 입력해 주세요")
                    // 생년을 입력하지 않았다면
                } else if (birthyearInput == "") {
                    dialog("태어난 년도를 력해 주세요")
                    // 생년의 형식이 잘못되었다면
                } else if (birthyearInput.toInt() < 1901 || birthyearInput.toInt() > 2155) {
                    dialog("생년은 1901~2155 사이여야 합니다")
                    // 이메일 중복확인을 하지 않았다면
                } else if (!emailCheck) {
                    dialog("이메일 중복확인을 해주세요")
                    // 닉네임 중복확인을 하지 않았다면
                } else if (!nicknameCheck) {
                    dialog("닉네임 중복확인을 해주세요")
                    // 형식체크 통과
                } else {
                    viewModel.joinButtonClicked(
                        emailInput,
                        nickNameInput,
                        passwordInput,
                        birthyearInput.toInt()
                    )
                }
            }

        }

        // Text 변화 감지 - 중복확인 후에 이메일이나 닉네임 input이 일어나면 다시 중복체크 해야 하므로
        // 이메일
        binding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailCheck = false
                binding.emailCheckButton.text = "중복확인"
                binding.emailCheckButton.setBackgroundColor(Color.parseColor("#C3BE9F98")) // String으로된 Color값을 Int로 바꾸기
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // 닉네임
        binding.nickNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nicknameCheck = false
                binding.nickNameCheckButton.text = "중복확인"
                binding.nickNameCheckButton.setBackgroundColor(Color.parseColor("#C3BE9F98")) // String으로된 Color값을 Int로 바꾸기
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }


    // 이메일 중복 체크 확인완료 dialogue
    private fun confirmEmailDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("사용 가능한 이메일입니다.")
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
        emailCheck = true
        binding.emailCheckButton.text = "확인완료"
        binding.emailCheckButton.setBackgroundColor(Color.parseColor("#FFDDD5")) // String으로된 Color값을 Int로 바꾸기
    }

    // 닉네임 중복 체크 확인완료 dialogue
    private fun confirmNicknameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("사용 가능한 닉네임입니다.")
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
        nicknameCheck = true
        binding.nickNameCheckButton.text = "확인완료"
        binding.nickNameCheckButton.setBackgroundColor(Color.parseColor("#FFDDD5")) // String으로된 Color값을 Int로 바꾸기
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