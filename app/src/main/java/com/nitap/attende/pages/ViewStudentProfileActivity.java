package com.nitap.attende.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.nitap.attende.MyUtils;
import com.nitap.attende.R;
import com.nitap.attende.models.MyConfiguration;

public class ViewStudentProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_profile);
        MyConfiguration myConfiguration = MyUtils.getConfiguration(this);

        TextView name,email,rollno,degree,branch,year,sem,sectionName;
        name = findViewById(R.id.fullname);
        assert myConfiguration.student != null;
        name.setText("NAME: "+myConfiguration.student.name.split(" ")[0]+" "+myConfiguration.student.name.split(" ")[1]);
        email = findViewById(R.id.email);
        email.setText("EMAIL: "+myConfiguration.student.email);
        rollno = findViewById(R.id.rollno);
        rollno.setText("ROLL NO: "+myConfiguration.student.rollno);
        degree = findViewById(R.id.degree);
        degree.setText("COURSE TYPE: "+myConfiguration.student.degree);
        branch = findViewById(R.id.branch);
        branch.setText("BRANCH: "+myConfiguration.student.branch);
        year = findViewById(R.id.year);
        year.setText("YEAR: "+myConfiguration.student.year);
        sem = findViewById(R.id.sem);
        sem.setText("SEM: "+myConfiguration.student.sem);
        sectionName = findViewById(R.id.sectionName);
        sectionName.setText("SECTION: "+myConfiguration.student.sectionName);
        TextView courseview = findViewById(R.id.courseList);
        courseview.setText("COURSES: " + String.valueOf(myConfiguration.student.courses));





    }
}