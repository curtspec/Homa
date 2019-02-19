package com.curtspec2018.homa.vo;

import java.util.ArrayList;

public class Floor implements Comparable<Floor>{

    private int floor;
    private ArrayList<Room> rooms;

    public Floor(int floor, ArrayList<Room> rooms) {
        this.floor = floor;
        this.rooms = rooms;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public int compareTo(Floor o) {
        return floor - o.floor;
    }
}
