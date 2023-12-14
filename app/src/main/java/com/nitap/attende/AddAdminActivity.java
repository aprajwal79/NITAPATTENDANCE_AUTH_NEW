package com.nitap.attende;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nitap.attende.models.Admin;
import com.ttv.facerecog.R;

public class AddAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        EditText e1 = findViewById(R.id.e1);
        EditText e2 = findViewById(R.id.e2);
        Button addAdmin = findViewById(R.id.b1);

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin admin = new Admin();
                admin.name= e1.getText().toString();
                admin.email = e2.getText().toString();
                admin.adminId = admin.email.replace(".","?");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("admins").child(admin.adminId);
                ref.setValue(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddAdminActivity.this, "Successfully added Admin", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddAdminActivity.this, "Failed to add Admin", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


    }
}