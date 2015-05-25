package com.example.mysqltest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

public class OptionFeedbackActivity extends Activity {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    String LOC = "OptionsFeedbackActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOC, " onCreate");
        setContentView(R.layout.confirmation_feedback);

        CharSequence replyText = getMessageText(getIntent());
        if (replyText != null) {
            int foo = Integer.parseInt(replyText.toString());
            Toast.makeText(this, "OptionFeedbackActivity " + foo, Toast.LENGTH_LONG).show();

//            try{
//
//            }
//            catch (Exception e){
//                e.printStackTrace();
//            }
        }



        //Go back to main activity without crashing the app
        Intent i = new Intent(OptionFeedbackActivity.this, NotificationBuilder.class);
        finish();
        startActivity(i);
    }



    // The getMessageText method shows hot to extract voice reply from Intent
    @TargetApi(20) //Suppressing compatibility errors between SDK18 adn SDK20
    private CharSequence getMessageText(Intent intent) {
        Log.d(LOC, " CharSequence getMessageText");
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
        }
        return null;
    }

}