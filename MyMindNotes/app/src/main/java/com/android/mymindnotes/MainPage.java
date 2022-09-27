package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
        Glide.with(this).load(R.drawable.mainbackground).into(binding.background);


        // 커스텀한 toolbar 적용시키기
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar); // 이 액티비티에서 툴바 사용
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 안 보이게 하기

        binding.addRecordButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RecordMindChoice.class);
            startActivity(intent);
        });

        // 닉네임 설정
        nickName = getSharedPreferences("nickName", Activity.MODE_PRIVATE);
        String nick = nickName.getString("nickName", "");
        binding.mainpagetext.setText("오늘 하루도 고생했어요, " + nick + " 님.");


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


    // xml로 작성한 액션바의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }


    // 엑션바 메뉴를 클릭했을 때 이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.emotionicon) {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.diaryicon) {
            Intent intent = new Intent(getApplicationContext(), Diary.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}