package com.curtspec2018.homa.vo;

import java.io.Serializable;

public class Room implements Serializable, Comparable<Room> {

    private String name;
    private String nickname;
    private int floor;
    private boolean isOccupied;
    private boolean isUnderGround;
    private String emptyTime;
    private Tenant tenants;

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

    public Room(String name, String nickname, int floor, boolean isOccupied, boolean isUnderGround, Tenant tenants) {
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

    public Tenant getTenants() {
        return tenants;
    }

    public void setTenants(Tenant tenants) {
        this.tenants = tenants;
    }

    @Override
    public int compareTo(Room o) {
        return name.compareTo(o.name);
    }
}
