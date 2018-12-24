package com.example.user.chatyt;

public class SellerInfo {

    String title;
    String sellerId;
    String department;
    String year;
    String semester;
    String imageUri;
    String emailId;
    String description;
    int price;

    public SellerInfo()
    {


    }

    public SellerInfo(String title,String sellerId, String department, String year, String semester,String imageUri,String emailId,String description,int price) {
        this.title=title;
        this.sellerId = sellerId;
        this.department = department;
        this.year = year;
        this.semester = semester;
        this.imageUri = imageUri;
        this.emailId = emailId;
        this.description = description;
        this.price=price;
    }

    public String getTitle() {return title; }

    public String getSellerId() {
        return sellerId;
    }

    public String getDepartment() {
        return department;
    }

    public String getYear() {
        return year;
    }

    public String getSemester() {
        return semester;
    }
    public String getImageUri() {
        return imageUri;
    }
    public String getEmailId() { return emailId;}

    public String getDescription(){return description;}

    public int getPrice(){ return price;}

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
