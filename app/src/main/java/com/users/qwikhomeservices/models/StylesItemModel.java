package com.users.qwikhomeservices.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class StylesItemModel extends BaseObservable {

    public String price;
    public String itemDescription;
    public String itemImage;
    public double rating;
    public String userPhoto;
    public String userName;
    public Object timeStamp;
    public String accountType;


    public StylesItemModel() {
    }

    public StylesItemModel(String price, String itemDescription, String itemImage, String userImage, String userName,
                           Object timestamp, String accountType) {
        this.price = price;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.userPhoto = userImage;
        this.userName = userName;
        this.accountType = accountType;
        this.timeStamp = timestamp;

    }

    public StylesItemModel(String price, String itemDescription, String itemImage, double rating) {
        this.price = price;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.rating = rating;
    }

    public StylesItemModel(String price, String itemDescription, String itemImage,
                           String userPhoto, String userName, Object timeStamp) {
        this.price = price;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.timeStamp = timeStamp;
    }


    @Bindable
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Bindable
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Bindable
    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }


    @Bindable
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimeStamp() {
        return (long) timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
