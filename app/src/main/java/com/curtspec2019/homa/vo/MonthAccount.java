package com.curtspec2019.homa.vo;

import com.curtspec2019.homa.G;

import java.util.ArrayList;

public class MonthAccount {

    private String when;
    private int rent;
    private static ArrayList<Account> staticIncomes = new ArrayList<>();
    private int sumSI;
    private ArrayList<Account> floatIncomes;
    private int sumFI;
    private int totalIncome;
    private static ArrayList<Account> staticExpense = new ArrayList<>();
    private int sumSE;
    private ArrayList<Account> floatExpense;
    private int sumFE;
    private int totalExpense;
    private int pure;

    public MonthAccount(String when, int rent, ArrayList<Account> floatIncomes, ArrayList<Account> floatExpense) {
        this.when = when;
        this.rent = rent;
        this.floatIncomes = floatIncomes;
        this.floatExpense = floatExpense;
        if (staticIncomes.size() == 0){
            if (G.getCurrentBuilding() != null)
            staticIncomes.add(0, new Account("관리비 합계", G.getCurrentBuilding().getTotalMaintenance()));
        }
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
