package com.example.joseph.briggsstrattonapp;

import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.support.v7.widget.Toolbar;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import fragments.BluetoothFragment;
import fragments.TractorFragment;
import fragments.TractorFragment.OnFragmentInteractionListener;

import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static com.example.joseph.briggsstrattonapp.R.id.fragment_tractor;

public class MainActivity extends AppCompatActivity {
    public static Button bluetoothConnectButton;
    public static Button readFromBluetooth;
    public static TextView outputText;

    public static BluetoothDevice bsDevice;
    public static BluetoothSocket socket;
    public static InputStream input;
    public static OutputStream output;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Button tractor_button,bluetooth_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tractor_button = (Button) findViewById(R.id.tractor_button);
        tractor_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TractorFragment.class);
               // intent.putExtra("mode", "Tractor");
                startActivity(intent);
            }
        });
        bluetooth_button = (Button) findViewById(R.id.bluetooth_button);
        bluetooth_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BluetoothFragment.class);
                //intent.putExtra("mode", "Bluetooth");
                startActivity(intent);
            }
        });
       // init();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        bluetoothConnectButton = (Button) findViewById(R.id.bluetoothConnect);
        readFromBluetooth = (Button) findViewById(R.id.read);
        outputText = (TextView) findViewById(R.id.bluetoothVal);

        //region bluetooth connection code

        bluetoothConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBluetoothAdapter == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Bluetooth is not supported on this device :(", Toast.LENGTH_LONG);
                    toast.show();
                }

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                String name = "";
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("B & S")) {
                        bsDevice = device;
                        name += device.getName() + " ";
                    }
                }
                Toast toast = Toast.makeText(getApplicationContext(), "Connected to: " + name, Toast.LENGTH_LONG);
                toast.show();
            }
        });
        //endregion
        //region read from device code
        readFromBluetooth.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View view) {
                openBT();
            }
        });
        //endregion

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openBT() {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
            socket = bsDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            BluetoothGatt gatt = bsDevice.connectGatt(this, false, new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicRead(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                }

                @Override
                public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                    super.onDescriptorRead(gatt, descriptor, status);
                }

                @Override
                public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                    super.onDescriptorWrite(gatt, descriptor, status);
                }

                @Override
                public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                    super.onReliableWriteCompleted(gatt, status);
                }

                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    super.onReadRemoteRssi(gatt, rssi, status);
                }

                @Override
                public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                    super.onMtuChanged(gatt, mtu, status);
                }
            });
            boolean connection = gatt.connect(); //this needs to go into onConnectionStateChange
            boolean discovery = gatt.discoverServices(); //this section needs to go into onServicesDiscovered
            BluetoothGattService ser = gatt.getService(UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214"));
            List<BluetoothGattCharacteristic> chars = ser.getCharacteristics();
            BluetoothGattCharacteristic switchChar = chars.get(0);
            boolean b = gatt.readCharacteristic(switchChar);
            // all the way up to here


            String value = switchChar.getStringValue(0); //this section needs to go into onCharacteristicRead
            outputText.setText(value);
            //up to here
        } catch (Exception e) {
            Toast exception = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            exception.show();
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
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