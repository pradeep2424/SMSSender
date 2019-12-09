package com.smser.smssender.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterReq {

    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Pass")
    @Expose
    private String pass;
    @SerializedName("ExpiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("TokenNo")
    @Expose
    private String tokenNo;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("IMEI")
    @Expose
    private String IMEI;
    @SerializedName("Model")
    @Expose
    private String model;
    @SerializedName("LoginID")
    @Expose
    private Integer agentCode;
    @SerializedName("TollFree")
    @Expose
    private String tollFree;
    @SerializedName("AdminCode")
    @Expose
    private String adminCode;
    @SerializedName("isadmin")
    @Expose
    private Boolean isAdmin;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(Integer agentCode) {
        this.agentCode = agentCode;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTollFree() {
        return tollFree;
    }

    public void setTollFree(String tollFree) {
        this.tollFree = tollFree;
    }

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
