package com.myFamily.woori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
    ActionBarDrawerToggle toggle;

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

        // ActionBar DrawerToggle - DrawerLayout을 열고 닫기 위한 아이콘을 툴바 왼쪽에 제공
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();

        // 이벤트 처리
        binding.navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            if (id == R.id.menu_anniversary) {
                AnniversaryFragment anniversaryFragment = new AnniversaryFragment();
                ft.replace(R.id.main, anniversaryFragment);
                ft.addToBackStack(null);
                ft.commit();
                binding.drawerLayout.closeDrawers();
            } else if (id == R.id.menu_memory) {
                MemoryFragment memoryFragment = new MemoryFragment();
                ft.replace(R.id.main, memoryFragment);
                ft.addToBackStack(null);
                ft.commit();
                binding.drawerLayout.closeDrawers();
            } else if (id == R.id.menu_contact) {
                ContactFragment contactFragment = new ContactFragment();
                ft.replace(R.id.main, contactFragment);
                ft.addToBackStack(null);
                ft.commit();
                binding.drawerLayout.closeDrawers();
            } else if (id == R.id.menu_letter){
                LetterFragment letterFragment = new LetterFragment();
                ft.replace(R.id.main, letterFragment);
                ft.addToBackStack(null);
                ft.commit();
                binding.drawerLayout.closeDrawers();
            } else if (id == R.id.menu_main) {
                // 메인 화면으로 돌아가기
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            return false;
        });
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
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

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

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