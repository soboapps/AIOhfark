package com.soboapps.ohfark;

public class Player {

        private String name;

        private int score = 0;
        private int numOfFarkles = 0; //Consecutive Farkles
        private boolean hasHighestScore = false;
        private boolean isOriginalWinner = false;
        private int inRoundScore = 0;


        public Player(String name){
                this.name = name;
        }

        public String getName()
        {
                return name;
        }

        public int getScore()
        {
                return score;
        }

        public void setScore(int score) {
                this.score = score;
        }

        public void incrementScore(int amount){
                score += amount;
        }

        public int getInRoundScore() {
                return inRoundScore;
        }

        public void resetInRoundScore() {
                inRoundScore = 0;
        }

        public void incrementInRoundScore(int amount){
                inRoundScore += amount;
        }

        public void setInRoundScore(int inRoundScore) {
                this.inRoundScore = inRoundScore;

        }

        public int getNumOfFarkles(){
                return numOfFarkles;
        }

        public void resetNumOfFarkles() {
                numOfFarkles = 0;
        }

        public void incrementNumOfFarkles(){
                numOfFarkles++;
        }

        public boolean hasHighestScore() {
                return hasHighestScore;
        }

        public void setHasHighestScore(boolean hasHighestScore) {
                this.hasHighestScore = hasHighestScore;
        }

        public boolean isOriginalWinner() {
                return isOriginalWinner;
        }

        public void setOriginalWinner(boolean isOriginalWinner) {
                this.isOriginalWinner = isOriginalWinner;
        }

}