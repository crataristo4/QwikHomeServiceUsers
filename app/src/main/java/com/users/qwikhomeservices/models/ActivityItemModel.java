package com.users.qwikhomeservices.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ActivityItemModel extends BaseObservable {
    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final int AUDIO_TYPE = 2;

    public int type, data;
    public String itemImage, itemDescription, userName, userPhoto, status, accountType, price;
    public Object timeStamp;

    public ActivityItemModel() {
    }

    //constructor to load image type
    public ActivityItemModel(int type, String itemImage, String itemDescription, String userName, String userPhoto, Object timeStamp) {
        this.type = type;
        this.itemImage = itemImage;
        this.itemDescription = itemDescription;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.timeStamp = timeStamp;
    }

    //constructor to load text type
    public ActivityItemModel(int type, String status, String userName, String userPhoto, Object timeStamp) {
        this.type = type;
        this.status = status;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.timeStamp = timeStamp;
    }

    public ActivityItemModel(int type, String itemDescription, String itemImage) {
        this.type = type;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;

    }

    public ActivityItemModel(int type, String itemDescription, int data) {
        this.type = type;
        this.itemDescription = itemDescription;
        this.data = data;

    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public long getTimeStamp() {
        return (long) timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Bindable
    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
