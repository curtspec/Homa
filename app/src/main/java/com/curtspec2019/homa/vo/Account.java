package com.curtspec2019.homa.vo;

public class Account implements Comparable<Account>{

    private String title;
    private int amount;

    public Account(String title, int amount) {
        this.title = title;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(Account o) {
        return title.compareTo(o.title);
    }
}
