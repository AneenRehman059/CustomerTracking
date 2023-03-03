package com.zasa.superduper.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static RetrofitInstance retrofitInstance;
    private Retrofit retrofit;
    private static final String BASE_URL = "http://valuechainapi.fuelly.pk/api/";

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