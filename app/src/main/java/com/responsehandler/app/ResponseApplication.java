package com.responsehandler.app;

import android.app.Application;
import android.content.Context;

public class ResponseApplication extends Application
{
    private Context clAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.clAppContext=this;
    }
    public Context getClAppContext()
    {
        return clAppContext;
    }
}
