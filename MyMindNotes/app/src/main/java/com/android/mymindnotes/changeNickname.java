package com.android.mymindnotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityChangeNicknameBinding;
import com.bumptech.glide.Glide;

public class changeNickname extends AppCompatActivity {
    ActivityChangeNicknameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeNicknameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainpagebackground2).into(binding.background);

        // 액션 바 타이틀
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 기본 타이틀 사용 안함
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); // 커스텀 사용
        getSupportActionBar().setCustomView(R.layout.changenickname_actionbartext); // 커스텀 사용할 파일 위치
        // Up 버튼 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Up 버튼 눌렀을 때 액션
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}