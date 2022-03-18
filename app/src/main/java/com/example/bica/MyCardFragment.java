package com.example.bica;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bica.model.PreferenceManager;
import com.example.bica.nfc.Nfc_Write_Activity;
import com.example.bica.qr.QR_Make_Activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCardFragment extends Fragment {

    Toolbar toolbar;        // 툴바
    AlertDialog.Builder builder;        //다이얼로그 창
//    private int WRITE_REQUEST_CODE = 43;
//    private ParcelFileDescriptor pfd;
//    private FileOutputStream fileOutputStream;
    //    public static String saveStorage = ""; //저장된 파일 경로
//    public static String saveData = ""; //저장된 파일 내용
//
//    PreferenceManager S_Preference;

    public MyCardFragment() {
        // Required empty public constructor
    }

    public static MyCardFragment newInstance() {
        MyCardFragment fragment = new MyCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_card, container, false);

        // Fragment에서 Toolbar 셋업
        toolbar = view.findViewById(R.id.tb_mycard);
        toolbar.inflateMenu(R.menu.menu_mycard); // 메뉴 어떤거 뜰건지 정하는 코드
        toolbar.setOnMenuItemClickListener(item -> { // 메뉴 눌렀을때 뭐할지 정해주는 코드
            switch (item.getItemId()) {
                case R.id.mycard_share: {
                    // 공유 뭘로 할지 다이얼로그 창 띄움
                    showDialog();
                    // navigate to settings screen
//                    StartRecord();
//
//                    Intent WriteTest = new Intent(getContext(), Nfc_Write_Activity.class);
//                    startActivity(WriteTest);
                    Toast.makeText(getActivity(), "공유", Toast.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.mycard_edit: {
                    // save profile changes
                    Toast.makeText(getActivity(), "수정", Toast.LENGTH_SHORT).show();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        });

        return view;
    }

    //다이얼로그 실행(공유방법 선택창)
    public void showDialog() {

        String[] navigate = {"QR 코드", "NFC"};

        builder = new AlertDialog.Builder(getContext());

        builder.setTitle("공유 방법을 선택해주세요");

        //다이얼로그에 리스트 담기
        builder.setItems(navigate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "선택" + navigate[which], Toast.LENGTH_SHORT).show();
                switch (which) {
                    case 0:
                        Intent startQR = new Intent(getContext(), QR_Make_Activity.class);
                        startActivity(startQR);
                        break;
                    case 1:
                        Toast.makeText(getContext(), "개발중", Toast.LENGTH_SHORT).show();
//                        Intent startNFC = new Intent(getContext(), Nfc_Write_Activity.class);
//                        startActivity(startNFC);
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void StartRecord() {
//        try {
//
//            File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/BICA");
//            if (!saveFile.exists()) { // 폴더 없을 경우
//                saveFile.mkdir(); // 폴더 생성
//            }
//
////            long now = System.currentTimeMillis();
////            Date date = new Date(now);
////            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfNow
////                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String formatDate = sdfNow.format(date);
//
//
//            /**
//             * SAF 파일 편집
//             * */
////            String fileName = formatDate+".txt";
//            long now = System.currentTimeMillis(); // 현재시간 받아오기
//            Date date = new Date(now); // Date 객체 생성
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String nowTime = sdf.format(date);
//
//            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile + "/20202020.txt", true));
//            buf.append(nowTime + " "); // 날짜 쓰기
//            buf.append("str"); // 파일 쓰기
//            buf.newLine(); // 개행
//            buf.close();
//
//            Log.d("MyCardFragment2", saveFile.getPath());
//
////            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
////            intent.addCategory(Intent.CATEGORY_OPENABLE);
////            intent.setType("text/plain" );
////            intent.putExtra(Intent.EXTRA_TITLE,fileName);
////
////            startActivityForResult(intent, WRITE_REQUEST_CODE);
//
//            String line = null; // 한줄씩 읽기
//// 폴더 생성
//            if (!saveFile.exists()) { // 폴더 없을 경우
//                saveFile.mkdir(); // 폴더 생성
//            }
//            try {
//                BufferedReader buf2 = new BufferedReader(new FileReader(saveFile + "/20202020.txt"));
//                while ((line = buf2.readLine()) != null) {
//                    Toast.makeText(getContext(), line, Toast.LENGTH_SHORT).show();
//                }
//                buf.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Uri uri = data.getData();
//            addText("test1", uri);
//        }
//    }
//
//    public void addText(String st, Uri uri) {
//        try {
//            pfd = this.getContext().getContentResolver().openFileDescriptor(uri, "w");
//            fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
//            if (fileOutputStream != null) fileOutputStream.write(st.getBytes());
//            fileOutputStream.close();
//            pfd.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public void putString(String st) throws IOException {
//
//    }

//    public void FinishRecord() throws IOException {
//        Toast.makeText(getContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
//        fileOutputStream.close();
//        pfd.close();
//
//    }

}