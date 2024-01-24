package com.nitap.attende;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.airbnb.lottie.L;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nitap.attende.activities.OnboardingExample1Activity;
import com.nitap.attende.activities.OnboardingFinishActivity;
import com.nitap.attende.models.Admin;
import com.nitap.attende.models.MyConfiguration;
import com.nitap.attende.models.MyStudent;
import com.nitap.attende.models.Section;
import com.nitap.attende.models.SectionInfo;
import com.nitap.attende.models.Student;
import com.nitap.attende.models.Class;
import com.nitap.attende.models.Teacher;
import com.nitap.attende.pages.HomeActivity;


import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class LoginActivity extends AppCompatActivity {

    public static Activity activity = null;
    public static Context context = null;
    public static   String SERVER_CLIENT_ID = "305352247527-l8grdm5f3nn1c47a93m8oek40r7851er.apps.googleusercontent.com";
    public static long mFileDownloadedId;
    public static boolean hasLeft = false;
    public static final int RC_SIGN_IN = 123;
    public static GoogleSignInClient mGoogleSignInClient;
    public static FirebaseAuth mAuth;
    public static String email;
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
    public static boolean isEnabled = false;

    public LoginUtils l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        LoginActivity.activity = this;
        LoginActivity.context = this;
        LoginActivity.isEnabled = false;
        LoginActivity.hasLeft = false;


        l=new LoginUtils(this);

        MyUtils.removeConfigurationBuilder(l.context);



        l.hasLeft = false;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        l.mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        l.mGoogleSignInClient.signOut();

        l.mAuth = FirebaseAuth.getInstance();
        if(l.mAuth.getCurrentUser()==null) {
            l.isEnabled = true;
            } else {
            l.isEnabled = false;
        }

       // signInButton = findViewById(R.id.google_btn);
/*
        signInButton.setOnClickListener(v -> {
            if(isEnabled) {
                isEnabled = false;
                display("Verifying your details, please wait");
                try {
                    signIn();
                } catch (Throwable th) {
                    th.printStackTrace();
                    Toast.makeText(this, th.getMessage(), Toast.LENGTH_SHORT).show();
                }


            } else {
                display("Processing, try again later");
            }

        });
*/

        //Call at end of OnCreate()
       // safeUpdateUI();

        //Handle new login
        //User credentials not saved locally
        //Fetch user from server or register new user
        //Perform Google oAuth
        performGoogleSignInClicked();


    }

    private void performGoogleSignInClicked() {
        Toast.makeText(this,"SignIn using your authorised account.",Toast.LENGTH_LONG).show();
        l.signOut(l.context);
        Intent signInIntent = l.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, l.RC_SIGN_IN);
    }

    public static void safeUpdateUI() {
        display("1");
        MyUtils.removeConfigurationBuilder(context);
        MyConfiguration myConfiguration = MyUtils.getConfiguration(context);

         if(myConfiguration!=null) {
             if(myConfiguration.student!=null && myConfiguration.teacher==null  && myConfiguration.admin==null){
                 hasLeft = true;
                 context.startActivity(new Intent(context,HomeActivity.class));
                 activity.finish();

             }else if(myConfiguration.student==null && myConfiguration.teacher!=null  && myConfiguration.admin==null) {
                 hasLeft = true;
                 context.startActivity(new Intent(context,TeacherDashboardActivity.class));
                 activity.finish();

             }else if(myConfiguration.student==null && myConfiguration.teacher==null  && myConfiguration.admin!=null){
                 hasLeft = true;
                 context.startActivity(new Intent(context,AdminActivity.class));
                 activity.finish();

                 display("Exiting UpdateUi");

             }
         } else {
             context.startActivity(new Intent(context, OnboardingExample1Activity.class));
             activity.finish();
         }








    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = l.mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(l.context,l.activity,currentUser);
        }


    }

    public void signIn(Context context) {

        l.signOut(l.context);
        Intent signInIntent = l.mGoogleSignInClient.getSignInIntent();
        signInIntent.putExtra("requestCode", RC_SIGN_IN);
        l.context.startActivity(signInIntent);



/*



        //GetGoogleIdOption googleIdOption = new GetGoogleIdOption(SERVER_CLIENT_ID,null,false,null,null,false,false);
            GetGoogleIdOption googleIdOption = DummyActivity.Companion.staticMethod();

            //display("got gid static");
          //  GetGoogleIdOption googleIdOption  =  new GetGoogleIdOption(SERVER_CLIENT_ID,"",true,"",null,false,false);
               // .setServerClientId(SERVER_CLIENT_ID)
               // .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
        //display("1");
        CredentialManager credentialManager = CredentialManager.create(getApplicationContext());

        credentialManager.getCredentialAsync(
                this,
                request,
                        null
                        , ContextCompat.getMainExecutor(this),
                        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                            @Override
                            public void onResult(GetCredentialResponse result) {
                                display("onresult");
                                handleSignIn(result);
                            }

                            @Override
                            public void onError(GetCredentialException e) {
                                display("onerror");
                                display(e.getMessage());
                                e.printStackTrace();
                                handleFailure(e);
                            }
                        }
                );

*/


    }

    private void handleFailure(GetCredentialException e) {
        display(e.getMessage());
        e.printStackTrace();
    }
