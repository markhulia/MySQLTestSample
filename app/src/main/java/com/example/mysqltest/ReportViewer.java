package com.example.mysqltest;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportViewer extends ListActivity {

    // JSON IDS:
    private static final String TAG_ITEM_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_LOCATION = "item_location";
    private static final String TAG_ITEM_QUANTITY = "item_quantity";
    private static final String TAG_ITEM_INFO = "item_info";
    private static final String TAG_ITEM_COMMENT= "comment";
    private static final String TAG_ITEMS_REPORT = "items_report";
    private ProgressDialog pDialog;
    // An array of all of items
    private JSONArray mList = null;
    // manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mItemList;
    private String READ_COMMENTS_URL = URL.URL + "report.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // note that use view_reportml instead of our single_item_view_view.xml
        setContentView(R.layout.view_report);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // loading the comments via AsyncTask
        new LoadReportItems().execute();
    }

    public void addComment(View v) {
        Intent i = new Intent(ReportViewer.this, ReportUpdater.class);
        startActivity(i);
    }

    /**
     * Retrieves recent items data from the server.
     */
    public void updateJSONdata() {

        // Instantiate the arraylist to contain all the JSON data.
        // we are going to use a bunch of key-value pairs, referring
        // to the json element name, and the content, for example,
        // message it the tag, and "I'm awesome" as the content..

        mItemList = new ArrayList<HashMap<String, String>>();

        // Bro, it's time to power up the J parser
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        // back a JSON object. Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

        // when parsing JSON stuff, we should probably
        // try to catch any exceptions:
        try {
            // mList will tell us how many "items" in the list are
            // available
            mList = json.getJSONArray(TAG_ITEMS_REPORT);

            // looping through all items according to the json object returned
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

                //JSON data is updated with new arraylist
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the parsed data into the listview.
     */
    private void updateList() {
        // For a ListActivity we need to set the List Adapter, and in order to do
        //that, we need to create a ListAdapter.  This SimpleAdapter,
        //will utilize our updated Hashmapped ArrayList,
        //use our single_item_view xml template for each item in our list,
        //and place the appropriate info from the list to the
        //correct GUI id.  Order is important here.
        ListAdapter adapter = new SimpleAdapter(this, mItemList,
                R.layout.single_item_view, new String[]{TAG_ITEM_NAME, TAG_ITEM_INFO,
                TAG_ITEM_QUANTITY}, new int[]{R.id.updateSingleItemQty, R.id.description,
                R.id.username});

        setListAdapter(adapter);
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // This method is triggered if an item is click within our
                // list. Should implement this to edit database elements

            }
        });
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
}
