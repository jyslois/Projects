package com.android.mymindnotes.data.retrofit.model;

public class ChangeUserNickname {
    public int user_index;
    public String nickname;

    public ChangeUserNickname() {

    }

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
