package com.yondev.shoppinglist;

import com.facebook.appevents.AppEventsLogger;
import com.yondev.shoppinglist.utils.FontsOverride;

/**
 * Created by ThinkPad on 5/7/2017.
 */

public class MyApplication extends com.orm.SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
        FontsOverride.setDefaultFont(this);
    }
}