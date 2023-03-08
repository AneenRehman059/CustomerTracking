package com.zasa.superduper.retrofit;

import static com.zasa.superduper.retrofit.ApiEndpoints.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static RetrofitInstance retrofitInstance;
    private Retrofit retrofit;

    private RetrofitInstance() {  //class constructure

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitInstance getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new RetrofitInstance();
        }
        return retrofitInstance;
    }
    public APIInterface getApiInterface() {
        return retrofit.create(APIInterface.class);
    }
}