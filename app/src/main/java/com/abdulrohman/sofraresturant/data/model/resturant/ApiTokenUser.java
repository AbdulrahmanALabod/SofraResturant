package com.abdulrohman.sofraresturant.data.model.resturant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiTokenUser {
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("user")
    @Expose
    private List<ResturantData> user;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public List<ResturantData>getUser() {
        return user;
    }

    public void setUser(List<ResturantData> user) {
        this.user = user;
    }

}
