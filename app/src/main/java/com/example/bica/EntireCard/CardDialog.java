package com.example.bica.EntireCard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CardDialog{

    private String TAG = "CardDialogTAG";

    private Context context;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    // 커스텀 다이얼로그의 각 위젯들을 정의한다.
    private MaterialToolbar cardView_toolbar;
    private ImageView iv_profile;
    private TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone, tv_Email, tv_Address, tv_group;
    private Spinner spinner_group;
    private Button btn_group;

    public CardDialog(Context context) {
        this.context = context;
    }
    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arr;


    CardViewModel cardViewModel;

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(Card cardInfo) {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_card_dialog);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes((WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        isFavorite(cardInfo);

        cardView_toolbar = dlg.findViewById(R.id.cardView_toolbar);

        iv_profile = dlg.findViewById(R.id.iv_profile);
        tv_company = dlg.findViewById(R.id.tv_company);
        tv_depart = dlg.findViewById(R.id.tv_depart);
        tv_name = dlg.findViewById(R.id.tv_name);
        tv_position = dlg.findViewById(R.id.tv_position);
        tv_Phone = dlg.findViewById(R.id.tv_Phone);
        tv_Email = dlg.findViewById(R.id.tv_Email);
        tv_Address = dlg.findViewById(R.id.tv_Address);
        tv_group = dlg.findViewById(R.id.tv_group);
        spinner_group = dlg.findViewById(R.id.spinner_group);
        btn_group = dlg.findViewById(R.id.btn_group);

        tv_company.setText(cardInfo.getCompany());
        tv_depart.setText(cardInfo.getDepart());
        tv_name.setText(cardInfo.getName());
        tv_position.setText(cardInfo.getPosition());
        tv_Phone.setText(cardInfo.getPhone());
        tv_Email.setText(cardInfo.getEmail());
        tv_Address.setText(cardInfo.getAddress());
        tv_group.setText(cardInfo.getGroup());
        btn_group.setText(cardInfo.getGroup());

        spinner_group.setVisibility(View.GONE);

        cardView_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        cardView_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.call:
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.fragment_card_call,null);


//                        final View popupView = getLayoutInflater().inflate(R.layout.fragment_card_call, null);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setView(popupView);

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        TextView tv_Pnum = alertDialog.findViewById(R.id.tv_Pnum);
                        tv_Pnum.setText(tv_Phone.getText());
                        String number=tv_Phone.getText().toString();

                        Button pnum_call = popupView.findViewById(R.id.pnum_call);

                        pnum_call.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View v){
                                String tel = "tel:" + number;
                                Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse(tel));

                                context.startActivity(intent);

//                                startActivity(intent);
                            }
                        });

                        Button pnum_save = popupView.findViewById(R.id.pnum_save);
                        pnum_save.setOnClickListener(new Button.OnClickListener(){
                            public void onClick(View v){
                                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                                intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);

                                context.startActivity(intent);
//                                startActivity(intent);
                            }
                        });

                        break;
                    case R.id.delete:
                        Toast.makeText(dlg.getContext(), "삭제 버튼", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit:
                        tv_group.setVisibility(View.GONE);
                        Toast.makeText(dlg.getContext(), "수정 버튼", Toast.LENGTH_SHORT).show();

                        arr = new ArrayList<>();
                        firestore.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        List<Object> list = (List<Object>) document.get("group");

                                        //Iterate through the list to get the desired values
                                        for(Object item : list){
                                            System.out.println("chip group object item " + item);
                                            System.out.println("chip group object item to String " + item.toString());
                                            arr.add(item.toString());
                                        }
                                        System.out.println("test for edit 1 " + arr);

                                    }
                                }
                                System.out.println("test for edit 2 " + arr);
                                spinner_group.setVisibility(View.VISIBLE);
                                btn_group.setVisibility(View.VISIBLE);

                                arrayAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arr);
                                spinner_group.setAdapter(arrayAdapter);

                                spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        tv_group.setText(arr.get(i));
                                        Toast.makeText(context.getApplicationContext(),arr.get(i)+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override public void onNothingSelected(AdapterView<?> adapterView) { } });


                                btn_group.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editGroup(cardInfo, tv_group.getText().toString());
                                        System.out.println("test for edit btn clicked ");
                                        tv_group.setVisibility(View.VISIBLE);
                                        spinner_group.setVisibility(View.GONE);
                                        btn_group.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });

                        break;
                    case R.id.favorite:
                        if(cardView_toolbar.getMenu().findItem(R.id.favorite).getIcon().equals(R.drawable.ic_star_filled_24)){
                            cardView_toolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_star_empty_24);
                        }else {
                            cardView_toolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_star_filled_24);
                        }
                        Toast.makeText(dlg.getContext(), "즐겨찾기 버튼", Toast.LENGTH_SHORT).show();
                        addFavorite(cardInfo);
                        break;
                }
                return false;
            }


        });
    }



    private void editGroup(Card cardInfo, String groupName) {
        firestore.collection("users").document(auth.getCurrentUser().getUid())
                .collection("BusinessCard")
                .whereEqualTo("phone", cardInfo.getPhone())
                .whereEqualTo("name", cardInfo.getName())
                .whereEqualTo("email", cardInfo.getEmail())
                .whereEqualTo("company", cardInfo.getCompany()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot item : queryDocumentSnapshots){
                            System.out.println("test for path "+ item);
                            System.out.println("test for path id "+ item.getId());
                            System.out.println("test for path doc "+ item.get(FieldPath.documentId()));

                            firestore.collection("users")
                                    .document(auth.getCurrentUser().getUid()).collection("BusinessCard")
                                    .document(item.getId())
                                    .update("group", groupName);
                        }

                    }
                });

        System.out.println("test for path ");



    }

    private void addFavorite(Card cardInfo){
        firestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("FavoriteCard")
                .add(cardInfo)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    cardView_toolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_star_filled_24);
                }else{
                    Log.e(TAG, task.getException().getMessage());
                }
            }
        });
    }

    public void isFavorite(Card cardInfo){
        firestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("FavoriteCard")
                .whereEqualTo("image",cardInfo.getImage()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        cardView_toolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_star_filled_24);
                    } else if (task.getResult().isEmpty()) {
                        cardView_toolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_star_empty_24);
                    } else {
                        Log.e(TAG, task.getException().getMessage());
                    }
                }
            }
        });
    }

}
