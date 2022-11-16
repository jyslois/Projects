package com.android.mymindnotes.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    // Retrofit Object
    private Retrofit retrofit;
    // initialization

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        // Retrofit creator
        retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
