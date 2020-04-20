package com.users.qwikhomeservices.interfaces;

public interface OtpReceivedInterface {

    void onOtpReceived(String otp);

    void onOtpTimeout();

}
