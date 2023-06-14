package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityFindPasswordBinding
import com.android.mymindnotes.presentation.viewmodels.FindPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FindPassword : AppCompatActivity() {
    private lateinit var binding: ActivityFindPasswordBinding
    // 뷰모델 객체 주입
    private val viewModel: FindPasswordViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    // 랜덤 비밀번호
    lateinit var randomPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground).into(binding!!.background)

        // 액션바 세팅
        // 액션 바 타이틀
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 기본 타이틀 사용 안함
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM // 커스텀 사용
        supportActionBar!!.setCustomView(R.layout.changepassword_actionbartext) // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // 버튼 클릭
        // 임시 비밀번호 보내기 버튼
        binding.sendEmailButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickSendEmailButton()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // 임시 비밀번호 보내기 버튼이 눌러졌으면
                    viewModel.sendEmailButton.collect {
                        val email = binding.emailInput.text.toString()
                        if (email == "") {
                            dialog("임시 비밀번호를 보낼 이메일을 입력해 주세요.")
                        } else {
                            // 임시 비밀번호 (랜덤)
                            val leftLimit = 48 // numeral '0'
                            val rightLimit = 122 // letter 'z'
                            val targetStringLength = 10
                            val random = Random()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                randomPassword = random.ints(leftLimit, rightLimit + 1)
                                    .filter { i: Int -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
                                    .limit(targetStringLength.toLong())
                                    .collect(
                                        { StringBuilder() },
                                        { obj: java.lang.StringBuilder, codePoint: Int ->
                                            obj.appendCodePoint(codePoint)
                                        }) { obj: java.lang.StringBuilder, s: java.lang.StringBuilder? ->
                                        obj.append(
                                            s
                                        )
                                    }
                                    .toString()
                            }
                            viewModel.changeToTemporaryPassword(email, randomPassword)
                        }
                    }
                }

                launch {
                    // 임시 비밀번호로 비밀번호 변경하기 결과 구독
                    viewModel.changeToTemporaryPasswordResult.collect {
                        if (it["code"].toString().toDouble() == 3007.0) {
                            dialog(it["msg"] as String?)
                        } else if (it["code"].toString().toDouble() == 3006.0) {
                            // 전송 완료 알림 띄우기
                            dialog(it["msg"] as String?)
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