package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.android.mymindnotes.databinding.ActivityMainMenuBinding;
import com.android.mymindnotes.presentation.ui.RecordMindChoice;
import com.bumptech.glide.Glide;


public class MainMenu extends AppCompatActivity {
    ActivityMainMenuBinding binding;
    SharedPreferences auto;
    SharedPreferences.Editor autoSaveEdit;

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
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auto = getSharedPreferences("autoSave", Activity.MODE_PRIVATE);
        autoSaveEdit = auto.edit();

        // gif 이미지를 이미지뷰에 띄우기
        Glide.with(this).load(R.drawable.mainmenubackground).into(binding.background);

        // 버튼 클릭 이벤트

        binding.recordDiaryButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), RecordMindChoice.class);
            startActivity(intent);
        });

        binding.diaryButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Diary.class);
            startActivity(intent);
        });

        binding.emotionInstructionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

        binding.accountsettingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AccountInformation.class);
            startActivity(intent);
        });

        binding.alarmsettingButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AlarmSetting.class);
            startActivity(intent);
        });


        // 글짜 크기 조절
        getStandardSize();
        binding.diaryButton.setTextSize((float) (standardSize_X / 23));
        binding.recordDiaryButton.setTextSize((float) (standardSize_X / 23));
        binding.emotionInstructionButton.setTextSize((float) (standardSize_X / 23));
        binding.accountsettingButton.setTextSize((float) (standardSize_X / 23));
        binding.alarmsettingButton.setTextSize((float) (standardSize_X / 23));

        binding.diarytitle.setTextSize((float) (standardSize_X / 18));
        binding.settingTitle.setTextSize((float) (standardSize_X / 18));

    }
}