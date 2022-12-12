package com.android.mymindnotes.model.retrofit;

import com.android.mymindnotes.model.ChangeToTemporaryPassword;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface ChangeToTempPassword {
    @PUT("/api/member/update/password/temp")
    Call<Map<String, Object>> toTemPassword(@Body ChangeToTemporaryPassword changeToTemporaryPassword);
}
