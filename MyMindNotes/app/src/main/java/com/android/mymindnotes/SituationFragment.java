package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mymindnotes.databinding.ActivitySituationFragmentBinding;
import com.android.mymindnotes.model.UserDiary;

public class SituationFragment extends Fragment {
    ActivitySituationFragmentBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySituationFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.content.setText(getArguments().getString("situation"));
    }

    public void refreshData(UserDiary diary) {
        binding.content.setText(diary.getSituation());
    }

    public static SituationFragment newInstance(Bundle bundle) {
        SituationFragment fragment = new SituationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}