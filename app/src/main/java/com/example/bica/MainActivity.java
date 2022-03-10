package com.example.bica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_main, fab_camera, fab_nfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        init();

        // 하단 탭 각 프래그먼트와 연결하는 코드
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);

        fab_main.setOnClickListener(this);
        fab_camera.setOnClickListener(this);
        fab_nfc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_main:
                anim();
                break;

            case R.id.fab_camera:
                anim();
                Intent startScanQR = new Intent(MainActivity.this, ScanQR.class);
                startActivity(startScanQR);
                break;
            case R.id.fab_nfc:
                anim();
                Toast.makeText(this, "nfc btn", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            fab_camera.startAnimation(fab_close);
            fab_nfc.startAnimation(fab_close);
            fab_camera.setClickable(false);
            fab_nfc.setClickable(false);
            isFabOpen = false;
        } else {
            fab_camera.startAnimation(fab_open);
            fab_nfc.startAnimation(fab_open);
            fab_camera.setClickable(true);
            fab_nfc.setClickable(true);
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
    }
}