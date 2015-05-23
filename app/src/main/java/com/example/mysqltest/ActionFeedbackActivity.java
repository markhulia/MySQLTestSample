package com.example.mysqltest;

/**
 * Created by markhulia on 17/05/15.
 */


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActionFeedbackActivity extends Activity {
    public static final String EXTRA_ACTION_FEEDBACK = "jorik";
    JSONParser jsonParser = new JSONParser();
    private String TAG = " Action Feedback ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_feedback);
        Toast.makeText(this, "ActionFeedbackActivity", Toast.LENGTH_LONG).show();
        //Go back to main activity without crashing the app



        Intent i = new Intent(ActionFeedbackActivity.this, NotificationBuilder.class);
        finish();
        startActivity(i);
    }



}