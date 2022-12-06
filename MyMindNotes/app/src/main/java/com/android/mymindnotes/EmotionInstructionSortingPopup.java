package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.RadioGroup;

import com.android.mymindnotes.databinding.ActivityEmotionInstructionSortingPopupBinding;

public class EmotionInstructionSortingPopup extends AppCompatActivity {
    ActivityEmotionInstructionSortingPopupBinding binding;

    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;
    RadioGroup emotionGroup;

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

    // 데이터 보내기
    public void sendIntent(String emotion) {
        Intent intent = new Intent();
        intent.putExtra("emotion", emotion);
        setResult(RESULT_OK, intent);
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionInstructionSortingPopupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 글자 크기 조절
        getStandardSize();
        binding.allEmotionsButton.setTextSize((float) (standardSize_X / 23));
        binding.angerButton.setTextSize((float) (standardSize_X / 23));
        binding.anticipationButton.setTextSize((float) (standardSize_X / 23));
        binding.disgustButton.setTextSize((float) (standardSize_X / 23));
        binding.fearButton.setTextSize((float) (standardSize_X / 23));
        binding.happinessButton.setTextSize((float) (standardSize_X / 23));
        binding.sadnessButton.setTextSize((float) (standardSize_X / 23));
        binding.surpriseButton.setTextSize((float) (standardSize_X / 23));
        binding.trustButton.setTextSize((float) (standardSize_X / 23));

        emotionGroup = binding.emotionsGroup;
        emotionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.allEmotionsButton:
                    sendIntent("모든 감정");
                    break;
                case R.id.happinessButton:
                    sendIntent("기쁨");
                    break;
                case R.id.anticipationButton:
                    sendIntent("기대");
                    break;
                case R.id.trustButton:
                    sendIntent("신뢰");
                    break;
                case R.id.surpriseButton:
                    sendIntent("놀람");
                    break;
                case R.id.sadnessButton:
                    sendIntent("슬픔");
                    break;
                case R.id.disgustButton:
                    sendIntent("혐오");
                    break;
                case R.id.fearButton:
                    sendIntent("공포");
                    break;
                case R.id.angerButton:
                    sendIntent("분노");
                    break;
            }
        });


    }
}