/*
    @SuppressLint("RestrictedApi")
    private void handleSignIn(GetCredentialResponse result) {
        Credential credential = result.getCredential();
        if (credential instanceof CustomCredential) {
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate on your server
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());
                firebaseAuthWithGoogle(this,activity,googleIdTokenCredential.getIdToken());
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e("TAG", "Unexpected type of credential");
            }
        } else {
            // Catch any unrecognized credential type here.
            Log.e("TAG", "Unexpected type of credential");
        }
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == l.RC_SIGN_IN && l.mAuth.getCurrentUser()==null)  {
            Toast.makeText(l.context, "Got Google account", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account == null){
                    Toast.makeText(l.context, "Failed to get the account", Toast.LENGTH_SHORT).show();
                }
                l.email = account.getEmail();
                display(l.email);
                firebaseAuthWithGoogle(l.context,l.activity,account.getIdToken());
                //firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(l.context, ""+ e, Toast.LENGTH_SHORT).show();
                l.isEnabled = true;
                //updateUI(null);
            }
        }



    }

    public void firebaseAuthWithGoogle(Context context,Activity activity,String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        l.mAuth.signInWithCredential(credential)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = l.mAuth.getCurrentUser();
                                getSaltAndUpdateUI(l.context,l.activity,user);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(l.context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            l.isEnabled = true;
                        }

                    }
                });
    }


    private void getSaltAndUpdateUI(Context context,Activity activity,FirebaseUser user) {

        DatabaseReference saltRef = FirebaseDatabase.getInstance().getReference().child("salt");
        saltRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                display(l.context,"Salt found");
                String salt = dataSnapshot.getValue(String.class);
                MyUtils.setSalt(l.context,salt);
                updateUI(l.context,l.activity,user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(l.context, "Salt not found", Toast.LENGTH_SHORT).show();
                MyUtils.setSalt(l.context,"NO_SALT");
                updateUI(l.context,l.activity,user);
            }
        });

    }

    private void updateUI(Context context, Activity activity,FirebaseUser currentUser) {
        l.updateUI(l.context,l.activity,currentUser);
        /*
        display(l.context,"1");
       if (currentUser == null) {
           return;
       }
       if (l.hasLeft) {
           return;
       }
display(context,"2");
        MyConfiguration myConfiguration = MyUtils.getConfiguration(context);

        if(myConfiguration==null ){
            display(l.context,"3");
            //determine email and register
            String email = currentUser.getEmail();
            String[] contents = Objects.requireNonNull(email).split("@");
            if (contents.length == 2 && Objects.equals(contents[1], "student.nitandhra.ac.in")) {
                checkIfStudentExists();
            } else {
                display(context,"checking if a teacher");
                checkIfUserIsTeacher(context);
            }




        }else if(myConfiguration.student!=null && myConfiguration.teacher==null  && myConfiguration.admin==null){
            hasLeft = true;
            context.startActivity(new Intent(context,HomeActivity.class));


        }else if(myConfiguration.student==null && myConfiguration.teacher!=null  && myConfiguration.admin==null) {
            hasLeft = true;
            context.startActivity(new Intent(context,TeacherDashboardActivity.class));
            activity.finish();

        }else if(myConfiguration.student==null && myConfiguration.teacher==null  && myConfiguration.admin!=null){
            hasLeft = true;
            context.startActivity(new Intent(context,AdminActivity.class));
            activity.finish();

            display(context,"Exiting UpdateUi");

        }




*/

    }

    public static void checkIfUserIsTeacher(Context context) {
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
                    LoginActivity.fetchSectionInfos(context,myConfiguration.teacher);

                } else {
                   checkIfUserIsAdmin();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void fetchSectionInfos(Context context,Teacher teacher) {
        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(context);
        assert myConfiguration != null;
        ArrayList<String> sectionIds = myConfiguration.teacher.sectionIds;
        ArrayList<SectionInfo> sectionInfos = new ArrayList<SectionInfo>();

        for (int i=0;i<sectionIds.size();i++) {
            String sectionId = sectionIds.get(i);
            SectionInfo sectionInfo = new SectionInfo();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("sections").child(sectionId);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    display(context,"got section info");
                    Section section = dataSnapshot.getValue(Section.class);
                    assert section != null;
                    sectionInfo.sectionId = section.sectionId;
                    sectionInfo.sectionName = section.sectionName;
                    sectionInfo.classId = section.classId;

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("classes").child(section.classId);


                    ref1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            display(context,"got class info");
                            Class class1 = dataSnapshot.getValue(Class.class);
                            assert class1 != null;
                            sectionInfo.degree = class1.degree;
                            sectionInfo.branch = class1.branch;
                            sectionInfo.year = class1.year;
                            sectionInfo.sem = class1.sem;

                            // ADD SECTION INFO OBJECT TO LIST
                            sectionInfos.add(sectionInfo);

                            if(sectionInfos.size() == sectionIds.size()) {
                                //Completed all tasks
                                myConfiguration.teacher.sectionInfos = sectionInfos;
                                //MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);
                                //MyConfiguration myConfiguration1 = MyUtils.getConfigurationBuilder(getApplicationContext());
                                MyUtils.saveConfiguration(context,myConfiguration);
                                MyUtils.removeConfigurationBuilder(context);

                                hasLeft=true;
                                context.startActivity(new Intent(context,TeacherDashboardActivity.class));
                                activity.finish();



                            } else {
                                display(context,"waiting for loop end");
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            display(context,"failed to get class info");
                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    display(context,"Failed to get section info");
                }
            });



        }



    }

    private static void checkIfUserIsAdmin() {
        String adminId = mAuth.getCurrentUser().getEmail().replace(".","?");

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("admins").child(adminId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //TODO:  Now admin exists, download admin object and save jsonString
                    MyConfiguration myConfiguration = new MyConfiguration();
                    myConfiguration.admin = snapshot.getValue(Admin.class);
                    MyUtils.saveConfiguration(context,myConfiguration);

                    hasLeft = true;
                    context.startActivity(new Intent(context,AdminActivity.class));
                    activity.finish();

                } else {
                    Toast.makeText(context, "Account not authorised, try again", Toast.LENGTH_SHORT).show();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void checkIfSectionExists() {
        String rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        String sectionId = rollno.substring(0,rollno.length()-2);
        Toast.makeText(context,"sec id " + sectionId, Toast.LENGTH_SHORT).show();
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("sections").child(sectionId);
        courseRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "section exists", Toast.LENGTH_SHORT).show();
                    // TODO: Now section details exist, hence get section details, then class details and redirect to photo upload
                    Section section = snapshot.getValue(Section.class);
                    MyConfiguration myConfiguration = new MyConfiguration();
                    myConfiguration.student = new Student();
                    myConfiguration.teacher =null;
                    myConfiguration.admin = null;

                    myConfiguration.student.max = section.max;
                    myConfiguration.student.startRollno = section.startRollno;
                    myConfiguration.student.endRollno = section.endRollno;
                    myConfiguration.student.sectionId = section.sectionId;
                    myConfiguration.student.sectionName = section.sectionName;
                    myConfiguration.student.classId = section.classId;

                    MyUtils.saveConfigurationBuilder(context,myConfiguration);
                    checkIfClassExists();


                } else {
                    // TODO: Now section details not found, display error
                   Toast.makeText(context, "Section not found, contact admin", Toast.LENGTH_SHORT).show();

                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void checkIfClassExists() {
        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(context);
        String classId = myConfiguration.student.classId;

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("classes").child(classId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(context, "class exists", Toast.LENGTH_SHORT).show();
                    // TODO: Now section and class exist,save class details and register new student
                    Class class1 = snapshot.getValue(Class.class);
                    myConfiguration.student.courses = class1.courses;
                    myConfiguration.student.degree = class1.degree;
                    myConfiguration.student.branch = class1.branch;
                    myConfiguration.student.year = class1.year;
                    myConfiguration.student.sem = class1.sem;

                    MyUtils.saveConfigurationBuilder(context,myConfiguration);

                    initialiseStudentCredentials();


                } else {
                    // TODO: Now section found but no class found, display error message
                    //MyUtils.removeAll(getApplicationContext());
                    Toast.makeText(context, "Class not found, contact admin", Toast.LENGTH_SHORT).show();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void initialiseStudentCredentials() {

        String email = mAuth.getCurrentUser().getEmail();

        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(context);
        myConfiguration.student.email = mAuth.getCurrentUser().getEmail();
        myConfiguration.student.rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        myConfiguration.student.regno = null;
        myConfiguration.student.name = mAuth.getCurrentUser().getDisplayName();

        MyUtils.saveConfigurationBuilder(context,myConfiguration);

        uploadPhoto();


    }

    private static void uploadPhoto() {
        Toast.makeText(context, "Upload photo to complete the registration", Toast.LENGTH_SHORT).show();
        hasLeft = true;
        rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        context.startActivity(new Intent(context,FaceRecognitionActivity.class));
        activity.finish();
    }

    private static void fetchSectionDetails() {
        display("fetching section details");
        String rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        String sectionId = rollno.substring(0,rollno.length()-2);

        Toast.makeText(context, "section id "+ sectionId, Toast.LENGTH_SHORT).show();
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("sections").child(sectionId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // TODO: Now section details exist, hence get section details, then class details and register face engine
                    Section section = snapshot.getValue(Section.class);
                    MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(context);
                    myConfiguration.student.sectionName = section.sectionName;
                    myConfiguration.student.classId = section.classId;
                    myConfiguration.student.max = section.max;
                    myConfiguration.student.startRollno = section.startRollno;
                    myConfiguration.student.endRollno = section.endRollno;
                    MyUtils.saveConfigurationBuilder(context,myConfiguration);

                    fetchClassDetails();
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void fetchClassDetails() {
        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(context);
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("classes").child(myConfiguration.student.classId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // TODO: Now section and class exist,save class details and register new student
                    Class class1 = snapshot.getValue(Class.class);
                    myConfiguration.student.degree = class1.degree;
                    myConfiguration.student.branch = class1.branch;
                    myConfiguration.student.year = class1.year;
                    myConfiguration.student.sem = class1.sem;
                    myConfiguration.student.courses = class1.courses;
                    MyUtils.saveConfigurationBuilder(context,myConfiguration);

                display("TODO: Load FaceFeatureInfoString into model");
                //TODO: Load FaceFeatureInfoString into model
                   // downloadPhoto();

                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void downloadPhoto() {
        String rollno = MyUtils.getConfigurationBuilder(context).student.rollno;
        String url = MyUtils.getConfigurationBuilder(context).student.photoUrl;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("students").child(rollno);

        downloadFile(context,rollno,".jpeg", Environment.DIRECTORY_DOWNLOADS,url);
    }


    static BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadedID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadedID == mFileDownloadedId) {
                String rollno = MyUtils.getConfigurationBuilder(context).student.rollno;
                display("Please ReUpload the same image "+rollno+".jpg to continue");

                hasLeft=true;
                display("TODO: Load FaceFeatureInfoString into model");
                //TODO: Load FaceFeatureInfoString into model
               // startActivity(new Intent(getApplicationContext(),ReUploadActivity.class));
               // finish();
            }
        }
    };


    private static void downloadFile(Context applicationContext, String filename, String extension, String directoryDownloads, String url) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
       // request.setDestinationInExternalFilesDir(applicationContext,"directory",filename+extension);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename + extension);
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        mFileDownloadedId = downloadManager.enqueue(request);
        applicationContext.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }




    public static void checkIfStudentExists() {
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

                    fetchSectionDetails();

                } else {
                    //TODO: Now student credentials are not found, ask for credentials and upload, also save jsonString
                    checkIfSectionExists();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public static void signOut(Context context) {
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

   public static void display(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static void display(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        LoginActivity.context =null;
        LoginActivity.activity =null;
        LoginActivity.isEnabled=false;
        LoginActivity.hasLeft=false;
        super.onDestroy();
    }




}