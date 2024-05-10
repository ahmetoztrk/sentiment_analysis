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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentimentanalysis.Analyzing.FilterPage.FilterPageActivity;
import com.example.sentimentanalysis.R;
import com.example.sentimentanalysis.StatisticsActivity;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static String S_EMOTION = "No Emotion";

    FacialExpressionRecognition facialExpressionRecognition;

    ImageView emotionPhotoImageView;
    Button camBtn, statBtn, guiTestBtn;
    TextView currentMoodTextView;

    Mat mat = null;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();

        initOpenCV();

        init();

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

    private void initOpenCV(){
        if(OpenCVLoader.initLocal()){
            Toast.makeText(this, "Application is starting...", Toast.LENGTH_SHORT).show();

            mat = new Mat();
        }else{
            Toast.makeText(this, "Application failed to start!", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        emotionPhotoImageView = findViewById(R.id.emotion_photoimage_view);
        camBtn = findViewById(R.id.camera_btn);
        statBtn = findViewById(R.id.statistics_btn);
        guiTestBtn = findViewById(R.id.gui_test_btn);
        currentMoodTextView = findViewById(R.id.current_mood_tv);

        currentMoodTextView.setText(String.format("'%s'", S_EMOTION));
    }

    private void setButtonsListener(){
        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent init = new Intent(MainActivity.this, CameraOpenCVActivity.class);
                //startActivity(init);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,101);
            }
        });

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        guiTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilterPageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            int inputSize = 48;

            facialExpressionRecognition = new FacialExpressionRecognition(getAssets(), MainActivity.this, "model.tflite", inputSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(requestCode==101 && data != null){
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            emotionPhotoImageView.setImageBitmap(bitmap);

            Utils.bitmapToMat(bitmap,mat);

            mat = facialExpressionRecognition.recognizeImage(mat);
            currentMoodTextView.setText(String.format("'%s'", S_EMOTION));

            Utils.matToBitmap(mat,bitmap);
            emotionPhotoImageView.setImageBitmap(bitmap);
        }
    }
}