package com.android.mymindnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mymindnotes.model.retrofit.DeleteUserApi;
import com.android.mymindnotes.data.retrofit.api.user.GetUserInfoApi;
import com.android.mymindnotes.data.retrofit.RetrofitService;
import com.android.mymindnotes.presentation.ui.MainActivity;
import com.bumptech.glide.Glide;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInformation extends AppCompatActivity {
    com.android.mymindnotes.databinding.ActivityAccountInformationBinding binding;
    AlertDialog alertDialog;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;
    SharedPreferences userindex;
    // 알람 시간 설정 여부와 UI text 세팅을 위한 sharedpreferences
    SharedPreferences alarm;
    SharedPreferences.Editor alarmEdit;
    // 부팅시 알람 재설정을 위한 sharedpreferences
    SharedPreferences timeSave;
    SharedPreferences.Editor timeSaveEdit;

    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
    public void getStandardSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.android.mymindnotes.databinding.ActivityAccountInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        userindex = getSharedPreferences("user", Activity.MODE_PRIVATE);
//        getUserInfo();
        alarm = getSharedPreferences("alarm", Activity.MODE_PRIVATE);
        alarmEdit = alarm.edit();
        timeSave = getSharedPreferences("time", Activity.MODE_PRIVATE);
        timeSaveEdit = timeSave.edit();

        // 글씨 크기 조절
        getStandardSize();
        binding.emailText.setTextSize((float) (standardSize_X / 23));
        binding.nickNameText.setTextSize((float) (standardSize_X / 23));
        binding.birthyearText.setTextSize((float) (standardSize_X / 23));
        binding.email.setTextSize((float) (standardSize_X / 24));
        binding.nickname.setTextSize((float) (standardSize_X / 24));
        binding.birthyear.setTextSize((float) (standardSize_X / 24));
        binding.changeNicknameButton.setTextSize((float) (standardSize_X / 24));
        binding.changePasswordButton.setTextSize((float) (standardSize_X / 24));
        binding.logoutButton.setTextSize((float) (standardSize_X / 25));
        binding.withdrawalButton.setTextSize((float) (standardSize_X / 25));

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 변경 버튼 이벤트 처리
        binding.changePasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(intent);
        });

        binding.changeNicknameButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ChangeNickname.class);
            startActivity(intent);
        });

        // 로그아웃 버튼 이벤트 처리
        binding.logoutButton.setOnClickListener(view -> {
            // 상태 저장
            autoSaveEdit.putBoolean("autoLoginCheck", false);
            autoSaveEdit.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        // 계정 탈퇴 버튼 이벤트 처리
        binding.withdrawalButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 화면 밖이나 뒤로가기 눌렀을 때 다이얼로그 닫히지 않게 하기
            builder.setCancelable(false);
            builder.setMessage("정말 탈퇴하시겠습니까?");
            builder.setNegativeButton("탈퇴", dialogListener);
            builder.setPositiveButton("취소", null);
            alertDialog = builder.show();
            // 메시지 크기 조절
            TextView messageText = alertDialog.findViewById(android.R.id.message);
            messageText.setTextSize((float) (standardSize_X / 24));
            // 버튼 크기 조절
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize((float) (standardSize_X / 25));
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize((float) (standardSize_X / 25));
            alertDialog.show();
        });
    }

    DialogInterface.OnClickListener dialogListener = (dialog, which) -> {
        // 탈퇴 눌렀을 때 이벤트 처리
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            deleteUser();
        }
    };

//    // 네트워크 통신: 회원 정보 가져오기
//    public void getUserInfo() {
//            // Retrofit 객체 생성
//            RetrofitService retrofitService = new RetrofitService();
//            // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
//            GetUserInfoApi getUserInfoApi = retrofitService.getRetrofit().create(GetUserInfoApi.class);
//            // Call 객체 획득
//            Call<Map<String, Object>> call = getUserInfoApi.getUserInfo(userindex.getInt("userindex", 0));
//            // 네트워킹 시도
//            call.enqueue(new Callback<Map<String, Object>>() {
//                @Override
//                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
//                    // 이메일, 닉네임, 생년 세팅
//                    binding.email.setText(String.valueOf(response.body().get("email")));
//                    binding.nickname.setText((String.valueOf(response.body().get("nickname"))));
//                    int birthyear = (int) Double.parseDouble(String.valueOf((response.body().get("birthyear"))));
//                    binding.birthyear.setText(String.valueOf(birthyear));
//                }
//                @Override
//                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
//                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            });
//    }

    // 네트워크 통신: 회원 탈퇴 처리
    public void deleteUser() {
                // Retrofit 객체 생성
                RetrofitService retrofitService = new RetrofitService();
                // Retrofit 객체에 Service 인터페이스 등록
                DeleteUserApi deleteUserApi = retrofitService.getRetrofit().create(DeleteUserApi.class);
                // Call 객체 획득
                Call<Map<String, Object>> call = deleteUserApi.deleteUser(userindex.getInt("userindex", 0));
                // 네트워킹 시도
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        // Object로 저장되어 있는 Double(스프링부트에서 더블로 저장됨)을 우선 String으로 만든 다음
                        // Double로 캐스팅한 다음에 int와 비교해야 오류가 나지 않는다. (Object == int 이렇게 비교되지 않는다)
                        if (Double.parseDouble(String.valueOf(response.body().get("code"))) == 4000) {
                            // 알람 삭제
                            AlarmSetting.stopAlarm(getApplicationContext());
                            // 모든 상태저장 삭제
                            // 알람 설정 해제
                            alarmEdit.clear();
                            alarmEdit.commit();
                            // 부팅시 알람 재설정을 위한 sharedPrefenreces의 시간 삭제하기
                            timeSaveEdit.clear();
                            timeSaveEdit.commit();
                            // 저장 설정 지우기
                            autoSaveEdit.clear();
                            autoSaveEdit.commit();
                            // 화면 전환
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
}