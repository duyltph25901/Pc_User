package com.example.pc_user.model;

public class Cart {
    private String id;
    private String idProduct;
    private double priceProduct;
    private int sumProduct;

    public Cart(String id, String idProduct, double priceProduct, int sumProduct) {
        this.id = id;
        this.idProduct = idProduct;
        this.priceProduct = priceProduct;
        this.sumProduct = sumProduct;
    }

    public Cart() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getSumProduct() {
        return sumProduct;
    }

    public void setSumProduct(int sumProduct) {
        this.sumProduct = sumProduct;
    }
}
