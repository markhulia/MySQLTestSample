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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActionFeedbackActivity extends Activity {
    public static final String EXTRA_ACTION_FEEDBACK = "jorik";
    JSONParser jsonParser = new JSONParser();
    TextView itemTitle, itemLocationTV, itemQuantityTV;
    EditText updateQty;
    private String TAG = " Action Feedback ";
    private String ITEM_NUMBER_URL = Globals.URL + "notifier.php";
    private int numberOfPackages;
    private boolean doubleBackToExitPressedOnce = false;
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList;
    Globals globals = new Globals();
    String  Loc = " ACtionFeedbackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(Loc, " onCreate");

        setContentView(R.layout.action_feedback);
        Toast.makeText(this, "ActionFeedbackActivity", Toast.LENGTH_LONG).show();
        //Go back to main activity without crashing the app

        new confirmPick().execute();

        Intent i = new Intent(ActionFeedbackActivity.this, NotificationBuilder.class);
        finish();
        startActivity(i);
    }

    public class confirmPick extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d(Loc, " doInBackground");
       // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ActionFeedbackActivity.this);
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("rowNr", String.valueOf(globals.getItemRowNumber())));
                params.add(new BasicNameValuePair("picked", "1"));

                jsonParser.makeHttpRequest(
                        ITEM_NUMBER_URL, "POST", params);
                return "success";
            } catch (Exception e) {
                Log.e(TAG, " crashed here");
                e.printStackTrace();
            }


            return null;
        }
    }

}