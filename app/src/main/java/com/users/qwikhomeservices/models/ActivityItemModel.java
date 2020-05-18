package com.users.qwikhomeservices.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.Map;


public class ActivityItemModel extends BaseObservable {
    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;
    public static final int AUDIO_TYPE = 2;

    public int type, data;
    public int numOfLikes, numOfComments;
    public Object timeStamp;
    public String itemImage, itemDescription, userName, userPhoto, status, accountType, price;
    public String id;
    Map<String, Boolean> isLiked;


    public ActivityItemModel() {
    }

    //constructor to load image type
    public ActivityItemModel(int type, String itemImage, String itemDescription,
                             String userName, String userPhoto, Object timeStamp,
                             String id, int numOfLikes, int numOfComments) {
        this.type = type;
        this.itemImage = itemImage;
        this.itemDescription = itemDescription;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.timeStamp = timeStamp;
        this.id = id;
        this.numOfLikes = numOfLikes;
        this.numOfComments = numOfComments;

    }

    //constructor to load text type
    public ActivityItemModel(int type, String status, String userName,
                             String userPhoto, Object timeStamp,
                             String id, int numOfLikes, int numOfComments) {
        this.type = type;
        this.status = status;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.timeStamp = timeStamp;
        this.id = id;
        this.numOfLikes = numOfLikes;
        this.numOfComments = numOfComments;
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

    @Bindable
    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    @Bindable
    public int getNumOfComments() {
        return numOfComments;
    }

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Boolean> getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Map<String, Boolean> isLiked) {
        this.isLiked = isLiked;
    }
}
