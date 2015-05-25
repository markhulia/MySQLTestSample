package com.example.mysqltest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by markhulia on 25/05/15.
 */
public class BackendMagic extends Activity {
    public static final String TAG_ITEM_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_LOCATION = "item_location";
    private static final String TAG_ITEM_QUANTITY = "item_quantity";
    private static final String TAG_ITEM_INFO = "item_info";
    private static final String TAG_ITEM_COMMENT = "comment";
    private static final String TAG_ITEMS_REPORT = "items_report";
    private static final String TAG_ROW_NUMBER = "rowNr";
    JSONParser jsonParser = new JSONParser();
    private String LOC = " BackendMagic";
    private String ITEM_NUMBER_URL = Globals.URL + "nextItem.php";
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            //post to server current row number to retreive a corresponding row
            //  mItemList = new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("rowNr", String.valueOf(Globals.getItemRowNumber())));
            jsonParser.makeHttpRequest(
                    ITEM_NUMBER_URL, "POST", params);
            Log.d(LOC, "Succeeded to post");

//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d(LOC, " doInBackgroun :cant Post");
//        }
            // JSONParser jParser = new JSONParser();
            JSONObject json = jsonParser.getJSONFromUrl(ITEM_NUMBER_URL);
            // String rowNr = String.valueOf(Globals.getItemRowNumber());

            //remove this line. it was in try{} above
            //  mItemList = new ArrayList<HashMap<String, String>>();


            //TODO add PHP checker, to check if DB has next line. If false, show report


//        try {
            mList = json.getJSONArray(TAG_ITEMS_REPORT);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(TAG_ITEM_QUANTITY, "999");
            mItemList.add(map);

            //Retrieve all elements of 1st object. '0' points to the 1st element
            JSONObject c = mList.getJSONObject(0);
            Globals.setItemName(c.getString(TAG_ITEM_NAME));
            Globals.setItemQuantity(Integer.parseInt(c.getString(TAG_ITEM_QUANTITY)));
            Globals.setItemRowNumber(Integer.parseInt(c.getString(TAG_ROW_NUMBER)));
            Globals.setItemLocation(c.getString(TAG_ITEM_LOCATION));
            Globals.setItemComment(c.getString(TAG_ITEM_COMMENT));

//
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i = Globals.getItemRowNumber();
        i++;
        Globals.setItemRowNumber(i);

    }

}
//            Log.d("Before ", c.getString(TAG_ITEM_ID));
//            Globals.setItemId(c.getString(TAG_ITEM_ID));
//            Log.d("After ", Globals.getItemId());
//
//            Log.d("Before ", c.getString(TAG_ITEM_NAME));
//            Globals.setItemName(c.getString(TAG_ITEM_NAME));
//            Log.d("After ", Globals.getItemName());
//
//            Log.d("Before ", c.getString(TAG_ITEM_QUANTITY));
//            Globals.setItemQuantity(Integer.parseInt(c.getString(TAG_ITEM_QUANTITY)));
//            Log.d("After ", String.valueOf(Globals.getItemQuantity()));
//
//            Log.d("Before ", c.getString(TAG_ITEM_LOCATION));
//            Globals.setItemLocation(c.getString(TAG_ITEM_LOCATION));
//            Log.d("After ", Globals.getItemLocation());
//
//            Log.d("Before ", c.getString(TAG_ITEM_INFO));
//            Globals.setItemIfno(c.getString(TAG_ITEM_INFO));
//            Log.d("After ", Globals.getItemIfno());
//
//            Log.d("Before ", c.getString(TAG_ITEM_COMMENT));
//            Globals.setItemComment(c.getString(TAG_ITEM_COMMENT));
//            Log.d("After ", Globals.getItemComment());
//
//
