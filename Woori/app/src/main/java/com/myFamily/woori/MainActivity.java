package com.myFamily.woori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myFamily.woori.databinding.ActivityMainBinding;
import com.myFamily.woori.databinding.ActivityMainFragmentBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 뷰 바인딩 객체 획득
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // 액티비티 화면 출력
        setContentView(binding.getRoot());

        // viewpager2
        binding.viewpager.setAdapter(new WooriAdapter(this));


        // tablayout과 viewpaer2 연결하게(tablayoutmediator)
        final List<String> tabList = Arrays.asList("Main", "기념", "추억", "연락", "편지");

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText(tabList.get(position));
                // 탭 안의 텍스트 중앙 정렬
                textView.setGravity(Gravity.CENTER);
                tab.setCustomView(textView);
            }
        }).attach();

    }


    // viewpager2 어뎁터
    class WooriAdapter extends FragmentStateAdapter {
        List<Fragment> fragments;
        WooriAdapter(FragmentActivity activity) {
            super(activity);
            fragments = new ArrayList<>();
            fragments.add(new MainFragment());
            fragments.add(new AnniversaryFragment());
            fragments.add(new MemoryFragment());
            fragments.add(new ContactFragment());
            fragments.add(new LetterFragment());
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

}