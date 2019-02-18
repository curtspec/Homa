package com.curtspec2018.homa.vo;

import java.util.ArrayList;

public class Room {

    private String name;
    private String nickname;
    private int floor;
    private boolean isOccupied;
    private boolean isUnderGround;
    private ArrayList<Tenant> tenants;
    private String emptyTime;

    public String getEmptyTime() {
        return emptyTime;
    }

    public void setEmptyTime(String emptyTime) {
        this.emptyTime = emptyTime;
    }

    public Room(String name, String nickname, int floor, boolean isOccupied, boolean isUnderGround) {
        this.name = name;
        this.nickname = nickname;
        this.floor = floor;
        this.isOccupied = isOccupied;
        this.isUnderGround = isUnderGround;
    }

    public Room(String name, String nickname, int floor, boolean isOccupied, boolean isUnderGround, ArrayList<Tenant> tenants) {
        this.name = name;
        this.nickname = nickname;
        this.floor = floor;
        this.isOccupied = isOccupied;
        this.isUnderGround = isUnderGround;
        this.tenants = tenants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isUnderGround() {
        return isUnderGround;
    }

    public void setUnderGround(boolean underGround) {
        isUnderGround = underGround;
    }

    public ArrayList<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(ArrayList<Tenant> tenants) {
        this.tenants = tenants;
    }
}
