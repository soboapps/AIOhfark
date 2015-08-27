package com.soboapps.ohfark;
import com.soboapps.ohfark.OhFarkActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

final class Scorer {

        private static OhFarkActivity UI;


        public final static int calculate(int[] array, boolean ignoreExtraDice, Context c) {

                int length = array.length;

                if (length == 0)
                        return 0;

                int[] newArray = count(array);

                if (length == 6) {

                        int straightStatus = isStraight(newArray, c, false);
                        if (straightStatus > 0) {
                                return straightStatus;
                        }

                        int sixOfAKindStatus = isSixOfAKind(newArray, c);
                        if (sixOfAKindStatus > 0)
                                return sixOfAKindStatus;

                        int threePairStatus = isThreePair(newArray, c, false);
                        if (threePairStatus > 0)
                                return threePairStatus;

                        int doubleTripletStatus = isDoubleTriplet(newArray, c);
                        if (doubleTripletStatus > 0)
                                return doubleTripletStatus;
                }

                int fiveOfAKindStatus = 0;
                if (length == 5)
                        fiveOfAKindStatus = isFiveOfAKind(newArray, c, length)[0];
                if (fiveOfAKindStatus > 0)
                        return fiveOfAKindStatus;

                int fourOfAKindStatus = 0;
                if (length == 4)
                        fourOfAKindStatus = isFourOfAKind(newArray, c, length)[0];
                if (fourOfAKindStatus > 0)
                        return fourOfAKindStatus;

                int threeOfAKindStatus = 0;
                if (length == 3)
                        threeOfAKindStatus = isThreeOfAKind(newArray, c, length)[0];
                if (threeOfAKindStatus > 0)
                        return threeOfAKindStatus;

                int combinationScore = scoreCombinations(newArray, c, length);

                if (combinationScore >= 0 && !ignoreExtraDice)
                        return 0;
                else
                        return Math.abs(combinationScore);

        }

        private final static int[] count(int[] array) {
                int numOf1 = 0;
                int numOf2 = 0;
                int numOf3 = 0;
                int numOf4 = 0;
                int numOf5 = 0;
                int numOf6 = 0;

                for (int j = 0; j < array.length; j++) {
                        switch (array[j]) {
                                case (1):
                                        numOf1++;
                                        break;
                                case (2):
                                        numOf2++;
                                        break;
                                case (3):
                                        numOf3++;
                                        break;
                                case (4):
                                        numOf4++;
                                        break;
                                case (5):
                                        numOf5++;
                                        break;
                                case (6):
                                        numOf6++;
                                        break;
                        }

                }

                int[] newArray = { numOf1, numOf2, numOf3, numOf4, numOf5, numOf6 };
                return newArray;
        }


        public final static int isStraight(int[] array, Context c, boolean calledPublicly) {
                if (array.length < 6)
                        return 0;

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(c);

                boolean toScoreStraight = prefs.getBoolean("toScoreStraightPref", true);

                if (!toScoreStraight)
                        return 0;

                int straightScore = 0;
                if (toScoreStraight)
                        straightScore = Integer.valueOf(prefs.getString("straightPref", "1500"));

                if (calledPublicly)
                        array = count(array);

                boolean isStraight = (array[0] == 1 && array[1] == 1 && array[2] == 1
                        && array[3] == 1 && array[4] == 1 && array[5] == 1);

                if (isStraight)
                        return straightScore;
                else
                        return 0;

        }

