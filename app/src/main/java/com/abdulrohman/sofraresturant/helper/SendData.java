package com.abdulrohman.sofraresturant.helper;

import java.io.Serializable;

public class SendData implements Serializable {
    private String name;
    private String e_mail;
    private String phone;
    private String periodReach;
    private String region;
    private String pass;
    private String confirmPass;
    private String minimumPrice;
    private String priceCharger;
    private String pathImg;

    public SendData(String name, String e_mail, String region, String minimumPrice, String pathImg) {
        this.name = name;
        this.e_mail = e_mail;
        this.region = region;
        this.minimumPrice = minimumPrice;
        this.pathImg = pathImg;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public SendData(String name, String e_mail, String phone,
                    String periodReach, String region, String pass,
                    String confirmPass, String minimumCharger, String priceCharger) {
        this.name = name;
        this.e_mail = e_mail;
        this.phone = phone;
        this.periodReach = periodReach;
        this.region = region;
        this.pass = pass;
        this.confirmPass = confirmPass;
        this.minimumPrice = minimumCharger;
        this.priceCharger = priceCharger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPeriodReach() {
        return periodReach;
    }

    public void setPeriodReach(String periodReach) {
        this.periodReach = periodReach;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

    public String getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(String minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String getPriceCharger() {
        return priceCharger;
    }

    public void setPriceCharger(String priceCharger) {
        this.priceCharger = priceCharger;
    }
}
