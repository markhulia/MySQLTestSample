package com.example.mysqltest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by markhulia on 23/05/15.
 */
public class Items {
    JSONParser jsonParser = new JSONParser();
    ReportViewer reportViewer = new ReportViewer();
    NotificationBuilder notificationBuilder = new NotificationBuilder();
    private String RESET_DATABASE = URL.URL + "resetReport.php";
    class nexItem extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("NotificationBuilder ", " nextItemOn pre-execute ");
        }

        @Override
        protected String doInBackground(String... args) {
            Log.d("NotificationBuilder ", "nextItem on In-Background");
            //      SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NotificationBuilder.this);
            //    String post_username = sp.getString("username", "anon");

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //        params.add(new BasicNameValuePair(" username", post_username));

                JSONObject json = jsonParser.makeHttpRequest(
                        RESET_DATABASE, "POST", params);
                return "success";
            } catch (Exception e) {
                Log.e("NotificationBuilder ", " crashed here");
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("NotificationBuilder ", " nextItem on Post-Execute");
        }
    }
}
