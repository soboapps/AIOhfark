package com.soboapps.ohfark;

import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Ai_Decision extends PreferenceActivity {

    public int GOB_SCORE;
    public int WINNING_SCORE;
    private GameController controller;
    private DieManager dM;
    private OhFarkActivity UI;

    // Called to find out if it's the AI's Turn or not
    public void aiDecide(OhFarkActivity UI){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.UI);

        GOB_SCORE = Integer.valueOf(prefs.getString("gobScorePref", "0"));

        String ListDifPreference;
        ListDifPreference = prefs.getString("player2DiffPref", "Easy");
        GOB_SCORE = Integer.valueOf(prefs.getString("gobScorePref", "0"));


        final int highlightedScore = Scorer.calculate(dM.getHighlighted(DieManager.ABS_VALUE_FLAG), false, this.UI);
        final int possibleScore = controller.currPlayer.getInRoundScore() + highlightedScore;
        final int turnScore = controller.currPlayer.getInRoundScore() + highlightedScore + controller.currPlayer.getScore();

        if (((ListDifPreference.equals("Easy")) && (highlightedScore > 0) &&  (turnScore >= GOB_SCORE) && (dM.numDiceRemain() != 0) && (possibleScore >= 300) && (dM.numDiceRemain() <= 2))
                || ((ListDifPreference.equals("Easy")) && (highlightedScore > 0) &&  (turnScore >= GOB_SCORE) && (dM.numDiceRemain() != 0) && (possibleScore >= 400))

                || ((ListDifPreference.equals("Medium")) && (highlightedScore > 0) &&  (turnScore >= GOB_SCORE) && (dM.numDiceRemain() != 0) && (possibleScore >= 300) && (dM.numDiceRemain() <= 3))
                || ((ListDifPreference.equals("Medium")) && (highlightedScore > 0) &&  (turnScore >= GOB_SCORE) && (dM.numDiceRemain() != 0) && (possibleScore >= 400))

                || ((ListDifPreference.equals("Hard")) && (highlightedScore > 0) &&  (turnScore >= GOB_SCORE) && (dM.numDiceRemain() != 0) && (possibleScore >= 300))) {

            this.UI.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Score the Dice
                    if ((controller.isRoundEnded == false && dM.numDiceRemain() == 0) || (controller.isLastRound == true && turnScore < WINNING_SCORE)){
                        controller.aiRoll();
                    } else {
                        if (highlightedScore > 0) {
                            controller.onScore();
                            Ai_Decision.this.UI.enableButtons();
                            Ai_Decision.this.UI.enableDice();
                        }

                    }
                }
            }, 1000);
        } else {
            // Roll the dice becasue you didn't
            // meet the criteria to score
            if ((controller.isRoundEnded == false) && (controller.gameOverMan == false)) {
                controller.aiRoll();
            }
        }
    }

}




