package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivitySituationFragmentBinding;
import com.android.mymindnotes.databinding.ActivityThoughtFragmentBinding;
import com.android.mymindnotes.model.UserDiary;

public class ThoughtFragment extends Fragment {
    ActivityThoughtFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityThoughtFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.content.setText(getArguments().getString("thought"));
    }

    public void refreshData(UserDiary diary) {
        binding.content.setText(diary.getThought());
    }

    public static ThoughtFragment newInstance(Bundle bundle) {
        ThoughtFragment fragment = new ThoughtFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}