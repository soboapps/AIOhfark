package com.soboapps.ohfark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class PlayerSetup extends PreferenceActivity {

        private int numOfPlayers = 0;
        private boolean playersPrefChanged = true;
        private boolean player2IsHumanPref = true;

        Boolean isNotAI = true;

        SharedPreferences prefs;
        PreferenceScreen screen;
        PreferenceScreen screenH;
        PreferenceScreen screenN;
        ListPreference l;
        PreferenceCategory h;
        PreferenceCategory n;
        Preference p2;
        Preference p3;
        Preference p4;
        CheckBoxPreference aI;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                prefs = PreferenceManager.getDefaultSharedPreferences(this);

                //CheckBoxPreference aI = new CheckBoxPreference(this);
                addPreferencesFromResource(R.xml.local_players_prefs);
                Preference o = findPreference("scorePrefs");

                aI = (CheckBoxPreference)findPreference("player2IsHumanPref");

            //CheckBox Ai = (CheckBox)PlayerSetup.this.findViewById(R.id.human_player);;

                p2 = findPreference("player2NamePref");
                p3 = findPreference("player3NamePref");
                p4 = findPreference("player4NamePref");



                isNotAI = Boolean.valueOf(prefs.getBoolean("player2IsHumanPref", true));

                if (aI.isChecked()){
                    //n.addPreference(p2);
                    getPreferenceScreen().findPreference("player2NamePref").setEnabled(true); //Enabling
                } else {

                    //n.removePreference(p2);
                    getPreferenceScreen().findPreference("player2NamePref").setEnabled(false);//Disabling
                }


                //prefs.setOnPreferenceClickListener(pref1_click);





                prefs = PreferenceManager.getDefaultSharedPreferences(this);

                screen = getPreferenceScreen();
                l = (ListPreference)findPreference("playerNumPref");


                screenH = getPreferenceScreen();
                h = (PreferenceCategory)findPreference("IsHumanPref");


                screenN = getPreferenceScreen();
                n = (PreferenceCategory)findPreference("NamePref");

                // Remove Player setup if not 3 or 4 players
                n.removePreference(p3);
                n.removePreference(p4);

                //Ai = (CheckBox)PlayerSetup.this.findViewById(R.id.human_player);
                //if (aI.isChecked()){
                //    n.addPreference(p2);
                //} else {
                //    n.removePreference(p2);
                //}


                numOfPlayers = Integer.valueOf(prefs.getString("playerNumPref", "2"));

                for (int i = 1; i <= numOfPlayers; i++) {


                    if (i == 1) {
                        //aI.setChecked(false);
                    } else {
                        //aI.setChecked(true);
                    }

                    if ((i == 2) && (aI.isChecked())) {
                        //screenH.removePreference(h);
                        getPreferenceScreen().findPreference("player2NamePref").setEnabled(true);
                    } else {
                        //screenH.addPreference(h);
                        getPreferenceScreen().findPreference("player2NamePref").setEnabled(false);//Disabling
                    }

                    if (i >= 3) {
                        screenH.removePreference(h);
                        aI.setChecked(true);
                        screen.addPreference(p3);
                        getPreferenceScreen().findPreference("player2NamePref").setEnabled(true); //Enabling
                    } else {
                        screenH.addPreference(h);
                        screen.removePreference(p3);
                    }

                    //if (i >= 3) {
                    //    screen.addPreference(p3);
                    //    getPreferenceScreen().findPreference("player2NamePref").setEnabled(true); //Enabling
                    //} else {
                    //    screen.removePreference(p3);
                    //}

                    if (i == 4) {
                        screen.addPreference(p4);
                    } else {
                        screen.removePreference(p4);
                    }

                }


                l.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference preference,
                                                      Object newValue) {

                        playersPrefChanged = true;

                        numOfPlayers = Integer.valueOf((String) newValue);

                        //enablePrefs();
                        setUpListeners(true);

                        return true;
                    }
                });


                if (savedInstanceState != null
                        && savedInstanceState.getBoolean("playersPrefChanged")) {
                        numOfPlayers = Integer.valueOf(2);
                        //enablePrefs();
                        setUpListeners(true);

                }

                // Open Scoring Prefs
                o.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                                startActivity(new Intent(PlayerSetup.this, ScoringOptions.class));
                                return true;
                        }
                });

                Preference p = findPreference("playPref");

                p.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(PlayerSetup.this, OhFarkActivity.class));
                        PlayerSetup.this.finish();
                        return true;
                    }

                });

            final CheckBoxPreference finalAI = aI;
            aI.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        if (finalAI.isChecked()){
                            //n.addPreference(p2);
                            getPreferenceScreen().findPreference("player2NamePref").setEnabled(true); //Enabling
                        } else {
                            //n.removePreference(p2);
                            getPreferenceScreen().findPreference("player2NamePref").setEnabled(false);//Disabling
                        }
                        return true;
                    }

                });



        }


        protected void enablePrefs() {

                Preference p = null;
                for (int i = 1; i <= 2; i++) {

                        Log.d(getClass().getName(), "player" + i + "Pref");
                        p = (Preference) findPreference("player" + i + "Pref");

                        if (i <= numOfPlayers) {

                                p.setEnabled(true);
                        } else {
                                p.setEnabled(false);
                        }
                }

                p = findPreference("playPref");
                p.setEnabled(true);
        }

        protected void setUpListeners(boolean setListeners) {

            CheckBoxPreference c = null;

            //aI.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            //    @Override
            //    public boolean onPreferenceClick(Preference preference) {
            //        return false;
            //    }
            //});


            for (int i = 2; i <= numOfPlayers; i++) {
                c = (CheckBoxPreference) findPreference("player" + i + "onePlayPref");

                //for (int i = 2; i <= numOfPlayers; i++) {

                        //if (aI.isChecked()){
                        //    n.addPreference(p2);
                        //} else {
                        //    n.removePreference(p2);
                        //}
                        if (i == 1) {
                           // aI.setChecked(false);
                        } else {
                           // aI.setChecked(true);
                        }

                        if ((i == 2) && (isNotAI)) {
                            //screenH.removePreference(h);
                            getPreferenceScreen().findPreference("player2NamePref").setEnabled(true);
                        } else {
                            //screenH.addPreference(h);
                            getPreferenceScreen().findPreference("player2NamePref").setEnabled(false);//Disabling
                        }


                        if (i >= 3) {
                            getPreferenceScreen().findPreference("player2NamePref").setEnabled(true); //Enabling
                            screen.addPreference(p3);
                            aI.setChecked(true);
                            screenH.removePreference(h);
                        } else {
                            screenH.addPreference(h);
                            screen.removePreference(p3);
                        }

                        //if (i >= 3) {
                        //    getPreferenceScreen().findPreference("player2NamePref").setEnabled(true); //Enabling
                        //    screen.addPreference(p3);
                        //} else {
                        //    screen.removePreference(p3);
                        //}

                        if (i == 4) {
                            screen.addPreference(p4);
                        } else {
                            screen.removePreference(p4);
                        }
                }

        }


        @Override
        protected void onSaveInstanceState(Bundle outState) {

                Log.d(getClass().getName(),"playersPrefChanged= " + playersPrefChanged);
                outState.putBoolean("playersPrefChanged", playersPrefChanged);
                super.onSaveInstanceState(outState);
        }


}