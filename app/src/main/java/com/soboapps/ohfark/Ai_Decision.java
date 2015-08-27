package com.soboapps.ohfark;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sdmei on 8/21/2015.
 */
public class Ai_Decision {

    private static OhFarkActivity UI;
    private GameController controller;
    private DieManager dM;


        public void Ai_Roll() {

            int scoreOnTable = Scorer.calculate(
                    dM.getHighlighted(DieManager.ABS_VALUE_FLAG), true, UI);
            //controller.onClickDice(UTILS.identifyView(v), toFarkle);

            Toast t = Toast.makeText(UI.getApplicationContext(), "Name is Android", Toast.LENGTH_LONG);
            t.show();

            UI.handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    //rollButton.setEnabled(true);
                    controller.animationsEnded(true);
                   // controller.onRoll();
                }
            }, 200 * 5 + 750);

        }




}




