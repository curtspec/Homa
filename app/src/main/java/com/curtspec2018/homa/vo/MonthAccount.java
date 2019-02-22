package com.curtspec2018.homa.vo;

import java.util.ArrayList;

public class MonthAccount {

    private String when;
    private int rent;
    private ArrayList<Account> staticIncomes;
    private int sumSI;
    private ArrayList<Account> floatIncomes;
    private int sumFI;
    private int totalIncome;
    private ArrayList<Account> staticExpense;
    private int sumSE;
    private ArrayList<Account> floatExpense;
    private int sumFE;
    private int totalExpense;
    private int pure;

    public MonthAccount(String when, int rent, ArrayList<Account> staticIncomes, ArrayList<Account> floatIncomes, ArrayList<Account> staticExpense, ArrayList<Account> floatExpense) {
        this.when = when;
        this.rent = rent;
        this.staticIncomes = staticIncomes;
        this.floatIncomes = floatIncomes;
        this.staticExpense = staticExpense;
        this.floatExpense = floatExpense;
        initValues();
    }

    public void initValues(){
        for (Account t : staticIncomes) sumSI += t.getAmount();
        for (Account t : floatIncomes) sumFI += t.getAmount();
        for (Account t : staticExpense) sumSE += t.getAmount();
        for (Account t : floatExpense) sumFE += t.getAmount();
        totalIncome = sumFI + sumSI;
        totalExpense = sumFE + sumSE;
        pure = totalIncome - totalExpense;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
        initValues();
    }

    public ArrayList<Account> getStaticIncomes() {
        return staticIncomes;
    }

    public void setStaticIncomes(ArrayList<Account> staticIncomes) {
        this.staticIncomes = staticIncomes;
        initValues();
    }

    public int getSumSI() {
        return sumSI;
    }

    public ArrayList<Account> getFloatIncomes() {
        return floatIncomes;
    }

    public void setFloatIncomes(ArrayList<Account> floatIncomes) {
        this.floatIncomes = floatIncomes;
        initValues();
    }

    public int getSumFI() {
        return sumFI;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public ArrayList<Account> getStaticExpense() {
        return staticExpense;
    }

    public void setStaticExpense(ArrayList<Account> staticExpense) {
        this.staticExpense = staticExpense;
        initValues();
    }

    public int getSumSE() {
        return sumSE;
    }

    public ArrayList<Account> getFloatExpense() {
        return floatExpense;
    }

    public void setFloatExpense(ArrayList<Account> floatExpense) {
        this.floatExpense = floatExpense;
        initValues();
    }

    public int getSumFE() {
        return sumFE;
    }

    public int getTotalExpense() {
        return totalExpense;
    }

    public int getPure() {
        return pure;
    }

}
