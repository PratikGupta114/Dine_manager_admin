package com.example.dine_manager_admin;

public class RestaurantDetails {

    public static final int STATUS_BLOCKED = 9746;
    public static final int STATUS_ALLOWED = 9747;

    private String UID, restaurantName, restaurantOwnerName, emailAddress, phoneNumber, password;
    private String dateCreatedOn;
    private String timeCreatedOn;
    private long   smsCreditsLeft;
    private int status;



    public RestaurantDetails() {}

    public String getUID() {
        return UID;
    }
    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantOwnerName() {
        return restaurantOwnerName;
    }
    public void setRestaurantOwnerName(String restaurantOwnerName) {
        this.restaurantOwnerName = restaurantOwnerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public long getSmsCreditsLeft() {
        return smsCreditsLeft;
    }
    public void setSmsCreditsLeft(long smsCreditsLeft) {
        this.smsCreditsLeft = smsCreditsLeft;
    }

    public String getDateCreatedOn() {
        return dateCreatedOn;
    }
    public void setDateCreatedOn(String dateCreatedOn) {
        this.dateCreatedOn = dateCreatedOn;
    }

    public String getTimeCreatedOn() {
        return timeCreatedOn;
    }
    public void setTimeCreatedOn(String timeCreatedOn) {
        this.timeCreatedOn = timeCreatedOn;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

}
