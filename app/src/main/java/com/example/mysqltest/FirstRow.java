package com.example.mysqltest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by markhulia on 24/05/15.
 * This class initializes the first row from the
 * database where the item status is "not picked".
 * It is called after Login
 */
public class FirstRow extends Activity {


    private static final String GET_FIRST_ROW_URL = Globals.URL + "getFirstRow.php";
    private static final String RANDOM_CRAP = Globals.URL + "randomCrap.php";
    private static final String TAG_ROW_NUMBER = "rowNr";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_LOCATION = "item_location";
    private static final String TAG_ITEM_QUANTITY = "item_quantity";
    private static final String TAG_ITEM_INFO = "item_info";
    private static final String TAG_ITEM_COMMENT = "comment";
    private static final String TAG_ITEMS_REPORT = "items_report";

    String Loc = " GetFirstRow";
    JSONParser jParser = new JSONParser();
    private JSONArray mList = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        Log.d(Loc, " onCreate");
    }

    public void onGetFirstRowClick(View view) {
        Log.d(Loc, " onGetFirstRowClick");
        new firstItem().execute();
    }

    class firstItem extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(Loc, " onPreExecute");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(Loc, " doInBackground");

            //create a JSON object which will pull the first item where "picked" = 0
            JSONObject json = jParser.getJSONFromUrl(RANDOM_CRAP);

            try {
                mList = json.getJSONArray(TAG_ITEMS_REPORT);
                JSONObject c = mList.getJSONObject(0);
                Globals.setItemName(c.getString(TAG_ITEM_NAME));
                Globals.setItemQuantity(Integer.parseInt(c.getString(TAG_ITEM_QUANTITY)));
                Globals.setItemRowNumber(Integer.parseInt(c.getString(TAG_ROW_NUMBER)));
                Globals.setItemLocation(c.getString(TAG_ITEM_LOCATION));
                Globals.setItemInfo(c.getString(TAG_ITEM_INFO));
                Globals.setItemComment(c.getString(TAG_ITEM_COMMENT));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "nothing";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(Loc, "NUMBER: " + String.valueOf(Globals.getItemRowNumber()));
            Log.e(Loc, "Name: " + Globals.getItemName());

            // If there are no items to show, report will be displayed instead
            if (mList == null) {
                Intent intent = new Intent(FirstRow.this, ReportViewer.class);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(FirstRow.this, NotificationBuilder.class);
                finish();
                startActivity(intent);
            }
        }

    }

}
