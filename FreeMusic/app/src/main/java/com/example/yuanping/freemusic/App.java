package com.example.yuanping.freemusic;

import android.app.Application;
import android.content.Context;

/**
 * Created by yuanping on 5/2/18.
 */

public class App extends Application {
    //TODO True of False ??
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

}
