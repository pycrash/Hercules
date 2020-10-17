package com.example.hercules.Models;

import java.io.Serializable;

public class ProductModel implements Serializable {

    private String productName;
    private int image;
    private double price;
    private String productID;
    private double fat, carbo, protein, calories, servings;



    public ProductModel() {}

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbo() {
        return carbo;
    }

    public void setCarbo(double carbo) {
        this.carbo = carbo;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getServings() {
        return servings;
    }

    public void setServings(double servings) {
        this.servings = servings;
    }

    public ProductModel(String productID, int image, String productName, double price, double fat, double carbo, double protein, double calories, double servings) {
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
