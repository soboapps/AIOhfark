package com.soboapps.ohfark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.View;

public final class UTILS {

        public static int identifyView(View v) {
                int imgIndex;

                switch (v.getId()) {
                case (R.id.img_1):
                        imgIndex = 0;
                        break;
                case (R.id.img_2):
                        imgIndex = 1;
                        break;
                case (R.id.img_3):
                        imgIndex = 2;
                        break;
                case (R.id.img_4):
                        imgIndex = 3;
                        break;
                case (R.id.img_5):
                        imgIndex = 4;
                        break;
                case (R.id.img_6):
                        imgIndex = 5;
                        break;
                default:
                        Log.w("DieManager.IdentifyView", "Could not identify view");
                        return -1;
                }

                return imgIndex;
        }

        public static CharSequence setSpanBetweenTokens(CharSequence text,
                        String token, CharacterStyle... cs) {
                // Start and end refer to the points where the span will apply
                int tokenLen = token.length();
                int start = text.toString().indexOf(token) + tokenLen;
                int end = text.toString().indexOf(token, start);

                if (start > -1 && end > -1) {
                        // Copy the spannable string to a mutable spannable string
                        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                        for (CharacterStyle c : cs)
                                ssb.setSpan(c, start, end, 0);

                        // Delete the tokens before and after the span
                        ssb.delete(end, end + tokenLen);
                        ssb.delete(start - tokenLen, start);

                        text = ssb;
                }

                return text;
        }

        public static AlertDialog buildDialog(Context c, String title,
                        String posText, DialogInterface.OnClickListener posListener,
                        String negText, DialogInterface.OnClickListener negListener) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setCancelable(false);

                builder.setMessage(title);
                builder.setCancelable(true);

                builder.setNegativeButton(negText, negListener);

                builder.setPositiveButton(posText, posListener);
                return builder.create();
        }
}