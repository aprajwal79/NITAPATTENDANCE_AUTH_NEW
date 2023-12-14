package com.nitap.attende;

import static com.ttv.facerecog.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.nitap.attende.models.MyConfiguration;
import com.nitap.attende.pages.HomeActivity;
import com.ttv.face.FaceFeatureInfo;
import com.ttv.face.FaceResult;
import com.ttv.facerecog.DBHelper;
import com.ttv.facerecog.FaceEntity;
import com.ttv.facerecog.ImageRotator;
//import com.ttv.facerecog.R;
import com.ttv.facerecog.R;
import com.ttv.facerecog.Utils;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;


public class ReUploadActivity extends AppCompatActivity {

    private DBHelper mydb ;
    public static ArrayList userLists;
    Button btnRegister, submitButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userLists = new ArrayList(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        Toast.makeText(getApplicationContext(), "RE UPLOAD ACTIVITY", Toast.LENGTH_SHORT).show();
        this.mydb = new DBHelper(this);
        this.mydb = new DBHelper((Context)this);


        submitButton = findViewById(id.button_next);

        btnRegister = findViewById(id.upload_btn);
        submitButton.setEnabled(false);
        btnRegister.setEnabled(true);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.PICK");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(FaceRecognitionActivity.this, HomeActivity.class));
                //finish();
            }
        });


    }

    void display(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {



        if (requestCode == 1 && resultCode == -1) {
            try {
                Context var10000 = (Context) com.nitap.attende.MainActivity.context;
                Uri var10001 = data != null ? data.getData() : null;
                if (data == null) {
                    //display("DATA NULL");
                } else {
                    display(data.getData().toString());
                }
                Intrinsics.checkNotNull(var10001);
                Bitmap var20 = ImageRotator.getCorrectlyOrientedImage(var10000, var10001);
                if (var20 == null) {
                   // display("BITMAP NULL");
                } else {
                    display(var20.toString());
                }
                Intrinsics.checkNotNullExpressionValue(var20, "ImageRotator.getCorrectl…Image(this, data?.data!!)");
                Bitmap bitmap = var20;
                List<FaceResult> var21 = com.nitap.attende.MainActivity.faceEngine.detectFace(bitmap);
                Intrinsics.checkNotNullExpressionValue(var21, "FaceEngine.getInstance(this).detectFace(bitmap)");
                if (var21 == null) {
                    //display("FACERESULT NULL");
                } else {
                    display(Objects.toString(var21.size()));
                }
                final List faceResults = var21;
                Collection var6 = (Collection)faceResults;
                if (var6.size() == 1) {
                    com.nitap.attende.MainActivity.faceEngine.extractFeature(bitmap, true, faceResults);
                   // display("FEATURES EXTRACTED");
                    // StringCompanionObject var7 = StringCompanionObject.INSTANCE;
                    String var8 = "User%03d";
                    Object[] var22 = new Object[1];
                    ArrayList var10003 = userLists;

                    if (var10003 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("userLists");
                    }

                    var22[0] = var10003.size() + 1;
                    Object[] var9 = var22;
                    String var23 = String.format(var8, Arrays.copyOf(var9, var9.length));
                    Intrinsics.checkNotNullExpressionValue(var23, "java.lang.String.format(format, *args)");
                    String userName = var23;
                    Rect cropRect = Utils.getBestRect(bitmap.getWidth(), bitmap.getHeight(), ((FaceResult)faceResults.get(0)).rect);
                    final Bitmap headImg = Utils.crop(bitmap, cropRect.left, cropRect.top, cropRect.width(), cropRect.height(), 120, 120);
                    View inputView = LayoutInflater.from(this).inflate(R.layout.dialog_input_view, (ViewGroup)null, false);
                    //final EditText editText = (EditText)inputView.findViewById(R.id.et_user_name);
                    ImageView ivHead = (ImageView)inputView.findViewById(R.id.iv_head);
                    ivHead.setImageBitmap(headImg);

                    Intrinsics.checkNotNullExpressionValue(var10000, "editText");
                    String s = MyUtils.getConfigurationBuilder(getApplicationContext()).student.rollno;//var10000.getText().toString();

                    boolean exists = false;
                    Iterator var5 = com.ttv.facerecog.MainActivity.Companion.getUserLists().iterator();

                    while(var5.hasNext()) {
                        FaceEntity user = (FaceEntity)var5.next();
                        if (TextUtils.equals((CharSequence)user.userName, (CharSequence)s)) {
                            exists = true;
                            break;
                        }
                    }


                    DBHelper var91 = mydb;
                    Intrinsics.checkNotNull(var9);
                    int user_id = var91.insertUser(s, headImg, ((FaceResult)faceResults.get(0)).feature);
                    FaceEntity face = new FaceEntity(user_id, s, headImg, ((FaceResult)faceResults.get(0)).feature);
                    com.ttv.facerecog.MainActivity.Companion.getUserLists().add(face);

                    FaceFeatureInfo faceFeatureInfo = new FaceFeatureInfo(user_id, ((FaceResult)faceResults.get(0)).feature);

                    boolean isSamePhoto = false;
                    isSamePhoto = test(faceFeatureInfo);
                    if (!isSamePhoto) {
                        display("Authentication failed, please upload the same photo");

                        return;
                    }


                    com.nitap.attende.MainActivity.faceEngine.registerFaceFeature(faceFeatureInfo);
                    Toast.makeText(getApplicationContext(), "Registered with FaceEngine", Toast.LENGTH_SHORT).show();

                    MyConfiguration myConfiguration = MyUtils.getConfigurationBuilder(getApplicationContext());
                    MyUtils.saveConfiguration(getApplicationContext(),myConfiguration);
                    MyUtils.removeConfigurationBuilder(getApplicationContext());

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    //confirmUpdateDialog.cancel();


                } else {
                    var6 = (Collection)faceResults;
                    if (var6.size() > 1) {
                        Toast.makeText((Context)this, (CharSequence)"Multiple face detected!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText((Context)this, (CharSequence)"No face detected!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception var13) {
                var13.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean test(FaceFeatureInfo faceFeatureInfo) {
        String reUploadedString = MyUtils.getStringFromObject(faceFeatureInfo.getFeatureData());
        String existingString = MyUtils.getConfigurationBuilder(this).student.faceFeatureInfoString;
        if(existingString.equals(reUploadedString)) {
            Toast.makeText(this, "BOTH SAME", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            Toast.makeText(this, "BOTH DIFFERENT", Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

}