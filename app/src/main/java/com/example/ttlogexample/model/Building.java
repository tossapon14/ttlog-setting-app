package com.example.ttlogexample.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Building {
    private String buildingName;
    private ArrayList<String> floor;
    private ArrayList<ArrayList<String>> room;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public ArrayList<String> getFloor() {
        return floor;
    }

    public void setFloor(ArrayList<String> floor) {
        this.floor = floor;
    }

    public ArrayList<ArrayList<String>> getRoom() {
        return room;
    }

    public void setRoom(ArrayList<ArrayList<String>> room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Building{" +
                "buildingName='" + buildingName + '\'' +
                ", floor=" + floor +
                ", room=" + room +
                '}';
    }
}
