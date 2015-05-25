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
    private static final String TAG_ITEMS_REPORT = "items_report";
    private static final String ROW_NUMBER = "rowNr";
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

                Log.e(Loc, "JSON OBJECT: "+c);
                rowNumber = c.getString(ROW_NUMBER);
                Log.d(Loc, "ItemIdString "+ rowNumber);

                Intent intent = new Intent(GetFirstRow.this, NotificationBuilder.class);
                finish();
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "nothing";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int i = Integer.parseInt(rowNumber);
            Globals.setItemRowNumber(i);
            Log.d(Loc, "NUMBER"+ String.valueOf(i));
        }

    }

}
