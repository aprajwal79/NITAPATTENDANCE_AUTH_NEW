package com.nitap.attende;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nitap.attende.models.MyConfiguration;

import com.nitap.attende.pages.TakeAttendance;
import com.ttv.facerecog.R;

import java.util.ArrayList;

public class TeacherDashboardActivity extends AppCompatActivity {

    MyConfiguration tConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert MyUtils.getConfiguration(getApplicationContext()).teacher!=null;
        setContentView(R.layout.activity_teacher_dashboard);

        tConfig = MyUtils.getConfiguration(getApplicationContext());
        TextView fname,lname;
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        String[] contents = tConfig.teacher.name.split(" ");
        fname.setText(contents[0]);
        lname.setText(contents[1]);
        ImageButton aboutUsBtn = findViewById(R.id.about_us_btn);
        aboutUsBtn.setOnClickListener(v -> {
            //signOut();
            Toast.makeText(getApplicationContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show();

            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityManager.clearApplicationUserData();
                    //dialog.cancel();
                }
            },1000);

    });

        ImageButton reportBtn = findViewById(R.id.report_btn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TakeAttendance.class));
            }
        });

        ImageButton profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ViewTeacherProfileActivity.class));
            }
        });


    }
}