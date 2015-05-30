package com.example.mysqltest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhulia on 28/05/15.
 */
public class ItemUpdate extends Activity {
    TextView tvItemName, tvItemComment, tvItemLocation;
    EditText etItemComment, etItemQuantity;
    String LOC = "ItemUpdate";
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    JSONParser jsonParser = new JSONParser();
    String picked = "1";
    String TAG = "ItemUpdate ";
    String numberOfItems, comment;
    private String ITEM_NUMBER_URL = Globals.URL + "notifier.php";

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
        numberOfItems = etItemQuantity.getText().toString();
        comment = etItemComment.getText().toString();

        if(comment.equals("")){
            comment = Globals.getItemComment();
        }

        new getItemNumber().execute();

    }

    public class getItemNumber extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOC, " onPreExecute");

        }

        @Override
        protected String doInBackground(String... args) {


            try {
                //If the number of items is 0, then picked is set to 0 ("not picked") by default
                if(numberOfItems.equals("0")){
                    picked = "0";
                }
                params.add(new BasicNameValuePair("picked", picked));
                params.add(new BasicNameValuePair("item_quantity", numberOfItems));
                params.add(new BasicNameValuePair("comment", comment));

                //Posting parameters to php
                jsonParser.makeHttpRequest(
                        ITEM_NUMBER_URL, "POST", params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Globals.setItemQuantity(Integer.parseInt(numberOfItems));
            Globals.setItemComment(comment);
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(ItemUpdate.this, "Entry updated", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(ItemUpdate.this, ReportViewer.class);
            finish();
            startActivity(intent);
        }
    }
}
