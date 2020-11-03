package com.example.hercules.Models;


public class UploadPDF {

    public String companyName;
    public String url;
    public String date;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public UploadPDF() {
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UploadPDF(String companyName, String url, String date) {
        this.companyName = companyName;
        this.url = url;
        this.date = date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUrl() {
        return url;
    }
}