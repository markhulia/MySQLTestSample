package com.example.mysqltest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by markhulia on 17/05/15.
 */
public class NotificationBuilder extends Activity {

    public static final int NOTIFICATION_ID = 1;
    public static int ITEM_ID;
    private static int FLAG = 0;
    TextView itemTitle, itemLocation, itemQuantity;
    EditText updateQty;
    private String ItemQty = "Quantity";
    private String ItemName = "Item Name";
    private String Location = "Item Location";
    private int numberOfPackages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_item_caller);
        itemTitle = (TextView) findViewById(R.id.showItemName);
        itemLocation = (TextView) findViewById(R.id.showItemLoc);
        itemQuantity = (TextView) findViewById(R.id.showItemQty);
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

        //TODO add PHP checker, to check if DB has next line. If false, show report
        itemTitle.setText(ItemName);
        itemLocation.setText(Location);
        itemQuantity.setText(ItemQty);
        updateQty = (EditText) findViewById(R.id.number_of_packages);

        // Intent replyIntent = new Intent(this, showItemLoc.class);
        String[] choices = NumberGenerator.getNumbers();
        RemoteInput remoteInput = new RemoteInput.Builder(OptionFeedbackActivity.EXTRA_VOICE_REPLY)
                .setLabel("Reply")
                .setChoices(choices)
                        //Set false if voice input option should be excluded
                .setAllowFreeFormInput(true)
                .build();

        PendingIntent confirmActionPendingIntent =
                getActionFeedbackPendingIntent("Send PHP queries from here", 0);

        PendingIntent replyPendingIntent = getConversationPendingIntent("", 1);

        NotificationCompat.Action confirmAction = new NotificationCompat.Action(
                R.drawable.ic_ok, "Confirm",
                confirmActionPendingIntent);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_add, ItemQty, replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .addAction(confirmAction)
                .addAction(replyAction);

        Bitmap prettyAvatar = getScaledLargeIconFromResource(R.drawable.ic_light);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(ItemName)
                .setContentText(ItemQty)
                .setSmallIcon(R.drawable.ic_task)
                .setContentIntent(getConversationPendingIntent("qty", 20))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(prettyAvatar)
                .extend(wearableExtender)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);


    }

    public void onUpdateButtonClick(View view) {
        String amount = updateQty.getText().toString();
        //trim().isEmpty() ignores whitespaces

        //add flag. if user updated amount, it will change flag from 0 to 1. If user
        //wants to update amount agian before pressing "next item" POP-up window should display info
        //that this item_id has already been updated and if user wants to proceed

        if (FLAG == 0) {
            if (amount != null && !amount.trim().isEmpty()) {
                numberOfPackages = Integer.parseInt(amount);
                Toast.makeText(this, "Items " + numberOfPackages, Toast.LENGTH_LONG).show();
                updateQty.getText().clear();
                FLAG = 1;
            }
        } else {
            //TODO: create separate confirmation dialog class
            //call confirmation dialog
            //OPTION_YES -> update itemQTY, set FLAG = 1;
            //OPTION NO -> return to parent activity
        }
    }
}
