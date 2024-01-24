package com.nitap.attende;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nitap.attende.activities.OnboardingExample1Activity;
import com.nitap.attende.models.Admin;
import com.nitap.attende.models.Class;
import com.nitap.attende.models.MyConfiguration;
import com.nitap.attende.models.MyStudent;
import com.nitap.attende.models.Section;
import com.nitap.attende.models.SectionInfo;
import com.nitap.attende.models.Student;
import com.nitap.attende.models.Teacher;
import com.nitap.attende.onboardingscreen.feature.onboarding.OnBoardingActivity;
import com.nitap.attende.pages.HomeActivity;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class LoginUtils {

    public  Activity activity = null;
    public  Context context = null;
    public static   String SERVER_CLIENT_ID = "305352247527-l8grdm5f3nn1c47a93m8oek40r7851er.apps.googleusercontent.com";
    public  long mFileDownloadedId;
    public  boolean hasLeft = false;
    public  final int RC_SIGN_IN = 123;
    public  GoogleSignInClient mGoogleSignInClient;
    public  FirebaseAuth mAuth;
    public  String email;
    Set<String> sections = new ArraySet<String>();
    Set<String> teacherEmailIds = new LinkedHashSet<String>() { };
    Set<String> adminEmailIds = new LinkedHashSet<String>() { };
    static String rollno, sectionCode;
    static Student student;
    static Section section;
    static Class class1;
    static Teacher teacher;
    //static FaceFeatureInfo faceFeatureInfo;
    Button btnRegister,submitButton;
    //private DBHelper mydb ;
    public static ArrayList userLists;
    public static String facetagForFaceInfo ;
    public static int searchIdForFaceInfo ;
    LinearLayout signInButton;
    public static Uri myUri;
    public static String myRollno;
    public static boolean shouldTrain = false;
    public  boolean isEnabled = false;

    public void initData(Activity activity) {
        this.activity = activity;
        this.context = (Context) activity;
        this.isEnabled = false;
        this.hasLeft = false;

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.context, gso);
        mGoogleSignInClient.signOut();
    }

    public void cleanData() {
        this.context =null;
        this.activity =null;
        this.isEnabled=false;
        this.hasLeft=false;
    }

    public void safeUpdateUI() {
        display("1");
        MyUtils.removeConfigurationBuilder(context);
        MyConfiguration myConfiguration = MyUtils.getConfiguration(context);

        if(myConfiguration!=null) {
            if(myConfiguration.student!=null && myConfiguration.teacher==null  && myConfiguration.admin==null){
                hasLeft = true;
                context.startActivity(new Intent(context, HomeActivity.class));
                activity.finish();
                cleanData();

            }else if(myConfiguration.student==null && myConfiguration.teacher!=null  && myConfiguration.admin==null) {
                hasLeft = true;
                context.startActivity(new Intent(context,TeacherDashboardActivity.class));
                activity.finish();
                cleanData();

            }else if(myConfiguration.student==null && myConfiguration.teacher==null  && myConfiguration.admin!=null){
                hasLeft = true;
                context.startActivity(new Intent(context,AdminActivity.class));
                activity.finish();
                cleanData();

                display("Exiting UpdateUi");

            }
        } else {
            context.startActivity(new Intent(context, OnBoardingActivity.class));
            activity.finish();
            cleanData();
        }








    }
    public void display(String msg) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
    }

    public void display(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public LoginUtils(Activity activity) {
        this.activity = activity;
        this.context = (Context) activity;
        this.isEnabled = false;
        this.hasLeft = false;
    }

    public  void signOut(Context context) {
        FirebaseAuth userAuth;
        GoogleSignInClient mGoogleSigninClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSigninClient = GoogleSignIn.getClient(context, gso);
        FirebaseAuth.getInstance().signOut();
        mGoogleSigninClient.signOut();
    }

    public void firebaseAuthWithGoogle(Context context,Activity activity,String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            email= Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                            checkIfUserExists(user);
                           // getSaltAndUpdateUI(context,activity,user);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);
                            isEnabled = true;
                        }

                    }
                });
    }

    private void checkIfUserExists(FirebaseUser user) {
        String email = mAuth.getCurrentUser().getEmail();
        String[] contents = Objects.requireNonNull(email).split("@");
        if (contents.length == 2 && Objects.equals(contents[1], "student.nitandhra.ac.in")) {
            checkIfStudentExists();
        } else {
            display(context,"checking if a admin");
            checkIfUserIsAdmin();
        }
    }


    public void getSaltAndUpdateUI(Context context,Activity activity,FirebaseUser user) {

        DatabaseReference saltRef = FirebaseDatabase.getInstance().getReference().child("salt");
        saltRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                display(context,"Salt found");
                String salt = dataSnapshot.getValue(String.class);
                MyUtils.setSalt(context,salt);
                updateUI(context,activity,user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Salt not found", Toast.LENGTH_SHORT).show();
                MyUtils.setSalt(context,"NO_SALT");

                //updateUI(context,activity,user);
            }
        });


    }


    public void updateUI(Context context, Activity activity,FirebaseUser currentUser) {
        display(context,"1");
        if (currentUser == null) {
            return;
        }
        if (hasLeft) {
            return;
        }
        display(context,"2");
        MyConfiguration myConfiguration = MyUtils.getConfiguration(context);

        if(myConfiguration==null ){
            display(context,"3");
            //determine email and register
            String email = currentUser.getEmail();
            String[] contents = Objects.requireNonNull(email).split("@");
            if (contents.length == 2 && Objects.equals(contents[1], "student.nitandhra.ac.in")) {
                checkIfStudentExists();
            } else {
                display(context,"checking if a admin");
                checkIfUserIsAdmin();
            }




        }else if(myConfiguration.student!=null && myConfiguration.admin==null  && myConfiguration.teacher==null){
            hasLeft = true;
            context.startActivity(new Intent(context,HomeActivity.class));
            activity.finish();
            cleanData();


        }else if(myConfiguration.student==null && myConfiguration.admin!=null  && myConfiguration.teacher==null) {
            hasLeft = true;
            context.startActivity(new Intent(context,AdminActivity.class));
            activity.finish();
            cleanData();

        }else if(myConfiguration.student==null && myConfiguration.admin==null  && myConfiguration.teacher!=null){
            hasLeft = true;
            context.startActivity(new Intent(context,TeacherDashboardActivity.class));
            activity.finish();
            cleanData();



        }






    }

    public void checkIfStudentExists() {
        display("Checking if student exists");
        String[] contents = mAuth.getCurrentUser().getEmail().split("@");
        String rollno = contents[0];
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("students").child(rollno);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    //TODO: Now student details already exist, hence get student object and store it;
                    MyStudent student = snapshot.getValue(MyStudent.class);
                    MyConfiguration myConfiguration = new MyConfiguration();
                    myConfiguration.student = new Student();
                    myConfiguration.teacher = null;
                    myConfiguration.admin = null;

                    myConfiguration.student.email = student.email;
                    myConfiguration.student.rollno = student.rollno;
                    myConfiguration.student.regno = student.regno;
                    myConfiguration.student.name = student.name;
                    myConfiguration.student.deviceHash = student.deviceHash;
                    myConfiguration.student.sectionId = student.sectionId;
                    myConfiguration.student.faceFeatureInfoString = student.faceFeatureInfoString;
                    myConfiguration.student.photoUrl = student.photoUrl;
                    MyUtils.saveConfigurationBuilder(context,myConfiguration);

                    // TODO:   fetchSectionDetails();

                } else {
                    //TODO: Now student credentials are not found, ask for credentials and upload, also save jsonString

                    // TODO :checkIfSectionExists();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void checkIfUserIsAdmin() {
        String adminId = mAuth.getCurrentUser().getEmail().replace(".","?");

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("admins").child(adminId);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //TODO:  Now admin exists, download admin object and save jsonString
                    MyUtils.removeConfigurationBuilder(LoginUtils.this.context);
                    MyConfiguration myConfiguration = new MyConfiguration();
                    myConfiguration.student=null;
                    myConfiguration.teacher=null;
                    myConfiguration.admin=new Admin();
                    myConfiguration.admin.name = mAuth.getCurrentUser().getDisplayName();
                    myConfiguration.admin.email = mAuth.getCurrentUser().getEmail();
                    MyUtils.saveConfigurationBuilder(context,myConfiguration);

                    DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference().child("getBranchOfATeacher").child(adminId);

                    branchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //DataSnapshot childSnapshot = snapshot.getChildren().get(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    checkIfAdminIsTeacher();
                    /*
                    hasLeft = true;
                    context.startActivity(new Intent(context,AdminActivity.class));
                    activity.finish();
                    cleanData();*/

                } else {
                    checkIfUserIsTeacher(context);
                   // Toast.makeText(context, "Account not authorised, try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkIfAdminIsTeacher() {
        String teacherID = mAuth.getCurrentUser().getEmail().replace(".","?");
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference().child("teachers").child(teacherID);
        teacherRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    //TODO: User is both Admin and Teacher
                    Teacher teacher = snapshot.getValue(Teacher.class);
                    MyConfiguration myConfiguration = new MyConfiguration();
                    myConfiguration.teacher = new Teacher();
                    myConfiguration.teacher = teacher;
                    assert myConfiguration.teacher != null;
                    myConfiguration.teacher.sectionInfos=new ArrayList<SectionInfo>();
                    MyUtils.saveConfigurationBuilder(context,myConfiguration);
                    display(context,"fetching section infos");
                    //LoginActivity.fetchSectionInfos(context,myConfiguration.teacher);

                } else {
                    checkIfUserIsAdmin();
                }
                teacherRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }



    public  void checkIfUserIsTeacher(Context context) {
        String email = mAuth.getCurrentUser().getEmail();
        String teacherId = email.replace(".","?");
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("teachers").child(teacherId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    //TODO:  Now teacher exists, download teacher object and save jsonString
                    Teacher teacher = snapshot.getValue(Teacher.class);
                    MyConfiguration myConfiguration = new MyConfiguration();
                    myConfiguration.teacher = new Teacher();
                    myConfiguration.teacher = teacher;
                    assert myConfiguration.teacher != null;
                    myConfiguration.teacher.sectionInfos=new ArrayList<SectionInfo>();
                    MyUtils.saveConfigurationBuilder(context,myConfiguration);
                    display(context,"fetching section infos");
                   // LoginActivity.fetchSectionInfos(context,myConfiguration.teacher);

                } else {
                    display("Unauthorized account, access denied");
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
