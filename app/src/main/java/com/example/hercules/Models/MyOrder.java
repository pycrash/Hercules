package com.example.hercules.Models;

import com.example.hercules.Database.Order;

import java.io.Serializable;
import java.util.List;

public class MyOrder implements Serializable {
    private String phone;
    private String email;
    private String name;
    private String total;
    private String address;
    private List<Order> cart;
    private String newTotal, date;
    private String orderId;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public MyOrder() {
    }

    public String getId() {
        return orderId;
    }

    public void setId(String id) {
        this.orderId = id;
    }

    public MyOrder(String orderId, String name, String phone, String email, String address, List<Order> orders,String status, String total, int discount, String newTotal, String date) {
        this.status = status;
        this.orderId = orderId;
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.total = total;
        this.address = address;
        this.cart = orders;
        this.date = date;
        this.discount = discount;
        this.newTotal = newTotal;
    }
    public String getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(String newTotal) {
        this.newTotal = newTotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    private int discount;



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getCart() {
        return cart;
    }

    public void setCart(List<Order> cart) {
        this.cart = cart;
    }
}


