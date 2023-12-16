package com.nitap.attende;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BluetoothScannerActivity extends AppCompatActivity {

    public static String key = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,";
    //"úêµ¶§µ¶§¥£औकखगघङ";

    public static String name = "UNKNOWN" ;
    Set<String> myset ;

   static List<String> mlist ;
   static ArrayList<String> mylist2 = new ArrayList<String>();
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

     BluetoothAdapter bluetoothAdapter;
    private final List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
     //DevicesAdapter devicesAdapter;
    public static TextView statusTextView;
    public static String s1 = "START";

    void t(String msg) {

        if(myset.add(msg)) {
            s1 = s1 + "    " + msg;
            if (statusTextView != null) {
                statusTextView.setText(s1);
                //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "STATUS_TEXT_VIEW IS NULL", Toast.LENGTH_SHORT).show();
            }

        }


    }

    static void display(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

     void display( String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private final BroadcastReceiver bluetoothDiscoveryReceiver = new BroadcastReceiver() {
        @SuppressLint({"NotifyDataSetChanged", "MissingPermission"})
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //super.onCreate(savedInstanceState);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                t(device.getName());
                //if (!mylist2.contains(device.getAddress())) { mylist2.add(device.getAddress() ); }
                if (!bluetoothDevices.contains(device)) {
                 //   bluetoothDevices.add(device);
                    // devicesAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    // @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            myset = null;
            myset = new LinkedHashSet<String>();
            myset.clear();

            setContentView(R.layout.activity_bluetooth);

            statusTextView = findViewById(R.id.status_text_view);

            boolean a1 = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
            boolean a2 = checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
            if(!a1) { display("Manifest.permission.BLUETOOTH_CONNECT permission required");  }
            if(!a2) { display("Manifest.permission.BLUETOOTH_SCAN permission required");  }
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (bluetoothAdapter == null) {
                statusTextView.setText("bluetooth_not_supported");
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
                }

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(bluetoothDiscoveryReceiver, filter);


                    ActivityCompat.requestPermissions(this,new String[] {
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission_group.LOCATION,
                            Manifest.permission_group.NEARBY_DEVICES,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT


                    },0);


               // display("ABOUT TO START BT SCAN");
                display("BLUETOOTH DISCOVERY STATUS :" + bluetoothAdapter.isDiscovering() );

                try {
                    //bluetoothAdapter.cancelDiscovery();
                } catch (Throwable e) {
                    display("DISCOVERY PRE STARTED");
                }

                boolean isCorrect = bluetoothAdapter.startDiscovery();
                if (isCorrect) {
                    display("DISCOVERY STARTED SUCCESSFULLY");
                    name = bluetoothAdapter.getName();
                    boolean success = bluetoothAdapter.setName(key);
                    display(success+  " key = " + key );

                } else {
                    display("DISCOVERY FAILED TO START");
                }


    }

} catch (Throwable e) {
    e.printStackTrace();
   // Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
    display(e.toString());
    if (statusTextView != null) {    statusTextView.setText(e.toString());  }
}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothDiscoveryReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                statusTextView.setText("location_permission_required");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                statusTextView.setText("bluetooth_enabled");
            } else {
                statusTextView.setText("bluetooth_disabled");
            }
        }
    }
}
