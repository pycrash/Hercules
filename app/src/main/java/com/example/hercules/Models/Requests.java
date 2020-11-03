package com.example.hercules.Models;

import com.example.hercules.Database.Order;

import java.io.Serializable;
import java.util.List;

public class Requests implements Serializable {
    private String orderID, date, companyName, phone, email, contactName, id, contactNumber, gstin, discount, address, pincode, state, status;
    private String newTotal, total, notes;
    private List<Order> cart;
    boolean cancelled;

    public Requests() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Requests(String orderID, String date, String companyName, String phone, String email, String contactName, String id,
                    String contactNumber, String gstin, String discount, String address, String pincode, String state,
                    String status, String newTotal, String total, List<Order> cart, String notes, boolean cancelled) {
        this.orderID = orderID;
        this.date = date;
        this.companyName = companyName;
        this.phone = phone;
        this.email = email;
        this.contactName = contactName;
        this.id = id;
        this.contactNumber = contactNumber;
        this.gstin = gstin;
        this.discount = discount;
        this.address = address;
        this.pincode = pincode;
        this.state = state;
        this.status = status;
        this.newTotal = newTotal;
        this.total = total;
        this.cart = cart;
        this.notes = notes;
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewTotal() {
        return newTotal;
    }

    public void setNewTotal(String newTotal) {
        this.newTotal = newTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getCart() {
        return cart;
    }

    public void setCart(List<Order> cart) {
        this.cart = cart;
    }
}