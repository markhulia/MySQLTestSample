package com.example.mysqltest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by markhulia on 17/05/15.
 */
public class NotificationBuilder extends Activity{
    public static final int NOTIFICATION_ID = 1;
    public static final String ItemID = "Item Id";
    public static final String ItemName = "Item Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
