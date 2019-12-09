package com.smser.smssender.model;

import java.io.Serializable;

public class SentData implements Serializable {

    private String sentNumber;
//    private String sentName;
    private String sentCount;

    public String getSentNumber() {
        return sentNumber;
    }

    public void setSentNumber(String sentNumber) {
        this.sentNumber = sentNumber;
    }

//    public String getSentName() {
//        return sentName;
//    }

//    public void setSentName(String sentName) {
//        this.sentName = sentName;
//    }

    public String getSentCount() {
        return sentCount;
    }

    public void setSentCount(String sentCount) {
        this.sentCount = sentCount;
    }

}
