package com.soboapps.ohfark;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.widget.Toast;

public class OhFarkActivity extends Activity implements AnimationEndListener {

    private Resources r;
    private TextView scoreBox;
    private TextView dieScore;
    private TextView winnerTextView;

    private TextView numFarkles;
    private Button scoreButton;
    private Button rollButton;
    private Button finalButton;
    private Button winnerButton;
    LinearLayout finalRoundLayout;
    LinearLayout winnerLayout;

    private GameController controller;
    //private Ai_Decision AiD;
    private ArrayList<MyImageView> imageViews = new ArrayList<MyImageView>();
    private int[] letters = new int[6];
    private boolean toFarkle = false;
    private int animationCount = 0;

    public Handler handler;

    private RateMyApp rate;

    private Player currPlayer;
    private Player lastPlayer;


    //  Shaker was annoying, but I left code here in case someone wants it.
    //private Shaker shaker=null;

    // Makes new images appear after every button click
    //@SuppressWarnings("deprecation")
    //@SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));

        //Initialize the RateMyApp component
        //set the title, days till the user is prompted and the no. of launches till the user is prompted
        rate = new RateMyApp(this, getString(R.string.app_name), 7, 2);
        //make all text white
        rate.setTextColor(Color.WHITE);
        //set message
        rate.setMessage(getString(R.string.dialog_message));
        //set text size
        rate.setTextSize(16);
        rate.start();



        //Hide the App Icon and Title for a cleaner look
        //if(Build.VERSION.SDK_INT >= 14){
        //	getActionBar().setDisplayShowHomeEnabled(false);
        //	getActionBar().setDisplayShowTitleEnabled(false);
        //}

        setContentView(R.layout.main);

        //Hide the ActionBar and System Notifications
        //hideSystemUI();

        //Create, Initialize and then load the Sound manager and Volume Control
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        SoundManager.getInstance();
        SoundManager.initSounds(this);
        SoundManager.loadSounds();

        r = getResources();
        initializeArrays();
        scoreBox = (TextView) findViewById(R.id.score_box);
        numFarkles = (TextView) findViewById(R.id.num_farkles);
        dieScore = (TextView) findViewById(R.id.die_score);
        scoreButton = (Button) findViewById(R.id.score_button);
        rollButton = (Button) findViewById(R.id.roll_button);
        finalButton = (Button) findViewById(R.id.finalRoundButton);
        finalRoundLayout = (LinearLayout) findViewById(R.id.finalRoundLayout);
        winnerButton = (Button) findViewById(R.id.winnerButton);
        winnerLayout = (LinearLayout) findViewById(R.id.winnerLayout);
        winnerTextView = (TextView) findViewById(R.id.winnerButton);



        handler = new Handler();

        //  Shaker was annoying, but I left code here in case someone wants it.
        //SharedPreferences myPrefs=PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences shakerPref = getSharedPreferences("shakerPrefCheck", MODE_PRIVATE);
        //if (shakerPref.getBoolean("shakerPrefCheck", true)){
        //	shaker=new Shaker(this, 1.25d, 500, this);
        //}

        // Restore GameController

        if (getLastNonConfigurationInstance() != null) {
            controller = (GameController) getLastNonConfigurationInstance();
            controller.newUI(this);
            updateImages(false, false);
        } else {
            controller = new GameController(this);
            //AiD = new Ai_Decision();
        }

        // Restore UI elements

