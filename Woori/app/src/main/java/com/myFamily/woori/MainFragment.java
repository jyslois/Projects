package com.myFamily.woori;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.myFamily.woori.databinding.ActivityMainBinding;
import com.myFamily.woori.databinding.ActivityMainFragmentBinding;
import com.myFamily.woori.databinding.FragmentContactBinding;

public class MainFragment extends Fragment implements View.OnClickListener {
    ActivityMainFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityMainFragmentBinding.inflate(inflator, container, false);

        binding.anniversarybutton.setOnClickListener(this);
        binding.memorybutton.setOnClickListener(this);
        binding.contactbutton.setOnClickListener(this);
        binding.letterbutton.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        // androidx.fragment.app.FragmentManager
        FragmentManager manager = getActivity().getSupportFragmentManager();
        // androidx.fragment.app.FragmentTransaction
        FragmentTransaction ft = manager.beginTransaction();

        if (v == binding.anniversarybutton) {
            AnniversaryFragment anniversaryFragment = new AnniversaryFragment();
            ft.replace(R.id.main, anniversaryFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (v == binding.memorybutton){
            MemoryFragment memoryFragment = new MemoryFragment();
            ft.replace(R.id.main, memoryFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (v == binding.contactbutton){
            ContactFragment contactFragment = new ContactFragment();
            ft.replace(R.id.main, contactFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (v == binding.letterbutton){
            LetterFragment letterFragment = new LetterFragment();
            ft.replace(R.id.main, letterFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

}