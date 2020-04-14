package com.users.quickhomeservices.interfaces;

public interface OtpReceivedInterface {

    void onOtpReceived(String otp);

    void onOtpTimeout();

}
