package com.responsehandler.app.Test;

import android.content.Context;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService
{
    private final Context context;

    public RetrofitService(Context context)
    {
        this.context=context;
    }
    public Retrofit getRetro()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/March/58c5d68f_xyz-reader/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
