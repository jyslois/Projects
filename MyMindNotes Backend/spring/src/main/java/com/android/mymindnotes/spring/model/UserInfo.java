package com.android.mymindnotes.spring.model;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.*;

public class UserInfo {
    @GeneratedValue
    private int user_index;
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 형식으로 입력해 주세요.")
    private String email;
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}", message = "비밀번호는 영문 대,소문자와 숫자가 적어도 1개 이상씩 포함된 6자 ~ 20자의 비밀번호여야 합니다.")
    private String password;
    @NotNull(message = "태어난 생년을 입력해 주세요.") // NotBlank는 String 타입에, NotNull은 int 타입에
    @Max(value = 2155, message = "생년은 1901~2155 사이여야 합니다.")
    @Min(value = 1901, message = "생년은 1901~2155 사이여야 합니다.")
    private int birthyear;

    public UserInfo() {
    }

    public UserInfo(int user_index, String email, String nickname, String password, int birthyear) {
        this.user_index = user_index;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.birthyear = birthyear;
    }

    public UserInfo(String email, String nickname, String password, int birthyear) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.birthyear = birthyear;
    }

    public int getUser_index() {
        return user_index;
    }

    public void setUser_index(int user_index) {
        this.user_index = user_index;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

}
