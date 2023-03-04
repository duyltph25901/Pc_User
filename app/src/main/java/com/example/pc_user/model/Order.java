package com.example.pc_user.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String id; // pk
    private List<String> idCart; // fk
    private double sumPrice;
    private InfoUser infoUser;
    private String createdAd;
    private boolean status;

    public Order() {}

    public Order(String id, List<String> idCart, double sumPrice, InfoUser infoUser, String createdAd, boolean status) {
        this.id = id;
        this.idCart = idCart;
        this.sumPrice = sumPrice;
        this.infoUser = infoUser;
        this.createdAd = createdAd;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIdCart() {
        return idCart;
    }

    public void setIdCart(List<String> idCart) {
        this.idCart = idCart;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public InfoUser getInfoUser() {
        return infoUser;
    }

    public void setInfoUser(InfoUser infoUser) {
        this.infoUser = infoUser;
    }

    public String getCreatedAd() {
        return createdAd;
    }

    public void setCreatedAd(String createdAd) {
        this.createdAd = createdAd;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
