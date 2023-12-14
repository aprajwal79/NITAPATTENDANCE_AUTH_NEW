package com.nitap.attende;

import android.annotation.SuppressLint;
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
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

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
import com.nitap.attende.models.Admin;
import com.nitap.attende.models.MyConfiguration;
import com.nitap.attende.models.MyStudent;
import com.nitap.attende.models.Section;
import com.nitap.attende.models.SectionInfo;
import com.nitap.attende.models.Student;
import com.nitap.attende.models.Class;
import com.nitap.attende.models.Teacher;
import com.nitap.attende.pages.HomeActivity;
import com.ttv.face.FaceFeatureInfo;
import com.ttv.facerecog.DBHelper;
import com.ttv.facerecog.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class LoginActivity extends AppCompatActivity {


    public   String SERVER_CLIENT_ID = "305352247527-l8grdm5f3nn1c47a93m8oek40r7851er.apps.googleusercontent.com";
    long mFileDownloadedId;
    public static boolean hasLeft = false;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    String email;
    Set<String> sections = new ArraySet<String>();
    Set<String> teacherEmailIds = new LinkedHashSet<String>() { };
    Set<String> adminEmailIds = new LinkedHashSet<String>() { };
    static String rollno, sectionCode;
    static Student student;
    static Section section;
    static Class class1;
    static Teacher teacher;
    static FaceFeatureInfo faceFeatureInfo;
    Button btnRegister,submitButton;
    private DBHelper mydb ;
    public static ArrayList userLists;
    public static String facetagForFaceInfo ;
    public static int searchIdForFaceInfo ;
    LinearLayout signInButton;
    public static Uri myUri;
    public static String myRollno;
    public static boolean shouldTrain = false;
    public static boolean isEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hasLeft = false;
        MyUtils.removeConfigurationBuilder(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null) {
            isEnabled = true;
            } else {
            isEnabled = false;
        }

        signInButton = findViewById(R.id.google_btn);

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
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }


    }

    private void signIn() {

        signOut(getApplicationContext());
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);



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

    @SuppressLint("RestrictedApi")
    private void handleSignIn(GetCredentialResponse result) {
        Credential credential = result.getCredential();
        if (credential instanceof CustomCredential) {
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate on your server
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());
                firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e("TAG", "Unexpected type of credential");
            }
        } else {
            // Catch any unrecognized credential type here.
            Log.e("TAG", "Unexpected type of credential");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN && mAuth.getCurrentUser()==null)  {
            Toast.makeText(getApplicationContext(), "Got Google account", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account == null){
                    Toast.makeText(this, "Failed to get the account", Toast.LENGTH_SHORT).show();
                }
                 email = account.getEmail();
                display(email);
                firebaseAuthWithGoogle(account.getIdToken());
                //firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, ""+ e, Toast.LENGTH_SHORT).show();
                isEnabled = true;
                //updateUI(null);
            }
        }



    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                                getSaltAndUpdateUI(user);


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            isEnabled = true;
                        }

                    }
                });
    }


    private void getSaltAndUpdateUI(FirebaseUser user) {

        DatabaseReference saltRef = FirebaseDatabase.getInstance().getReference().child("salt");
        saltRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                display("Salt found");
                String salt = dataSnapshot.getValue(String.class);
                MyUtils.setSalt(getApplicationContext(),salt);
                updateUI(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Salt not found", Toast.LENGTH_SHORT).show();
                MyUtils.setSalt(getApplicationContext(),"NO_SALT");
                updateUI(user);
            }
        });

    }

    private void updateUI(FirebaseUser currentUser) {
        display("1");
       if (currentUser == null) {
           return;
       }
       if (hasLeft) {
           return;
       }
display("2");
        MyConfiguration myConfiguration = MyUtils.getConfiguration(this);

        if(myConfiguration==null ){
            display("3");
            //determine email and register
            String email = currentUser.getEmail();
            String[] contents = Objects.requireNonNull(email).split("@");
            if (contents.length == 2 && Objects.equals(contents[1], "student.nitandhra.ac.in")) {
                checkIfStudentExists();
            } else {
                display("checking if a teacher");
                checkIfUserIsTeacher();
            }




        }else if(myConfiguration.student!=null && myConfiguration.teacher==null  && myConfiguration.admin==null){
            hasLeft = true;
            startActivity(new Intent(this,HomeActivity.class));
            finish();

        }else if(myConfiguration.student==null && myConfiguration.teacher!=null  && myConfiguration.admin==null) {
            hasLeft = true;
            startActivity(new Intent(this,TeacherDashboardActivity.class));
            finish();

        }else if(myConfiguration.student==null && myConfiguration.teacher==null  && myConfiguration.admin!=null){
            hasLeft = true;
            startActivity(new Intent(this,AdminActivity.class));
            finish();

            display("Exiting UpdateUi");

        }






    }

    private void checkIfUserIsTeacher() {
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
                    myConfiguration.teacher.sectionInfos=new ArrayList<SectionInfo>();
                    MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);
                    display("fetching section infos");
                    fetchSectionInfos(myConfiguration.teacher);

                } else {
                   checkIfUserIsAdmin();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSectionInfos(Teacher teacher) {
        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(this);
        ArrayList<String> sectionIds = myConfiguration.teacher.sectionIds;
        ArrayList<SectionInfo> sectionInfos = new ArrayList<SectionInfo>();

        for (int i=0;i<sectionIds.size();i++) {
            String sectionId = sectionIds.get(i);
            SectionInfo sectionInfo = new SectionInfo();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("sections").child(sectionId);
            ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    display("got section info");
                    Section section = dataSnapshot.getValue(Section.class);
                    sectionInfo.sectionId = section.sectionId;
                    sectionInfo.sectionName = section.sectionName;
                    sectionInfo.classId = section.classId;

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("classes").child(section.classId);
                    ref1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            display("got class info");
                            Class class1 = dataSnapshot.getValue(Class.class);
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
                                MyUtils.saveConfiguration(getApplicationContext(),myConfiguration);
                                MyUtils.removeConfigurationBuilder(getApplicationContext());

                                hasLeft=true;
                                startActivity(new Intent(getApplicationContext(),TeacherDashboardActivity.class));
                                finish();



                            } else {
                                display("waiting for loop end");
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            display("failed to get class info");
                        }
                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    display("Failed to get section info");
                }
            });



        }



    }

    private void checkIfUserIsAdmin() {
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
                    MyUtils.saveConfiguration(getApplicationContext(),myConfiguration);

                    hasLeft = true;
                    startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Account not authorised, try again", Toast.LENGTH_SHORT).show();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkIfSectionExists() {
        String rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        String sectionId = rollno.substring(0,rollno.length()-2);
        Toast.makeText(this,"sec id " + sectionId, Toast.LENGTH_SHORT).show();
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("sections").child(sectionId);
        courseRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "section exists", Toast.LENGTH_SHORT).show();
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

                    MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);
                    checkIfClassExists();


                } else {
                    // TODO: Now section details not found, display error
                   Toast.makeText(getApplicationContext(), "Section not found, contact admin", Toast.LENGTH_SHORT).show();

                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkIfClassExists() {
        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(this);
        String classId = myConfiguration.student.classId;

        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("classes").child(classId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "class exists", Toast.LENGTH_SHORT).show();
                    // TODO: Now section and class exist,save class details and register new student
                    Class class1 = snapshot.getValue(Class.class);
                    myConfiguration.student.courses = class1.courses;
                    myConfiguration.student.degree = class1.degree;
                    myConfiguration.student.branch = class1.branch;
                    myConfiguration.student.year = class1.year;
                    myConfiguration.student.sem = class1.sem;

                    MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);

                    initialiseStudentCredentials();


                } else {
                    // TODO: Now section found but no class found, display error message
                    //MyUtils.removeAll(getApplicationContext());
                    Toast.makeText(LoginActivity.this, "Class not found, contact admin", Toast.LENGTH_SHORT).show();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialiseStudentCredentials() {

        String email = mAuth.getCurrentUser().getEmail();

        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(this);
        myConfiguration.student.email = mAuth.getCurrentUser().getEmail();
        myConfiguration.student.rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        myConfiguration.student.regno = null;
        myConfiguration.student.name = mAuth.getCurrentUser().getDisplayName();

        MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);

        uploadPhoto();


    }

    private void uploadPhoto() {
        Toast.makeText(getApplicationContext(), "Upload photo to complete the registration", Toast.LENGTH_SHORT).show();
        hasLeft = true;
        rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        startActivity(new Intent(this,FaceRecognitionActivity.class));
        finish();
    }

    private void fetchSectionDetails() {
        String rollno = mAuth.getCurrentUser().getEmail().split("@")[0];
        String sectionId = rollno.substring(0,rollno.length()-2);

        Toast.makeText(this, "section id "+ sectionId, Toast.LENGTH_SHORT).show();
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference().child("sections").child(sectionId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // TODO: Now section details exist, hence get section details, then class details and register face engine
                    Section section = snapshot.getValue(Section.class);
                    MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(getApplicationContext());
                    myConfiguration.student.sectionName = section.sectionName;
                    myConfiguration.student.classId = section.classId;
                    myConfiguration.student.max = section.max;
                    myConfiguration.student.startRollno = section.startRollno;
                    myConfiguration.student.endRollno = section.endRollno;
                    MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);

                    fetchClassDetails();
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchClassDetails() {
        MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(getApplicationContext());
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
                    MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);

                    downloadPhoto();

                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void downloadPhoto() {
        String rollno = MyUtils.getConfigurationBuilder(this).student.rollno;
        String url = MyUtils.getConfigurationBuilder(this).student.photoUrl;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("students").child(rollno);

        downloadFile(getApplicationContext(),rollno,".jpeg", Environment.DIRECTORY_DOWNLOADS,url);
    }


    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadedID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadedID == mFileDownloadedId) {
                String rollno = MyUtils.getConfigurationBuilder(getApplicationContext()).student.rollno;
                display("Please ReUpload the same image "+rollno+".jpg to continue");

                hasLeft=true;
                startActivity(new Intent(getApplicationContext(),ReUploadActivity.class));
                finish();
            }
        }
    };


    private void downloadFile(Context applicationContext, String filename, String extension, String directoryDownloads, String url) {

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
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




    private void checkIfStudentExists() {
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
                    MyUtils.saveConfigurationBuilder(getApplicationContext(),myConfiguration);

                    fetchSectionDetails();

                } else {
                    //TODO: Now student credentials are not found, ask for credentials and upload, also save jsonString
                    checkIfSectionExists();
                }
                courseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

    }







}