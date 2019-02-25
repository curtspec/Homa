package com.curtspec2018.homa.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Schedule {

    private Calendar date;
    private int type;
    private String where;
    private String title;
    private String subTitle;

    public static final int TYPE_PAYDAY = 0X0;
    public static final int TYPE_SCHEDULE = 0X3;
    public static final int TYPE_CONTRACT = 0X1;
    public static final int TYPE_CONTRACT_OVER = 0X2;

    private Schedule(Calendar date, String where, int type) {
        this.date = date;
        this.where = where;
        this.type = type;
        switch (type){
            case TYPE_PAYDAY :
                title = "월세 입금일";
                subTitle = "매월 " + date.get(Calendar.DAY_OF_MONTH) +"일 월세 납입일";
                break;
            case TYPE_CONTRACT:
                title = "계약일";
                subTitle = new SimpleDateFormat("yyyy.MM.dd").format(new Date(date.getTimeInMillis())) + "계약 시작일";
                break;
            case TYPE_CONTRACT_OVER:
                title = "계약 종료일";
                subTitle = new SimpleDateFormat("yyyy.MM.dd").format(new Date(date.getTimeInMillis())) + "계약 종료일";
                break;
        }
    }

    private Schedule(Calendar date, String title, String subTitle) {
        this.date = date;
        this.title = title;
        this.subTitle = subTitle;
        type = TYPE_SCHEDULE;
        where = "개인 일정";
    }

    public static Schedule getInstanceFromTenant(Calendar date, String where, int type){
        return new Schedule(date, where, type);
    }

    public static Schedule getInstanceFromMemo(Calendar date, String title, String subTitle){
        return new Schedule(date, title, subTitle);
    }

    public Calendar getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public String getWhere() {
        return where;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
