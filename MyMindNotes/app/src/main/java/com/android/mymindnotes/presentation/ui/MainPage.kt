package com.android.mymindnotes.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.DialogInterface
import android.content.Intent
import com.android.mymindnotes.AlarmSetting
import android.os.Bundle
import android.graphics.Color
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
import com.android.mymindnotes.MainMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.databinding.ActivityMainPageBinding
import com.android.mymindnotes.presentation.viewmodels.MainPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainPage : AppCompatActivity() {
    private lateinit var binding: ActivityMainPageBinding

    // viewModel 객체 주입
    private val viewModel: MainPageViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background)
        // 메뉴 이미지
        binding.mainmenu.setColorFilter(Color.parseColor("#BCFFD7CE"))

        // 버튼 클릭 이벤트
        // 일기 쓰기 버튼 클릭
        binding.addRecordButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickAddRecordButton()
            }
        }

        // 메뉴 버튼 클릭
        binding.mainmenu.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickMainMenuButton()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 클릭 이벤트 감지
                // 일기 쓰기 버튼 클릭 이벤트 감지
                launch {
                    viewModel.clickAddRecordButton.collect {
                        if (it) {
                            val intent = Intent(applicationContext, RecordMindChoice::class.java)
                            startActivity(intent)
                        }
                    }
                }

                // 메뉴 버튼 클릭 이벤트 감지
                launch {
                    viewModel.clickMainMenuButton.collect {
                        if (it) {
                            val intent = Intent(applicationContext, MainMenu::class.java)
                            startActivity(intent)
                        }
                    }
                }

                // 최초 접속 시에 알람 설정 다이얼로그 띄워주기
                // 최초 값 구독
                launch {
                    viewModel.firstTime.collect {
                        if (it) {
                            // 다이얼로그 띄우기
                            val builder = AlertDialog.Builder(baseContext)
                            builder.setMessage("하루에 한 번 일기 쓰기를 위한 알람을 설정하시겠어요?")
                            builder.setNegativeButton("아니요", null)
                            builder.setPositiveButton("예", dialogListener)
                            val alertDialog = builder.show()
                            alertDialog.show()
                            // 최초 접속 값 false로 변경 (다음부터 안 뜨게)
                            viewModel.saveFirstTime(false)
                        }
                    }
                }

                // 닉네임 세팅
                // 회원 정보 값 구독
                launch {
                    viewModel.userInfo.collect {
                        // 닉네임 세팅
                        val nick = it["nickname"] as String?
                        binding.mainpagetext.text = "오늘 하루도 고생했어요, $nick 님."
                    }
                }

            }
        }

    }

    var initTime = 0L
    // 뒤로가기 버튼 클릭
    override fun onBackPressed() {
        if (System.currentTimeMillis() - initTime > 3000) {
            // 메세지 띄우기
            val toast = Toast.makeText(baseContext, "종료하려면 한 번 더 누르세요", Toast.LENGTH_SHORT)
            toast.show()
            // 현재 시간을 initTime에 지정
            initTime = System.currentTimeMillis()
        } else {
            // 3초 이내에 BackButton이 두 번 눌린 경우 앱 종료
            finishAffinity()
        }
    }

    var dialogListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
        if (which == DialogInterface.BUTTON_POSITIVE) {
            // 알람 설정 페이지로 이동
            val intent = Intent(applicationContext, AlarmSetting::class.java)
            startActivity(intent)
        }
    }


}