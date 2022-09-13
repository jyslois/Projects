package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

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

        });

        binding.oldMindButton.setOnClickListener(view -> {

        });
    }

}