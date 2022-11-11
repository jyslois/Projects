package com.android.mymindnotes.spring.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserInfoEdit {
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}", message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 6자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    public UserInfoEdit(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public UserInfoEdit() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