        if (savedInstanceState != null
                && savedInstanceState.getBoolean("hasState")) {

            scoreBox.setText(savedInstanceState.getCharSequence("scoreText"));
            numFarkles.setText(savedInstanceState.getCharSequence("numFarklesText"));
            dieScore.setText(savedInstanceState.getCharSequence("dieScoreText"));
            scoreButton.setText(savedInstanceState.getCharSequence("diePointsText"));
            scoreButton.setEnabled(savedInstanceState.getBoolean("scoreState"));
            rollButton.setEnabled(savedInstanceState.getBoolean("rollState"));
            finalButton.setEnabled(savedInstanceState.getBoolean("visibleState"));
            finalRoundLayout.setEnabled(savedInstanceState.getBoolean("visibleLState"));
            winnerButton.setEnabled(savedInstanceState.getBoolean("visibleState"));
            winnerButton.setText(savedInstanceState.getCharSequence("winnerText"));
            winnerLayout.setEnabled(savedInstanceState.getBoolean("visibleLState"));
        }
    }

    public void aiBgrd(){
        LinearLayout lL = (LinearLayout)findViewById(R.id.llParent);
        lL.setBackgroundColor(Color.parseColor("#455A64"));
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        //BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        //bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        //LinearLayout layout = (LinearLayout) findViewById(R.id.llParent);
        //layout.setBackgroundDrawable(bitmapDrawable);
    }

    public void defaultBgrd(){
        LinearLayout lL = (LinearLayout)findViewById(R.id.llParent);
        lL.setBackgroundColor(Color.parseColor("#263238"));
    }


	/* - May want to hide the ActionBar
	//Hide the ActionBar and System Notifications
	@SuppressLint("NewApi")
	private void hideSystemUI() {
		
		if(Build.VERSION.SDK_INT >= 14){
		
		View decorView = getWindow().getDecorView();
	    // Set the IMMERSIVE flag.
	    // Set the content to appear under the system bars so that the content
	    // doesn't resize when the system bars hide and show.
		decorView.setSystemUiVisibility(
	            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	            // View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	            | View.SYSTEM_UI_FLAG_IMMERSIVE);
		}
	}
	*/


    // These arrays allows the use of a for loop in updateImages()
    private void initializeArrays() {
        imageViews.add((MyImageView) findViewById(R.id.img_1));
        imageViews.add((MyImageView) findViewById(R.id.img_2));
        imageViews.add((MyImageView) findViewById(R.id.img_3));
        imageViews.add((MyImageView) findViewById(R.id.img_4));
        imageViews.add((MyImageView) findViewById(R.id.img_5));
        imageViews.add((MyImageView) findViewById(R.id.img_6));

        letters[0] = R.drawable.dief;
        letters[1] = R.drawable.diea;
        letters[2] = R.drawable.dier;
        letters[3] = R.drawable.diek;
        letters[4] = R.drawable.diel;
        letters[5] = R.drawable.diee;
        //hideSystemUI();

    }

    public void onRoll(View v) {

        controller.onRoll();
        // TODO Check to see if @currPlayer is AI

        //Hide the ActionBar and System Notifications... Again
        //hideSystemUI();
    }

    //Disable the Roll and Score Buttons
    //so the Human cannot cheat, sad huh?
    // TODO AI
    public void disableButtons(){
        rollButton.setClickable(false);
        scoreButton.setClickable(false);
    }


    //Enable the Roll and Score buttons after
    //the Machine has taken it's turn
    // TODO AI
    public void enableButtons(){
        rollButton.setClickable(true);
        scoreButton.setClickable(true);
    }


    //Disable the layout for all of the dice
    //so the Human cannot cheat, sad huh.
    // TODO AI
    public void disableDice(){
        LinearLayout dice1_3 = (LinearLayout) findViewById(R.id.die_1_3);
        for ( int i = 0; i < dice1_3.getChildCount();  i++ ){
            View view = dice1_3.getChildAt(i);
            view.setEnabled(false);
        }
        LinearLayout dice4_6 = (LinearLayout) findViewById(R.id.die_4_6);
        for ( int i = 0; i < dice4_6.getChildCount();  i++ ){
            View view = dice4_6.getChildAt(i);
            view.setEnabled(false);
        }
    }

    //Enable the Layout for all of the dice
    //for the Human to play
    // TODO AI
    public void enableDice(){
        LinearLayout dice1_3 = (LinearLayout) findViewById(R.id.die_1_3);
        for ( int i = 0; i < dice1_3.getChildCount();  i++ ){
            View view = dice1_3.getChildAt(i);
            view.setEnabled(true);
        }
        LinearLayout dice4_6 = (LinearLayout) findViewById(R.id.die_4_6);
        for ( int i = 0; i < dice4_6.getChildCount();  i++ ){
            View view = dice4_6.getChildAt(i);
            view.setEnabled(true);
        }
    }


    public void onScore(View v) {
        SharedPreferences myPrefs=PreferenceManager.getDefaultSharedPreferences(this);

        // Calculate Score
        controller.onScore();

        // Check to see if the AI is playing and Roll if it is.
        // Make sure the Game is not over.
        if (controller.isMachinePlayer()== true || controller.gameOverMan == false){
            controller.aiRoll();
            //AiD.aiRoll(this);
        }
        //   Using toast's to for work the logic
        //String strI = Integer.toString(possibleScore);

        //Flip the Canvas
        flipCanvas();
    }


    public void onClickDice(View v) {

        controller.onClickDice(UTILS.identifyView(v), toFarkle);

        //Hide the ActionBar and System Notifications... Again
        //hideSystemUI();
    }

    // Called from the GameController Object
    public void rollButtonState(boolean state) {
        rollButton.setEnabled(state);
    }

    public void updateRollButtonText(CharSequence charSequence) {
        rollButton.setText(charSequence);
    }

    // Called from the GameController Object
    public void scoreButtonState(boolean state) {
        scoreButton.setEnabled(state);
    }

    // Called from the GameController Object
    public void finalRoundButtonState(boolean state) {
        finalButton.setEnabled(state);
    }

    // Called from the GameController Object
    public void finalRoundLayout(boolean state) {
        finalButton.setEnabled(state);
    }

    // Called from the GameController Object
    public void winnerButtonState(boolean state) {
        winnerButton.setEnabled(state);
    }

    // Called from the GameController Object
    public void winnerLayout(boolean state) {
        winnerButton.setEnabled(state);
    }

    public void updateWinnerButtonText(CharSequence charSequence) {
        winnerButton.setText(charSequence);
    }

    public void updateScoreBox(CharSequence charSequence) {
        scoreBox.setText(charSequence);
    }

    public void updatenumOfFarkles(CharSequence charSequence) {
        numFarkles.setText(charSequence);
    }

    public void updateCurrentScore(CharSequence charSequence) {
        dieScore.setText(charSequence);
    }

    public void updatepPoints(CharSequence charSequence) {
        scoreButton.setText(charSequence);
    }

    public void updateImages(boolean toAnimate, boolean isFarkle) {
        // This is used in onAnimationEnd() to determine when all animations
        // have ended. Since all the animations are starting when this method is
        // called the count is 0
        animationCount = 0;

        MyImageView i = null;
        for (int j = 0; j < imageViews.size(); j++) {

            Animation a = AnimationUtils.loadAnimation(this, R.anim.dice_animation);

            i = imageViews.get(j);

            // If were gonna animate we need to know when the animation ends.
            // Hence were gonna attach this instance as a listener so
            // onAnimationEnd() gets called when animations finish.
            if (toAnimate) {
                i.setAnimationEndListerner(this);
            }

            // Get the right image from the Resources. *See in DiceManager
            // getImage()
            i.setImageDrawable(r.getDrawable(controller.getImage(j)));
            if (toAnimate && controller.shouldAnimate(j)) {
                i.clearAnimation();
                i.startAnimation(a);
            }

        }

        // Used in onAnimatonEnds()
        if (isFarkle)
            toFarkle = true;


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("hasState", true);
        outState.putBoolean("scoreState", scoreButton.isEnabled());
        outState.putBoolean("rollState", rollButton.isEnabled());
        outState.putCharSequence("scoreText", scoreBox.getText());
        outState.putCharSequence("numFarklesText", numFarkles.getText());
        outState.putCharSequence("dieScoreText", dieScore.getText());
        outState.putCharSequence("winnerText", winnerTextView.getText() );

        super.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return controller;
    }

    public void showFarkle() {

        // Play Awww Sound
        SharedPreferences myPrefs=PreferenceManager.getDefaultSharedPreferences(this);
        if (myPrefs.getBoolean("soundPrefCheck", true)) {
            SoundManager.playSound(9, 1);  // Awww Sound

        }

        // Show Farkle Animation
        farkleAnimation();

        // Flip the Canvas
        flipCanvas();

    }

    private void farkleAnimation() {
        // Show Farkle Animation
        animationCount = 0;

        rollButton.setEnabled(false);
        scoreButton.setEnabled(false);

        MyImageView i = null;
        for (int j = 0; j < imageViews.size(); j++) {

            Animation a = AnimationUtils.loadAnimation(OhFarkActivity.this,
                    R.anim.dice_animation);
            a.setStartOffset(j * 200);
            i = imageViews.get(j);
            i.setImageDrawable(r.getDrawable(letters[j]));
            i.clearAnimation();
            i.startAnimation(a);

        }
        controller.clearTable();
        toFarkle = false;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                rollButton.setEnabled(true);
                controller.animationsEnded(true);
            }
        }, 200 * 5 + 750);

        // Check to see if the AI is playing and Roll if it is.
        // Make sure the Game is not over.
        if (controller.isMachinePlayer()== true || controller.gameOverMan == false){
            controller.aiRoll();
            //AiD.aiRoll(this);
        }

    }


    @Override
    public void onAnimationEnd(View v) {
        animationCount++;

        // NumOnTable = the dice on the table
        if (animationCount == controller.numOnTable() && toFarkle) {

            //Pause before showing the Farkle
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    showFarkle();
                }

            }, 250 * controller.numOnTable());



        } else {
            //No more animations so alert the controller.
            controller.animationsEnded(false);

        }

    }

    private void flipCanvas() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (myPrefs.getBoolean("player2IsHumanPref", true))  {

            if (myPrefs.getBoolean("screenPrefCheck", true)) {
                Flip180 llParent = (Flip180) this.findViewById(R.id.llParent);
                Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_dialog);
                llParent.startAnimation(rotateAnimation);
                llParent.rotate();
            }
        }
    }

    // Action Bar Menu
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater prefs = getMenuInflater();
        prefs.inflate(R.menu.prefs_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menuNewGame:
                startActivity(new Intent(OhFarkActivity.this, PlayerSetup.class));
                finish();
                return true;
            case R.id.menuOptions:
                startActivity(new Intent(OhFarkActivity.this, Options.class));
                return true;
            case R.id.menuHelp:
                startActivity(new Intent(OhFarkActivity.this, Help.class));
                return true;
            case R.id.menuAbout:
                startActivity(new Intent(OhFarkActivity.this, About.class));
                return true;
            case R.id.menuExit:
                finish();
        }
        return false;
    }

    //@Override
    //public void shakingStarted() {
    //	SharedPreferences shakerPref = getSharedPreferences("shakerPrefCheck", MODE_PRIVATE);
    //	// Check to see if the Roll button is Enabled
    //	if (shakerPref.getBoolean("shakerPrefCheck", true)){
    //	if (rollButton.isEnabled()) {
    //	}
    //}
    //}

    //@Override
    //public void shakingStopped() {
    //	SharedPreferences shakerPref = getSharedPreferences("shakerPrefCheck", MODE_PRIVATE);
    //	// Check to see if the Roll button is Enabled
    //	if (shakerPref.getBoolean("shakerPrefCheck", true)){
    //		if (rollButton.isEnabled()) {
    //
    //			// Roll the Dice
    //			controller.onRoll();
    //		}
    //	}
    //}

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Kills & force closes the app
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //if(shaker != null)
        //  shaker.close();
    }

}