package com.curtspec2019.homa.vo;


import com.curtspec2019.homa.SMS.SMSActivity;

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
    private String tag;

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
        int month;
        int year;
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
                            Calendar payday = Calendar.getInstance();
                            payday.set(year-1, month, t.getPayday());
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

    public ArrayList<Room> getArrears(){
        ArrayList<Room> arrears = new ArrayList<>();
        for (Floor f : floors){
            for (Room r : f.getRooms()){
                Tenant t = r.getTenants();
                if (t != null && t.getArrear() > 0){
                    arrears.add(r);
                }
            }
        }
        return arrears;
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
        Floor f;
        for (int i = 0; i < floors.size(); i++){
            f = floors.get(i);
            if (f.getFloor() == floor){
                f.getRooms().remove(room);
                if (f.getRooms().size() == 0){
                    floors.remove(i);
                }
                return;
            }
        }
    }

    public void addRoom(Room room){
        int floor = room.getFloor();
        int index = -1;
        for (int i = 0; i < floors.size(); i++){
            if (floor == floors.get(i).getFloor()) {
                index = i;
                break;
            }
        }
        if (index < 0){
            ArrayList rooms = new ArrayList();
            rooms.add(room);
            floors.add(new Floor(floor, rooms));
        }else {
            floors.get(index).getRooms().add(room);
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
                if (index >= 0){
                    editTarget.remove(index);
                    editTarget.add(index, room);
                }
                break;
            }
        }
    }

    public boolean isExist(String name){
        for (Floor f : floors){
            for (Room r : f.getRooms()){
                if (r.getName().equals(name)) return true;
            }
        }
        return false;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setValues(String profileUrl, String name, String address, int numOfFloor, boolean isElevator, boolean isParking, boolean isUnderGround){
        this.profileUrl = profileUrl;
        this.name = name;
        this.address = address;
        this.numOfFloor = numOfFloor;
        this.isElevator = isElevator;
        this.isParking = isParking;
        this.isUnderGround = isUnderGround;
    }
}
