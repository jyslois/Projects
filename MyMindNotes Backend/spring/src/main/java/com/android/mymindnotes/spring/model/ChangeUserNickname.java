package com.android.mymindnotes.spring.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ChangeUserNickname {
    private int user_index;
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자여야 합니다.")
    private String nickname;

    public ChangeUserNickname(int user_index, String nickname) {
        this.user_index = user_index;
        this.nickname = nickname;
    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
