package com.sanilk.securedroid.Entities;

/**
 * Created by sanil on 13/4/18.
 */

public class User {
    public String email;
    public String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
