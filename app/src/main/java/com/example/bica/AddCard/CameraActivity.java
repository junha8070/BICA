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
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.member.LoginActivity;
import com.example.bica.member.MemberViewModel;
import com.example.bica.member.RegisterCardActivity;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {

    PreviewView previewView;
    private Button startButton, btn_detection_image, captureButton, btn_add_card;
    TextView text_info;
    EditText user_name, user_email, user_num, company_name, company_address, company_occupation, company_depart, company_position, card_memo;
    ImageView imageView;
    String TAG = "MainActivity";
    ProcessCameraProvider processCameraProvider;
    //int lensFacing = CameraSelector.LENS_FACING_FRONT;
    int lensFacing = CameraSelector.LENS_FACING_BACK;
    ImageCapture imageCapture;
    InputImage inputImage;
    ImageLabeler labeler;
    TextRecognizer recognizer;

    private BusinessCardViewModel businessCardViewModel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        startButton = (Button) findViewById(R.id.start_btn);
        btn_detection_image = findViewById(R.id.btn_detection_image);
        captureButton = findViewById(R.id.capture_btn);
        imageView = findViewById(R.id.card_imgv);
        text_info = findViewById(R.id.text_info);
        btn_add_card = findViewById(R.id.btn_add_card);

        user_name = findViewById(R.id.user_name);
        user_num = findViewById(R.id.user_num);
        user_email = findViewById(R.id.user_email);
        company_name = findViewById(R.id.company_name);
        company_address = findViewById(R.id.company_address);
        company_depart = findViewById(R.id.company_depart);
        company_position = findViewById(R.id.company_position);
        company_occupation = findViewById(R.id.company_occupation);
        card_memo = findViewById(R.id.card_memo);

        startButton.setVisibility(View.VISIBLE);
        captureButton.setVisibility(View.GONE);
        btn_detection_image.setVisibility(View.GONE);
        recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());  // 글자 인식
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS); // 라벨 인식
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
                    startButton.setVisibility(View.GONE);
                    bindPreview();
                    bindImageCapture();
                    captureButton.setVisibility(View.VISIBLE);
                }

            }
        });

        // IMAGE DETECTION 버튼
        btn_detection_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputImage != null) {
                    TextRecognition(recognizer);
                    TextLabeling(labeler);
                } else {
                    Toast.makeText(CameraActivity.this, "명함 사진이 존재하지 않습니다. 이미지 촬영을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureButton.setVisibility(View.GONE);
                btn_detection_image.setVisibility(View.VISIBLE);
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

                                // Proxy image -> bitmap
                                byte[] byteImage = ImageUtil.jpegImageToJpegByteArray(image);
                                BitmapFactory bitmapFactory = new BitmapFactory();
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                Bitmap bitmap = bitmapFactory.decodeByteArray(byteImage, /* offset= */ 0, byteImage.length);
                                System.out.println("test bitmap " + bitmap.getWidth());
                                System.out.println("test bitmap " + bitmap.getHeight());
                                bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 3, 0, 1500, bitmap.getHeight());

                                // bitmap이미지 회전
                                bitmap = rotateImage(bitmap, 90);
                                inputImage = InputImage.fromBitmap(bitmap, 0);

                                // imageView에 bitmap이미지 띄우기
                                imageView.setVisibility(View.VISIBLE);
                                previewView.setVisibility(View.GONE);
                                imageView.setImageBitmap(bitmap);
                                super.onCaptureSuccess(image);
                            }
                        }
                );
            }
        });

        btn_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessCardViewModel = new ViewModelProvider(CameraActivity.this).get(BusinessCardViewModel.class);

                String name = user_name.getText().toString().trim();
                String email = user_email.getText().toString().trim();
                String phone = user_num.getText().toString().trim();
                String company = company_name.getText().toString().trim();
                String address = company_address.getText().toString().trim();
                String occupation = company_occupation.getText().toString().trim();
                String depart = company_depart.getText().toString().trim();
                String position = company_position.getText().toString().trim();
                String memo = card_memo.getText().toString().trim();

                ProgressDialog mDialog = null;
                if (name.isEmpty() == false && email.isEmpty() == false && phone.isEmpty() == false && company.isEmpty() == false && address.isEmpty() == false && occupation.isEmpty() == false && depart.isEmpty() == false) {
                    Log.d(TAG, "완료 버튼");
                    mDialog = new ProgressDialog(CameraActivity.this);
                    mDialog.setMessage("명함입력중입니다.");
                    mDialog.show();

                    if (position.isEmpty()) {
                        position = "";
                    }
                    if (memo.isEmpty()) {
                        memo = "";
                    }

                    Card cardAccount = new Card();
                    cardAccount.setName(name);
                    cardAccount.setEmail(email);
                    cardAccount.setPhone(phone);
                    cardAccount.setCompany(company);
                    cardAccount.setAddress(address);
                    cardAccount.setOccupation(occupation);
                    cardAccount.setDepart(depart);
                    cardAccount.setPosition(position);
                    cardAccount.setMemo(memo);

                    businessCardViewModel.addBusinessCard(cardAccount);
                    mDialog.dismiss();

                    Intent startMain = new Intent(CameraActivity.this, MainActivity.class);
                    startActivity(startMain);
                    finish();
                }
                //필수정보가 부족할 때
                else {
                    mDialog.dismiss();
                    Toast.makeText(CameraActivity.this, "필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    // 이미지 회전 함수
    public Bitmap rotateImage(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    // previewView에 카메라 띄우는 함수
    void bindPreview() {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    // 이미지 캡처를 위한 함수
    void bindImageCapture() {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        imageCapture = new ImageCapture.Builder()
                .build();

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageCapture);
    }

    // 텍스트 인식
    private void TextRecognition(TextRecognizer recognizer) {
        Task<Text> result = recognizer.process(inputImage)
                // 이미지 인식에 성공하면 실행되는 리스너
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        Log.e("ML 텍스트 인식", "성공");
                        // Task completed successfully
                        String resultText = visionText.getText();
                        text_info.setText(resultText);  // 인식한 텍스트를 TextView에 세팅
                    }
                })
                // 이미지 인식에 실패하면 실행되는 리스너
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ML 텍스트 인식", "실패: " + e.getMessage());
                            }
                        });
    }

    // 텍스트 라벨링
    public void TextLabeling(ImageLabeler labeler) {
        Task<List<ImageLabel>> result = labeler.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {

                        // Task completed successfully
                        // ...
                        Log.e("ML 텍스트 라벨링", "성공: " + String.valueOf(labels));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.e("ML 텍스트 라벨링", "실패: " + e.getMessage());
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        processCameraProvider.unbindAll();
    }
}