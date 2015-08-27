package com.soboapps.ohfark;

/**
 * This class is used to get the Application context and to be
 * able to use it anywhere in any other class that does not have
 * a Context.  Now every where call   OhFarkContext.getAppContext()
 * to get the application context statically.
 */

import android.app.Application;
import android.content.Context;

public class OhFarkContext extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        OhFarkContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return OhFarkContext.context;
    }
}