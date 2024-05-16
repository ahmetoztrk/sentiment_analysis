package com.example.sentimentanalysis.ImageProcessing;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentimentanalysis.Analyzing.FilterPage.FilterPageActivity;
import com.example.sentimentanalysis.Constants;
import com.example.sentimentanalysis.MainActivity;
import com.example.sentimentanalysis.ProfilePageActivity;
import com.example.sentimentanalysis.R;
import com.example.sentimentanalysis.StatisticsPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    FacialExpressionRecognition facialExpressionRecognition;

    ImageView emotionPhotoImageView;
    Button camBtn, statBtn, guiTestBtn, exitBtn, profileBtn;
    TextView welcomeTextView, currentMoodTextView;

    Mat mat = null;
    Bitmap bitmap;

    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getPermission();

        initOpenCV();

        init();

        initFirebase();

        setButtonsListener();
    }

    private void getPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (checkSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 123);
            }
        }else {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }

            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            getPermission();
        }
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null){
            FirebaseAPIGetData(user.getUid());
        }
    }

    private void initOpenCV(){
        if(OpenCVLoader.initLocal()){
            if(Constants.S_DEBUG_MODE){
                Toast.makeText(this, "Application is starting...", Toast.LENGTH_SHORT).show();
            }

            mat = new Mat();
        }else{
            if(Constants.S_DEBUG_MODE){
                Toast.makeText(this, "Application failed to start!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init(){
        welcomeTextView = findViewById(R.id.welcome_text_view);
        emotionPhotoImageView = findViewById(R.id.emotion_photoimage_view);
        camBtn = findViewById(R.id.camera_btn);
        statBtn = findViewById(R.id.statistics_btn);
        guiTestBtn = findViewById(R.id.gui_test_btn);
        exitBtn = findViewById(R.id.exit_btn);
        profileBtn = findViewById(R.id.profile_btn);
        currentMoodTextView = findViewById(R.id.current_mood_tv);

        currentMoodTextView.setText(String.format("'%s'", Constants.S_EMOTION));
    }

    private void setButtonsListener(){
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,101);
            }
        });

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, StatisticsPageActivity.class);
                startActivity(intent);
            }
        });

        guiTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, FilterPageActivity.class);
                startActivity(intent);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();

                Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, ProfilePageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            int inputSize = 48;

            facialExpressionRecognition = new FacialExpressionRecognition(getAssets(), HomePageActivity.this, "model.tflite", inputSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(requestCode==101 && data != null){
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            emotionPhotoImageView.setImageBitmap(bitmap);

            Utils.bitmapToMat(bitmap,mat);

            mat = facialExpressionRecognition.recognizeImage(mat);
            currentMoodTextView.setText(String.format("'%s'", Constants.S_EMOTION));

            Utils.matToBitmap(mat,bitmap);
            emotionPhotoImageView.setImageBitmap(bitmap);
        }
    }

    private void FirebaseAPIGetData(String uid){
        reference = FirebaseDatabase.getInstance().getReference("users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    if(Objects.equals(snp.getKey(), "username")){
                        welcomeTextView.setText(snp.getValue().toString() + "!");
                    }else if(Objects.equals(snp.getKey(), "age")){
                        String current = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String[] currentArr = new String[3];

                        if(current != null){
                            currentArr = current.split("/");
                        }

                        String birth = snp.getValue().toString();
                        String[] birthArr = new String[3];

                        if(birth != null){
                            birthArr = birth.split("/");
                        }

                        if (currentArr.length == 3 && birthArr.length == 3) {
                            int currentDay = Integer.parseInt(currentArr[0]);
                            int currentMonth = Integer.parseInt(currentArr[1]);
                            int currentYear = Integer.parseInt(currentArr[2]);

                            int birthDay = Integer.parseInt(birthArr[0]);
                            int birthMonth = Integer.parseInt(birthArr[1]);
                            int birthYear = Integer.parseInt(birthArr[2]);

                            age = currentYear - birthYear;

                            if (birthMonth > currentMonth || (birthMonth == currentMonth && birthDay > currentDay)) {
                                age--;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}