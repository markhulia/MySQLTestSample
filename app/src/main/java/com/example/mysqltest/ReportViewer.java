package com.example.mysqltest;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportViewer extends ListActivity {

    // JSON IDS:
    public static final String TAG_ITEM_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_LOCATION = "item_location";
    private static final String TAG_ITEM_QUANTITY = "item_quantity";
    private static final String TAG_ITEM_INFO = "item_info";
    private static final String TAG_ITEM_COMMENT = "comment";
    private static final String TAG_ITEMS_REPORT = "items_report";
    private String TAG = " ReportView ";
    private ProgressDialog pDialog;
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList;
    private String READ_COMMENTS_URL = URL.URL + "report.php";
    private String RESET_DATABASE = URL.URL + "resetReport.php";
    JSONParser jsonParser = new JSONParser();
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // note that use view_reportml instead of our single_item_view_view.xml
        setContentView(R.layout.view_report);
    }

    //Exit app only after "Back" button is clicked twice
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
            //reset bool variable after 2 seconds
        }, 2000);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loading the comments via AsyncTask
        new LoadReportItems().execute();
    }

    /**
     * Retrieves recent items data from the server.
     */
    public void updateJSONdata() {
        mItemList = new ArrayList<HashMap<String, String>>();
        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

        try {
            mList = json.getJSONArray(TAG_ITEMS_REPORT);
            for (int i = 0; i < mList.length(); i++) {
                JSONObject c = mList.getJSONObject(i);

                // gets the content of each tag
                String iName = c.getString(TAG_ITEM_NAME);
                String iInfo = c.getString(TAG_ITEM_INFO);
                String iQuantity = c.getString(TAG_ITEM_QUANTITY);
                String iLocation = c.getString(TAG_ITEM_LOCATION);
                String iComment = c.getString(TAG_ITEM_COMMENT);

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                map.put(TAG_ITEM_QUANTITY, iQuantity);
                map.put(TAG_ITEM_NAME, iName);
                map.put(TAG_ITEM_INFO, iInfo);
                map.put(TAG_ITEM_COMMENT, iComment);
                map.put(TAG_ITEM_LOCATION, iLocation);

                // adding HashList to ArrayList
                mItemList.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into the listview.
     */
    private void updateList() {
        final ListAdapter adapter = new SimpleAdapter(this, mItemList,
                R.layout.single_item_view,
                new String[]{TAG_ITEM_NAME, TAG_ITEM_INFO,
                        TAG_ITEM_QUANTITY, TAG_ITEM_COMMENT, TAG_ITEM_LOCATION},
                new int[]{R.id.singleItemView_itemName, R.id.singleItemView_ItemInfo,
                        R.id.login_username});

        setListAdapter(adapter);
        final ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {

                Toast.makeText(ReportViewer.this, "Position " + mItemList.get(position).get(TAG_ITEM_COMMENT), Toast.LENGTH_LONG).show();
                Log.d(" ReportView onItemClick", mItemList.get(position).get(TAG_ITEM_COMMENT));
            }
        });
    }


    public void resetReport(View v) {
        new updateReport().execute();
    }
    public class LoadReportItems extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportViewer.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
        }
    }

    class updateReport extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "On pre-execute");
        }

        @Override
        protected String doInBackground(String... args) {
            Log.d(TAG, " on In-Background");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ReportViewer.this);
            String post_username = sp.getString("username", "anon");

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", post_username));

                JSONObject json = jsonParser.makeHttpRequest(
                        RESET_DATABASE, "POST", params);
                return "success";
            } catch (Exception e) {
                Log.e(TAG, " crashed here");
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "on Post-Execute");
        }
    }
}
