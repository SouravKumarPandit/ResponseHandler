package com.responsehandler.app.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiService
{
    @GET("xyz-reader.json")
    Call<List<ResponseDto>> getJson();
}
