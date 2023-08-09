package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.text.TextWatcher
import android.text.Editable
import android.widget.Toast
import android.content.Intent
import android.graphics.Color
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityChangeNicknameBinding
import com.android.mymindnotes.presentation.viewmodels.ChangeNickNameViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class ChangeNicknameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeNicknameBinding
    private val viewModel: ChangeNickNameViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 닉네임 중복체크 했는지 여부
    var nicknameCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background)

        // 엑션바 설정
        setActionBar()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is ChangeNickNameViewModel.ChangeNickNameUiState.Loading -> {
                            binding.nickNameInput.hint = "특수문자 제외 2자 이상"
                        }

                        // 닉네임 중복 체크 결과
                        is ChangeNickNameViewModel.ChangeNickNameUiState.NickNameDuplicateChecked -> {
                            confirmNicknameDialog(uiState.msg)
                        }

                        // 닉네임 수정 결과
                        is ChangeNickNameViewModel.ChangeNickNameUiState.NickNameChanged -> {
                            Toast.makeText(
                                applicationContext,
                                uiState.msg,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            // 화면 전환
                            val intent = Intent(applicationContext, MainPageActivity::class.java)
                            startActivity(intent)
                        }

                        // 애러 처리
                        is ChangeNickNameViewModel.ChangeNickNameUiState.Error -> {
                            dialog(uiState.error)
                        }

                    }
                }

            }
        }

        // 닉네임 형식 체크
        val nicknamePattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$"

        // 버튼 클릭
        // 닉네임 중복 확인 버튼
        binding.checkNicknameButton.setOnClickListener {
            lifecycleScope.launch {
                val nickName = binding.nickNameInput.text.toString()
                if (nickName == "") {
                    dialog("닉네임을 입력해 주세요.")
                    // 닉네임 형식 체크
                } else if (!Pattern.matches(nicknamePattern, nickName)) {
                    dialog("특수문자를 제외한 2~10자여야 합니다.")
                } else {
                    // 형식 체크 통과
                    if (!nicknameCheck) {
                        // 네트워크 통신(닉네임이 중복 체크)
                        viewModel.checkNickNameButtonClicked(nickName)
                    }
                }
            }
        }

        // 닉네임 변경 버튼
        binding.changeNicknameButton.setOnClickListener {
            lifecycleScope.launch {
                if (!nicknameCheck) {
                    dialog("닉네임 중복확인을 해주세요.")
                } else {
                    val nickName = binding.nickNameInput.text.toString()
                    viewModel.changeNickNameButtonClicked(nickName)
                }
            }
        }


        // text Change 변화 감지- 중복확인 후에 닉네임 input이 일어나면 다시 중복체크 해야 하므로
        binding.nickNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                nicknameCheck = false
                binding.checkNicknameButton.text = "중복확인"
                binding.checkNicknameButton.setBackgroundColor(Color.parseColor("#C3BE9F98")) // String으로된 Color값을 Int로 바꾸기

            }

            override fun afterTextChanged(s: Editable) {}
        })



    }

    // 액션 바 설정
    private fun setActionBar() {
        // 액션 바 타이틀
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 사용 안함
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM // 커스텀 사용
        supportActionBar?.setCustomView(R.layout.changenickname_actionbartext) // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // 알림 dialogue
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // 닉네임 중복 체크 확인완료 dialogue
    private fun confirmNicknameDialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
        nicknameCheck = true
        binding.checkNicknameButton.text = "확인완료"
        binding.checkNicknameButton.setBackgroundColor(Color.parseColor("#FFDDD5")) // String으로된 Color값을 Int로 바꾸기
    }
}

