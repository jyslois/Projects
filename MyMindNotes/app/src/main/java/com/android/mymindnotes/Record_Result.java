package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityRecordResultBinding;

public class Record_Result extends AppCompatActivity {
    ActivityRecordResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}