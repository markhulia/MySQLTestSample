package com.example.mysqltest;

/**
 * Created by markhulia on 17/05/15.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActionFeedbackActivity extends Activity {
    public static final String EXTRA_ACTION_FEEDBACK = "jorik";
    private static final String RANDOM_CRAP = Globals.URL + "randomCrap.php";
    JSONParser jsonParser = new JSONParser();
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    Globals globals = new Globals();
    String Loc = " ActionFeedbackActivity";
    JSONParser jParser = new JSONParser();
    private String TAG = " Action Feedback ";
    private String ITEM_NUMBER_URL = Globals.URL + "notifier.php";
    private String NEXT_ITEM_URL = Globals.URL + "nextItem.php";
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Loc, " onCreate");
        setContentView(R.layout.action_feedback);
        Toast.makeText(this, "ActionFeedbackActivity", Toast.LENGTH_LONG).show();
        new confirmPick().execute();
    }

    public class confirmPick extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            Log.d(Loc, " doInBackground");
            try {
                // Building Parameters
                params.add(new BasicNameValuePair("rowNr", String.valueOf(Globals.getItemRowNumber())));
                params.add(new BasicNameValuePair("picked", "1"));
                params.add(new BasicNameValuePair("item_quantity", String.valueOf(Globals.getItemQuantity())));

                //Posting parameters to php
                jsonParser.makeHttpRequest(
                        ITEM_NUMBER_URL, "POST", params);
                //in case of successful post, increment the row number by one.
                //In this case, the next "SELECT" query will pull most recent row
                int rn = Globals.getItemRowNumber();
                rn++;
                Globals.setItemRowNumber(rn);

            } catch (Exception e) {
                Log.e(TAG, " crashed here");
                e.printStackTrace();
            }
            try {
                params.add(new BasicNameValuePair("rowNr", String.valueOf(Globals.getItemRowNumber())));
                //Posting parameters to php
                jsonParser.makeHttpRequest(
                        NEXT_ITEM_URL, "POST", params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject json = jParser.getJSONFromUrl(RANDOM_CRAP);

            try {
                mList = json.getJSONArray(Globals.TAG_ITEMS_REPORT);

                Log.e(Loc, "Inside JSON: " + mList);
                JSONObject c = mList.getJSONObject(0);
                Globals.setItemName(c.getString(Globals.TAG_ITEM_NAME));
                Globals.setItemQuantity(Integer.parseInt(c.getString(Globals.TAG_ITEM_QUANTITY)));
                Globals.setItemRowNumber(Integer.parseInt(c.getString(Globals.TAG_ROW_NUMBER)));
                Globals.setItemLocation(c.getString(Globals.TAG_ITEM_LOCATION));
                Globals.setItemInfo(c.getString(Globals.TAG_ITEM_INFO));
                Globals.setItemComment(c.getString(Globals.TAG_ITEM_COMMENT));


                Log.d("Before ", String.valueOf(Globals.getItemRowNumber()));
//                Globals.setItemId(c.getString(TAG_ITEM_ID));
//                Log.d("After ", Globals.getItemId());

                Log.d("Before ", Globals.getItemName());
//                Globals.setItemName(c.getString(TAG_ITEM_NAME));
//                Log.d("After ", Globals.getItemName());

                Log.d("Before ", String.valueOf(Globals.getItemQuantity()));
//                Globals.setItemQuantity(Integer.parseInt(c.getString(TAG_ITEM_QUANTITY)));
//                Log.d("After ", String.valueOf(Globals.getItemQuantity()));

                Log.d("Before ", Globals.getItemLocation());
//                Globals.setItemLocation(c.getString(TAG_ITEM_LOCATION));
//                Log.d("After ", Globals.getItemLocation());

                Log.d("Before ", Globals.getItemInfo());
//                Globals.setItemInfo(c.getString(TAG_ITEM_INFO));
//                Log.d("After ", Globals.getItemInfo());

                Log.d("Before ", Globals.getItemComment());
//                Globals.setItemComment(c.getString(TAG_ITEM_COMMENT));
//                Log.d("After ", Globals.getItemComment());


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(Loc, " onPostExecute");
            Log.d(Loc, "i onPostExecute NUMBER " + String.valueOf(Globals.getItemRowNumber()));
            //update next row number

            //if json object is empty, start report atctivity, else, buid another notification
            if (mList == null) {
                Intent intent = new Intent(ActionFeedbackActivity.this, ReportViewer.class);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(ActionFeedbackActivity.this, NotificationBuilder.class);
                finish();
                startActivity(intent);
            }
        }
    }

}