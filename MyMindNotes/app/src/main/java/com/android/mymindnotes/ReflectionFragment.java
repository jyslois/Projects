package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivityReflectionFragmentBinding;
import com.android.mymindnotes.data.retrofit.model.diary.UserDiary;

public class ReflectionFragment extends Fragment {
    ActivityReflectionFragmentBinding binding;
    // 화면 크기에 따른 글자 크기 조절
    int standardSize_X, standardSize_Y;
    float density;

    // 화면 크기에 따라서 글자 사이즈 조절하기
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }
    public void getStandardSize() {
        Point ScreenSize = getScreenSize(getActivity());
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityReflectionFragmentBinding.inflate(getLayoutInflater());
        getStandardSize();
        binding.content.setTextSize((float) (standardSize_X / 22));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.content.setText(getArguments().getString("reflection"));
    }

    public void refreshData(UserDiary diary) {
        binding.content.setText(diary.getReflection());
    }

    public static ReflectionFragment newInstance(Bundle bundle) {
        ReflectionFragment fragment = new ReflectionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}