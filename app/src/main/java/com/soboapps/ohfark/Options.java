package com.soboapps.ohfark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Options extends PreferenceActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.options_prefs);

                Preference p = findPreference("scorePrefs");
                Preference f = findPreference("screenPrefCheck");
                Preference s = findPreference("soundPrefCheck");
                //Preference v = findPreference("shakerPrefCheck");
                //Preference p1 = findPreference("onePlayPref");
                Preference p2 = findPreference("twoPlayPref");
                Preference reset = findPreference("resetPrefs");

                // Open Scoring Prefs
                p.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                                startActivity(new Intent(Options.this, ScoringOptions.class));
                                return true;
                        }
                });
                
                // Set Screen Flip Pref
                f.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                    		
                            return true;
                    }
                });
                
                // Set Sound Pref
                s.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                    		
                            return true;
                    }
                });
                
                //// Set Shaker Pref
                //v.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                //
                //    @Override
                //    public boolean onPreferenceClick(Preference preference) {
                //    		
                //            return true;
                //    }
                //});
                
                //Start a New 2 player Game from the Options Screen
                p2.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
	                    	Intent intent = new Intent(getApplicationContext(), OhFarkActivity.class);
	                    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    	intent.putExtra("EXIT", true);
	                    	startActivity(intent);
                            finish();
                            return true;
                    }
                });
                
                // Reset All the Options back to Default
                reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
                        SharedPreferences settings =  getPreferenceManager().getSharedPreferences();
                        SharedPreferences.Editor editor = settings.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = getIntent();
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
						return false;
					}
                });
        }             
}