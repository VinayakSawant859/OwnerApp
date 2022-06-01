package com.example.recyclerviewdemoproj.Models;

public class adminModel {

    private String dcID;
    private String dcName;
    private String dcAdminName;
    private String dcEmail;
    private String dcNewPass;
    private String dcConfPass;
    private String dcAddress;
    private String dcP1;
    private String dcP2;
    private String dcOcc;

    public adminModel(){}

    public adminModel(String dcID, String dcName,
                      String dcAdminName, String dcEmail,
                      String dcNewPass, String dcConfPass,
                      String dcAddress, String dcP1,
                      String dcP2, String dcOcc)
    {
        this.dcID = dcID;
        this.dcName = dcName;
        this.dcAdminName = dcAdminName;
        this.dcEmail = dcEmail;
        this.dcNewPass = dcNewPass;
        this.dcConfPass = dcConfPass;
        this.dcAddress = dcAddress;
        this.dcP1 = dcP1;
        this.dcP2 = dcP2;
        this.dcOcc = dcOcc;
    }

    public String getDcID() {
        return dcID;
    }

    public void setDcID(String dcID) {
        this.dcID = dcID;
    }

    public String getDcName() {
        return dcName;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getDcAdminName() {
        return dcAdminName;
    }

    public void setDcAdminName(String dcAdminName) {
        this.dcAdminName = dcAdminName;
    }

    public String getDcEmail() {
        return dcEmail;
    }

    public void setDcEmail(String dcEmail) {
        this.dcEmail = dcEmail;
    }

    public String getDcNewPass() {
        return dcNewPass;
    }

    public void setDcNewPass(String dcNewPass) {
        this.dcNewPass = dcNewPass;
    }

    public String getDcConfPass() {
        return dcConfPass;
    }

    public void setDcConfPass(String dcConfPass) {
        this.dcConfPass = dcConfPass;
    }

    public String getDcAddress() {
        return dcAddress;
    }

    public void setDcAddress(String dcAddress) {
        this.dcAddress = dcAddress;
    }

    public String getDcP1() {
        return dcP1;
    }

    public void setDcP1(String dcP1) {
        this.dcP1 = dcP1;
    }

    public String getDcP2() {
        return dcP2;
    }

    public void setDcP2(String dcP2) {
        this.dcP2 = dcP2;
    }

    public String getDcOcc() {
        return dcOcc;
    }

    public void setDcOcc(String dcOcc) {
        this.dcOcc = dcOcc;
    }
}
