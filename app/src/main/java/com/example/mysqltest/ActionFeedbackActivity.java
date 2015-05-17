package com.example.mysqltest;

/**
 * Created by markhulia on 17/05/15.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;


public class ActionFeedbackActivity extends ActionBarActivity {
    public static final String EXTRA_ACTION_FEEDBACK = "jorik";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_feedback);
        Toast.makeText(this, "ActionFeedbackActivity", Toast.LENGTH_LONG).show();

        //Go back to main activity without crashing the app
        Intent i = new Intent(ActionFeedbackActivity.this, NotificationBuilder.class);
        finish();
        startActivity(i);
    }
}
