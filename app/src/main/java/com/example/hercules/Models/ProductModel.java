package com.example.hercules.Models;

import java.io.Serializable;

public class ProductModel implements Serializable {

    private String productName;
    private int image;
    private double price;
    private String productID;
    private String fat, carbo, protein, calories, servings;


    public ProductModel() {
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCarbo() {
        return carbo;
    }

    public void setCarbo(String carbo) {
        this.carbo = carbo;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public ProductModel(String productID, int image, String productName, double price, String fat, String carbo, String protein, String calories, String servings) {
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.productID = productID;
        this.fat = fat;
        this.carbo = carbo;
        this.protein = protein;
        this.calories = calories;
        this.servings = servings;
    }

    public String getProductName() {
        return productName;
    }


    public int getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public void setImage(int image) {
        this.image = image;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
