package com.abdulrohman.sofraresturant.data.api;

import android.util.Log;

import com.abdulrohman.sofraresturant.helper.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String TAG = "ApiClient";

    private static String BASE_URL = Constant.API_V2;
    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder builder = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();
            Log.d( TAG, "getClient: build()" );
        }
        return retrofit;
    }

}
