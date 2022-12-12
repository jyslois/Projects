package com.android.mymindnotes.model.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    // okHttp logging interpreter: httpLoggingInterceptor() 메소드를 addInterceptor()의 파라미터에서 호출
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor()).build();

    // Retrofit Object
    private Retrofit retrofit;
    // initialization

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        // Retrofit creator - .client(OkHttpClient Object)로 okHttp logging interpreter 등록
        // 안드로이드에서는 localhost 말고 다른 주소를 넣어주어야 한다.
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080/").client(client).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


    // okHttp logging interpreter - httpLoggingInterceptor() 메서드 작성. 로그 메시지 tag로 뭐 할지 설정.
    private HttpLoggingInterceptor httpLoggingInterceptor(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                android.util.Log.e("LogNetwork :", message + "");
            }
        });

        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }
}



