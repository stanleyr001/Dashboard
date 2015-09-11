package com.topicplaces.dashboard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.Map;
import java.util.TreeMap;

import main.java.SNSController;

public class Dashboard extends AppCompatActivity {

    private GridView dashboardTopicsGridView;
    private DashboardID[] dashboardIDs;

    private String dashboardTopicTitle = "Dashboard Testing",
                   dashboardMessageTitle = "Dashboard Permanent Message",
                   dashTID,
                   dashGID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dashboardTopicsGridView = (GridView)findViewById(R.id.dashboard_topics_gridview);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String USER = "stanleyr001";
            String PASSWORD = "stanleyr001";
            String ENDPOINT = "http://tse.topicplaces.com/api/2/";
            SNSController topicsController = new SNSController(ENDPOINT);
            String UID = topicsController.verifyUsername(USER);
            String authKey = topicsController.acquireKey(USER, PASSWORD);

            Map<String, String> privateTopics = topicsController.getPrivateTopicMap(UID);
            dashTID = privateTopics.get(dashboardTopicTitle);

            Map<String, String> topicMessages = topicsController.getPrivateMessageMap(dashTID, authKey);
            dashGID = topicMessages.get(dashboardMessageTitle);

            Map<String, String> privateAttributes = topicsController.getAttributes(dashGID, true, authKey);

            String[] dashboardMessageKeys = (String[]) privateAttributes.keySet().toArray();
            Log.d("DASHBOARDMESSAGEIDS", "DashboardMessageIDs succesfully completed.");

            ArrayAdapter<String> dashboardAdapter = new ArrayAdapter<String>(
                    getBaseContext(),
                    R.layout.dashboard_gridview_layout,
                    R.id.dashboard_topics_gridview,
                    dashboardMessageKeys);

            dashboardTopicsGridView.setAdapter(dashboardAdapter);



        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}