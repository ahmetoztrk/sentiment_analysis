package com.example.sentimentanalysis.ImageProcessing;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sentimentanalysis.R;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CameraOpenCVActivity extends CameraActivity {

    public static int S_CAMERA_INDEX = 1;

    FacialExpressionRecognition facialExpressionRecognition;

    CameraBridgeViewBase cameraBridgeViewBase;

    Mat mRgba, mGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        getPermission();

        init();

        setCameraViewListener();
    }

    private void getPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            getPermission();
        }
    }

    private void init() {
        cameraBridgeViewBase = findViewById(R.id.opencv_java_camera_2_view);
        cameraBridgeViewBase.setCameraIndex(S_CAMERA_INDEX);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);

        initOpenCV();

        try {
            int inputSize = 48;

            facialExpressionRecognition = new FacialExpressionRecognition(getAssets(), CameraOpenCVActivity.this, "model.tflite", inputSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initOpenCV(){
        if(OpenCVLoader.initLocal()){
            if(MainActivity.S_DEBUG_MODE) {
                Toast.makeText(this, "Application is starting...", Toast.LENGTH_SHORT).show();
            }

            cameraBridgeViewBase.enableView();
        }else{
            if(MainActivity.S_DEBUG_MODE) {
                Toast.makeText(this, "Application failed to start!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setCameraViewListener() {
        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                mRgba = new Mat(height, width, CvType.CV_8UC4);
                mGray = new Mat(height, width, CvType.CV_8UC1);
            }

            @Override
            public void onCameraViewStopped() {
                mRgba.release();
                mGray.release();
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                mRgba = inputFrame.rgba();
                mGray = inputFrame.gray();

                mRgba = facialExpressionRecognition.recognizeImage(mRgba);

                return mRgba;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraBridgeViewBase.enableView();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraBridgeViewBase.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cameraBridgeViewBase.disableView();
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {

        return Collections.singletonList(cameraBridgeViewBase);
    }
}