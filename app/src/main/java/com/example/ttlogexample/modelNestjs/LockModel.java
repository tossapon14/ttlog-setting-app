package com.example.ttlogexample.modelNestjs;

import java.util.ArrayList;

public class LockModel {
    private int lockId;
    private String keyId;
    private String addBy;
    private String lockData;

    private String mac;
    private String hotel;
    private String roomNumber;
    private long date;

    public String getLockMac(){return mac;}
    private ArrayList<String> cardIdlist;

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public String getLockData() {
        return lockData;
    }

    public void setLockData(String lockData) {
        this.lockData = lockData;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAddBy() {
        return addBy;
    }

    public void setAddBy(String addBy) {
        this.addBy = addBy;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ArrayList<String> getCardIdlist() {
        return cardIdlist;
    }

    public void setCardIdlist(ArrayList<String> cardIdlist) {
        this.cardIdlist = cardIdlist;
    }

    @Override
    public String toString() {
        return "LockModel{" +
                "lockId=" + lockId +
                ", keyId='" + keyId + '\'' +
                ", addBy='" + addBy + '\'' +
                ", hotelName='" + hotel + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", date=" + date +
                ", cardIdlist=" + cardIdlist +
                '}';
    }
}