        public final static int isThreePair(int[] array, Context c, boolean calledPublicly) {

                if (array.length < 6)
                        return 0;

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

                int threePairScore = Integer.valueOf(prefs.getString("threePairPref", "750"));

                if (prefs.getString("Four of A Kind", "2x the 3 Of A Kind Value") != null) {
                        //if (prefs.getString(UI.getString(R.string.stFourOfKind), UI.getString(R.string.stFourOfKind2XValue)) != null) {

                        if (calledPublicly)
                                array = count(array);

                        int count = 0;

                        if ((array[0] >= 2) && (array[3] != 4 ) && (array[5] != 4 ))
                                count += array[0] / 2;
                        if (array[1] >= 2 )
                                count += array[1] / 2;
                        if (array[2] >= 2)
                                count += array[2] / 2;
                        if (array[3] >= 2)
                                count += array[3] / 2;
                        if ((array[4] >= 2) && (array[3] != 4 ) && (array[5] != 4 ))
                                count += array[4] / 2;
                        if (array[5] >= 2)
                                count += array[5] / 2;

                        if (count == 3)
                                return threePairScore;
                        else
                                return 0;

                } else {

                        if (calledPublicly)
                                array = count(array);

                        int count = 0;

                        if (array[0] >= 2)
                                count += array[0] / 2;
                        if (array[1] >= 2)
                                count += array[1] / 2;
                        if (array[2] >= 2)
                                count += array[2] / 2;
                        if (array[3] >= 2)
                                count += array[3] / 2;
                        if (array[4] >= 2)
                                count += array[4] / 2;
                        if (array[5] >= 2)
                                count += array[5] / 2;

                        if (count == 3)
                                return threePairScore;
                        else
                                return 0;

                }

        }


        private final static int isSixOfAKind(int[] array, Context c) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

                String sixKindPref = prefs.getString("sixOfAKindPref", "4x the 3 Of A Kind Value");
                int sixOfAKindMult = (sixKindPref.contains("3000")) ? 3000 : Integer
                        .valueOf("" + sixKindPref.charAt(0));

                String sixOnesPref = prefs.getString("sixonesPref", "10000");
                int sixOnesMult = (sixOnesPref.contains("10000")) ? 10000 : Integer
                        .valueOf("" + sixOnesPref.charAt(0));

                int score = 0;

                if (array[0] == 6) {
                        score = sixOnesMult;
                        array[0] = 0;
                }
                if (array[1] == 6) {
                        score = 200 * sixOfAKindMult;
                        array[1] = 0;
                }
                if (array[2] == 6) {
                        score = 300 * sixOfAKindMult;
                        array[2] = 0;
                }
                if (array[3] == 6) {
                        score = 400 * sixOfAKindMult;
                        array[3] = 0;
                }
                if (array[4] == 6) {
                        score = 500 * sixOfAKindMult;
                        array[4] = 0;
                }
                if (array[5] == 6) {
                        score = 600 * sixOfAKindMult;
                        array[5] = 0;
                }

                if (sixOfAKindMult > 100)
                        return sixOfAKindMult;

                if (sixKindPref.contains("3000") && score != sixOnesMult)
                        score = 3000;
                if (sixKindPref.contains("6000") && score != sixOnesMult)
                        score = 6000;
                if (sixKindPref.contains("10000") && score != sixOnesMult)
                        score = 10000;

