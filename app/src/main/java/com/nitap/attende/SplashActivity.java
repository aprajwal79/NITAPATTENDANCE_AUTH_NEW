package com.nitap.attende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.splashscreen.SplashScreenViewProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nitap.attende.activities.OnboardingExample1Activity;
import com.nitap.attende.models.MyConfiguration;
import com.nitap.attende.pages.HomeActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    LoginUtils l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        l = new LoginUtils(this);
        l.initData(this);


        l.mAuth = FirebaseAuth.getInstance();


        MyUtils.removeConfigurationBuilder(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        l.mGoogleSignInClient = GoogleSignIn.getClient(l.context, gso);
        l.mGoogleSignInClient.signOut();






        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        l.safeUpdateUI();
       // setContentView(R.layout.activity_splash);
    }


    private void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        l.cleanData();
        super.onDestroy();
    }

}