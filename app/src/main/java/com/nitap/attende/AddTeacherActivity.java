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
import com.nitap.attende.models.Teacher;


import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Arrays;

public class AddTeacherActivity extends AppCompatActivity {

    Button addTeacher ;
    EditText a,b,c,d,e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        addTeacher =findViewById(R.id.btnAddTeacher);
        a=findViewById(R.id.e1);
        b=findViewById(R.id.e2);
        c=findViewById(R.id.e3);
        d =findViewById(R.id.e4);
        e=findViewById(R.id.e5);

        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "NOT YET IMPLEMENTED", Toast.LENGTH_SHORT).show();
                Teacher teacher = new Teacher();
                teacher.sectionInfos = null;
                teacher.sectionIds = new ArrayList<>();
                teacher.courses = new ArrayList<>();
                teacher.name = a.getText().toString();
                teacher.email = b.getText().toString();
                teacher.teacherId = teacher.email.replace(".","?");
                teacher.branch = c.getText().toString();
                teacher.courses = new ArrayList<>(Arrays.asList(d.getText().toString().split(",")));
                teacher.sectionIds = new ArrayList<>(Arrays.asList(e.getText().toString().split(",")));

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("teachers").child(teacher.teacherId);
                ref.setValue(teacher).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddTeacherActivity.this, "Added teacher successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddTeacherActivity.this, "Failed to add teacher", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}