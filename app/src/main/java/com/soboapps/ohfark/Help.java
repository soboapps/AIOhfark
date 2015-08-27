package com.soboapps.ohfark;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class Help extends Activity {

       // private int numOfPlayers = 0;
       // private boolean playersPrefChanged = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
    	    super.onCreate(savedInstanceState);
    	    setContentView(R.layout.help);
    	    WebView webview = (WebView) findViewById(R.id.webView1);
    	    webview.getSettings().setSupportZoom(true);
    	    webview.getSettings().setBuiltInZoomControls(true);
    	    webview.loadUrl("file:///android_asset/ohfarkhelp.html");

        }
}        
