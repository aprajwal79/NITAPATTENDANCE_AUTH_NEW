package com.nitap.attende.pages;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.nitap.attende.LoginActivity;
import com.nitap.attende.MyUtils;
import com.nitap.attende.R;
import com.nitap.attende.databinding.ActivityHomeBinding;
import com.nitap.attende.models.MyConfiguration;

import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth userAuth;
    ActivityHomeBinding binding;
    private GoogleSignInClient mGoogleSigninClient;
    ImageButton profileBtn;
    MyConfiguration myConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myConfiguration = MyUtils.getConfiguration(this);
        assert myConfiguration.student!=null;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(this, gso);



        binding.aboutUsBtn.setOnClickListener(v -> {
            //signOut();
            Toast.makeText(getApplicationContext(), "Signed Out Successfully", Toast.LENGTH_SHORT).show();

            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activityManager.clearApplicationUserData();

                }
            },1000);



        });
        assert MyUtils.getConfiguration(this).student!=null;



        binding.attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "VERIFY CLICKED", Toast.LENGTH_SHORT).show();
                Toast.makeText(HomeActivity.this, "CameraActivity.class not found", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                //startActivityForResult(intent, 2);


            }
             });

        profileBtn = findViewById(R.id.profile_btn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyConfiguration myConfiguration = MyUtils.getConfiguration(getApplicationContext());
                assert  myConfiguration.student!=null;
                startActivity(new Intent(getApplicationContext(),ViewStudentProfileActivity.class));
            }
        });

        TextView fname,lname;
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        String[] contents = MyUtils.getConfiguration(this).student.name.split(" ");
        fname.setText(contents[0]);
        lname.setText(contents[1]);



    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == -1) {
            Intrinsics.checkNotNull(data);
            int verifyResult = data.getIntExtra("verifyResult", Toast.LENGTH_SHORT);
            String recogName = data.getStringExtra("verifyName");
            if (verifyResult == 1) {
                Toast.makeText((Context)this, (CharSequence)("Verify succeed! " + recogName), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, AttendanceActivity.class));
            } else {
                Toast.makeText((Context)this, (CharSequence)"Verify failed!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


}