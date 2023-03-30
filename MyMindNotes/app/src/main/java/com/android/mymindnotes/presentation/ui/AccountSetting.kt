package com.android.mymindnotes.presentation.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.AlarmSetting
import com.android.mymindnotes.R
import com.android.mymindnotes.databinding.ActivityAccountInformationBinding
import com.android.mymindnotes.presentation.viewmodels.AccountSettingViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountSetting : AppCompatActivity() {
    private lateinit var binding: ActivityAccountInformationBinding
    private val viewModel: AccountSettingViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background)

        // 버튼 클릭 이벤트 함수 콜
        // 비밀번호 변경 클릭
        binding.changePasswordButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickChangePasswordButton()
            }
        }

        // 닉네임 변경 버튼 클릭
        binding.changeNicknameButton.setOnClickListener { view: View? ->
            lifecycleScope.launch {
                viewModel.clickChangeNicknameButton()
            }
        }

        // 로그아웃 버튼 클릭
        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickLogoutButton()
            }
        }

        // 계정 탈퇴 버튼 클릭
        binding.withdrawalButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickWithdrawalButton()
            }
        }


        // 플로우 구독
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 회원정보 플로우 구독, 화면 세팅
                launch {
                    viewModel.userInfo.collect {
                        // 이메일, 닉네임, 생년 세팅
                        binding.email.text = it["email"] as String?
                        binding.nickname.text = it["nickname"] as String?
                        binding.birthyear.text = it["birthyear"]?.toString()?.toDouble()?.toInt()?.toString()
                    }
                }

                // 클릭 이벤트 플로우 구독, 이벤트 처리
                // 비밀번호 변경 이벤트 처리
                launch {
                    viewModel.changePasswordButton.collect {
                        val intent = Intent(applicationContext, ChangePassword::class.java)
                        startActivity(intent)
                    }
                }

                // 닉네임 변경 이벤트 처리
                launch {
                    viewModel.changeNicknameButton.collect {
                        val intent = Intent(applicationContext, ChangeNickname::class.java)
                        startActivity(intent)
                    }
                }

                // 로그아웃 이벤트 처리
                launch {
                    viewModel.logoutButton.collect {
                        // 상태 저장
                        viewModel.saveAutoLoginCheck(false)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                // 회원 탈퇴 이벤트 처리
                launch {
                    viewModel.withdrawalButton.collect {
                        withdrawDialog()
                    }
                }

                // 회원 탈퇴 결과 처리
                launch {
                    viewModel.deleteUserResult.collect {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (it["code"].toString().toDouble() == 4000.0) {
                            // 알람 삭제
                            AlarmSetting.stopAlarm(applicationContext)
                            // 모든 상태저장 삭제
                            // 알람 설정 해제 (임시)
                            viewModel.clearAlarmSharedPreferences()
                            // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기 (임시)
                            viewModel.clearTimeSharedPreferences()
                            // 저장 설정 지우기
                            viewModel.clearAutoSaveSharedPreferences()
                            // 화면 전환
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }

                // 에러 감지
                // 에러 값 구독
                launch {
                    viewModel.error.collect {
                        if (it) {
                            dialog("서버와의 통신에 실패했습니다. 인터넷 연결 확인 후 앱을 다시 시작해주세요.")
                        }
                    }
               }
            }
        }

    }

    // 탈퇴 시 확인 다이얼로그
    var dialogListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
        // 탈퇴 눌렀을 때 이벤트 처리
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            lifecycleScope.launch {
                viewModel.deleteUser()
            }
        }
    }

    fun withdrawDialog() {
        val builder = AlertDialog.Builder(this)
        // 화면 밖이나 뒤로가기 눌렀을 때 다이얼로그 닫히지 않게 하기
        builder.setCancelable(false)
        builder.setMessage("정말 탈퇴하시겠습니까?")
        builder.setNegativeButton("탈퇴", dialogListener)
        builder.setPositiveButton("취소", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // error dialoguee
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

}