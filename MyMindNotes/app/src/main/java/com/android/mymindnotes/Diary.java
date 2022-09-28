package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.mymindnotes.databinding.ActivityDiaryBinding;
import com.bumptech.glide.Glide;

public class Diary extends AppCompatActivity {
    ActivityDiaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.diarybackground).into(binding.background);

        // 일기 내용 클릭 시 상세 페이지 보기
        binding.recordClickButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Record_Result.class);
            startActivity(intent);
        });

        // 커스텀한 toolbar 적용시키기
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar); // 이 액티비티에서 툴바 사용
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 안 보이게 하기
    }

    // xml로 작성한 액션바의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_diary, menu);

        return true;
    }


}