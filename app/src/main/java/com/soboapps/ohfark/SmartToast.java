package com.soboapps.ohfark;

import android.preference.PreferenceManager;

/**
 * Created by sdmei on 8/17/2015.
 */
public class SmartToast {

    public void show() {
        if (PreferenceManager.getDefaultSharedPreferences(OhFarkContext.getAppContext()).getBoolean("showToast", true)) {
            //super.show();
        }
    }

}
