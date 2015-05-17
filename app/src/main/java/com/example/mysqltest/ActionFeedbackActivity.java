package com.example.mysqltest;

/**
 * Created by markhulia on 17/05/15.
 */

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


public class ActionFeedbackActivity extends ActionBarActivity {
    public static final String EXTRA_ACTION_FEEDBACK = "action_feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_feedback);

        String actionFeedback = getIntent().getStringExtra(EXTRA_ACTION_FEEDBACK);
        if (actionFeedback != null) {
            ((TextView) findViewById(R.id.action_feedback)).setText(actionFeedback);
        }
    }
}