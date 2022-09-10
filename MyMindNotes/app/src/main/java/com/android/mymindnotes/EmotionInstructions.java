package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityEmotionInstructionsBinding;

public class EmotionInstructions extends AppCompatActivity {
    ActivityEmotionInstructionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}