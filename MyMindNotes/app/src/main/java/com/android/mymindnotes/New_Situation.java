package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityNewSituationBinding;

public class New_Situation extends AppCompatActivity {
    ActivityNewSituationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewSituationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}