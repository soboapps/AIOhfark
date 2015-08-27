package com.soboapps.ohfark;

public interface GameEventsListener {
        
        void onGameStart();
        void onRoundStart(String playerUp);
        void onDiceRolled(String playerName);
        void onFarkleRolled(String playerName);
        void onPlayerScored(String playerName);
        void onDiceClicked(String playerName);
        void onRoundEnd(String playerDone, String playerUp);
        void onPlayerWon(String playerWon);
        void onGameEnd(String winner);

}