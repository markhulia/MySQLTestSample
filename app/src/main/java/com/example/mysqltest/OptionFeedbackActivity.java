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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OptionFeedbackActivity extends Activity {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static final String RANDOM_CRAP = Globals.URL + "randomCrap.php";
    String LOC = "OptionsFeedbackActivity";
    Globals globals = new Globals();
    JSONParser jsonParser = new JSONParser();
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    String picked;
    String Loc = " ActionFeedbackActivity";
    JSONParser jParser = new JSONParser();
    private String numberOfItems;
    private String TAG = " Action Feedback ";
    private String ITEM_NUMBER_URL = Globals.URL + "notifier.php";
    private String NEXT_ITEM_URL = Globals.URL + "nextItem.php";
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList;

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

    public class updateAmount extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //If the number of items is 0, then picked is set to 0 ("not picked") by default
                if (!numberOfItems.equals("0")) {
                    picked = "1";
                } else {
                    picked = "0";
                }

                params.add(new BasicNameValuePair("picked", picked));
                params.add(new BasicNameValuePair("rowNr", String.valueOf(globals.getItemRowNumber())));
                params.add(new BasicNameValuePair("item_quantity", numberOfItems));
                params.add(new BasicNameValuePair("comment", Globals.getItemComment()));

                //Posting parameters to php
                jsonParser.makeHttpRequest(
                        ITEM_NUMBER_URL, "POST", params);

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

            JSONObject json = jParser.getJSONFromUrl(NEXT_ITEM_URL);

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
            Log.d(LOC, " onPostExecute");
            //if json object is empty, start report atctivity, else, buid another notification
            if (mList == null) {
                Intent intent = new Intent(OptionFeedbackActivity.this, ReportViewer.class);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(OptionFeedbackActivity.this, NotificationBuilder.class);
                finish();
                startActivity(intent);
            }
        }

    }

}