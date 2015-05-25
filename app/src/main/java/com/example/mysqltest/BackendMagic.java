package com.example.mysqltest;

import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by markhulia on 25/05/15.
 */
public class BackendMagic {
    public static final String TAG_ITEM_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_LOCATION = "item_location";
    private static final String TAG_ITEM_QUANTITY = "item_quantity";
    private static final String TAG_ITEM_INFO = "item_info";
    private static final String TAG_ITEM_COMMENT = "comment";
    private static final String TAG_ITEMS_REPORT = "items_report";
    private static boolean FLAG = false;
    TextView itemTitle, itemLocationTV, itemQuantityTV;
    EditText updateQty;
    String LOC = " NotificationBuilder";
    JSONParser jsonParser = new JSONParser();
    private String ITEM_NUMBER_URL = Globals.URL + "nextItem.php";
    private int numberOfPackages;
    private boolean doubleBackToExitPressedOnce = false;
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();





}
