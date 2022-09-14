package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityNewEmotionBinding;

public class New_Emotion extends AppCompatActivity {
    ActivityNewEmotionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewEmotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.RecordEmotionHelpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        });

    }
}