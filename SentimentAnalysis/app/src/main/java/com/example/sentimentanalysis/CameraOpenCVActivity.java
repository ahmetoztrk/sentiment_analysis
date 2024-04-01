package com.example.sentimentanalysis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class CameraOpenCVActivity extends CameraActivity {

    private Mat mRgba;
    private Mat mGray;
    CameraBridgeViewBase cameraBridgeViewBase;
    public static int CAMERA_INDEX = 1;

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
        cameraBridgeViewBase.setCameraIndex(CAMERA_INDEX);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);

        if (OpenCVLoader.initLocal()) {
            Log.d("LOADED", "success");

            cameraBridgeViewBase.enableView();
        } else {
            Log.d("LOADED", "error");
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