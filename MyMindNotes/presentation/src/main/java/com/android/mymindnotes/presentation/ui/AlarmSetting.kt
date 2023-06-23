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
import com.android.mymindnotes.presentation.R
import com.android.mymindnotes.presentation.databinding.ActivityAlarmSettingBinding
import com.android.mymindnotes.presentation.viewmodels.AlarmSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class AlarmSetting : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmSettingBinding
    private val viewModel: AlarmSettingViewModel by viewModels()

    // 다이얼로그 변수
    lateinit var alertDialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.N)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background)

        // 액션 바 타이틀
        supportActionBar!!.setDisplayShowTitleEnabled(false) // 기본 타이틀 사용 안함
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM // 커스텀 사용
        supportActionBar!!.setCustomView(R.layout.changepassword_actionbartext) // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {

                        // 알람 상태 가져오기
                        is AlarmSettingViewModel.AlarmSettingUiState.AlarmState -> {
                            // 알람이 설정된 적이 있으면
                            if (uiState.isSet) {
                                binding.alarmSwitch.isChecked = true
                                // On일 때의 동작
                                binding.timeText.setTextColor(Color.BLACK)
                                binding.setTimeButtton.visibility = View.VISIBLE
                                binding.setTimeButtton.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                                lifecycleScope.launch {
                                    viewModel.uiState
                                        .filterIsInstance<AlarmSettingViewModel.AlarmSettingUiState.AlarmTime>()
                                        .collect { alarmTime ->
                                            if (alarmTime.time.isNullOrEmpty()) {
                                                binding.setTimeButtton.text = "시간선택(클릭)"
                                                binding.setTimeButtton.paintFlags =
                                                    Paint.UNDERLINE_TEXT_FLAG
                                            } else {
                                                binding.setTimeButtton.text = alarmTime.time
                                                binding.setTimeButtton.paintFlags =
                                                    Paint.UNDERLINE_TEXT_FLAG
                                            }
                                        }
                                }
                            } else {
                                // 알람이 설정된 적이 없거나 off이다면
                                binding.alarmSwitch.isChecked = false
                                // Off일 때의 동작
                                binding.timeText.setTextColor(Color.parseColor("#979696"))
                                binding.setTimeButtton.visibility = View.INVISIBLE
                            }
                        }

                        // alarmSwitch 이벤트 구독
                        is AlarmSettingViewModel.AlarmSettingUiState.AlarmSwitchState -> {
                            val isChecked = uiState.isChecked
                            if (isChecked) {
                                // Android 12까지는 앱을 설치하면 기본적으로 Notification을 띄울 수 있었지만,
                                // 안드로이드 13, 티라미슈 - 2022년 2월 10일 처음공개 - 부터는 Notification 런타임 권한이 추가되었고, 이제 이 권한으로 앱의 Notification 발송 권한을 제어할 수 있도록 변경되었다.
                                // 또한, 기본적으로 Runtime permission은 OFF이기 때문에, 앱은 사용자에게 이 권한을 받기 전까지 노티피케이션을 발송할 수 없다.
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    // 권한 허용을 받았다면
                                    if (ContextCompat.checkSelfPermission(
                                            applicationContext,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // On일 때의 동작 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
                                        binding.timeText.setTextColor(Color.BLACK)
                                        binding.setTimeButtton.text = "시간선택(클릭)"
                                        binding.setTimeButtton.visibility = View.VISIBLE
                                        binding.setTimeButtton.paintFlags =
                                            Paint.UNDERLINE_TEXT_FLAG
                                        // 상태 저장
                                        viewModel.saveAlarmState(true)

                                    } else {
                                        // 권한 허용을 받지 못했다면
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                } else {
                                    // On일 때의 동작 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
                                    binding.timeText.setTextColor(Color.BLACK)
                                    binding.setTimeButtton.text = "시간선택(클릭)"
                                    binding.setTimeButtton.visibility = View.VISIBLE
                                    binding.setTimeButtton.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                                    // 상태 저장
                                    viewModel.saveAlarmState(true)

                                }
                            } else {
                                // Off일 때의 동작
                                binding.timeText.setTextColor(Color.parseColor("#979696"))
                                // Text 원래대로 되돌리기. 그런 다음 보이지 않게 하기.
                                binding.setTimeButtton.text = "시간선택(클릭)"
                                binding.setTimeButtton.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                                binding.setTimeButtton.visibility = View.GONE
                                // 모든 상태저장 삭제
                                viewModel.clearAlarmSettings()
                                // 알람 설정 해제
                                // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
                                viewModel.clearTimeSettings()
                                viewModel.stopAlarm()

                                Toast.makeText(applicationContext, "알람이 해제되었습니다", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        // setTimeButton 클릭 이벤트 감지
                        is AlarmSettingViewModel.AlarmSettingUiState.SetTimeButtonState -> {
                            val state = uiState.isClicked
                            if (state) {
                                val hour = viewModel.getHour().first()
                                val minute = viewModel.getMinute().first()

                                val dialog = TimePickerDialog(
                                    this@AlarmSetting,
                                    R.style.PinkTimePickerTheme,
                                    { _, hourOfDay, minute ->
                                        var min = ""
                                        var time = ""
                                        var daynight = ""
                                        var hour = ""

                                        min = if (minute in 0..9) {
                                            "0$minute"
                                        } else {
                                            "$minute"
                                        }

                                        Log.e("알람 체크", "분 - $min")

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


                                        // store selected time
                                        launch {
                                            viewModel.saveTime(time)
                                        }

                                        launch {
                                            viewModel.saveHour(hourOfDay)
                                        }

                                        launch {
                                            viewModel.saveMinute(Integer.parseInt(min))
                                        }

                                        // Delete time in sharedPrefenreces to reset alarm on boot
                                        launch {
                                            viewModel.clearTimeSettings()
                                        }

                                        // Unset the alarm that was originally set.
                                        viewModel.stopAlarm()


                                        // Set the alarm to the selected time.
                                        val calendar = Calendar.getInstance()
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                        calendar.set(Calendar.MINUTE, minute)
                                        calendar.set(Calendar.SECOND, 0)

                                        // if earlier than the current time
                                        if (calendar.before(Calendar.getInstance())) {
                                            // set to the next day
                                            calendar.add(Calendar.DATE, 1)
                                        }

                                        launch {
                                            // Save calendar time to sharedPrefenreces to reset alarm at bootup
                                            viewModel.saveRebootTime(calendar.timeInMillis)
                                        }


                                        viewModel.setAlarm(calendar)


                                        Toast.makeText(
                                            applicationContext,
                                            "매일 $time 분에 알람이 울려요",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    hour,
                                    minute,
                                    false
                                )



                                dialog.show()
                            }
                        }

                        is AlarmSettingViewModel.AlarmSettingUiState.AlarmTime -> {
                            // Do nothing
                        }

                    }
                }
            }


        }


        // 알람 스위치 바뀔 때의 이벤트
        binding.alarmSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            lifecycleScope.launch {
                viewModel.changeAlarmSwitch(isChecked)
            }
        }


        // 클릭 시 다일러로그 띄우기, OK버튼 누르면 기본 알람 설정 해제. 선택한 시간으로 새로운 알람 설정. 버튼 텍스트 선택한 시간으로 변경.
        binding.setTimeButtton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clickSetTimeButton()
            }
        }


    }

    // 권한 요청 팝업창
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 만약 허용해주었다면
                // On일 때의 동작 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
                binding.timeText.setTextColor(Color.BLACK)
                binding.setTimeButtton.visibility = View.VISIBLE
                binding.setTimeButtton.paintFlags = Paint.UNDERLINE_TEXT_FLAG

                // 상태 저장
                lifecycleScope.launch {
                    viewModel.saveAlarmState(true)
                }
                binding.setTimeButtton.text = "시간선택(클릭)"
            } else {
                // 허용해주지 않았다면
                Toast.makeText(this, "설정 > 앱 > 권한에서 알림 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                binding.alarmSwitch.isChecked = false
            }
        }

}