package com.nitap.attende.pages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nitap.attende.EncryptActivity;
import com.nitap.attende.MyUtils;
import com.nitap.attende.models.MyConfiguration;
import com.ttv.facerecog.databinding.ActivityAttendanceBinding;

public class AttendanceActivity extends AppCompatActivity {

    boolean isGivingAttendance = false;
    ActivityAttendanceBinding binding;
    String bt_name ;
    BluetoothAdapter bluetoothAdapter;
String sOldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        MyConfiguration myConfiguration = MyUtils.getConfiguration(getApplicationContext());

        if(myConfiguration!= null){
            bt_name = myConfiguration.student.rollno;
            bt_name = EncryptActivity.encrypt(bt_name);

        }else{
            Toast.makeText(this, "can't access local storage", Toast.LENGTH_SHORT).show();
        }

        String data[] = myConfiguration.student.courses.toArray(new String[0]);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, data);
        binding.spinner.setAdapter(spinnerArrayAdapter);


        binding.attendanceBtn.setOnClickListener(v -> {

            if (isGivingAttendance) {
                Toast.makeText(getApplicationContext(), "Already giving attendance", Toast.LENGTH_SHORT).show();
                return;}

            if (ContextCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(AttendanceActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                }

            }
            if (ContextCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(AttendanceActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
                }

            }

            if (ContextCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                } else if (!bluetoothAdapter.isDiscovering()) {
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);


                    bluetoothLauncher.launch(discoverableIntent);
                }

            }


        });

    }

    public boolean setBluetooth(boolean enable) {
        final long lTimeToGiveUp_ms = System.currentTimeMillis() + 40000;

        Toast.makeText(this, "Entered SetBluetooth", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(AttendanceActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

            if (bluetoothAdapter.isEnabled()) {

                 sOldName = bluetoothAdapter.getName();

                Boolean flag = bluetoothAdapter.setName(bt_name);
                isGivingAttendance = true;
                Toast.makeText(this, "Giving Attendance, please do not close the app", Toast.LENGTH_LONG).show();

            }


        }

        return true;
    }

    ActivityResultLauncher<Intent> bluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getResultCode() == 120) {
                    setBluetooth(true);
                } else {
                    Toast.makeText(this, "Not discoverable", Toast.LENGTH_SHORT).show();
                }

            }
    );


    @SuppressLint("MissingPermission")
    @Override
    public void onStop() {
        super.onStop();
        try{
            bluetoothAdapter.setName(sOldName);
        }catch(Throwable e) {
            e.printStackTrace();
        }
    }



}