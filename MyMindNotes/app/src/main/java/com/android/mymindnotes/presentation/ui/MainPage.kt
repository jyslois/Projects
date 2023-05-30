package com.android.mymindnotes.presentation.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.graphics.Color
import android.os.SystemClock
import android.util.Log
import com.bumptech.glide.Glide
import com.android.mymindnotes.R
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

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

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
                // 최초 접속 시에 알람 설정 다이얼로그 띄워주기
                // 최초 값 구독
                launch {
                    Log.e("FirstTimeCheck", "Activity - launch 안에 들어옴")
                    viewModel.firstTime.collect {
                        Log.e("FirstTimeCheck", "결과 최종으로 돌어옴 - 결과: $it")
                        if (it) {
                            Log.e("FirstTimeCheck", "결과 최종 - true일 경우. 완료")
                            // 다이얼로그 띄우기
                            setAlarmDialog()
                            viewModel.saveFirstTime(false)
                        }
                    }
                }

                // 닉네임 세팅
                // 회원 정보 값 구독
                launch {
                    viewModel.userInfo.collect {
                        // 닉네임 세팅
                        Log.e("UserInfoCheck", "결과 최종으로 돌어옴 - 결과: $it")
                        val nick = it["nickname"] as String?
                        if (nick != null) {
                            binding.mainpagetext.text = "오늘 하루도 고생했어요, $nick 님."
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

                // 클릭 이벤트 감지
                // 일기 쓰기 버튼 클릭 이벤트 감지
                launch {
                    viewModel.clickAddRecordButton.collect {
                        if (it) {
                            startActivity<RecordMindChoice>()
//                            val intent = Intent(applicationContext, RecordMindChoice::class.java)
//                            startActivity(intent)
                        }
                    }
                }

                // 메뉴 버튼 클릭 이벤트 감지
                launch {
                    viewModel.clickMainMenuButton.collect {
                        if (it) {
                            startActivity<MainMenu>()
//                            val intent = Intent(applicationContext, MainMenu::class.java)
//                            startActivity(intent)
                        }
                    }
                }

            }
        }

    }

    // 뒤로가기 버튼 클릭
    var initTime = 0L
    override fun onBackPressed() {
        /*
        System.currentTimeMillis()는 현재 시간을 반환하는데, 이것은 사용자가 설정을 변경하거나 네트워크 시간 동기화로 인해 변할 수 있다.
        따라서, 시간이 절대적으로 지나가는 것을 측정하려면 이 방법이 적합하지 않을 수 있다. 반면에 SystemClock.elapsedRealtime()는 장치가
        부팅된 이후의 경과 시간(잠자기 모드에서는 시간이 흐르지 않음)을 반환하기 때문에 사용자 설정이나 네트워크에 의해 영향을 받지 않으므로, 상대적인 시간 측정에 더 적합하다.
         */
        if (SystemClock.elapsedRealtime() - initTime > 3000) {
            // 메세지 띄우기
            val toast = Toast.makeText(applicationContext, "종료하려면 한 번 더 누르세요", Toast.LENGTH_SHORT)
            toast.show()
            // 현재 시간을 initTime에 지정
            initTime = SystemClock.elapsedRealtime()
        } else {
            // 3초 이내에 BackButton이 두 번 눌린 경우 앱 종료
            finishAffinity()
        }
    }

    var dialogListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
        if (which == DialogInterface.BUTTON_POSITIVE) {
            // 알람 설정 페이지로 이동
            startActivity<AlarmSetting>()
//            val intent = Intent(this, AlarmSetting::class.java)
//            startActivity(intent)
        }
    }

    // 알림 설정 dialogue
    private fun setAlarmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("하루에 한 번 일기 쓰기를 위한 알람을 설정하시겠어요?")
        builder.setNegativeButton("아니요", null)
        builder.setPositiveButton("예", dialogListener)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // error dialogue
    fun dialog(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
        builder.setPositiveButton("확인", null)
        alertDialog = builder.show()
        alertDialog.show()
    }

    // 실체화한 타입 파라미터로 클래스 참조 대신하기 - 액티비티의 클래스를 java.lang.Class로 전달하는 대신, 실체화한 타입 파라미터 사용하기
    inline fun <reified T : Activity> Context.startActivity() {  // 타입 파라미터를 reified로 표시
        val intent = Intent(this, T::class.java) // T::class로 타입 파라미터의 클래스를 가져온다
        // getApplicationContext()을 사용하게 될 경우에는, 애플리케이션 컨텍스트가 액티비티의 라이프사이클과 연관되지 않기 때문에 task stack에 문제가 발생하여,
        // 사용자가 "뒤로" 버튼을 눌렀을 때 기대하는 동작과 다르게 동작할 수 있다.
        startActivity(intent)
    }

}