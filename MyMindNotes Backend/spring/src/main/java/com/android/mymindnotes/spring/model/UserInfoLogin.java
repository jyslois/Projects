package com.android.mymindnotes.spring.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserInfoLogin {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 형식으로 입력해 주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}", message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 6자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    public UserInfoLogin() {

    }

    public UserInfoLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
