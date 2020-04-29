package com.users.qwikhomeservices.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("txRef")
    @Expose
    private String txRef;
    @SerializedName("orderRef")
    @Expose
    private String orderRef;
    @SerializedName("flwRef")
    @Expose
    private String flwRef;
    @SerializedName("redirectUrl")
    @Expose
    private String redirectUrl;
    @SerializedName("device_fingerprint")
    @Expose
    private String deviceFingerprint;
    @SerializedName("settlement_token")
    @Expose
    private Object settlementToken;
    @SerializedName("cycle")
    @Expose
    private String cycle;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("charged_amount")
    @Expose
    private int chargedAmount;
    @SerializedName("appfee")
    @Expose
    private double appfee;
    @SerializedName("merchantfee")
    @Expose
    private int merchantfee;
    @SerializedName("merchantbearsfee")
    @Expose
    private int merchantbearsfee;
    @SerializedName("chargeResponseCode")
    @Expose
    private String chargeResponseCode;
    @SerializedName("raveRef")
    @Expose
    private String raveRef;
    @SerializedName("chargeResponseMessage")
    @Expose
    private String chargeResponseMessage;
    @SerializedName("authModelUsed")
    @Expose
    private String authModelUsed;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("IP")
    @Expose
    private String iP;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("modalauditid")
    @Expose
    private String modalauditid;
    @SerializedName("vbvrespmessage")
    @Expose
    private String vbvrespmessage;
    @SerializedName("authurl")
    @Expose
    private String authurl;
    @SerializedName("vbvrespcode")
    @Expose
    private String vbvrespcode;
    @SerializedName("acctvalrespmsg")
    @Expose
    private Object acctvalrespmsg;
    @SerializedName("acctvalrespcode")
    @Expose
    private String acctvalrespcode;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("paymentPlan")
    @Expose
    private Object paymentPlan;
    @SerializedName("paymentPage")
    @Expose
    private Object paymentPage;
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("fraud_status")
    @Expose
    private String fraudStatus;
    @SerializedName("charge_type")
    @Expose
    private String chargeType;
    @SerializedName("is_live")
    @Expose
    private int isLive;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("deletedAt")
    @Expose
    private Object deletedAt;
    @SerializedName("AccountId")
    @Expose
    private int accountId;
    @SerializedName("customer.id")
    @Expose
    private int customerId;
    @SerializedName("customer.phone")
    @Expose
    private Object customerPhone;
    @SerializedName("customer.fullName")
    @Expose
    private String customerFullName;
    @SerializedName("customer.customertoken")
    @Expose
    private Object customerCustomertoken;
    @SerializedName("customer.email")
    @Expose
    private String customerEmail;
    @SerializedName("customer.createdAt")
    @Expose
    private String customerCreatedAt;
    @SerializedName("customer.updatedAt")
    @Expose
    private String customerUpdatedAt;
    @SerializedName("customer.deletedAt")
    @Expose
    private Object customerDeletedAt;
    @SerializedName("customer.AccountId")
    @Expose
    private int customerAccountId;
    @SerializedName("meta")
    @Expose
    private List<Object> meta = null;
    @SerializedName("flwMeta")
    @Expose
    private FlwMeta flwMeta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxRef() {
        return txRef;
    }

    public void setTxRef(String txRef) {
        this.txRef = txRef;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getFlwRef() {
        return flwRef;
    }

    public void setFlwRef(String flwRef) {
        this.flwRef = flwRef;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

    public Object getSettlementToken() {
        return settlementToken;
    }

    public void setSettlementToken(Object settlementToken) {
        this.settlementToken = settlementToken;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(int chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    public double getAppfee() {
        return appfee;
    }

    public void setAppfee(double appfee) {
        this.appfee = appfee;
    }

    public int getMerchantfee() {
        return merchantfee;
    }

    public void setMerchantfee(int merchantfee) {
        this.merchantfee = merchantfee;
    }

    public int getMerchantbearsfee() {
        return merchantbearsfee;
    }

    public void setMerchantbearsfee(int merchantbearsfee) {
        this.merchantbearsfee = merchantbearsfee;
    }

    public String getChargeResponseCode() {
        return chargeResponseCode;
    }

    public void setChargeResponseCode(String chargeResponseCode) {
        this.chargeResponseCode = chargeResponseCode;
    }

    public String getRaveRef() {
        return raveRef;
    }

    public void setRaveRef(String raveRef) {
        this.raveRef = raveRef;
    }

    public String getChargeResponseMessage() {
        return chargeResponseMessage;
    }

    public void setChargeResponseMessage(String chargeResponseMessage) {
        this.chargeResponseMessage = chargeResponseMessage;
    }

    public String getAuthModelUsed() {
        return authModelUsed;
    }

    public void setAuthModelUsed(String authModelUsed) {
        this.authModelUsed = authModelUsed;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIP() {
        return iP;
    }

    public void setIP(String iP) {
        this.iP = iP;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModalauditid() {
        return modalauditid;
    }

    public void setModalauditid(String modalauditid) {
        this.modalauditid = modalauditid;
    }

    public String getVbvrespmessage() {
        return vbvrespmessage;
    }

    public void setVbvrespmessage(String vbvrespmessage) {
        this.vbvrespmessage = vbvrespmessage;
    }

    public String getAuthurl() {
        return authurl;
    }

    public void setAuthurl(String authurl) {
        this.authurl = authurl;
    }

    public String getVbvrespcode() {
        return vbvrespcode;
    }

    public void setVbvrespcode(String vbvrespcode) {
        this.vbvrespcode = vbvrespcode;
    }

    public Object getAcctvalrespmsg() {
        return acctvalrespmsg;
    }

    public void setAcctvalrespmsg(Object acctvalrespmsg) {
        this.acctvalrespmsg = acctvalrespmsg;
    }

    public String getAcctvalrespcode() {
        return acctvalrespcode;
    }

    public void setAcctvalrespcode(String acctvalrespcode) {
        this.acctvalrespcode = acctvalrespcode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Object getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(Object paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public Object getPaymentPage() {
        return paymentPage;
    }

    public void setPaymentPage(Object paymentPage) {
        this.paymentPage = paymentPage;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public int getIsLive() {
        return isLive;
    }

    public void setIsLive(int isLive) {
        this.isLive = isLive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Object getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(Object customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public Object getCustomerCustomertoken() {
        return customerCustomertoken;
    }

    public void setCustomerCustomertoken(Object customerCustomertoken) {
        this.customerCustomertoken = customerCustomertoken;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerCreatedAt() {
        return customerCreatedAt;
    }

    public void setCustomerCreatedAt(String customerCreatedAt) {
        this.customerCreatedAt = customerCreatedAt;
    }

    public String getCustomerUpdatedAt() {
        return customerUpdatedAt;
    }

    public void setCustomerUpdatedAt(String customerUpdatedAt) {
        this.customerUpdatedAt = customerUpdatedAt;
    }

    public Object getCustomerDeletedAt() {
        return customerDeletedAt;
    }

    public void setCustomerDeletedAt(Object customerDeletedAt) {
        this.customerDeletedAt = customerDeletedAt;
    }

    public int getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(int customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public List<Object> getMeta() {
        return meta;
    }

    public void setMeta(List<Object> meta) {
        this.meta = meta;
    }

    public FlwMeta getFlwMeta() {
        return flwMeta;
    }

    public void setFlwMeta(FlwMeta flwMeta) {
        this.flwMeta = flwMeta;
    }
}
