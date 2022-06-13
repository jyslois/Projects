package com.myFamily.woori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_anniversary) {
            Toast t = Toast.makeText(this, "anniversary clicked", Toast.LENGTH_SHORT);
            t.show();
        } else if (item.getItemId() == R.id.menu_main) {
            Toast t = Toast.makeText(this, "main clicked", Toast.LENGTH_SHORT);
            t.show();
        } else if (item.getItemId() == R.id.menu_contact) {
            Toast t = Toast.makeText(this, "contact clicked", Toast.LENGTH_SHORT);
            t.show();
        } else if (item.getItemId() == R.id.menu_letter) {
            Toast t = Toast.makeText(this, "letter clicked", Toast.LENGTH_SHORT);
            t.show();
        } else if (item.getItemId() == R.id.menu_memory) {
            Toast t = Toast.makeText(this, "memory clicked", Toast.LENGTH_SHORT);
            t.show();
        }
        return super.onOptionsItemSelected(item);
    }

}