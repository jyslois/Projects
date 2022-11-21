package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.android.mymindnotes.databinding.ActivityMainPageBinding;
import com.bumptech.glide.Glide;

public class MainPage extends AppCompatActivity {
    ActivityMainPageBinding binding;
    SharedPreferences nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        binding.addRecordButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RecordMindChoice.class);
            startActivity(intent);
        });

        // 닉네임 설정
        nickName = getSharedPreferences("nickName", Activity.MODE_PRIVATE);
        String nick = nickName.getString("nickName", "");
        binding.mainpagetext.setText("오늘 하루도 고생했어요, " + nick + " 님.");

        // 메뉴 이미지
        ImageView mainmenu = binding.mainmenu;
        mainmenu.setColorFilter(Color.parseColor("#C3BE9F98"));

        mainmenu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);
        });


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

}