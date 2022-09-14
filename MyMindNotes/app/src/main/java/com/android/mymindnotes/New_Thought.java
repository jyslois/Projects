package com.android.mymindnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.mymindnotes.databinding.ActivityNewThoughtBinding;

public class New_Thought extends AppCompatActivity {
    ActivityNewThoughtBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewThoughtBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}