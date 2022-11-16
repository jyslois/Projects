package com.android.mymindnotes.model;

public class UserInfoEdit {
    public String nickname;
    public String password;

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
