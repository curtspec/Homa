package com.curtspec2018.homa.vo;


import com.curtspec2018.homa.SMS.SMSActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Building implements Serializable {

    private String profileUrl;
    private String name;
    private String address;
    private int numOfFloor;
    private boolean isElevator;
    private boolean isParking;
    private boolean isUnderGround;

    private ArrayList<Floor> floors = new ArrayList<>();
    private ArrayList<MonthAccount> accounts = new ArrayList<>();
    private MonthAccount currnetMonth;

    public Building(String profileUrl, String name, String address, int numOfFloor, boolean isElevator, boolean isParking, boolean isUnderGround) {
        this.profileUrl = profileUrl;
        this.name = name;
        this.address = address;
        this.numOfFloor = numOfFloor;
        this.isElevator = isElevator;
        this.isParking = isParking;
        this.isUnderGround = isUnderGround;
    }

    public Building(String name, String address, int numOfFloor, boolean isElevator, boolean isParking, boolean isUnderGround) {
        this.name = name;
        this.address = address;
        this.numOfFloor = numOfFloor;
        this.isElevator = isElevator;
        this.isParking = isParking;
        this.isUnderGround = isUnderGround;
        profileUrl = "default";
    }

    public ArrayList<Schedule> getSchedulesFromTenant(){
        ArrayList<Schedule> schedules = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        Calendar payday = today;
        int month = 0;
        int year = 0;
        for (Floor f : floors){
            for (Room r : f.getRooms()){
                month = today.get(Calendar.MONTH);
                year = today.get(Calendar.YEAR);
                if (r.isOccupied()){
                    Tenant t = r.getTenants();
                    if (t != null) {
                        schedules.add(Schedule.getInstanceFromTenant(t.getContractDay(), r.getName(), Schedule.TYPE_CONTRACT));
                        schedules.add(Schedule.getInstanceFromTenant(t.getContractOver(), r.getName(), Schedule.TYPE_CONTRACT_OVER));
                        for (int i = 0; i < 24; i++) {
                            payday.set(year, month, t.getPeriod());
                            schedules.add(Schedule.getInstanceFromTenant(payday, r.getName(), Schedule.TYPE_PAYDAY));
                            month++;
                            if (month > 11){
                                year ++;
                                month = 0;
                            }
                        }//월세를 Schedule 로 추가.
                    }//세입자가 없는 경우 하지않도록
                }//if (r.isOccupied)
            } // 각 방
        }
        return schedules;
    }

    public ArrayList<SMSActivity.Target> getAddableTarget(){
        ArrayList<SMSActivity.Target> addable = new ArrayList<>();
        String name = null;
        String number = null;
        for (Floor f : floors){
            for (Room r : f.getRooms()){
                if (r.isOccupied() && r.getTenants() != null){
                    Tenant t = r.getTenants();
                    name = t.getTenantName();
                    number = t.getPhoneNumber();
                    if (name != null && number != null){
                        addable.add(new SMSActivity.Target(r.getName(), name, number));
                    }
                }
            }
        }
        return addable;
    }

    public ArrayList<Room> getEmptyRoom(){
        ArrayList<Room> emptyRoom = new ArrayList<>();
        for (Floor f: floors){
            for (Room r : f.getRooms()){
                if (!r.isOccupied() && r.getTenants() == null){
                    emptyRoom.add(r);
                }
            }
        }
        return emptyRoom;
    }

    public int getTotalRent(){
        int total = 0;
        for (Floor f : floors){
            for (Room r : f.getRooms()){
                if (r.isOccupied() && r.getTenants() != null){
                    total += r.getTenants().getRent() * 10000;
                }
            }
        }
        return total;
    }

    public int getTotalMaintenance(){
        int maintenance = 0;
        for (Floor f : floors){
            for (Room r : f.getRooms()){
                if (r.isOccupied() && r.getTenants() != null){
                    maintenance += r.getTenants().getMaintenanceFee() * 10000;
                }
            }
        }
        return maintenance;
    }

    public void removeRoom(Room room){
        int floor = room.getFloor();
        for (Floor f : floors){
            if (f.getFloor() == floor) f.getRooms().remove(room);
        }
    }

    public void setRoom(Room room){
        int floor = room.getFloor();
        int index = -1;
        for (Floor f : floors){
            if (f.getFloor() == floor){
                ArrayList<Room> editTarget = f.getRooms();
                for (int i = 0 ; i < editTarget.size(); i++){
                    if(editTarget.get(i).equals(room)){
                        index = i;
                        break;
                    }
                }
                if (index > 0){
                    editTarget.remove(index);
                    editTarget.add(index, room);
                }
                break;
            }
        }
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getNumOfFloor() {
        return numOfFloor;
    }

    public boolean isElevator() {
        return isElevator;
    }

    public boolean isParking() {
        return isParking;
    }

    public boolean isUnderGround() {
        return isUnderGround;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNumOfFloor(int numOfFloor) {
        this.numOfFloor = numOfFloor;
    }

    public void setElevator(boolean elevator) {
        isElevator = elevator;
    }

    public void setParking(boolean parking) {
        isParking = parking;
    }

    public void setUnderGround(boolean underGround) {
        isUnderGround = underGround;
    }

    public void setFloors(ArrayList<Floor> floors) {
        this.floors = floors;
    }

    public ArrayList<MonthAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<MonthAccount> accounts) {
        this.accounts = accounts;
    }

    public MonthAccount getCurrnetMonth() {
        return currnetMonth;
    }

    public void setCurrnetMonth(MonthAccount currnetMonth) {
        this.currnetMonth = currnetMonth;
    }
}
