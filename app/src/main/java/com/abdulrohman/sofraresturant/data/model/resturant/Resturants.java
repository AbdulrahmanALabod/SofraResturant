
package com.abdulrohman.sofraresturant.data.model.resturant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resturants {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ResturantPagnition data;

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

    public ResturantPagnition getData() {
        return data;
    }

    public void setData(ResturantPagnition data) {
        this.data = data;
    }

}
