package com.example.sentimentanalysis.ImageProcessing;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.sentimentanalysis.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FacialExpressionRecognition {

    Interpreter interpreter;
    int INPUT_SIZE;
    int height = 0;
    int width = 0;
    GpuDelegate gpuDelegate;
    CascadeClassifier cascadeClassifier;

    public FacialExpressionRecognition(AssetManager assetManager, Context context, String modelPath, int inputSize) throws IOException {
        INPUT_SIZE = inputSize;

        Interpreter.Options options = new Interpreter.Options();
        gpuDelegate = new GpuDelegate();

        options.addDelegate(gpuDelegate);

        options.setNumThreads(4);

        interpreter = new Interpreter(loadModelFile(assetManager,modelPath),options);

        if(MainActivity.S_DEBUG_MODE) {
            Toast.makeText(context, "Facial Expression Model is loaded.", Toast.LENGTH_SHORT).show();
        }

        try {
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);

            File cascadeDir = context.getDir("cascade",Context.MODE_PRIVATE);

            File mCascadeFile = new File(cascadeDir,"haarcascade_frontalface_alt");

            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int byteRead;

            while ((byteRead=is.read(buffer)) != -1){
                os.write(buffer,0,byteRead);
            }

            is.close();
            os.close();

            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

            if(MainActivity.S_DEBUG_MODE) {
                Toast.makeText(context, "Facial Expression Classifier is loaded.", Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Mat recognizeImage(Mat mat_image){
        /*if(CameraOpenCVActivity.CAMERA_INDEX == 0){
            Core.flip(mat_image.t(),mat_image,1);
        } else if (CameraOpenCVActivity.CAMERA_INDEX == 1) {
            Core.flip(mat_image.t(),mat_image,0);
        }*/

        //Core.flip(mat_image.t(),mat_image,0);


        Mat grayscaleImage = new Mat();
        Imgproc.cvtColor(mat_image,grayscaleImage,Imgproc.COLOR_RGBA2GRAY);

        height=grayscaleImage.height();
        width=grayscaleImage.width();

        int absoluteFaceSize=(int)(height*0.1);

        MatOfRect faces = new MatOfRect();

        if(cascadeClassifier!=null){
            cascadeClassifier.detectMultiScale(grayscaleImage,faces,1.1,2,2,
                    new Size(absoluteFaceSize,absoluteFaceSize),new Size());
        }

        Rect[] faceArray=faces.toArray();

        for(int i=0;i<faceArray.length;i++){
            Imgproc.rectangle(mat_image,faceArray[i].tl(),faceArray[i].br(),new Scalar(0,255,0,255),2);

            Rect roi=new Rect((int) faceArray[i].tl().x,(int) faceArray[i].tl().y,
                    ((int) faceArray[i].br().x)-((int) faceArray[i].tl().x),
                    ((int) faceArray[i].br().y)-((int) faceArray[i].tl().y));

            Mat cropped_rgba=new Mat(mat_image,roi);

            Bitmap bitmap = Bitmap.createBitmap(cropped_rgba.cols(),cropped_rgba.rows(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(cropped_rgba,bitmap);

            Bitmap scaledBitmap=Bitmap.createScaledBitmap(bitmap,48,48,false);

            ByteBuffer byteBuffer = convertBitmapToByteBuffer(scaledBitmap);

            float[][] emotion=new float[1][1];

            interpreter.run(byteBuffer,emotion);

            float emotion_v=(float)Array.get(Array.get(emotion,0),0);

            MainActivity.S_EMOTION = get_emotion_text(emotion_v);

            /*Imgproc.putText(mat_image,emotion_s+" ("+emotion_v+")",
                    new Point((int)faceArray[i].tl().x+10,(int)faceArray[i].tl().y+20),
                    1,3,new Scalar(0,0,255,150),2);*/
        }

        /*if(CameraOpenCVActivity.CAMERA_INDEX == 0){
            Core.flip(mat_image.t(),mat_image,0);
        }
        else if (CameraOpenCVActivity.CAMERA_INDEX == 1) {
            Core.flip(mat_image.t(),mat_image,0);
        }*/

        //Core.flip(mat_image.t(),mat_image,0 );

        return mat_image;
    }

    private String get_emotion_text(float emotion_v) {
        if(emotion_v>=0 & emotion_v<0.5){
            return "Surprise";
        }
        else if(emotion_v>=0.5 & emotion_v <1.5){
            return "Fear";
        }
        else if(emotion_v>=1.5 & emotion_v <2.5){
            return "Angry";
        }
        else if(emotion_v>=2.5 & emotion_v <3.5){
            return "Neutral";
        }
        else if(emotion_v>=3.5 & emotion_v <4.5){
            return "Sad";
        }
        else if(emotion_v>=4.5 & emotion_v <5.5){
            return "Disgust";
        }
        else {
            return "Happy";
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap scaledBitmap) {
        ByteBuffer byteBuffer;
        int size_image=INPUT_SIZE;
        byteBuffer=ByteBuffer.allocateDirect(12 * size_image*size_image);

        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues=new int[size_image*size_image];
        scaledBitmap.getPixels(intValues,0,scaledBitmap.getWidth(),0,0,scaledBitmap.getWidth(),scaledBitmap.getHeight());
        int pixel=0;
        for(int i=0;i<size_image;++i){
            for (int j=0;j<size_image;++j){
                final int val=intValues[pixel++];

                byteBuffer.putFloat((((val>>16)&0xFF))/255.0f);
                byteBuffer.putFloat((((val>>8)&0xFF))/255.0f);
                byteBuffer.putFloat(((val & 0xFF))/255.0f);
            }
        }

        return byteBuffer;
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException{
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(modelPath);

        FileInputStream inputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();

        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }
}