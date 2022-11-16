package com.android.mymindnotes.model;

public class UserInfo {

    public int user_index;
    public String email;
    public String nickname;
    public String password;
    public int birthyear;

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
