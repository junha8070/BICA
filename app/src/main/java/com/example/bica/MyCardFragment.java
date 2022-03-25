package com.example.bica;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.model.Card;
import com.example.bica.model.PreferenceManager;
import com.example.bica.nfc.Nfc_Write_Activity;
import com.example.bica.qr.QR_Make_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;

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
    private TextView tv_mycardname,tv_myPosition,tv_myOccupation,tv_myTeamName,tv_myCompany_Name,tv_myGroupName,tv_myPhoneNum,tv_my_Email,tv_myCompany_Address,tv_myMemo,tv_Pnum;
    private EditText et_mycardname,et_myPosition,et_myOccupation,et_myTeamName,et_myCompany_Name,et_myGroupName,et_myPhoneNum,et_my_Email,et_myCompany_Address,et_myMemo;
    private View view;
    private String Pnum;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Toolbar toolbar,tb_mycard_commit;        // 툴바
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_card, container, false);
        tv_mycardname = view.findViewById(R.id.tv_mycardname);
        tv_myPosition = view.findViewById(R.id.tv_myPosition);
        tv_myOccupation = view.findViewById(R.id.tv_myOccupation);
        tv_myTeamName = view.findViewById(R.id.tv_myTeamName);
        tv_myCompany_Name = view.findViewById(R.id.tv_myCompany_Name);
        tv_myGroupName = view.findViewById(R.id.tv_myGroupName);
        tv_myPhoneNum = view.findViewById(R.id.tv_myPhoneNum);
        tv_my_Email = view.findViewById(R.id.tv_my_Email);
        tv_myCompany_Address = view.findViewById(R.id.tv_myCompany_Address);
        tv_myMemo = view.findViewById(R.id.tv_myMemo);

        tv_Pnum = view.findViewById(R.id.tv_Pnum);

        et_mycardname=view.findViewById(R.id.et_mycardname);
        et_myPosition=view.findViewById(R.id.et_myPosition);
        et_myOccupation=view.findViewById(R.id.et_myOccupation);
        et_myTeamName=view.findViewById(R.id.et_myTeamName);
        et_myCompany_Name=view.findViewById(R.id.et_myCompany_Name);
        et_myGroupName=view.findViewById(R.id.et_myGroupName);
        et_myPhoneNum=view.findViewById(R.id.et_myPhoneNum);
        et_my_Email=view.findViewById(R.id.et_my_Email);
        et_myCompany_Address=view.findViewById(R.id.et_myCompany_Address);
        et_myMemo=view.findViewById(R.id.et_myMemo);

        System.out.println("test " + auth.getCurrentUser().getEmail());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cards").document(auth.getCurrentUser().getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Card card = documentSnapshot.toObject(Card.class);

                        tv_mycardname.setText(card.getName());
                        tv_myPosition.setText(card.getPosition());
                        tv_myOccupation.setText(card.getOccupation());
                        tv_myTeamName.setText(card.getDepart());
                        tv_myCompany_Name.setText(card.getCompany());
                        //그룹이름
                        tv_myPhoneNum.setText(card.getPhone());
                        Pnum = card.getPhone();
                        tv_my_Email.setText(card.getEmail());
                        tv_myCompany_Address.setText(card.getAddress());
                        tv_myMemo.setText(card.getMemo());
                        System.out.println("test " + card.getAddress());
                    }
                });
        // Fragment에서 Toolbar 셋업
        tb_mycard_commit= view.findViewById(R.id.tb_mycard_commit);
        tb_mycard_commit.inflateMenu(R.menu.menu_edit_mycard);
        toolbar = view.findViewById(R.id.tb_mycard1);
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
                    //메뉴수정
                    toolbar.setVisibility(View.GONE);
                    tb_mycard_commit.setVisibility(View.VISIBLE);
                    tb_mycard_commit.setOnMenuItemClickListener(item1 -> {
                        switch (item1.getItemId()){
                            case R.id.commit:{
                                toolbar.setVisibility(View.VISIBLE);
                                tb_mycard_commit.setVisibility(View.GONE);

                                et_mycardname.setVisibility(View.GONE);
                                tv_mycardname.setVisibility(View.VISIBLE);
                                String myname = et_mycardname.getText().toString();
                                tv_mycardname.setText(myname);

                                et_myPosition.setVisibility(View.GONE);
                                tv_myPosition.setVisibility(View.VISIBLE);
                                String myPosition = et_myPosition.getText().toString();
                                tv_myPosition.setText(myPosition);

                                et_myOccupation.setVisibility(View.GONE);
                                tv_myOccupation.setVisibility(View.VISIBLE);
                                String myOccupation = et_myOccupation.getText().toString();
                                tv_myOccupation.setText(myOccupation);

                                et_myTeamName.setVisibility(View.GONE);
                                tv_myTeamName.setVisibility(View.VISIBLE);
                                String myTeamName = et_myTeamName.getText().toString();
                                tv_myTeamName.setText(myTeamName);

                                et_myCompany_Name.setVisibility(View.GONE);
                                tv_myCompany_Name.setVisibility(View.VISIBLE);
                                String myCompany_Name = et_myCompany_Name.getText().toString();
                                tv_myCompany_Name.setText(myCompany_Name);

                                et_myGroupName.setVisibility(View.GONE);
                                tv_myGroupName.setVisibility(View.VISIBLE);
                                String myGroupName = et_myGroupName.getText().toString();
                                tv_myGroupName.setText(myGroupName);

                                et_myPhoneNum.setVisibility(View.GONE);
                                tv_myPhoneNum.setVisibility(View.VISIBLE);
                                String myPhoneNum = et_myPhoneNum.getText().toString();
                                tv_myPhoneNum.setText(myPhoneNum);

                                et_my_Email.setVisibility(View.GONE);
                                tv_my_Email.setVisibility(View.VISIBLE);
                                String my_Email = et_my_Email.getText().toString();
                                tv_my_Email.setText(my_Email);

                                et_myCompany_Address.setVisibility(View.GONE);
                                tv_myCompany_Address.setVisibility(View.VISIBLE);
                                String myCompany_Address = et_myCompany_Address.getText().toString();
                                tv_myCompany_Address.setText(myCompany_Address);

                                et_myMemo.setVisibility(View.GONE);
                                tv_myMemo.setVisibility(View.VISIBLE);
                                String myMemo = et_myMemo.getText().toString();
                                tv_myMemo.setText(myMemo);


                                DocumentReference sfDocRef = db.collection("cards").document(auth.getCurrentUser().getEmail());

                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        if (!tv_mycardname.equals(et_mycardname.getText().toString())) {
                                            transaction.update(sfDocRef, "name", et_mycardname.getText().toString());
                                        }
                                        if (!tv_myPosition.equals(et_myPosition.getText().toString())) {
                                            transaction.update(sfDocRef, "position", et_myPosition.getText().toString());
                                        }
                                        if (!tv_myOccupation.equals(et_myOccupation.getText().toString())) {
                                            transaction.update(sfDocRef, "occupation", et_myOccupation.getText().toString());
                                        }
                                        if (!tv_myTeamName.equals(et_myTeamName.getText().toString())) {
                                            transaction.update(sfDocRef, "depart", et_myTeamName.getText().toString());
                                        }
                                        if (!tv_myCompany_Name.equals(et_myCompany_Name.getText().toString())) {
                                            transaction.update(sfDocRef, "company", et_myCompany_Name.getText().toString());
                                        }
                                        /*if (!tv_myGroupName.equals(et_myGroupName.getText().toString())) {
                                            transaction.update(sfDocRef, "name", et_myGroupName.getText().toString());
                                        }*/
                                        if (!tv_myPhoneNum.equals(et_myPhoneNum.getText().toString())) {
                                            transaction.update(sfDocRef, "phone", et_myPhoneNum.getText().toString());
                                        }
                                        if (!tv_my_Email.equals(et_my_Email.getText().toString())) {
                                            transaction.update(sfDocRef, "email", et_my_Email.getText().toString());
                                        }
                                        if (!tv_myCompany_Address.equals(et_myCompany_Address.getText().toString())) {
                                            transaction.update(sfDocRef, "address", et_myCompany_Address.getText().toString());
                                        }
                                        if (!tv_myMemo.equals(et_myMemo.getText().toString())) {
                                            transaction.update(sfDocRef, "memo", et_myMemo.getText().toString());
                                        }

                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Transaction success!");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Transaction failure.", e);
                                    }
                                });

                                return true;
                            }
                            default:
                                return super.onOptionsItemSelected(item);
                        }
                    });
                    db.collection("cards").document(auth.getCurrentUser().getEmail()).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Card card = documentSnapshot.toObject(Card.class);

                                    tv_mycardname.setText(card.getName());
                                    tv_myPosition.setText(card.getPosition());
                                    tv_myOccupation.setText(card.getOccupation());
                                    tv_myTeamName.setText(card.getDepart());
                                    tv_myCompany_Name.setText(card.getCompany());
                                    //그룹이름
                                    tv_myPhoneNum.setText(card.getPhone());
                                    Pnum = card.getPhone();
                                    tv_my_Email.setText(card.getEmail());
                                    tv_myCompany_Address.setText(card.getAddress());
                                    tv_myMemo.setText(card.getMemo());
                                    System.out.println("test " + card.getAddress());
                                }
                            });


                    // 수정
                    tv_mycardname.setVisibility(view.GONE);
                    et_mycardname.setVisibility(view.VISIBLE);
                    String myname = (String)tv_mycardname.getText();
                    et_mycardname.setText(myname);

                    tv_myPosition.setVisibility(view.GONE);
                    et_myPosition.setVisibility(view.VISIBLE);
                    String myposition = (String)tv_myPosition.getText();
                    et_myPosition.setText(myposition);

                    tv_myOccupation.setVisibility(view.GONE);
                    et_myOccupation.setVisibility(view.VISIBLE);
                    String myOccupation = (String)tv_myOccupation.getText();
                    et_myOccupation.setText(myOccupation);

                    tv_myTeamName.setVisibility(view.GONE);
                    et_myTeamName.setVisibility(view.VISIBLE);
                    String myTeamName = (String)tv_myTeamName.getText();
                    et_myTeamName.setText(myTeamName);

                    tv_myCompany_Name.setVisibility(view.GONE);
                    et_myCompany_Name.setVisibility(view.VISIBLE);
                    String myCompany_Name = (String)tv_myCompany_Name.getText();
                    et_myCompany_Name.setText(myCompany_Name);

                    tv_myGroupName.setVisibility(view.GONE);
                    et_myGroupName.setVisibility(view.VISIBLE);
                    String myGroupName = (String)tv_myGroupName.getText();
                    et_myGroupName.setText(myGroupName);

                    tv_myPhoneNum.setVisibility(view.GONE);
                    et_myPhoneNum.setVisibility(view.VISIBLE);
                    String myPhoneNum = (String)tv_myPhoneNum.getText();
                    et_myPhoneNum.setText(myPhoneNum);

                    tv_my_Email.setVisibility(view.GONE);
                    et_my_Email.setVisibility(view.VISIBLE);
                    String my_Email = (String)tv_my_Email.getText();
                    et_my_Email.setText(my_Email);

                    tv_myCompany_Address.setVisibility(view.GONE);
                    et_myCompany_Address.setVisibility(view.VISIBLE);
                    String myCompany_Address = (String)tv_myCompany_Address.getText();
                    et_myCompany_Address.setText(myCompany_Address);

                    tv_myMemo.setVisibility(view.GONE);
                    et_myMemo.setVisibility(view.VISIBLE);
                    String myMemo = (String)tv_myMemo.getText();
                    et_myMemo.setText(myMemo);

                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        });


/*
        tv_myPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View popupView = getLayoutInflater().inflate(R.layout.fragment_card_call, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setView(popupView);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tv_Pnum = alertDialog.findViewById(R.id.tv_Pnum);
                tv_Pnum.setText(tv_myPhoneNum.getText());

                Button pnum_call = popupView.findViewById(R.id.pnum_call);
                pnum_call.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        String tel = "tel:" + Pnum;
                        startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                    }
                });

                Button pnum_save = popupView.findViewById(R.id.pnum_save);
                pnum_save.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        // Creates a new Intent to insert a contact
                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        // Sets the MIME type to match the Contacts Provider
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, Pnum);
                        startActivity(intent);
                    }
                });
            }
        });
*/

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