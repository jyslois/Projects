package com.android.mymindnotes.data.retrofit.model;

public class ChangeToTemporaryPassword {
    public String email;
    public String password;

    public ChangeToTemporaryPassword(String email, String password) {
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
