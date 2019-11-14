package com.abdulrohman.sofraresturant.data.model.resturant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResturantsInformatiom {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ResturantData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResturantData getData() {
        return data;
    }

    public void setData(ResturantData data) {
        this.data = data;
    }

}
