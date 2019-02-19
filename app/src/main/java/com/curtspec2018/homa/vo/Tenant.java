package com.curtspec2018.homa.vo;

import java.io.Serializable;
import java.util.Calendar;

public class Tenant implements Serializable {

    private int rent;
    private int maintenanceFee;
    private int deposit;
    private int payday;
    private int arrear;
    private Calendar contractDay;
    private Calendar contractOver;
    private int period;
    private String tenantName;
    private String phoneNumber;

    public Tenant(int rent, int maintenanceFee, int deposit, int payday, int arrear, Calendar contractDay, int period, String tenantName, String phoneNumber) {
        this.rent = rent;
        this.maintenanceFee = maintenanceFee;
        this.deposit = deposit;
        this.payday = payday;
        this.arrear = arrear;
        this.contractDay = contractDay;
        this.period = period;
        this.tenantName = tenantName;
        this.phoneNumber = phoneNumber;
        contractOver = contractDay;
        contractOver.set(contractOver.get(Calendar.YEAR) + period, contractOver.get(Calendar.MONTH), contractOver.get(Calendar.DAY_OF_MONTH));
    }

    public Tenant(int rent, int maintenanceFee, int deposit, int payday, int arrear, Calendar contractDay, int period) {
        this.rent = rent;
        this.maintenanceFee = maintenanceFee;
        this.deposit = deposit;
        this.payday = payday;
        this.arrear = arrear;
        this.contractDay = contractDay;
        this.period = period;
        contractOver = contractDay;
        contractOver.set(contractOver.get(Calendar.YEAR) + period, contractOver.get(Calendar.MONTH), contractOver.get(Calendar.DAY_OF_MONTH));
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getMaintenanceFee() {
        return maintenanceFee;
    }

    public void setMaintenanceFee(int maintenanceFee) {
        this.maintenanceFee = maintenanceFee;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getPayday() {
        return payday;
    }

    public void setPayday(int payday) {
        this.payday = payday;
    }

    public int getArrear() {
        return arrear;
    }

    public void setArrear(int arrear) {
        this.arrear = arrear;
    }

    public Calendar getContractDay() {
        return contractDay;
    }

    public void setContractDay(Calendar contractDay) {
        this.contractDay = contractDay;
    }

    public Calendar getContractOver() {
        return contractOver;
    }

    public void setContractOver(Calendar contractOver) {
        this.contractOver = contractOver;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
