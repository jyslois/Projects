package com.android.mymindnotes.retrofit;

import com.android.mymindnotes.model.ChangeUserNickname;
import com.android.mymindnotes.model.ChangeUserPassword;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ChangeNicknameApi {
    @PUT("/api/member/update/nickname")
    Call<Map<String, Object>> updateUserNickname(@Body ChangeUserNickname changeUserNickname);
}
