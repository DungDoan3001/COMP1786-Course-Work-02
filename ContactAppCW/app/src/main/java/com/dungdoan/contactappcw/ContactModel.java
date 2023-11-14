package com.dungdoan.contactappcw;

public class ContactModel {
    private String ID;
    private String Name;
    private String PhoneNumber;
    private String Address;
    private String Email;
    private String Image;

    public ContactModel(String id, String name, String phoneNumber, String address, String email, String image) {
        ID = id;
        Name = name;
        PhoneNumber = phoneNumber;
        Address = address;
        Email = email;
        Image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

}
