package com.example.ttlogexample.modelNestjs;

import com.example.ttlogexample.model.ServerError;

public class UserModel extends ServerError {
    public String key;
    public String email;
    public String password;
    public String name;
    public String hotel;
    public TtlockData ttlockData;
    public String tel;
    public long date;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public TtlockData getTtlockData() {
        return ttlockData;
    }

    public void setTtlockData(TtlockData ttlockData) {
        this.ttlockData = ttlockData;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @Override
    public String toString() {
        return "UserModel{" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", hotel='" + hotel + '\'' +
                ", ttlockData=" + ttlockData +
                ", tel='" + tel + '\'' +
                ", date=" + date +
                '}';
    }
}
