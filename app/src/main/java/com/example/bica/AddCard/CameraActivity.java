package com.example.bica.AddCard;

import static androidx.camera.core.AspectRatio.RATIO_16_9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.internal.utils.ImageUtil;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bica.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    PreviewView previewView;
    private Button startButton, stopButton, captureButton;
    ImageView imageView;
    String TAG = "MainActivity";
    ProcessCameraProvider processCameraProvider;
    //int lensFacing = CameraSelector.LENS_FACING_FRONT;
    int lensFacing = CameraSelector.LENS_FACING_BACK;
    ImageCapture imageCapture;
    private File outputDirectory;
    private Uri savedUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        startButton = (Button) findViewById(R.id.start_btn);
        stopButton = findViewById(R.id.stop_btn);
        captureButton = findViewById(R.id.capture_btn);
        imageView = findViewById(R.id.card_imgv);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);

        try {
            processCameraProvider = ProcessCameraProvider.getInstance(this).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CameraActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    bindPreview();
                    bindImageCapture();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCameraProvider.unbindAll();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCapture.takePicture(ContextCompat.getMainExecutor(CameraActivity.this),
                        new ImageCapture.OnImageCapturedCallback() {
                            @SuppressLint({"UnsafeOptInUsageError", "RestrictedApi"})
                            @Override
                            public void onCaptureSuccess(@NonNull ImageProxy image) {
                                System.out.println("test take pic " + image.getImage().toString());
                                int width = image.getWidth();
                                int height = image.getHeight();

                                System.out.println("test image width " + width);
                                System.out.println("test image height " + height);
                                byte[] byteImage = ImageUtil.jpegImageToJpegByteArray(image);

//                                @SuppressLint("RestrictedApi")
//                                byte[] jpegByteArray = ImageUtil.yuv_420_888toNv21(image);
                                BitmapFactory bitmapFactory = new BitmapFactory();
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                Bitmap bitmap = bitmapFactory.decodeByteArray(
                                        byteImage, /* offset= */ 0, byteImage.length);
                                System.out.println("test bitmap " + bitmap.getWidth());
                                System.out.println("test bitmap " + bitmap.getHeight());
                                // 지금 겁나 큼 풀 화면...
                                bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/3, 0, 1500, bitmap.getHeight());


                                imageView.setVisibility(View.VISIBLE);
                                previewView.setVisibility(View.GONE);
                                imageView.setImageBitmap(bitmap);
                                super.onCaptureSuccess(image);
                            }
                        }
                );
            }
        });
    }
//
//    static public Bitmap rotateBitmap(Bitmap bitmap, int degree){
//        Bitmap rotatedBitmap;
//        return rotatedBitmap;
//    }

    void bindPreview() {
//            previewView.setScaleType(PreviewView.ScaleType.FILL_CENTER);

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    void bindImageCapture() {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        imageCapture = new ImageCapture.Builder()
                .build();

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageCapture);
    }

    //    void bindImageAnalysis() {
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(lensFacing)
//                .build();
//        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                .build();
//        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(),
//                new ImageAnalysis.Analyzer() {
//                    @Override
//                    public void analyze(@NonNull ImageProxy image) {
//                        /*
//                        @SuppressLint("UnsafeExperimentalUsageError")
//                        Image mediaImage = image.getImage();
//                        */
//                        ///*
//                        @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError", "RestrictedApi"})
//                        byte[] byteImage = ImageUtil.jpegImageToJpegByteArray(image);
//
////                                @SuppressLint("RestrictedApi")
////                                byte[] jpegByteArray = ImageUtil.yuv_420_888toNv21(image);
//                        BitmapFactory bitmapFactory = new BitmapFactory();
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//
//                        Bitmap bitmap = bitmapFactory.decodeByteArray(
//                                byteImage, /* offset= */ 0, byteImage.length);
//
//
//                        int rotationDegrees = image.getImageInfo().getRotationDegrees();
//                        Log.d(TAG, Float.toString(rotationDegrees)); //90 //0, 90, 180, 90 //이미지를 바르게 하기위해 시계 방향으로 회전해야할 각도
//                        try{
//                            Matrix matrix = new Matrix();
//                            matrix.postRotate(rotationDegrees);
//                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Bitmap finalBitmap = bitmap;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageView.setImageBitmap(finalBitmap);
//                            }
//                        });
//
//                        image.close();
//                    }
//                }
//        );
//
//        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
//    }
    @Override
    protected void onPause() {
        super.onPause();
        processCameraProvider.unbindAll();
    }
}