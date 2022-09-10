package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.android.mymindnotes.databinding.ActivityMainPageBinding;

public class MainPage extends AppCompatActivity {
    ActivityMainPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 커스텀한 toolbar 적용시키기
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar); // 이 액티비티에서 툴바 사용
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 안 보이게 하기
    }

    // xml로 작성한 액션바의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // 액션바 [감정 설명서] 아이콘 사이즈 조절
        Bitmap emotionIcon = BitmapFactory.decodeResource(getResources(), R.drawable.emotioninstructionicon); //Converting drawable into bitmap
        Bitmap new_emotionIcon = resizeBitmapImageFn(emotionIcon, 70); //resizing the bitmap
        Drawable d = new BitmapDrawable(getResources(),new_emotionIcon); //Converting bitmap into drawable
        menu.getItem(0).setIcon(d); //choose the item number you want to set

        // 액션바 [나의 마음 일지] 아이콘 사이즈 조절
        Bitmap DiaryIcon = BitmapFactory.decodeResource(getResources(), R.drawable.diaryicon); //Converting drawable into bitmap
        Bitmap new_DiaryIcon = resizeBitmapImageFn(DiaryIcon, 65); //resizing the bitmap
        Drawable f = new BitmapDrawable(getResources(),new_DiaryIcon); //Converting bitmap into drawable
        menu.getItem(1).setIcon(f); //choose the item number you want to set

        // 액션바 [감정 설명서] 아이콘 하얀색 틴트
        Drawable drawable = menu.findItem(R.id.EmotionInstructions).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.white));
        menu.findItem(R.id.EmotionInstructions).setIcon(drawable);

        // 액션바 [나의 마음 일지] 아이콘 하얀색 틴트
        Drawable drawable2 = menu.findItem(R.id.Diary).getIcon();
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, ContextCompat.getColor(this,R.color.white));
        menu.findItem(R.id.Diary).setIcon(drawable2);

        return true;
    }

    // 엑션바 메뉴 아이콘 사이즈 조절을 위한 메서드
    private Bitmap resizeBitmapImageFn(
            Bitmap bmpSource, int maxResolution){
        int iWidth = bmpSource.getWidth();
        int iHeight = bmpSource.getHeight();
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f;

        if(iWidth > iHeight ){
            if(maxResolution < iWidth ){
                rate = maxResolution / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < iHeight ){
                rate = maxResolution / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(
                bmpSource, newWidth, newHeight, true);
    }


    // 엑션바 메뉴를 클릭했을 때 이벤트
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.EmotionInstructions) {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.Diary) {

        }
        return super.onOptionsItemSelected(item);
    }
}