package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivityReflectionFragmentBinding;
import com.android.mymindnotes.databinding.ActivitySituationFragmentBinding;
import com.android.mymindnotes.model.UserDiary;

public class ReflectionFragment extends Fragment {
    ActivityReflectionFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityReflectionFragmentBinding.inflate(getLayoutInflater());
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