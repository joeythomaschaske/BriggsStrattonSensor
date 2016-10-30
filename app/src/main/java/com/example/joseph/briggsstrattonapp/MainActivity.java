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
    private Button Engine_button, Battery_button, Oil_button, tractor_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tractor_button = (Button) findViewById(R.id.tractor_button);
        tractor_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String TAG = "MyActivity";
                Log.v(TAG, "hello world");
                //Intent intent = new Intent(getApplicationContext(), TractorFragment.class);
                //startActivity(intent);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //NEED TO FIX THIS TO START FRAGMENT
                //ft.add(TractorFragment.newInstance("",""), null);
                ft.commit();
            }
        });
    }
}
