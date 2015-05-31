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
    private static final String RANDOM_CRAP = Globals.URL + "randomCrap.php";
    private static boolean FLAG = false;
    TextView itemTitle, itemLocationTV, itemQuantityTV;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    EditText updateQty;
    String numberOfItems;
    String picked;
    String LOC = " NotificationBuilder";
    JSONParser jsonParser = new JSONParser();
    private String NEXT_ITEM_URL = Globals.URL + "nextItem.php";
    private String NOTIFIER_URL = Globals.URL + "notifier.php";
    private String ITEM_NUMBER_URL = Globals.URL + "nextItem.php";
    private int numberOfPackages;
    private boolean doubleBackToExitPressedOnce = false;
    private JSONArray mList = null;
    private ArrayList<HashMap<String, String>> mItemList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_item_caller);

        itemTitle = (TextView) findViewById(R.id.showItemName);
        itemLocationTV = (TextView) findViewById(R.id.showItemLoc);
        itemQuantityTV = (TextView) findViewById(R.id.showItemQty);
        updateQty = (EditText) findViewById(R.id.number_of_packages);

        itemTitle.setText(Globals.getItemName());
        itemLocationTV.setText(Globals.getItemLocation());
        itemQuantityTV.setText(String.valueOf(Globals.getItemQuantity()));

        Log.d(LOC, " onCreate");
        Log.d(LOC, " value of global rowNumber: " + Globals.getItemRowNumber());


        String[] choices = NumberGenerator.getNumbers();
        RemoteInput remoteInput = new RemoteInput.Builder(OptionFeedbackActivity.EXTRA_VOICE_REPLY)
                .setLabel("Reply")
                .setChoices(choices)
                        //Set false if voice input option should be excluded
                .setAllowFreeFormInput(true)
                .build();

        PendingIntent confirmActionPendingIntent =
                getActionFeedbackPendingIntent("Action Feedback", 0);

        PendingIntent replyPendingIntent = getOptionFeedbackPendingIntent("Option Feedback", 1);

        NotificationCompat.Action confirmAction = new NotificationCompat.Action(
                R.drawable.ic_ok, "Confirm",
                confirmActionPendingIntent);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_add,
                        TAG_ITEM_QUANTITY, replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .addAction(confirmAction)
                        .addAction(replyAction);

        Bitmap prettyAvatar = getScaledLargeIconFromResource(R.drawable.ic_light);

        Notification notification = new NotificationCompat.Builder(NotificationBuilder.this)
                .setContentTitle(Globals.getItemName())
                .setContentText(String.valueOf(Globals.getItemQuantity()))
                .setSmallIcon(R.drawable.ic_task)
                .setContentIntent(getOptionFeedbackPendingIntent("qty", 20))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(prettyAvatar)
                .extend(wearableExtender)
                .build();

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(NotificationBuilder.this);
        notificationManager.notify(NOTIFICATION_ID, notification);


//        new getItemNumber().execute();
//        getItemNumber getN = new getItemNumber();
//        if(getN.getStatus()== AsyncTask.Status.FINISHED) {

    }


    private PendingIntent getOptionFeedbackPendingIntent(String string, int requestCode) {
        Log.d(LOC, " PendingIntent getConversationPI");
        Intent conversationIntent = new Intent(this, OptionFeedbackActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(OptionFeedbackActivity.class);
        taskStackBuilder.addNextIntent(conversationIntent);
        return taskStackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent getActionFeedbackPendingIntent(String actionFeedback, int requestCode) {
        Log.d(LOC, " PendingIntent getACtionFeedbackPI");
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

        Toast.makeText(this, "onNextItemClick", Toast.LENGTH_SHORT).show();
    }

    public void onUpdateButtonClick(View view) {
        Log.d(LOC, " onUpdateButtonClick");
        new updateItem().execute();

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

    public class updateItem extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOC, " onPreExecute");
            numberOfItems = updateQty.getText().toString();


        }

        @Override
        protected String doInBackground(String... args) {
            Log.d(LOC, " doInBackground");


            try {
                //If the number of items is 0, then picked is set to 0 ("not picked") by default
                if (numberOfItems.matches("")) {
                    Toast.makeText(NotificationBuilder.this, "Enter the number of packages ", Toast.LENGTH_SHORT).show();
                    params.add(new BasicNameValuePair("item_quantity", String.valueOf(Globals.getItemQuantity())));
                    Log.e(LOC, "if");
                } else {
                    params.add(new BasicNameValuePair("item_quantity", numberOfItems));
                    Globals.setItemQuantity(Integer.parseInt(numberOfItems));
                }

                if (!numberOfItems.equals("0")) {
                    picked = "1";
                } else {
                    picked = "0";
                }
                params.add(new BasicNameValuePair("rowNr", String.valueOf(Globals.getItemRowNumber())));
                params.add(new BasicNameValuePair("picked", picked));
                params.add(new BasicNameValuePair("comment", Globals.getItemComment()));


                //Posting parameters to php
                jsonParser.makeHttpRequest(
                        NOTIFIER_URL, "POST", params);


            } catch (Exception e) {
                e.printStackTrace();
            }
            int rn = Globals.getItemRowNumber();
            rn++;
            Globals.setItemRowNumber(rn);


            try {
                params.add(new BasicNameValuePair("rowNr", String.valueOf(Globals.getItemRowNumber())));
                //Posting parameters to php
                jsonParser.makeHttpRequest(
                        NEXT_ITEM_URL, "POST", params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject json = jsonParser.getJSONFromUrl(RANDOM_CRAP);

            try

            {
                mList = json.getJSONArray(Globals.TAG_ITEMS_REPORT);

                Log.e(LOC, "Inside JSON: " + mList);
                JSONObject c = mList.getJSONObject(0);
                Globals.setItemName(c.getString(Globals.TAG_ITEM_NAME));
                Globals.setItemQuantity(Integer.parseInt(c.getString(Globals.TAG_ITEM_QUANTITY)));
                Globals.setItemRowNumber(Integer.parseInt(c.getString(Globals.TAG_ROW_NUMBER)));
                Globals.setItemLocation(c.getString(Globals.TAG_ITEM_LOCATION));
                Globals.setItemInfo(c.getString(Globals.TAG_ITEM_INFO));
                Globals.setItemComment(c.getString(Globals.TAG_ITEM_COMMENT));
            } catch (JSONException e) {
                e.printStackTrace();

            }


            // Intent replyIntent = new Intent(this, showItemLoc.class);

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(LOC, " onPostExecute :value of row NUMBER " +
                    String.valueOf(Globals.getItemRowNumber()));

            if (mList == null) {
                Intent intent = new Intent(NotificationBuilder.this, ReportViewer.class);
                finish();
                startActivity(intent);
            } else {
                Intent intent = new Intent(NotificationBuilder.this, NotificationBuilder.class);
                finish();
                startActivity(intent);
            }

        }
    }


}