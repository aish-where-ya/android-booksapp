package com.example.user.chatyt;

public class UserInformation {
    public String name, email_id;
    public long contact_no;

    public UserInformation() {
    }

    public UserInformation(String name, long contact_no, String email_id) {
        this.name = name;
        this.contact_no = contact_no;
        this.email_id = email_id;
    }

    public long getContactNo(){return contact_no; }

    public String getEmail_id(){return email_id;}
}
