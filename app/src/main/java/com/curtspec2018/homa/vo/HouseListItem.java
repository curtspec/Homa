package com.curtspec2018.homa.vo;

public class HouseListItem {
    private  String name;
    private String imgPath;
    private String address;
    private String detailAdd;

    public HouseListItem(String name, String imgPath, String address, String detailAdd) {
        this.name = name;
        this.imgPath = imgPath;
        this.address = address;
        this.detailAdd = detailAdd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAdd() {
        return detailAdd;
    }

    public void setDetailAdd(String detailAdd) {
        this.detailAdd = detailAdd;
    }
}
