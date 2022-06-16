package com.myFamily.woori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.myFamily.woori.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SearchView searchView;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 뷰 바인딩 객체 획득
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // 액티비티 화면 출력
        setContentView(binding.getRoot());

        binding.anniversarybutton.setOnClickListener(this);
        binding.memorybutton.setOnClickListener(this);
        binding.contactbutton.setOnClickListener(this);
        binding.letterbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // androidx.fragment.app.FragmentManager
        FragmentManager manager = getSupportFragmentManager();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // androidx.fragment.app.FragmentManager
        FragmentManager manager = getSupportFragmentManager();
        // androidx.fragment.app.FragmentTransaction
        FragmentTransaction ft = manager.beginTransaction();

        if (item.getItemId() == R.id.menu_anniversary) {
            AnniversaryFragment anniversaryFragment = new AnniversaryFragment();
            ft.replace(R.id.main, anniversaryFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (item.getItemId() == R.id.menu_main) {
            // 메인 화면으로 돌아가기
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_contact) {
            ContactFragment contactFragment = new ContactFragment();
            ft.replace(R.id.main, contactFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (item.getItemId() == R.id.menu_letter) {
            LetterFragment letterFragment = new LetterFragment();
            ft.replace(R.id.main, letterFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (item.getItemId() == R.id.menu_memory) {
            MemoryFragment memoryFragment = new MemoryFragment();
            ft.replace(R.id.main, memoryFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.query_hint));
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
            Toast t = Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

}