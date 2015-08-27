package com.soboapps.ohfark;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PlayerSetup extends PreferenceActivity {

        private int numOfPlayers = 0;
        private boolean playersPrefChanged = true;

        SharedPreferences setNoti;
        boolean showHelp1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.local_players_prefs);
                Preference reset = findPreference("resetPrefs");

                PreferenceScreen screen = getPreferenceScreen();
                PreferenceCategory l = (PreferenceCategory)findPreference("NumOfPlayerPref");
                screen.removePreference(l);

                ((PreferenceGroup) findPreference("playerPrefs")).removePreference(l);

                if (savedInstanceState != null
                        && savedInstanceState.getBoolean("playersPrefChanged")) {
                        numOfPlayers = Integer.valueOf(2);
                        enablePrefs();
                        setUpListeners(false);

                }

                /*
                l.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                        @Override
                        public boolean onPreferenceChange(Preference preference,
                                                          Object newValue) {

                                playersPrefChanged = true;

                                numOfPlayers = Integer.valueOf((String) newValue);

                                enablePrefs();
                                setUpListeners(true);

                                return true;
                        }
                });
                */

                Preference p = findPreference("playPref");

                p.setOnPreferenceClickListener(new OnPreferenceClickListener() {

                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(PlayerSetup.this, OhFarkActivity.class));
                        PlayerSetup.this.finish();
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

                setNoti = PreferenceManager.getDefaultSharedPreferences(this );
                //SharedPref tutorial
                showHelp1 = setNoti.getBoolean("help", true);

                if (showHelp1 == true) {
                        showActivityOverlay();
                }

        }

        private void showActivityOverlay() {
                final Dialog dialog = new Dialog(this,
                        android.R.style.Theme_Translucent_NoTitleBar);

                dialog.setContentView(R.layout.info_overview);

                RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.llOverlay_activity);
                layout.setBackgroundColor(Color.TRANSPARENT);
                layout.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                                dialog.dismiss();
                                SharedPreferences.Editor editor = setNoti.edit();
                                editor.putBoolean("help", false);
                                editor.commit();
                        }
                });
                dialog.show();
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

                if (showHelp1 == true) {
                        showActivityOverlay();
                }

                for (int i = 2; i <= numOfPlayers; i++) {
                        c = (CheckBoxPreference) findPreference("player" + i
                                + "IsHumanPref");

                        EditTextPreference e = (EditTextPreference) findPreference("player"
                                + i + "NamePref");
                        ListPreference l = (ListPreference) findPreference("player" + i
                                + "DiffPref");

                        if (c.isChecked() == true) {
                                e.setEnabled(true);
                                l.setEnabled(false);

                        } else {
                                e.setEnabled(false);
                                l.setEnabled(true);
                        }

                        if (setListeners) {

                                c.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

                                        @Override
                                        public boolean onPreferenceChange(Preference preference, Object newValue) {

                                                int i = Integer.valueOf(""
                                                        + preference.getKey().charAt(6));

                                                EditTextPreference e = (EditTextPreference) findPreference("player"
                                                        + i + "NamePref");
                                                ListPreference l = (ListPreference) findPreference("player"
                                                        + i + "DiffPref");

                                                if (((Boolean) newValue).booleanValue() == true) {
                                                        e.setEnabled(true);
                                                        l.setEnabled(false);
                                                } else {
                                                        e.setEnabled(false);
                                                        l.setEnabled(true);
                                                }

                                                return true;
                                        }

                                });
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