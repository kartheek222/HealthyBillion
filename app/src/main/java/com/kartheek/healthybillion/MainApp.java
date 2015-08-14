package com.kartheek.healthybillion;

import android.app.Application;

import com.kartheek.healthybillion.volley.RequestManager;


/**
 * Created by kartheek on 20/7/15.
 */
public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.init(this);
    }

}
