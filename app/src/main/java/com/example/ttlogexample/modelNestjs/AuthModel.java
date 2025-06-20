package com.example.ttlogexample.modelNestjs;

import com.example.ttlogexample.model.ServerError;

public class AuthModel extends ServerError {

    private String access_token;
    private String hotel;


    public String getAccess_token() {
        return access_token;
    }

    public void setHotel(String h) {
        this.hotel = h;
    }

    public String getHotel() {
        return this.hotel;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public String toString() {
        return "AuthModel{" +
                "access_token='" + access_token + '\'' +
                '}';
    }
}
