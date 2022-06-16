package com.myFamily.woori;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myFamily.woori.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        FragmentContactBinding binding = FragmentContactBinding.inflate(inflator, container, false);
        return binding.getRoot();
    }

}