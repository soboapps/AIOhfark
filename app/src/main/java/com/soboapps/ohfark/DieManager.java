package com.soboapps.ohfark;

import java.util.HashMap;
import android.util.Log;

public class DieManager {

        // Will return the Indexes of the dice when this is used
        public static final int INDEX_FLAG = 1;
        // Will return the values of the dice when this is used
        public static final int VALUE_FLAG = 2;
        // Will return the absolute values of the dice when this is used
        public static final int ABS_VALUE_FLAG = 3;

        // The array that holds the dice
        private int[] diceValues = { 0, 0, 0, 0, 0, 0 };

        private HashMap<Integer, Integer> diceImages = new HashMap<Integer, Integer>();
        private HashMap<Integer, Integer> deadImages = new HashMap<Integer, Integer>();
        private HashMap<Integer, Integer> letterImages = new HashMap<Integer, Integer>();

        // Sets @newValue to dieValues[@index]
        public void setValue(int index, int newValue) {
                Log.w(getClass().getName(), "Index = " + index + " Value = " + newValue);
                diceValues[index] = newValue;
        }

        public DieManager() {
                super();
                initializeMaps();
        }

        private void initializeMaps() {

                // Shows the Green Selected Dice
                diceImages.put(-6, R.drawable.die6_select);
                diceImages.put(-5, R.drawable.die5_select);
                diceImages.put(-4, R.drawable.die4_select);
                diceImages.put(-3, R.drawable.die3_select);
                diceImages.put(-2, R.drawable.die2_select);
                diceImages.put(-1, R.drawable.die1_select);

                // Shows the Red Rolled Dice
                diceImages.put(1, R.drawable.die1_roll);
                diceImages.put(2, R.drawable.die2_roll);
                diceImages.put(3, R.drawable.die3_roll);
                diceImages.put(4, R.drawable.die4_roll);
                diceImages.put(5, R.drawable.die5_roll);
                diceImages.put(6, R.drawable.die6_roll);

                // I want it to show these grey dice once the dice are dead (Still working on it)
                deadImages.put(0, R.drawable.die1_dead);
                deadImages.put(1, R.drawable.die2_dead);
                deadImages.put(2, R.drawable.die3_dead);
                deadImages.put(3, R.drawable.die4_dead);
                deadImages.put(4, R.drawable.die5_dead);
                deadImages.put(5, R.drawable.die6_dead);

                // Shows a blank dice for all images
                letterImages.put(0, R.drawable.die_no);
                letterImages.put(1, R.drawable.die_no);
                letterImages.put(2, R.drawable.die_no);
                letterImages.put(3, R.drawable.die_no);
                letterImages.put(4, R.drawable.die_no);
                letterImages.put(5, R.drawable.die_no);

        }

        public void rollDice() {

                boolean isNewRound = (numOnTable() == 0);
                for (int j = 0; j < 6; j++) {

                        // If its a new round then the dice value can be changed from 0.
                        // Else it can't
                        if (isNewRound || diceValues[j] != 0)
                                diceValues[j] = (int) ((Math.random() * 6) + 1);
                        // Comment out the above line of code
                        // to Roll all the same dice (6, 3's) for testing
                        // Change (3) to what ever die you want to test with
                        // Example (1) will test with all 1's
                        //diceValues[j] = (int) ((3));



                }
        }

        // Returns the value or absolute value
        public int getValue(int index, int flag) {
                if (flag == ABS_VALUE_FLAG)
                        return Math.abs(diceValues[index]);

                return diceValues[index];
        }

        // If a dice value is 0 then its a letter
        public int numOnTable() {
                int count = 6;
                for (int i : diceValues) {
                        if (i == 0)
                                count--;
                }
                return count;
        }

        // Picking up makes the dice value 0
        public void pickUp(int[] highlighted) {

                for (int i = 0; i < highlighted.length; i++)
                        diceValues[highlighted[i]] = 0;

        }

        // A negative value means a die is highlighted
        public void toggleHighlight(int index) {
                diceValues[index] *= -1;
        }

        public void clearTable() {
                diceValues[0] = 0;
                diceValues[1] = 0;
                diceValues[2] = 0;
                diceValues[3] = 0;
                diceValues[4] = 0;
                diceValues[5] = 0;

        }

        // Return the dice that aren't 0
        public int[] diceOnTable(int flag) {
                if (flag == ABS_VALUE_FLAG) {
                        int[] array = new int[diceValues.length];

                        for (int i = 0; i < diceValues.length; i++)
                                array[i] = Math.abs(diceValues[i]);

                        return array;
                }

                return diceValues;
        }

        //Returns dice that are negative
        public int[] getHighlighted(int flag) {
                int[] dirtyArray = { 0, 0, 0, 0, 0, 0 };
                int count = 0;

                for (int j = 0; j < 6; j++) {
                        if (diceValues[j] < 0) {

                                if (flag == INDEX_FLAG)
                                        dirtyArray[count++] = j;

                                if (flag == VALUE_FLAG)
                                        dirtyArray[count++] = diceValues[j];

                                if (flag == ABS_VALUE_FLAG)
                                        dirtyArray[count++] = Math.abs(diceValues[j]);
                        }
                }

                int[] cleanArray = new int[count];

                //Returns an array of length 0
                if (count == 0)
                        return cleanArray;

                System.arraycopy(dirtyArray, 0, cleanArray, 0, count);
                return cleanArray;

        }

        // Finds in dieValues same @value and excludes @index
        public int[] findPairs(int index, int flag) {

                int[] dirtyArray = { 0, 0, 0, 0, 0, 0 };

                int count = 0;

                for (int j = 0; j < 6; j++) {

                        if (j != index
                                && Math.abs(diceValues[j]) == Math.abs(diceValues[index])) {

                                if (flag == INDEX_FLAG)
                                        dirtyArray[count++] = j;

                                if (flag == VALUE_FLAG)
                                        dirtyArray[count++] = diceValues[j];

                                if (flag == ABS_VALUE_FLAG)
                                        dirtyArray[count++] = Math.abs(diceValues[j]);
                        }

                }

                int[] cleanArray = new int[count];

                if (count == 0)
                        return cleanArray;

                System.arraycopy(dirtyArray, 0, cleanArray, 0, count);
                return cleanArray;
        }

        //Get the Dice Images
        public Integer getImage(int index) {
                if (diceValues[index] == 0) {
                        return letterImages.get(index);


                } else {
                        return diceImages.get((diceValues[index]));
                }
        }

        //Get the Number of dice remaining that are not 0
        public int numDiceRemain() {
                int counter = 0;
                for (int j = 0; j < diceValues.length; j++) {
                        if (diceValues[j] > 0)
                                counter++;
                }
                return counter;
        }
}