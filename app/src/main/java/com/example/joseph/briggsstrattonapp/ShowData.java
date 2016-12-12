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
    private String tempValue;
    private String oilValue;
    private String hoursValue;
    private String bladesValue;
    private String airValue;
    private String powerValue;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
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
        TextView oilView = (TextView) findViewById(R.id.oil_text_view);
        TextView airView = (TextView) findViewById(R.id.air_text_view);
        TextView bladesView = (TextView) findViewById(R.id.blade_text_view);

        if(b!=null)
        {
            // 'b' used with b.get is the bundle that fetches data from other activity
            voltageValue =(String) b.get("voltage");
            voltView.setText(voltageValue + " V");

            tempValue =(String) b.get("temp");
            tempView.setText(tempValue + " F");

            powerValue =(String) b.get("power");
            powView.setText(powerValue.toUpperCase());

            hoursValue =(String) b.get("hours");
            hoursView.setText(hoursValue);

            oilValue =(String) b.get("oil");
            oilView.setText(oilValue + "%");

            bladesValue =(String) b.get("blades");
            bladesView.setText(bladesValue + "%");

            airValue =(String) b.get("air");
            airView.setText(airValue + "%");
        }

        setProgBarColorAndProgress(voltageValue);
        setProgBarColorAndProgressTemp(tempValue);
        setProgBarColorAndProgressAir(airValue);
        setProgBarColorAndProgressOil(oilValue);
        setProgBarColorAndProgressBlades(bladesValue);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setProgBarColorAndProgress(String voltageValue) {
        int value = Integer.parseInt(voltageValue);
        ProgressBar voltage = (ProgressBar) findViewById(R.id.volt_prog_bar);

        if(value < 0) new IllegalStateException("Voltage can't be < 0! This error occured while fetching voltage value from bluetooth!");
        if(value >= 0 && value < 11) {
            voltage.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffVoltageNotification();
        }
        else if(value == 11) {
            voltage.getProgressDrawable().setColorFilter(Color.YELLOW,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffVoltageNotification();
        }
        else if(value >= 12) {
            voltage.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        }
        voltage.setProgress(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setProgBarColorAndProgressTemp(String tempValue) {
        int value = Integer.parseInt(tempValue);
        ProgressBar temp = (ProgressBar) findViewById(R.id.temp_progress_bar);

        if(value >= 300) {
            temp.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffTempNotification();
        }
        else if(value > 0 && value < 275) {
            temp.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else if(value < 300 && value >= 275) {
            temp.getProgressDrawable().setColorFilter(Color.YELLOW,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffTempNotification();
        }
        temp.setProgress(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setProgBarColorAndProgressOil(String oilValue) {
        int value = Integer.parseInt(oilValue);
        ProgressBar oil = (ProgressBar) findViewById(R.id.oil_progress_bar);

        if(value >= 90) {
            oil.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else if(value >= 80 && value < 90) {
            oil.getProgressDrawable().setColorFilter(Color.YELLOW,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffOilNotification();
        }
        else if(value < 80) {
            oil.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffOilNotification();
        }
        oil.setProgress(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setProgBarColorAndProgressBlades(String oilValue) {
        int value = Integer.parseInt(bladesValue);
        ProgressBar blades = (ProgressBar) findViewById(R.id.blades_progress_bar);

        if(value >= 90) {
            blades.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else if(value >= 80 && value < 90) {
            blades.getProgressDrawable().setColorFilter(Color.YELLOW,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffBladesNotification();
        }
        else if(value < 80) {
            blades.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffBladesNotification();
        }
        blades.setProgress(value);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setProgBarColorAndProgressAir(String airValue) {
        int value = Integer.parseInt(airValue);
        ProgressBar air = (ProgressBar) findViewById(R.id.air_filt_prog_bar);

        if(value >= 90) {
            air.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else if(value >= 80 && value < 90) {
            air.getProgressDrawable().setColorFilter(Color.YELLOW,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffAirNotification();
        }
        else if(value < 80) {
            air.getProgressDrawable().setColorFilter(Color.RED,android.graphics.PorterDuff.Mode.SRC_IN);
            setOffAirNotification();
        }
        air.setProgress(value);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOffTempNotification() {
        int value = Integer.parseInt(tempValue);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.engine_img);

        if(value >= 300) {
            mBuilder.setContentTitle("High Temp!");
            mBuilder.setContentText("Engines running too hot!(" + tempValue + " degrees F");
        }
        else if(value >= 275 && value < 300) {
            mBuilder.setContentTitle("High Temp!");
            mBuilder.setContentText("Engine starting to get hot!(" + tempValue + " degrees F");
        }
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOffBladesNotification() {
        int value = Integer.parseInt(bladesValue);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.tractor_img);

        if(value < 80) {
            mBuilder.setContentTitle("Change Blades!");
            mBuilder.setContentText("Change Blades ASAP!(" + bladesValue + " %");
        }

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOffOilNotification() {
        int value = Integer.parseInt(oilValue);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.tractor_img);

        if(value < 80) {
            mBuilder.setContentTitle("Change Oil!");
            mBuilder.setContentText("Change Oil ASAP!(" + oilValue + " %");
        }

        Intent resultIntent = new Intent(this, ShowData.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ShowData.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, mBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOffAirNotification() {
        int value = Integer.parseInt(airValue);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.tractor_img);

        if(value < 80) {
            mBuilder.setContentTitle("Low Air Filter!");
            mBuilder.setContentText("Change air filter ASAP!(" + airValue + " %");
        }
        else if(value >= 80 && value < 90) {
            mBuilder.setContentTitle("Air Filter getting low!");
            mBuilder.setContentText("Replace air filter soon!(" + airValue + " %");
        }
        Intent resultIntent = new Intent(this, ShowData.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ShowData.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, mBuilder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOffVoltageNotification() {
        int value = Integer.parseInt(voltageValue);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.battery_img);
        if(value >= 0 && value < 11) {
            mBuilder.setContentTitle("Very Low Voltage!");
            mBuilder.setContentText("Very low battery voltage detected!");
        }
        // < 10.5 red, 10.5->12 yellow, > 12 green
        else if(value >= 11) {
            mBuilder.setContentTitle("Low Voltage!");
            mBuilder.setContentText("Battery voltage is starting to get low!");
        }
        Intent resultIntent = new Intent(this, ShowData.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ShowData.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(3, mBuilder.build());
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
