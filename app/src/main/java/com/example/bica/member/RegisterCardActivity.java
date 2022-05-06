package com.example.bica.member;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import androidx.room.Room;

import com.example.bica.AddCard.CameraActivity;
import com.example.bica.CardDao;
import com.example.bica.CardRoomDB;
import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.example.bica.mycard.SearchAddressActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;

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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegisterCardActivity extends AppCompatActivity {

    private static final String TAG = "RegisterCardActivity";
    private CardDao mcardDao;
    Button btn_complete, btn_later, startBtn, captureBtn;
    ImageView imageView;
    PreviewView previewView;
    TextView tv_title;
    EditText edt_username, edt_useremail, edt_phonenum, edt_companyname, edt_companyadr, edt_occupation, edt_teamname, edt_position, edt_memo;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    ProcessCameraProvider processCameraProvider;
    int lensFacing = CameraSelector.LENS_FACING_BACK;
    ImageCapture imageCapture;
    InputImage inputImage;
    Uri imageUri;
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_card);
        init();
        CardRoomDB cardRoomDB = Room.databaseBuilder(getApplicationContext(), CardRoomDB.class,"CardRoomDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);

        try {
            processCameraProvider = ProcessCameraProvider.getInstance(this).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(RegisterCardActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startBtn.setVisibility(View.GONE);
                    bindPreview();
                    bindImageCapture();
                    captureBtn.setVisibility(View.VISIBLE);
                }

            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureBtn.setVisibility(View.GONE);
                imageCapture.takePicture(ContextCompat.getMainExecutor(RegisterCardActivity.this),
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
                                imageUri = getImageUri(getApplicationContext(), bitmap);

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

        mcardDao = cardRoomDB.cardDao();

        edt_companyadr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(RegisterCardActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(intent);
            }
        });

        edt_phonenum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberViewModel = new ViewModelProvider(RegisterCardActivity.this).get(MemberViewModel.class);
                //ToDo: 이미지 추가
                String name = edt_username.getText().toString().trim();
                String email = edt_useremail.getText().toString().trim();
                String phone = edt_phonenum.getText().toString().trim();
                String company = edt_companyname.getText().toString().trim();
                String address = edt_companyadr.getText().toString().trim();
                String occupation = edt_occupation.getText().toString().trim();
                String depart = edt_teamname.getText().toString().trim();
                String position = edt_position.getText().toString().trim();
                String memo = edt_memo.getText().toString().trim();

                ProgressDialog mDialog = null;
                if (name.isEmpty() == false && email.isEmpty() == false && phone.isEmpty() == false && company.isEmpty() == false && address.isEmpty() == false && occupation.isEmpty() == false && depart.isEmpty() == false && position.isEmpty() == false) {
                    Log.d(TAG, "완료 버튼");
                    mDialog = new ProgressDialog(RegisterCardActivity.this);
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

                    db.collection("users")
                            .document(auth.getCurrentUser()
                                    .getUid())
                            .collection("myCard")
                            .whereEqualTo("name", name)
                            .whereEqualTo("phone", phone)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("find card uid");
                                        System.out.println("find card uid1 " + task.getResult().toString());

                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            System.out.println("find card uid1 " + document.getId());
                                            String BC_uid = document.getId();

                                            StorageReference storageReference = mStorage.getReference()
                                                    .child("Images").child(auth.getCurrentUser().getUid()).child("myCard").child(BC_uid);
                                            System.out.println("이미지 주소1 " + imageUri.toString());

                                            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @RequiresApi(api = Build.VERSION_CODES.O)
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                                    System.out.println("이미지 주소4 " + imageUri.toString());

                                                    while (!imageUrl.isComplete()) ;
                                                    System.out.println("이미지 주소" + imageUrl.getResult().toString());
                                                    cardAccount.setImage(imageUrl.getResult().toString());

                                                }
                                            });
                                        }

                                    }
                                }
                            });
                    memberViewModel.registerCard(cardAccount);
                    mDialog.dismiss();

                    Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
                    startActivity(startMain);
                    finish();
                }
                //필수정보가 부족할 때
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegisterCardActivity.this, "필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
             }
        });

        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startMain = new Intent(RegisterCardActivity.this, MainActivity.class);
                startActivity(startMain);
            }
        });
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        String data=result.getData().getStringExtra("data");
                        edt_companyadr.setText(data);
                    }
                }
            }
    );

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // 바이너리 바이트 배열을 스트링으로
    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    // 바이너리 바이트를 스트링으로
    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
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

    public void init() {
        btn_complete = findViewById(R.id.btn_complete);
        btn_later = findViewById(R.id.btn_later);
        imageView = findViewById(R.id.imageView);
        previewView = findViewById(R.id.previewView);
        startBtn = findViewById(R.id.startBtn);
        captureBtn = findViewById(R.id.captureBtn);
        tv_title = findViewById(R.id.register_card_title);
        edt_username = findViewById(R.id.user_name);
        edt_useremail = findViewById(R.id.user_email);
        edt_phonenum = findViewById(R.id.user_phonenum);
        edt_companyname = findViewById(R.id.company_name);
        edt_companyadr = findViewById(R.id.company_address);
        edt_occupation = findViewById(R.id.occupation);
        edt_teamname = findViewById(R.id.teamname);
        edt_position = findViewById(R.id.position);
        edt_memo = findViewById(R.id.memo);
    }
}


