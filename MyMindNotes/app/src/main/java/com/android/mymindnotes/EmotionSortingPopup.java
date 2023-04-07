package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.RadioGroup;

import com.android.mymindnotes.databinding.ActivityEmotionSortingPopupBinding;

public class EmotionSortingPopup extends AppCompatActivity {
    ActivityEmotionSortingPopupBinding binding;
    RadioGroup emotionGroup;


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
        binding = ActivityEmotionSortingPopupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


