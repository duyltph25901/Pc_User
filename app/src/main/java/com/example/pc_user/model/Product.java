package com.example.pc_user.model;


import java.io.Serializable;
public class Product implements Serializable {
    private String id; // pk
    private String name;
    private String image;
    private String path;
    private String intro;
    private double price;
    private int discount;
    private int sum;
    private int purchases;

    public Product(String id, String name, String image, String path, String intro, double price, int discount, int sum, int purchases) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.path = path;
        this.intro = intro;
        this.price = price;
        this.discount = discount;
        this.sum = sum;
        this.purchases = purchases;
    }

    public Product() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getPurchases() {
        return purchases;
    }

    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getTax() {
        return (
                getPrice() - getPrice() * discount / 100
                );
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", path='" + path + '\'' +
                ", intro='" + intro + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", sum=" + sum +
                ", purchases=" + purchases +
                '}';
    }
}
