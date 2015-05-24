package com.example.mysqltest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by markhulia on 17/05/15.
 */
public class NotificationBuilder extends Activity {

    public static final int NOTIFICATION_ID = 1;
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
    private String ITEM_NUMBER_URL = URL.URL + "nextItem.php";
    private int numberOfPackages;
    private boolean doubleBackToExitPressedOnce = false;
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_item_caller);


        new getItemNumber().execute();
//        getItemNumber getN = new getItemNumber();
//        if(getN.getStatus()== AsyncTask.Status.FINISHED) {

    }


    private PendingIntent getConversationPendingIntent(String string, int requestCode) {
        Intent conversationIntent = new Intent(this, OptionFeedbackActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(OptionFeedbackActivity.class);
        taskStackBuilder.addNextIntent(conversationIntent);
        return taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent getActionFeedbackPendingIntent(String actionFeedback, int requestCode) {
        Intent actionFeedbackIntent = new Intent(this, ActionFeedbackActivity.class);
        actionFeedbackIntent.putExtra(ActionFeedbackActivity.EXTRA_ACTION_FEEDBACK, actionFeedback);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this)
                .addParentStack(ActionFeedbackActivity.class)
                .addNextIntent(actionFeedbackIntent);
        return taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    //Scale image to the size required by notification
    private Bitmap getScaledLargeIconFromResource(int resource) {
        Resources res = getResources();
        int height = (int) res.getDimension(android.R.dimen.notification_large_icon_height);
        int width = (int) res.getDimension(android.R.dimen.notification_large_icon_width);
        Bitmap largeIcon = BitmapFactory.decodeResource(res, resource);
        return Bitmap.createScaledBitmap(largeIcon, width, height, false);
    }


    //Pending activity passes the context of the app. On wearable,
    // it adds "open Application" action button
    @TargetApi(20)
    public void onNextItemClick(View view) {

        Toast.makeText(this, "onNextItemClick", Toast.LENGTH_LONG).show();
    }

    public void onUpdateButtonClick(View view) {
        String amount = updateQty.getText().toString();
        //trim().isEmpty() ignores whitespaces

        //add flag. if user updated amount, it will change flag from 0 to 1. If user
        //wants to update amount agian before pressing "next item" POP-up window should display info
        //that this item_id has already been updated and if user wants to proceed
        Toast.makeText(this, "onUpdateButtonClick", Toast.LENGTH_LONG).show();

        if (FLAG == false) {
            if (amount != null && !amount.trim().isEmpty()) {
                numberOfPackages = Integer.parseInt(amount);
                Toast.makeText(this, "Items " + numberOfPackages, Toast.LENGTH_LONG).show();
                updateQty.getText().clear();
                FLAG = true;
            }
        } else {
            //TODO: create separate confirmation dialog class
            //call confirmation dialog
            //OPTION_YES -> update itemQTY, set FLAG = 1;
            //OPTION NO -> return to parent activity
        }
    }

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

    public class getItemNumber extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("getItemNumber", "On pre-execute");
            itemTitle = (TextView) findViewById(R.id.showItemName);
            itemLocationTV = (TextView) findViewById(R.id.showItemLoc);
            itemQuantityTV = (TextView) findViewById(R.id.showItemQty);

        }

        @Override
        protected String doInBackground(String... args) {
            Log.d("getItemNumber", " on In-Background");
            mItemList = new ArrayList<HashMap<String, String>>();
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(ITEM_NUMBER_URL);
            int rowNrInt = 1;
            String rowNr = String.valueOf(rowNrInt);
            try {
                mList = json.getJSONArray(TAG_ITEMS_REPORT);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_ITEM_QUANTITY, rowNr);
                mItemList.add(map);
                // PAY ATTENTION TO i < 2 ==========================================================
              //  for (int i = 0; i < json.length(); i++) {
                    JSONObject c = mList.getJSONObject(0);


                    // gets the content of each tag
                    final String itemIdString = c.getString(TAG_ITEM_ID);
                    final String itemName = c.getString(TAG_ITEM_NAME);
                    final String itemQuantityString = c.getString(TAG_ITEM_QUANTITY);
                    final String itemLocation = c.getString(TAG_ITEM_LOCATION);
                    String itemInfo = c.getString(TAG_ITEM_INFO);
                    Log.d(" Item_ID ", itemIdString);
                    Log.d(" name ", itemName);
                    Log.d(" quantity ", itemQuantityString);
                    // creating new HashMapHashMap<String, String> map = new HashMap<>();
                    // adding HashList to ArrayList



                    Log.d("Before invoke ", "---------------------------- " + itemName);
                    //retreive row from DB
                    Log.d("After invoke ", "---------------------------- " + itemName);
                    //TODO add PHP checker, to check if DB has next line. If false, show report


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemTitle.setText(itemName);
                            itemLocationTV.setText(itemLocation);
                            itemQuantityTV.setText(itemQuantityString);
                            updateQty = (EditText) findViewById(R.id.number_of_packages);
                        }
                    });


                    // Intent replyIntent = new Intent(this, showItemLoc.class);
                    String[] choices = NumberGenerator.getNumbers();
                    RemoteInput remoteInput = new RemoteInput.Builder(OptionFeedbackActivity.EXTRA_VOICE_REPLY)
                            .setLabel("Reply")
                            .setChoices(choices)
                                    //Set false if voice input option should be excluded
                            .setAllowFreeFormInput(true)
                            .build();

                    PendingIntent confirmActionPendingIntent =
                            getActionFeedbackPendingIntent("confirmation dawg", 0);

                    PendingIntent replyPendingIntent = getConversationPendingIntent("reply dawg", 1);

                    NotificationCompat.Action confirmAction = new NotificationCompat.Action(
                            R.drawable.ic_ok, "Confirm",
                            confirmActionPendingIntent);

                    NotificationCompat.Action replyAction =
                            new NotificationCompat.Action.Builder(R.drawable.ic_add, TAG_ITEM_QUANTITY, replyPendingIntent)
                                    .addRemoteInput(remoteInput)
                                    .build();

                    NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                            .addAction(confirmAction)
                            .addAction(replyAction);

                    Bitmap prettyAvatar = getScaledLargeIconFromResource(R.drawable.ic_light);

                    Notification notification = new NotificationCompat.Builder(NotificationBuilder.this)
                            .setContentTitle(itemName)
                            .setContentText(itemQuantityString)
                            .setSmallIcon(R.drawable.ic_task)
                            .setContentIntent(getConversationPendingIntent("qty", 20))
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setLargeIcon(prettyAvatar)
                            .extend(wearableExtender)
                            .build();

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationBuilder.this);
                    notificationManager.notify(NOTIFICATION_ID, notification);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success33";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("getItemNumber", "on Post-Execute");
        }
    }


}