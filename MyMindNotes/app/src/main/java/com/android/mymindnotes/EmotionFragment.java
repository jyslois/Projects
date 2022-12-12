package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivityEmotionFragmentBinding;
import com.android.mymindnotes.model.UserDiary;

public class EmotionFragment extends Fragment {
    ActivityEmotionFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityEmotionFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.contentEmotion.setText("내가 느낀 감정은 \"" + getArguments().getString("emotion") + "\"");
        binding.contentEmotionDescription.setText(getArguments().getString("emotionText"));
    }

    public void refreshData(UserDiary diary) {
        binding.contentEmotion.setText(diary.getEmotion());
        binding.contentEmotionDescription.setText(diary.getEmotionDescription());
    }

    public static EmotionFragment newInstance(Bundle bundle) {
        EmotionFragment fragment = new EmotionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}