                if (score != 0)
                        return score;
                else
                        return 0;
        }

        private final static int isDoubleTriplet(int[] array, Context c) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

                boolean toVariateTwoTripsScore = prefs.getBoolean("toScoreTwoTripsPref", false);

                if (!toVariateTwoTripsScore)
                        return 0;

                int twoTripsScore = 0;
                if (toVariateTwoTripsScore)
                        twoTripsScore = Integer.valueOf(prefs.getString("twoTripletScorePref", "2500"));

                int count = 0;

                if (array[0] == 3)
                        count++;
                if (array[1] == 3)
                        count++;
                if (array[2] == 3)
                        count++;
                if (array[3] == 3)
                        count++;
                if (array[4] == 3)
                        count++;
                if (array[5] == 3)
                        count++;

                if (count == 2)
                        return twoTripsScore;
                else
                        return 0;
        }

        private final static int scoreCombinations(int[] array, Context c, int length) {

                int score = 0;

                int[] fiveArray = isFiveOfAKind(array, c, length);
                int[] fourArray = isFourOfAKind(array, c, length);
                int[] threeArray = isThreeOfAKind(array, c, length);
                //int[] threePairArray = isThreePair(array, c, length);


                //int[] twoArray = isOnePair(array, c, length, dM);

                int[] temp = null;

                if (fiveArray[0] != 0) {
                        temp = fiveArray;
                }
                else if (fourArray[0] != 0) {
                        temp = fourArray;
                }
                else if (threeArray[0] != 0) {
                        temp = threeArray;
                }
                //else if (twoArray[0] != 0)
                //    temp = twoArray;

                if (temp != null) {

                        for (int i = 1; i <= 6; i++) {
                                if (temp[i] == 0)
                                        array[i - 1] = 0;
                        }

                }

                score += fiveArray[0];
                score += fourArray[0];
                score += threeArray[0];
                //score += twoArray[0];

                int scoreOfRemainders = scoreRemainders(array);

                if (scoreOfRemainders > 0)
                        score *= -1;

                score += scoreOfRemainders;

                return score;

        }

        private final static int[] isFiveOfAKind(int[] array, Context c, int length) {

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(c);

                String fiveKindPref = prefs.getString("fiveOfAKindPref", "3x the 3 Of A Kind Value");
                int fiveOfAKindMult = (fiveKindPref.contains("2000")) ? 2000 : Integer
                        .valueOf("" + fiveKindPref.charAt(0));

                int score = 0;

                if (array[0] == 5) {
                        score = 1000 * fiveOfAKindMult;
                        array[0] = 0;
                }
                if (array[1] == 5) {
                        score = 200 * fiveOfAKindMult;
                        array[1] = 0;
                }
                if (array[2] == 5) {
                        score = 300 * fiveOfAKindMult;
                        array[2] = 0;
                }
                if (array[3] == 5) {
                        score = 400 * fiveOfAKindMult;
                        array[3] = 0;
                }
                if (array[4] == 5) {
                        score = 500 * fiveOfAKindMult;
                        array[4] = 0;
                }
                if (array[5] == 5) {
                        score = 600 * fiveOfAKindMult;
                        array[5] = 0;
                }

                if (score != 0) {

                        if (fiveOfAKindMult > 100)
                                score = fiveOfAKindMult;
                        else if (length > 5)
                                score *= -1;
                }

                int[] returnArray = { score, array[0], array[1], array[2], array[3],
                        array[4], array[5] };

                return returnArray;

        }

        private final static int[] isFourOfAKind(int[] array, Context c, int length) {

                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(c);

                String fourKindPref = prefs.getString("fourOfAKindPref", "2x the 3 Of A Kind Value");
                int fourOfAKindMult = (fourKindPref.contains("1000")) ? 1000 : Integer
                        .valueOf("" + fourKindPref.charAt(0));

                int score = 0;

                if (array[0] == 4) {
                        score = 1000 * fourOfAKindMult;
                        array[0] = 0;
                }
                if (array[1] == 4) {
                        score = 200 * fourOfAKindMult;
                        array[1] = 0;
                }
                if (array[2] == 4) {
                        score = 300 * fourOfAKindMult;
                        array[2] = 0;
                }
                if (array[3] == 4) {
                        score = 400 * fourOfAKindMult;
                        array[3] = 0;
                }
                if (array[4] == 4) {
                        score = 500 * fourOfAKindMult;
                        array[4] = 0;
                }
                if (array[5] == 4) {
                        score = 600 * fourOfAKindMult;
                        array[5] = 0;
                }

                if (score != 0) {

                        if (fourOfAKindMult > 100)
                                score = fourOfAKindMult;
                        else if (length > 4)
                                score *= -1;
                }

                int[] returnArray = { score, array[0], array[1], array[2], array[3],
                        array[4], array[5] };

                return returnArray;
        }

        private final static int[] isThreeOfAKind(int[] array, Context c, int length) {
                int score = 0;


                if (array[0] == 3) {
                        score += 1000;
                        array[0] = 0;
                }
                if (array[1] == 3) {
                        score += 200;
                        array[1] = 0;
                }
                if (array[2] == 3) {
                        score += 300;
                        array[2] = 0;
                }
                if (array[3] == 3) {
                        score += 400;
                        array[3] = 0;
                }
                if (array[4] == 3) {
                        score += 500;
                        array[4] = 0;
                }
                if (array[5] == 3) {
                        score += 600;
                        array[5] = 0;
                }

                if (score != 0 && length > 3) {
                        score *= -1;
                }

                int[] returnArray = { score, array[0], array[1], array[2], array[3],
                        array[4], array[5] };

                return returnArray;

        }

        private final static int scoreRemainders(int[] array) {

                boolean thereAreExtra = (array[1] != 0 || array[2] != 0
                        || array[3] != 0 || array[5] != 0);

                int score = 0;
                if (thereAreExtra) {
                        score += array[0] * 100;
                        score += array[4] * 50;
                } else {
                        score += array[0] * -100;
                        score += array[4] * -50;
                }

                return score;
        }

}