package com.android.mymindnotes;


import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Toast;
import com.android.mymindnotes.databinding.ActivityMainPageBinding;
import com.android.mymindnotes.model.retrofit.GetUserInfoApi;
import com.android.mymindnotes.model.retrofit.RetrofitService;
import com.bumptech.glide.Glide;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPage extends AppCompatActivity {
    com.android.mymindnotes.databinding.ActivityMainPageBinding binding;
    String nick;
    SharedPreferences userindex;

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
        getUserNickname();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userindex = getSharedPreferences("userindex", Activity.MODE_PRIVATE);

        getUserNickname();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        binding.addRecordButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RecordMindChoice.class);
            startActivity(intent);
        });

        // 메뉴 이미지
        binding.mainmenu.setColorFilter(Color.parseColor("#BCFFD7CE"));
        binding.mainmenu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);
        });

        // 글짜 크기 조절
        getStandardSize();
        binding.mainpagetext.setTextSize((float) (standardSize_X / 23));

    }


    // 뒤로가기 버튼 두 번 누르면 앱 종료
    long initTime = 0L;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - initTime > 3000) {
            // 메세지 띄우기
            Toast toast = Toast.makeText(this, "종료 하려면 한 번 더 누르세요", Toast.LENGTH_SHORT);
            toast.show();

            // 현재 시간을 initTime에 지정
            initTime = System.currentTimeMillis();
        } else {
            // 3초 이내에 BackButton이 두 번 눌린 경우 앱 종료
            finishAffinity();
        }
    }


    // 네트워크 통신 : 회원 정보 가져오기(닉네임 세팅하기)
    public void getUserNickname() {
        Thread thread = new Thread(() -> {
            // Retrofit 객체 생성
            RetrofitService retrofitService = new RetrofitService();
            // Retrofit 객체에 인터페이스(Api) 등록, Call 객체 반환하는 Service 객체 생성
            GetUserInfoApi getUserInfoApi = retrofitService.getRetrofit().create(GetUserInfoApi.class);
            // Call 객체 획득
            Call<Map<String, Object>> call = getUserInfoApi.getUserInfo(userindex.getInt("userindex", 0));
            // 네트워킹 시도
            call.enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    // 닉네임 세팅
                    nick = (String) response.body().get("nickname");
                    binding.mainpagetext.setText("오늘 하루도 고생했어요, " + nick + " 님.");
                }
                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), "네트워크 연결에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        });
        thread.start();
    }

}