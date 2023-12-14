package com.nitap.attende;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nitap.attende.models.MyConfiguration;
import com.nitap.attende.models.SectionInfo;
import com.nitap.attende.models.Teacher;
import com.ttv.facerecog.R;

import java.util.ArrayList;

public class ViewTeacherProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher_profile);

        Teacher teacher = MyUtils.getConfiguration(this).teacher;

        TextView name,email,branch,sections;
        name = findViewById(R.id.fullname);
        assert teacher != null;
        name.setText("NAME: "+teacher.name);
        email = findViewById(R.id.email);
        email.setText("EMAIL: "+teacher.email);
        branch = findViewById(R.id.branch);
        branch.setText("BRANCH: "+teacher.branch);
        sections = findViewById(R.id.sections);
        ArrayList<SectionInfo> sectionInfos = teacher.sectionInfos;
        String s = "";
        for (SectionInfo info : sectionInfos) {
            s = s + info.sectionId + ", ";
        }
        sections.setText("SECTIONS: " + s);
/*
        String courses = "";
        for (String course : teacher.courses) {
            courses = course + ", ";
        }
        */
        String coursesString = new String(String.valueOf(teacher.courses));


        TextView courseview = findViewById(R.id.courses);
        courseview.setText("COURSES: " + coursesString);



       // sections.setText("SECTION: "+student.sectionName);


    }
}