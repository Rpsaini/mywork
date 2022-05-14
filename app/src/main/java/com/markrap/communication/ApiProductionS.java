package com.markrap.communication;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markrap.utility.AppConstants;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiProductionS {

    // private static final String BASE_URL = "https://api.stackexchange.com/2.2/";
    private final Context context;

    private ApiProductionS(Context context) {
        this.context = context;
    }

    public static ApiProductionS getInstance(Context context) {
        return new ApiProductionS(context);
    }

    private Retrofit provideRestAdapter() {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(AppConstants.apiUlr)
                .client(OkHttpProduction.getOkHttpClient(context, true))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <S> S provideService(Class<S> serviceClass)
    {
        return provideRestAdapter().create(serviceClass);
    }
}