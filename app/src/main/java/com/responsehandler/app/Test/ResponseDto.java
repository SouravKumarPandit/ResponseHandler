package com.responsehandler.app.Test;

import com.google.gson.annotations.SerializedName;


public class ResponseDto
{

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("author")
    public String author;
    @SerializedName("body")
    public String body;
    @SerializedName("thumb")
    public String thumb;
    @SerializedName("photo")
    public String photo;
    @SerializedName("aspect_ratio")
    public double aspect_ratio;
    @SerializedName("published_date")
    public String published_date;
}
