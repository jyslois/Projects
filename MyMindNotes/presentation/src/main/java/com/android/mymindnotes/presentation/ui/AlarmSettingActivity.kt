package com.android.mymindnotes.presentation.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import android.os.Bundle
import com.bumptech.glide.Glide
import android.widget.Toast
import android.widget.CompoundButton
import android.os.Build
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.mymindnotes.core.hilt.sharedpreferencesModule.Alarm
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityAlarmSettingBinding
import com.android.mymindnotes.presentation.viewmodels.AlarmSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AlarmSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmSettingBinding
    private val viewModel: AlarmSettingViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background)

        // 액션 바 세팅
        setActionBar()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        is AlarmSettingViewModel.AlarmSettingUiState.Loading -> {
                            uiWhenSwitchedOff()
                        }


                        // AlarmSwitch 이벤트
                        is AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOnWithTime -> {
                            // On이고 이미 설정한 알람이 존재한다면, 그 시간을 화면에 표시
                            uiWhenSwitchedOnWithTime(uiState.time)
                        }

                        is AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOn -> {
                            // 그냥 On일 때의 동작 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
                            uiWhenSwitchedOn()
                        }

                        is AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchedOff -> {
                            // Off일 때 동작
                            uiWhenSwitchedOff()

                            Toast.makeText(applicationContext, "알람이 해제되었습니다", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
                }
            }


        }


        // 알람 스위치 바뀔 때의 이벤트
        binding.alarmSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            lifecycleScope.launch {
                // 알람 스위치 켰을 때
                if (isChecked) {
                    // Android 12까지는 앱을 설치하면 기본적으로 Notification을 띄울 수 있었지만,
                    // 안드로이드 13, 티라미슈 - 2022년 2월 10일 처음공개 - 부터는 Notification 런타임 권한이 추가되었고, 이제 이 권한으로 앱의 Notification 발송 권한을 제어할 수 있도록 변경되었다.
                    // 또한, 기본적으로 Runtime permission은 OFF이기 때문에, 앱은 사용자에게 이 권한을 받기 전까지 노티피케이션을 발송할 수 없다.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // 권한 허용을 받았다면
                        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                        ) {
                            viewModel.changeAlarmSwitch(true)
                        } else {
                            // 권한 허용을 받지 못했다면
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        viewModel.changeAlarmSwitch(true)

                    }
                } else {
                    // 알람 스위치 껐을 때
                    viewModel.changeAlarmSwitch(false)
                }

            }
        }


        // 클릭 시 다일러로그 띄우기, OK버튼 누르면 기본 알람 설정 해제. 선택한 시간으로 새로운 알람 설정. 버튼 텍스트 선택한 시간으로 변경.
        binding.setTimeButtton.setOnClickListener {
            lifecycleScope.launch {
                val hour = viewModel.getHour()
                val minute = viewModel.getMinute()

                val dialog = TimePickerDialog(this@AlarmSettingActivity, R.style.PinkTimePickerTheme, { _, hourOfDay, minute ->
                    var min = ""
                    var time = ""
                    var daynight = ""
                    var hour = ""

                    min = if (minute in 0..9) {
                        "0$minute"
                    } else {
                        "$minute"
                    }

                    // Convert the selected 24-hour time to 12-hour format (AM/PM)
                    // 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11: AM
                    // 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23: PM
                    if (hourOfDay in 0..11) {
                        daynight = "오전"
                        hour = hourOfDay.toString()

                        if (hourOfDay == 0) {
                            hour = "12"
                        }
                    } else {
                        daynight = "오후"
                        hour = (hourOfDay - 12).toString()
                        if (hourOfDay == 12) {
                            hour = "12"
                        }
                    }
                    // set text to selected time
                    time = "$daynight $hour:$min"
                    binding.setTimeButtton.text = time
                    binding.setTimeButtton.paintFlags =
                        Paint.UNDERLINE_TEXT_FLAG


                    viewModel.doAlarmSettings(time, hourOfDay, Integer.parseInt(min), minute)


                    Toast.makeText(applicationContext, "매일 $time 분에 알람이 울려요", Toast.LENGTH_SHORT).show()
                }, hour, minute, false)

                dialog.show()
            }
        }


    }

    private fun setActionBar() {
        // 액션 바 타이틀
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 기본 타이틀 사용 안함
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM // 커스텀 사용
        supportActionBar!!.setCustomView(R.layout.changepassword_actionbartext) // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    // 알람이 켜지기만 한 상태(설정 안 되어 있으면)의 UI
    private fun uiWhenSwitchedOn() {
        binding.alarmSwitch.isChecked = true
        // on일 때의 ui설정 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
        binding.setTimeButtton.text = "시간선택(클릭)"
        binding.timeText.setTextColor(Color.BLACK)
        binding.setTimeButtton.visibility = View.VISIBLE
        binding.setTimeButtton.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    // 알람도 켜져 있고, 알람 시간도 설정되어 있을 때 UI
    private fun uiWhenSwitchedOnWithTime(time: String) {
        binding.alarmSwitch.isChecked = true
        // on일 때의 ui설정 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
        binding.setTimeButtton.text = time
        binding.timeText.setTextColor(Color.BLACK)
        binding.setTimeButtton.visibility = View.VISIBLE
        binding.setTimeButtton.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    // 알람이 꺼져 있을 때 UI
    private fun uiWhenSwitchedOff() {
        binding.alarmSwitch.isChecked = false
        // off일 때의 ui설정 - 시간 바꾸는 버튼의 텍스트 보이지 않게 하고, timeText 텍스트 글짜색 흐리게 바꾸기
        binding.timeText.setTextColor(Color.parseColor("#979696"))
        binding.setTimeButtton.visibility = View.INVISIBLE
    }

    // 권한 요청 팝업창
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 만약 허용해주었다면
                lifecycleScope.launch {
                    viewModel.changeAlarmSwitch(true)
                }

            } else {
                // 허용해주지 않았다면
                Toast.makeText(this, "설정 > 앱 > 권한에서 알림 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                binding.alarmSwitch.isChecked = false
            }
        }

}