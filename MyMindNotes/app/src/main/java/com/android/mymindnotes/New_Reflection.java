package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityNewReflectionBinding;

public class New_Reflection extends AppCompatActivity {
    ActivityNewReflectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewReflectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}