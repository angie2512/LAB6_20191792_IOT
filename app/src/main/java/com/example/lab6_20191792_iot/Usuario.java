package com.example.lab6_20191792_iot;


import java.io.Serializable;

public class Usuario implements Serializable {
    private String password;
    private String email;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
