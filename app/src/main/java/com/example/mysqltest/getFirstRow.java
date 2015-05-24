package com.example.mysqltest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhulia on 24/05/15.
 */
public class GetFirstRow extends Activity {


    public static final String TAG_ITEM_ID = "item_id";
    private static final String GET_FIRST_ROW_URL = URL.URL + "randomCrap.php";
    private static final String TAG_ITEMS_REPORT = "items_report";
    String itemIdString;
    String TAG = "GetFirstRow";
    private JSONArray mList = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
    }

    public void onGetFirstRowClick(View view) {

        new firstItem().execute();
    }

    class firstItem extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("getItemNumber", "On pre-execute");
        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("rowNr", "5"));

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.makeHttpRequest(
                        GET_FIRST_ROW_URL, "POST", params);

                Log.d(TAG, jsonObject.toString());
            } catch (Exception e) {
                Log.d(TAG, "some exception");
            }

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(GET_FIRST_ROW_URL);


            try {


                mList = json.getJSONArray(TAG_ITEMS_REPORT);
                // PAY ATTENTION TO i < 2 ==========================================================
                JSONObject c = mList.getJSONObject(0);

                // gets the content of each tag
                itemIdString = c.getString(TAG_ITEM_ID);
                Log.d(" Item ID ", itemIdString);
                //Toast.makeText(GetFirstRow.this, "1st item id " + itemIdString, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(GetFirstRow.this, NotificationBuilder.class);
                finish();
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return itemIdString;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("getItemNumber", "on Post-Execute");
        }

    }

}
