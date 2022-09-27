package com.android.mymindnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.mymindnotes.databinding.ActivityDiaryBinding;

public class Diary extends AppCompatActivity {
    ActivityDiaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 커스텀한 toolbar 적용시키기
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar); // 이 액티비티에서 툴바 사용
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 안 보이게 하기

        // 일기 내용 클릭 시 상세 페이지 보기
        binding.recordClickButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Record_Result.class);
            startActivity(intent);
        });

    }

    // xml로 작성한 액션바의 메뉴 설정
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // 액션바 [감정 설명서] 아이콘 사이즈 조절
        Bitmap emotionIcon = BitmapFactory.decodeResource(getResources(), R.drawable.emotionicon); //Converting drawable into bitmap
        Bitmap new_emotionIcon = resizeBitmapImageFn(emotionIcon, 70); //resizing the bitmap
        Drawable d = new BitmapDrawable(getResources(),new_emotionIcon); //Converting bitmap into drawable
        menu.getItem(0).setIcon(d); //choose the item number you want to set

        // 액션바 [나의 마음 일지] 아이콘 사이즈 조절
        Bitmap DiaryIcon = BitmapFactory.decodeResource(getResources(), R.drawable.diary_icon); //Converting drawable into bitmap
        Bitmap new_DiaryIcon = resizeBitmapImageFn(DiaryIcon, 65); //resizing the bitmap
        Drawable f = new BitmapDrawable(getResources(),new_DiaryIcon); //Converting bitmap into drawable
        menu.getItem(1).setIcon(f); //choose the item number you want to set

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
        if (item.getItemId() == R.id.emotionicon) {
            Intent intent = new Intent(getApplicationContext(), EmotionInstructions.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}