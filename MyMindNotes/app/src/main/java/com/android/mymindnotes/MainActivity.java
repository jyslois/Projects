package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;

import com.android.mymindnotes.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SharedPreferences auto;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);

        // 만약 로그인 상태 유지 값이 true로 저장되어 있다면
        if (auto.getBoolean("autoLoginCheck", false)) {
            // 자동 로그인해서 메인페이지로 넘어가기
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
        }

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainbackground2).into(binding.background);

        // 로그인 클릭 시 화면 전환
        Button loginButton = binding.loginButton;

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

        // 회원가입 클릭 시 화면 전환
        Button joinButton = binding.joinButton;

        joinButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Join.class);
            startActivity(intent);
        });

        // 글짜 크기 조절
        getStandardSize();
        binding.loginButton.setTextSize((float) (standardSize_X / 22));
        binding.joinButton.setTextSize((float) (standardSize_X / 22));

    }

    // 뒤로가기 누를 시 앱 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}