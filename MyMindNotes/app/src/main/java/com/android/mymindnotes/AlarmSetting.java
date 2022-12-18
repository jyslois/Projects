package com.android.mymindnotes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.mymindnotes.databinding.ActivityAlarmSettingBinding;
import com.bumptech.glide.Glide;

import java.util.Calendar;

public class AlarmSetting extends AppCompatActivity {
    ActivityAlarmSettingBinding binding;
    SharedPreferences alarm;
    SharedPreferences.Editor alarmEdit;
    TimePickerDialog dialog;
    private static AlarmManager alarmManager;
    private static PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 액션 바 타이틀
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.changepassword_actionbartext); // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarm = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        alarmEdit = alarm.edit();

        // 알람 상태 불러오기
        // true: if(true), false: if(false), none: if(false)
        if (alarm.getBoolean("alarm", false)) {
            // 알람이 설정된 적이 있으면
            binding.alarmSwitch.setChecked(true);
            // On일 때의 동작
            binding.timeText.setTextColor(Color.BLACK);
            binding.setTimeButtton.setVisibility(View.VISIBLE);
            if (alarm.getString("time", "").equals("")) {
                binding.setTimeButtton.setText("오후 10:00");
            } else {
                binding.setTimeButtton.setText(alarm.getString("time", ""));
            }
        } else {
            // 알람이 설정된 적이 없거나 off이다면
            binding.alarmSwitch.setChecked(false);
            // Off일 때의 동작
            binding.timeText.setTextColor(Color.parseColor("#979696"));
            binding.setTimeButtton.setVisibility(View.INVISIBLE);
        }

        // 권한 요청 팝업창
        ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // 만약 허용해주었다면
                // On일 때의 동작 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
                binding.timeText.setTextColor(Color.BLACK);
                binding.setTimeButtton.setVisibility(View.VISIBLE);
                // 상태 저장
                alarmEdit.putBoolean("alarm", true);
                alarmEdit.commit();
                // 오후 10시로 설정 저장
                alarmEdit.putString("time", "오후 10:00");
                alarmEdit.commit();
                binding.setTimeButtton.setText("오후 10:00");
                // 오후 10시로 기본 알람 설정
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 22);
                calendar.set(Calendar.MINUTE, 00);
                calendar.set(Calendar.SECOND, 00);
                setAlarm(calendar, getApplicationContext());
            } else {
                // 허용해주지 않았다면
                Toast.makeText(this, "설정 > 앱 > 권한에서 알림 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                binding.alarmSwitch.setChecked(false);
            }
        });

        // 알람 스위치 바뀔 때의 이벤트
        binding.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 권한 허용을 받았다면
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == 0) {
                    // On일 때의 동작 - timeText 색깔 변경하고 시간 바꾸는 버튼의 텍스트 보이기
                    binding.timeText.setTextColor(Color.BLACK);
                    binding.setTimeButtton.setVisibility(View.VISIBLE);
                    // 상태 저장
                    alarmEdit.putBoolean("alarm", true);
                    alarmEdit.commit();
                    // 오후 10시로 설정 저장
                    alarmEdit.putString("time", "오후 10:00");
                    alarmEdit.commit();
                    binding.setTimeButtton.setText("오후 10:00");
                    // 오후 10시로 기본 알람 설정
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 22);
                    calendar.set(Calendar.MINUTE, 00);
                    calendar.set(Calendar.SECOND, 00);
                    setAlarm(calendar, getApplicationContext());
                // 권한 허용을 받지 못했다면
                } else {
                    permissionLauncher.launch("android.permission.POST_NOTIFICATIONS");
                }
            } else {
                // Off일 때의 동작
                binding.timeText.setTextColor(Color.parseColor("#979696"));
                // Text 원래대로 오후 10시로 돌리기
                binding.setTimeButtton.setText("오후 10:00");
                binding.setTimeButtton.setVisibility(View.GONE);
                // 모든 상태저장 삭제
                alarmEdit.clear();
                alarmEdit.commit();
                // 알람 설정 해제
                stopAlarm(getApplicationContext());
            }
        });



        // 클릭 시 다일러로그 띄우기, OK버튼 누르면 기본 알람 설정 해제. 선택한 시간으로 새로운 알람 설정. 버튼 텍스트 선택한 시간으로 변경.
        binding.setTimeButtton.setOnClickListener(view -> {
            dialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String min = "";
                    String time = "";
                    String daynight = "";
                    String hour = "";

                    if (minute >= 0 && minute < 10) {
                        min = "0" + minute;
                    } else {
                        min = "" + minute;
                    }

                    // 선택한 24시 시간을 12시간으로(오전/오후) 표시.
                    // 24, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11: 오전
                    // 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23: 오후
                    if (hourOfDay >= 0 && hourOfDay <= 11) {
                        daynight = "오전";
                        hour = hourOfDay + "";

                        if (hourOfDay == 0) {
                            hour = "12";
                        }
                    } else {
                        daynight = "오후";
                        hour = hourOfDay - 12 + "";
                        if (hourOfDay == 12) {
                            hour = "12";
                        }
                    }
                    // 선택한 시간으로 텍스트 설정
                    time = daynight + " " + hour + ":" + min;
                    binding.setTimeButtton.setText(time);

                    // 선택한 시간 저장
                    alarmEdit.putString("time", time);
                    alarmEdit.commit();

                    alarmEdit.putInt("hour", hourOfDay);
                    alarmEdit.commit();

                    alarmEdit.putInt("minute", Integer.parseInt(min));
                    alarmEdit.commit();

                    // 원래 설정되어 있던 알람 설정 해제.
                    stopAlarm(getApplicationContext());
                    // 선택한 시간으로 알람 설정.
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 00);

                    // 현재 시간보다 이전이면
                    if (calendar.before(Calendar.getInstance())) {
                        // 다음 날로 설정
                        calendar.add(Calendar.DATE, 1);
                    }

                    setAlarm(calendar, getApplicationContext());
                }
            }, alarm.getInt("hour", 22), alarm.getInt("minute", 00), false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        });

    }

    // 알람 설정
    static void setAlarm(Calendar calendar, Context context) {
        // 알람 메니져 선언
        AlarmSetting.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Receiver 설정
        Intent intent = new Intent(context, AlarmReceiver.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 1, intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Log.e("PendingIntent", "PendingIntent - Start>>" + pendingIntent);


////         알람설정: API 19부터는 모든 반복 알람이 부정확해짐. cancel이 제대로 되는지를 위한 테스트.
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                30 * 1000, pendingIntent);

        // 마시멜로(6.0 / api23) 버전부터 도즈모드가 도입되면서 기존에 사용하던 setExact, set 메소드를 사용했을 경우 도즈모드에 진입한 경우 알람이 울리지 않는다.  'setExactAndAllowWhileIdle'은 도즈모드에서도 잠깐 깨어나 알람을 울리게 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    // 알람 중지
    static void stopAlarm(Context context) {
        // 알람 메니져 선언
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),1, intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_NO_CREATE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),1, intent, PendingIntent.FLAG_NO_CREATE);
        }

        Log.e("PendingIntent", "PendingIntent - Stop>>" + pendingIntent);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

}
