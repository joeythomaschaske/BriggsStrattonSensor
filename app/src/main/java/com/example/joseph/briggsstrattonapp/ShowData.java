package com.example.joseph.briggsstrattonapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.security.AccessController.getContext;

public class ShowData extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private String voltageValue;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MMMM,YYYY hh,mm,a");
        //date and time stuff
        String strDate = sdf.format(c.getTime());
        // Parsing the string to make it look nicer
        String []parseDate;
        parseDate = strDate.split(" ");
        String firstPart = parseDate[0];
        String secondPart = parseDate[1];

        TextView edtDate = (TextView) findViewById(R.id.stats_date);
        edtDate.setText(firstPart);
        TextView edtTime = (TextView) findViewById(R.id.stats_time);
        edtTime.setText(secondPart);
        Bundle b = getIntent().getExtras();
        // Will update our textViews with new values
        TextView hoursView = (TextView) findViewById(R.id.hours_view);
        TextView voltView = (TextView) findViewById(R.id.volt_view);
        TextView tempView = (TextView) findViewById(R.id.temp_view);
        TextView powView = (TextView) findViewById(R.id.pow_view);
        if(b!=null)
        {
            voltageValue =(String) b.get("voltage");
            voltView.setText(voltageValue + " V");

            String tempValue =(String) b.get("temp");
            tempView.setText(tempValue + " F");

            String powerValue =(String) b.get("power");
            powView.setText(powerValue.toUpperCase());

            String hoursValue =(String) b.get("hours");
            hoursView.setText(hoursValue);
        }

        setProgBarColorAndProgress(voltageValue);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setProgBarColorAndProgress(String voltageValue) {
        int value = Integer.parseInt(voltageValue);
        ProgressBar voltage = (ProgressBar) findViewById(R.id.volt_prog_bar);

        if(value < 0) new IllegalStateException("Voltage can't be < 0! This error occured while fetching voltage value from bluetooth!");
        if(value >= 0 && value < 5) {
            voltage.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffVoltageNotification();
        }
        else if(value > 4 && value < 8) {
            voltage.getProgressDrawable().setColorFilter(Color.YELLOW,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffVoltageNotification();
        }
        else if(value > 7) {
            voltage.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        }
        voltage.setProgress(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOffVoltageNotification() {
        int value = Integer.parseInt(voltageValue);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.battery_img);
        if(value >= 0 && value < 5)
            mBuilder.setContentTitle("Very Low Voltage!");
        else if(value > 4 && value < 8)
            mBuilder.setContentTitle("Low Voltage!");
        if(value >= 0 && value < 5)
            mBuilder.setContentText("Very low battery voltage detected!");
        else if(value > 4 && value < 8)
            mBuilder.setContentText("Battery voltage is starting to get low!");
        Intent resultIntent = new Intent(this, ShowData.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ShowData.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ShowData Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
