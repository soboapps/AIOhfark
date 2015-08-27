package com.soboapps.ohfark;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ScoringOptions extends PreferenceActivity {
	
        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.local_score_prefs);
        }
}