package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityRecordMindChoiceBinding;

public class RecordMindChoice extends AppCompatActivity {
    ActivityRecordMindChoiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRecordMindChoiceBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.newMindButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), New_Emotion.class);
            startActivity(intent);
        });

        binding.oldMindButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Old_Situation.class);
            startActivity(intent);
        });
    }

}