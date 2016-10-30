package com.example.joseph.briggsstrattonapp;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import fragments.TractorFragment;

public class MainActivity extends AppCompatActivity {
    private Button Engine_button, Battery_button, Oil_button, tractor_button, bluetooth_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tractor_button = (Button) findViewById(R.id.tractor_button);
        tractor_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TractorActivity.class);
                startActivity(intent);
            }
        });
        bluetooth_button = (Button) findViewById(R.id.bluetooth_button);
        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });
    }
}
