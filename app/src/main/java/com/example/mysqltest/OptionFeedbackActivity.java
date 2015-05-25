package com.example.mysqltest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class OptionFeedbackActivity extends Activity {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    String LOC = "OptionsFeedbackActivity";
    Globals globals = new Globals();
    JSONParser jsonParser = new JSONParser();
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    private String ITEM_NUMBER_URL = Globals.URL + "notifier.php";
    private String numberOfItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOC, " onCreate");
        setContentView(R.layout.confirmation_feedback);

        CharSequence replyText = getMessageText(getIntent());
        //int foo is number of packages

        // int foo = Integer.parseInt(replyText.toString());
        numberOfItems = replyText.toString();
        Toast.makeText(this, "OptionFeedbackActivity " + replyText, Toast.LENGTH_LONG).show();

        new updateAmount().execute();


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

        Log.d(LOC, remoteInput.getCharSequence(EXTRA_VOICE_REPLY).toString());

        if (remoteInput != null) {
            return remoteInput.getCharSequence(EXTRA_VOICE_REPLY);
        }
        return null;
    }


    public class updateAmount extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            //add condition if quantity is 0, set picked to 0!!!!


            params.add(new BasicNameValuePair("picked", "1"));
            params.add(new BasicNameValuePair("rowNr", String.valueOf(globals.getItemRowNumber())));
            params.add(new BasicNameValuePair("item_quantity", numberOfItems));

            //Posting parameters to php
            jsonParser.makeHttpRequest(
                    ITEM_NUMBER_URL, "POST", params);

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOC, " onPostExecute");
            int i = Globals.getItemRowNumber();
            i++;
            Globals.setItemRowNumber(i);
        }

    }

}