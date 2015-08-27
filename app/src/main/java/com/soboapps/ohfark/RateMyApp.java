package com.soboapps.ohfark;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class RateMyApp {

    //application title
    private String appTitle;

    //days till the dialog is shown
    private int daysToPrompt;

    //launches till the dialog is shown
    private int launchesToPrompt;

    //message to display
    private String message;

    //text color
    private int textColor;

    //text size
    private int textSize;

    private AlertDialog dialog;

    private SharedPreferences prefs;


    private Context ctx;

    /**
     * Constructor. It initializes all the needed variables.
     *
     * @param ctx the context
     * @param appTitle the application title. Will be shown in dialog title
     * @param daysToPrompt no of days that must pass in order to prompt the user.
     * @param launchesToPrompt no of app launches that must pass in order to prompt the user.
     */
    public RateMyApp(Context ctx, String appTitle, int daysToPrompt, int launchesToPrompt){

        this.ctx = ctx;
        this.appTitle = appTitle;
        this.daysToPrompt = daysToPrompt;
        this.launchesToPrompt = launchesToPrompt;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

    }

    public void start(){

        //if the "not show again" check box was checked => return and don't show anything
        if(prefs.getBoolean("not_show_again", false)){

            return;
        }

        //first launch date
        long firstLaunch = prefs.getLong("first_launch", -1);
        if(firstLaunch == -1){

            firstLaunch = System.currentTimeMillis();
            prefs.edit().putLong("first_launch", firstLaunch).commit();
        }

        //launch counter
        int launchCounter = prefs.getInt("launch_counter", 0) +1;
        prefs.edit().putInt("launch_counter", launchCounter).commit();

        //verify to show dialog
        if(launchCounter >= launchesToPrompt){

            if(System.currentTimeMillis() >= firstLaunch + (daysToPrompt * 24 * 60 * 60 * 1000)){

                //show the dialog
                showDialog();

            }
        }

    }

    private void showDialog(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ctx);

        //inflate the dialog layout
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog,null);

        //get the visual elements from the inflated layouts
        TextView messageTv = (TextView)layout.findViewById(R.id.message);

        //if a text color has been set => assign it
        if(textColor!=0){
            messageTv.setTextColor(textColor);
        }

        //if a text size has been set => assign it
        if(textSize != 0){
            messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }


        //if a custom message has been set => assign it
        if(message!=null){
            messageTv.setText(message);
        }

        alertBuilder.setView(layout);
        alertBuilder.setTitle(ctx.getString(R.string.dialog_message_rate) + " " + appTitle);
        alertBuilder.setIcon(android.R.drawable.btn_star_big_on );

        //on clicking the "Yes" button => go to app's Google Play page
        //and don't show again the dialog (assuming the user has rated the app)
        //alertBuilder.setPositiveButton("Yes", new OnClickListener() {
        alertBuilder.setPositiveButton(ctx.getString(R.string.dialog_message_yes), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {

                    prefs.edit().putBoolean("not_show_again", true).commit();
                    ctx.startActivity(myAppLinkToMarket);

                } catch (ActivityNotFoundException e) {

                    //the device hasn't installed Google Play
                    Toast.makeText(ctx, ctx.getString(R.string.dialog_message_no_gp), Toast.LENGTH_LONG).show();
                }

            }
        });

        //on the "Never" button Never ask again
        alertBuilder.setNeutralButton(ctx.getString(R.string.dialog_message_never), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                prefs.edit().putBoolean("not_show_again", true).commit();

            }
        });

        //on the "Not now" button postpone the showing of the dialog
        //that is restarting the counting and the days
        alertBuilder.setNegativeButton(ctx.getString(R.string.dialog_message_not_now), new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                prefs.edit().putInt("launch_counter", 0).commit();
                prefs.edit().putLong("first_launch", System.currentTimeMillis()).commit();
                dialog.dismiss();

            }
        });

        dialog = alertBuilder.create();
        dialog.show();

    }

    public String getMessage() {
        return message;
    }

    /**
     * Set a message to display inside the dialog.
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public int getTextColor() {
        return textColor;
    }

    /**
     * Set the color for the text inside the dialog. Default color is black.
     * @param textColor
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    /**
     * Set the text size of the text inside the dialog. The unit used is SP.
     * @param textSize text size in SP
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

}
