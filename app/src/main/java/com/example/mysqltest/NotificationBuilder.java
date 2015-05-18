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
    private String ItemQty = "Quantity";
    private String ItemName = "Item Name";
    private String Location = "Item Location";
    private int numberOfPackages;
    TextView itemTitle, itemLocation, itemQuantity;
    EditText updateQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_item_caller);
        itemTitle = (TextView) findViewById(R.id.showItemName);
        itemLocation = (TextView) findViewById(R.id.showItemLoc);
        itemQuantity = (TextView) findViewById(R.id.showItemQty);
        Toast.makeText(this, "OnCreate", Toast.LENGTH_LONG).show();
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
    public void onVoiceReplyClick(View view) {

        itemTitle.setText(ItemName);
        itemLocation.setText(Location);
        itemQuantity.setText(ItemQty);
        updateQty = (EditText) findViewById(R.id.number_of_packages);
        String amount = updateQty.getText().toString();

        //trim().isEmpty() ignores whitespaces
        if (amount != null && !amount.trim().isEmpty()) {
            numberOfPackages = Integer.parseInt(amount);
            //updateQty.setText("");
        }
        // Intent replyIntent = new Intent(this, showItemLoc.class);
        String[] choices = NumberGenerator.getNumbers();
        RemoteInput remoteInput = new RemoteInput.Builder(OptionFeedbackActivity.EXTRA_VOICE_REPLY)
                .setLabel("Reply")
                .setChoices(choices)
                        //Set false if voice input option should be excluded
                .setAllowFreeFormInput(true)
                .build();

        PendingIntent confirmActionPendingIntent =
                getActionFeedbackPendingIntent("Im pretty sure I don`t need this view", 0);

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
                .setContentIntent(getConversationPendingIntent("Pretty Rabbit", 20))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(prettyAvatar)
                .extend(wearableExtender)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);


    }

    public void onUpdateButtonClick(View view) {
        Toast.makeText(this, "Items " + numberOfPackages, Toast.LENGTH_LONG).show();

        itemTitle.setText(ItemName);
        itemLocation.setText(Location);
        itemQuantity.setText(ItemQty);

        updateQty.setText("");

    }
}