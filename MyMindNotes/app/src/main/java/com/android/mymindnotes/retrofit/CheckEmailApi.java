package com.android.mymindnotes.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CheckEmailApi {
    // 추상 메서드 정의
    // HTTP method 중 GET 사용
    // Get 방식으로 연동이 이루어져야하며 baseUrl 뒤에 Path로 "/api/member/checkEmail/{email}" 지정
    @GET("/api/member/checkEmail/{email}")
    // 서버측에서 Map<String, Object>를 return 시켜주므로 여기도 call<> 안에 타입을 Map<String, Object>로 넣기
    // Paramater로 email 보내기
    Call<Map<String, Object>> checkEmail(@Path("email") String email);
}

