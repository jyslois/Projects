package com.android.mymindnotes.spring.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ChangeUserPassword {
    private int user_index;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}", message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 6자 ~ 20자의 비밀번호여야 합니다.")
    private String password;
    private String originalpassword;

    public ChangeUserPassword(int user_index, String password, String originalpassword) {
        this.user_index = user_index;
        this.password = password;
        this.originalpassword = originalpassword;
    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalpassword() {
        return originalpassword;
    }

    public void setOriginalpassword(String originalpassword) {
        this.originalpassword = originalpassword;
    }
}
