package com.android.mymindnotes.spring.model;

public class UserInfo {
    private String email;
    private String nickname;
    private String password;
    private int birthyear;

    public UserInfo(String email, String nickname, String password, int birthyear) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.birthyear = birthyear;
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
