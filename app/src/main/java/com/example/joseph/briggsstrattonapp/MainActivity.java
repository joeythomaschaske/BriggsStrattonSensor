package com.example.joseph.briggsstrattonapp;
import android.app.PendingIntent;
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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import static android.R.attr.type;
import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ;
import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    public static Button bluetoothConnectButton;
    public static Button readFromBluetooth;
    public static TextView voltageText;
    public static TextView tempText;
    public static TextView powerText;
    private String voltage_value,temp_value,power_value;

    public static BluetoothDevice bsDevice;
    public static  BluetoothSocket socket;
    public static InputStream input;
    public static OutputStream output;

    public static ArrayList<BluetoothGattCharacteristic> queue = new ArrayList();
    private Button bluetooth_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        bluetoothConnectButton = (Button) findViewById(R.id.bluetooth_button);
        readFromBluetooth = (Button) findViewById(R.id.tractor_button);
        // voltageText = (TextView) findViewById(R.id.voltage);
        // tempText = (TextView) findViewById(R.id.temp);
        // powerText = (TextView) findViewById(R.id.onOff);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        bluetoothConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                Intent intent = new Intent(getApplicationContext(), ShowData.class);
                // For right now, I'm just putting dummy data through to symbolize voltage,temp etc.
                // since i don't have arduino on me.
                intent.putExtra("voltage",voltage_value);
                intent.putExtra("temp",temp_value);
                intent.putExtra("power",power_value);
                intent.putExtra("hours","103");
                startActivity(intent);
            }
        });

        readFromBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowData.class);
                // For right now, I'm just putting dummy data through to symbolize voltage,temp etc.
                // since i don't have arduino on me.
                intent.putExtra("voltage","12");
                intent.putExtra("temp","276");
                intent.putExtra("power","on");
                intent.putExtra("hours","103");
                intent.putExtra("oil","90");
                intent.putExtra("blades","50");
                intent.putExtra("air","70");
                startActivity(intent);
            }
        });
    }
    //
    private void init() {

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
        readFromBluetooth.setOnClickListener(new View.OnClickListener(){
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
                    gatt.discoverServices();
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                    BluetoothGattService ser = gatt.getService(UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214"));
                    List<BluetoothGattCharacteristic> chars = ser.getCharacteristics();
                    BluetoothGattCharacteristic voltage = chars.get(0);
                    BluetoothGattCharacteristic temp = chars.get(1);
                    BluetoothGattCharacteristic onOff = chars.get(2);
                    queue.add(voltage);
                    queue.add(temp);
                    queue.add(onOff);
                    boolean volt = gatt.readCharacteristic(queue.remove(0));
                    //boolean temperature = gatt.readCharacteristic(temp);
                    //boolean power = gatt.readCharacteristic(onOff);
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicRead(gatt, characteristic, status);
                    Log.d("UUID", characteristic.getUuid().toString());
                    if(characteristic.getUuid().toString().equals("19b10001-e8f2-537e-4f6c-d104768a1214")) {
                        //voltage
                        final String value = characteristic.getStringValue(0); //this section needs to go into onCharacteristicRead
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // voltageText.setText(value);
                                voltage_value = "" + value;
                            }
                        });

                    } else if(characteristic.getUuid().toString().equals("19b10002-e8f2-537e-4f6c-d104768a1214")) {
                        //temp
                        final String value = characteristic.getStringValue(0); //this section needs to go into onCharacteristicRead
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // tempText.setText(value);
                                temp_value = "" + value;
                            }
                        });
                    } else if(characteristic.getUuid().toString().equals("19b10003-e8f2-537e-4f6c-d104768a1214")) {
                        // On Off
                        final String value = characteristic.getStringValue(0); //this section needs to go into onCharacteristicRead
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                               // powerText.setText(value);
                                power_value = "" + value;
                            }
                        });
                    }
                    if(!queue.isEmpty()) {

                        boolean read = gatt.readCharacteristic(queue.remove(0));
                        System.out.println("debug line");
                    }
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
//            boolean discovery = gatt.discoverServices(); //this section needs to go into onServicesDiscovered
//            BluetoothGattService ser = gatt.getService(UUID.fromString("19B10000-E8F2-537E-4F6C-D104768A1214"));
//            List<BluetoothGattCharacteristic> chars = ser.getCharacteristics();
//            BluetoothGattCharacteristic switchChar = chars.get(0);
//            boolean b = gatt.readCharacteristic(switchChar);
//            // all the way up to here
//
//
//            String value = switchChar.getStringValue(0); //this section needs to go into onCharacteristicRead
//            outputText.setText(value);
//            //up to here
        } catch (Exception e) {
            Toast exception = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            exception.show();
            Log.d("Error", e.getMessage() + " " + e.getLocalizedMessage());
        }
    }


}
