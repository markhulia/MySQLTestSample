package com.example.mysqltest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by markhulia on 28/05/15.
 */
public class ItemUpdate extends Activity {
    TextView tvItemName, tvItemComment, tvItemLocation;
    EditText etItemComment, etItemQuantity;
    String LOC = "ItemUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item_update);
        Log.e(LOC, "onCreate");

        tvItemName = (TextView) findViewById(R.id.singleItemUpdate_tvItemName);
        tvItemLocation = (TextView) findViewById(R.id.singleItemUpdate_tvItemLocation);
        tvItemComment = (TextView) findViewById(R.id.singleItemUpdate_tvItemComment);
        etItemQuantity = (EditText) findViewById(R.id.singleItemUpdate_etItemQty);
        etItemComment = (EditText) findViewById(R.id.singleItemUpdate_etItemComment);

        tvItemName.setText(Globals.getItemName());
        tvItemLocation.setText(Globals.getItemLocation());
        tvItemComment.setText(Globals.getItemComment());
        etItemQuantity.setHint(String.valueOf(Globals.getItemQuantity()));
        etItemComment.setHint(Globals.getItemComment());

    }

    public void onUpdateSingleItemButtonClick(View view) {
        new getItemNumber().execute();

    }

    public class getItemNumber extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOC, " onPreExecute");

        }

        @Override
        protected String doInBackground(String... args) {


            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(ItemUpdate.this, "Entry updated", Toast.LENGTH_LONG).show();
        }
    }
}
