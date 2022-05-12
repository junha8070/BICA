package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.bica.AddCard.CameraActivity;
import com.example.bica.AddCard.ScanQR;
import com.example.bica.member.LoginActivity;
import com.example.bica.member.RegisterCardActivity;
import com.example.bica.model.Card;
import com.example.bica.mycard.MyCardViewModel;
import com.example.bica.nfc.Nfc_Read_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_main, fab_camera, fab_nfc, fab_qrcode, fab_direct;
    private MyCardViewModel myCardViewModel;
//    final int PERMISSIONS_REQUEST_CODE = 1;
//    PowerManager powerManager;
//    PowerManager.WakeLock wakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        init();

        System.out.println("test for commit 4");

        myCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(this.getApplication())).get(MyCardViewModel.class);

//        myCardViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
//            @Override
//            public void onChanged(List<Card> cards) {
//                if(cards != null){
//                    Log.d("MainActivityTAG", String.valueOf(cards.size()));
//                }
//                else{
//                    Log.d("MainActivityTAG", "Fail");
//                }
//            }
//        });


//        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WAKELOCK");


//        //TODO: WAKELOCK 방출작업 해줘야함
//        wakeLock.acquire(); // WakeLock 깨우기
//        boolean shouldProviceRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환
//
//        if (shouldProviceRationale) {
//            //앱에 필요한 권한이 없어서 권한 요청
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
//        } else {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
//            //권한있을때.
//            //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
//            String root = Environment.getExternalStorageDirectory().getAbsolutePath(); //내장에 만든다
//            String directoryName = "SaveStorage";
//            final File myDir = new File(root + "/" + directoryName);
//            if (!myDir.exists()) {
//                boolean wasSuccessful = myDir.mkdir();
//                if (!wasSuccessful) {
//                    System.out.println("file: was not successful.");
//                } else {
//                    System.out.println("file: 최초로 앨범파일만듬." + root + "/" + directoryName);
//                }
//            } else {
//                System.out.println("file: " + root + "/" + directoryName +"already exists");
//            }
//        }
//        wakeLock.release(); // WakeLock 해제

        // 하단 탭 각 프래그먼트와 연결하는 코드
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        fab_main.setOnClickListener(this);
        fab_camera.setOnClickListener(this);
        fab_nfc.setOnClickListener(this);
        fab_qrcode.setOnClickListener(this);
        fab_direct.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_main:
                anim();
                break;

            case R.id.fab_qrcode:
                anim();
                Intent startScanQR = new Intent(MainActivity.this, ScanQR.class);
                startActivity(startScanQR);
                break;
            case R.id.fab_nfc:
                anim();
                Intent startNfcRead = new Intent(MainActivity.this, Nfc_Read_Activity.class);
                startActivity(startNfcRead);
                break;
            case R.id.fab_camera:
                anim();
                Intent startOCR = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(startOCR);
                break;
            case R.id.fab_direct:
                anim();
                Intent startDirect = new Intent(MainActivity.this, RegisterCardActivity.class);
                startActivity(startDirect);
                break;

            default:
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            fab_camera.startAnimation(fab_close);
            fab_nfc.startAnimation(fab_close);
            fab_qrcode.startAnimation(fab_close);
            fab_direct.startAnimation(fab_close);
            fab_camera.setClickable(false);
            fab_nfc.setClickable(false);
            fab_qrcode.setClickable(false);
            fab_direct.setClickable(false);
            isFabOpen = false;
        } else {
            fab_camera.startAnimation(fab_open);
            fab_nfc.startAnimation(fab_open);
            fab_qrcode.startAnimation(fab_open);
            fab_direct.startAnimation(fab_open);
            fab_qrcode.setClickable(true);
            fab_camera.setClickable(true);
            fab_nfc.setClickable(true);
            fab_direct.setClickable(true);
            isFabOpen = true;
        }
    }

    public void init() {

        // 버튼 올라오고 내려오는 애니메이션 초기화
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        // 요소 id 연결
        fab_main = findViewById(R.id.fab_main);
        fab_camera = findViewById(R.id.fab_camera);
        fab_nfc = findViewById(R.id.fab_nfc);
        fab_qrcode = findViewById(R.id.fab_qrcode);
        fab_direct = findViewById(R.id.fab_direct);
    }

//    private void requestPermission() {
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_CODE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //권한 허용 선택시
//                    //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
//                    String root = Environment.getExternalStorageDirectory().getAbsolutePath(); //내장에 만든다
//                    String directoryName = "SaveStorage";
//                    final File myDir = new File(root + "/" + directoryName);
//                    if (!myDir.exists()) {
//                        boolean wasSuccessful = myDir.mkdir();
//                        if (!wasSuccessful) {
//                            System.out.println("file: was not successful.");
//                        } else {
//                            System.out.println("file: 최초로 앨범파일만듬." + root + "/" + directoryName);
//                        }
//                    } else {
//                        System.out.println("file: " + root + "/" + directoryName +"already exists");
//                    }
////                    String textFileName = "/Data.txt";
////                    //TODO 파일 생성
////                    File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SaveStorage"); //TODO 저장 경로
////                    Log.d("MyCardFragment", String.valueOf(storageDir));
////                    //TODO 폴더 생성
////                    if(!storageDir.exists()){ //TODO 폴더 없을 경우
////                        storageDir.mkdir(); //TODO 폴더 생성
////                    }
//                } else {
//                    //사용자가 권한 거절시
//                    denialDialog();
//                }
//                return;
//            }
//        }
//    }
//
//    public void denialDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("알림")
//                .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
//                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent();
//                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package",
//                                BuildConfig.APPLICATION_ID, null);
//                        intent.setData(uri);
//                        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent); //확인버튼누르면 바로 어플리케이션 권한 설정 창으로 이동하도록
//                    }
//                })
//                .create()
//                .show();
//    }
}