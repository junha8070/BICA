package com.example.bica.AddCard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.example.bica.mycard.SearchAddressActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    PreferenceManager pref;

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
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private BusinessCardViewModel businessCardViewModel;
    Uri imageUri;
    String str_image;


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
        recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());  // ?????? ??????
        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS); // ?????? ??????
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

        // IMAGE DETECTION ??????
        btn_detection_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputImage != null) {
                    TextRecognition(recognizer);
                    TextLabeling(labeler);
                } else {
                    Toast.makeText(CameraActivity.this, "?????? ????????? ???????????? ????????????. ????????? ????????? ?????? ????????????.", Toast.LENGTH_SHORT).show();
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

                                // bitmap????????? ??????
                                bitmap = rotateImage(bitmap, 90);
                                inputImage = InputImage.fromBitmap(bitmap, 0);
                                imageUri = getImageUri(getApplicationContext(), bitmap);
                                // imageView??? bitmap????????? ?????????
                                imageView.setVisibility(View.VISIBLE);
                                previewView.setVisibility(View.GONE);
                                imageView.setImageBitmap(bitmap);
                                super.onCaptureSuccess(image);
                            }
                        }
                );
            }
        });

        company_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????? ?????? ?????? ???????????? ??????
                Intent intent=new Intent(CameraActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(intent);
            }
        });

        company_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //?????? ?????? ?????? ???????????? ??????
                Intent intent=new Intent(CameraActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(intent);
            }
        });

        btn_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("complete test image" + imageUri.toString());
                String[] arr = imageUri.toString().split("/");
                System.out.println("complete test image " + arr[6]);

                StorageReference storageReference = mStorage.getReference()
                        .child("Images").child(auth.getCurrentUser().getUid()).child("BusinessCard").child(arr[6]);

                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                            System.out.println("imageUri test 2" + imageUri.toString());
//                                                    System.out.println("imageUri test 3" + imageUrl.getResult().toString());

                            while (!imageUrl.isComplete()) {
                                System.out.println("imageUri fail");
                            }

                            businessCardViewModel = new ViewModelProvider(CameraActivity.this).get(BusinessCardViewModel.class);

                            String email = user_email.getText().toString().trim();
                            String name = user_name.getText().toString().trim();
                            String phone = user_num.getText().toString().trim();
                            String company = company_name.getText().toString().trim();
                            String address = company_address.getText().toString().trim();
                            String occupation = company_occupation.getText().toString().trim();
                            String depart = company_depart.getText().toString().trim();
                            String position = company_position.getText().toString().trim();
                            String memo = card_memo.getText().toString().trim();
                            String image = imageUrl.getResult().toString();

                            ProgressDialog mDialog = null;

                            if (name.isEmpty() == false && email.isEmpty() == false && phone.isEmpty() == false && company.isEmpty() == false && address.isEmpty() == false && occupation.isEmpty() == false && depart.isEmpty() == false) {
                                Log.d(TAG, "?????? ??????");
                                mDialog = new ProgressDialog(CameraActivity.this);
                                mDialog.setMessage("????????????????????????.");
                                mDialog.show();

                                Card businessCard = new Card();
                                businessCard.setName(name);
                                businessCard.setEmail(email);
                                businessCard.setCompany(company);
                                businessCard.setAddress(address);
                                businessCard.setPhone(phone);
                                businessCard.setOccupation(occupation);
                                businessCard.setDepart(depart);
                                businessCard.setPosition(position);
                                businessCard.setMemo(memo);
                                businessCard.setImage(image);
                                businessCardViewModel.addBusinessCard(businessCard);

                                mDialog.dismiss();
                                Intent startMain = new Intent(CameraActivity.this, MainActivity.class);
                                startActivity(startMain);

                                String edit_title = phone;
                                String edit_content = name+"("+company+")";
                                // String ?????? JSONObject??? ???????????? ????????? ??? ????????? ????????? ????????? ???????????? JSON ????????? ??????
                                String save_form = "{\"title\":\""+phone+"\"," +
                                        "\"name\":\""+name+"\"," +
                                        "\"email\":\""+email+"\"," +
                                        "\"company\":\""+company+"\"," +
                                        "\"address\":\""+address+"\"," +
                                        "\"phone\":\""+phone+"\"," +
                                        "\"occupation\":\""+occupation+"\"," +
                                        "\"depart\":\""+depart+"\"," +
                                        "\"position\":\""+position+"\"," +
                                        "\"memo\":\""+memo+"\"}";


                                // key?????? ????????? ????????? ?????? ???????????? ??????
                                long now = System.currentTimeMillis();
                                Date mDate = new Date(now);
                                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String getTime = simpleDate.format(mDate).toString();

                                Log.d("WriteActivity","?????? : "+edit_title+", ?????? : "+edit_content+", ???????????? : "+getTime);
                                //PreferenceManager ??????????????? ????????? ?????? ???????????? ??????
                                pref.setString(getApplication(),getTime,save_form);


                                // Intent??? ?????? MainActivity??? ??????
                                Intent intent = new Intent();
                                intent.putExtra("date",getTime);
                                intent.putExtra("title",edit_title);
                                intent.putExtra("content",edit_content);
                                setResult(RESULT_OK, intent);

                                finish();
                            }
                            //??????????????? ????????? ???
                            else {
                                mDialog.dismiss();
                                Toast.makeText(CameraActivity.this, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
            }
        });
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        String data=result.getData().getStringExtra("data");
                        company_address.setText(data);
                    }
                }
            }
    );


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "IMG_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }


    // ???????????? ???????????? ???????????????
    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    // ????????? ?????? ??????
    public Bitmap rotateImage(Bitmap src, float degree) {
        // Matrix ?????? ??????
        Matrix matrix = new Matrix();
        // ?????? ?????? ??????
        matrix.postRotate(degree);
        // ???????????? Matrix ??? ???????????? Bitmap ?????? ??????
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    // previewView??? ????????? ????????? ??????
    void bindPreview() {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    // ????????? ????????? ?????? ??????
    void bindImageCapture() {
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build();
        imageCapture = new ImageCapture.Builder()
                .build();

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageCapture);
    }

    // ????????? ??????
    private void TextRecognition(TextRecognizer recognizer) {
        Task<Text> result = recognizer.process(inputImage)
                // ????????? ????????? ???????????? ???????????? ?????????
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        Log.e("ML ????????? ??????", "??????");
                        // Task completed successfully
                        String resultText = visionText.getText();
                        text_info.setText(resultText);  // ????????? ???????????? TextView??? ??????
                    }
                })
                // ????????? ????????? ???????????? ???????????? ?????????
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ML ????????? ??????", "??????: " + e.getMessage());
                            }
                        });
    }

    // ????????? ?????????
    public void TextLabeling(ImageLabeler labeler) {
        Task<List<ImageLabel>> result = labeler.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {

                        // Task completed successfully
                        // ...
                        Log.e("ML ????????? ?????????", "??????: " + String.valueOf(labels));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        Log.e("ML ????????? ?????????", "??????: " + e.getMessage());
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        processCameraProvider.unbindAll();
    }
}