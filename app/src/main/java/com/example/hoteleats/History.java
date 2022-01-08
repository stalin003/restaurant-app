package com.example.hoteleats;

public class History {
    private String name;
    private String time;
    private String productList;
    private String totalPrice;

    public History(String name, String time, String productList, String totalPrice) {
        this.name = name;
        this.time = time;
        this.productList = productList;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductList() {
        return productList;
    }

    public void setProductList(String productList) {
        this.productList = productList;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
