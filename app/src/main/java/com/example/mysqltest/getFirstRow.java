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

import java.util.PriorityQueue;

/**
 * Created by markhulia on 24/05/15.
 */
public class GetFirstRow extends Activity {


    private static final String GET_FIRST_ROW_URL = Globals.URL + "getFirstRow.php";
    private static final String RANDOM_CRAP  = Globals.URL + "randomCrap.php";
    public static final String TAG_ITEM_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_LOCATION = "item_location";
    private static final String TAG_ITEM_QUANTITY = "item_quantity";
    private static final String TAG_ITEM_INFO = "item_info";
    private static final String TAG_ITEM_COMMENT = "comment";
    private static final String TAG_ITEMS_REPORT = "items_report";
    private static final String TAG_ROW_NUMBER = "rowNr";
    private JSONArray mList = null;
    String rowNumber;
    String Loc = " GetFirstRow";
    JSONParser jParser = new JSONParser();

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
            JSONObject json = jParser.getJSONFromUrl(RANDOM_CRAP);

            try {
                mList = json.getJSONArray(TAG_ITEMS_REPORT);
                JSONObject c = mList.getJSONObject(0);
                Globals.setItemName(c.getString(TAG_ITEM_NAME));
                Globals.setItemQuantity(Integer.parseInt(c.getString(TAG_ITEM_QUANTITY)));
                Globals.setItemRowNumber(Integer.parseInt(c.getString(TAG_ROW_NUMBER)));
                Globals.setItemLocation(c.getString(TAG_ITEM_LOCATION));
                Globals.setItemComment(c.getString(TAG_ITEM_COMMENT));
                Log.e(Loc, "JSON OBJECT: "+c);
                rowNumber = c.getString(TAG_ROW_NUMBER);
                Log.d(Loc, "ItemIdString "+ rowNumber);

                Intent intent = new Intent(GetFirstRow.this, NotificationBuilder.class);
                finish();
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Globals.setItemRowNumber(999);

            return "nothing";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            int i = Integer.parseInt(rowNumber);
//            i++;
//            Globals.setItemRowNumber(i);
            Log.d(Loc, "NUMBER"+ String.valueOf(Globals.getItemRowNumber()));
        }

    }

}
