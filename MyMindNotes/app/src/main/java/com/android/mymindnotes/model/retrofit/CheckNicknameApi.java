package com.android.mymindnotes.model.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CheckNicknameApi {
    @GET("/api/member/checkNickname/{nickname}")
    Call<Map<String, Object>> checkNickname(@Path("nickname") String nickname);
}
