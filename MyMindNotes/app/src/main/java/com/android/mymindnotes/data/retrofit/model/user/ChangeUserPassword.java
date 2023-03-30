package com.android.mymindnotes.data.retrofit.model.user;

public class ChangeUserPassword {
    public int user_index;
    public String password;
    public String originalpassword;

    public ChangeUserPassword() {

    }

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
