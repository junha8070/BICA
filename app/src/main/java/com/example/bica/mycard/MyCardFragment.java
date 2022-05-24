package com.example.bica.mycard;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.ActivityViewModelLazyKt;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentViewModelLazyKt;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bica.MainActivity;
import com.example.bica.R;
import com.example.bica.favorite.FavoriteAdapter;
import com.example.bica.model.Card;
import com.example.bica.AddCard.QR_Make_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyCardFragment extends Fragment {
    private TextView tv_mycardname,tv_myPosition,tv_myOccupation,tv_myTeamName,tv_myCompany_Name,tv_myGroupName,tv_myPhoneNum,tv_my_Email,tv_myCompany_Address,tv_myMemo,tv_Pnum;
    private EditText et_mycardname,et_myPosition,et_myOccupation,et_myTeamName,et_myCompany_Name,et_myGroupName,et_myPhoneNum,et_my_Email,et_myMemo,et_myCompany_Address;
    private TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone, tv_Email, tv_Address;
    private ImageView iv_mycard;
    private View view;
    private String Pnum, str_card_info, cardUid;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    Toolbar toolbar,tb_mycard_commit;        // 툴바
    AlertDialog.Builder builder;        //다이얼로그 창
    private MyCardViewModel myCardViewModel;
    private Card newCard = new Card();
    private Card prevCard = new Card();
    private Intent intent;
    private int pagenum;

    private ArrayList<Card> arrCard = new ArrayList<>();

    ViewPager2 viewPager2;
    MyCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        myCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(MyCardViewModel.class);
        myCardViewModel.getUserInfo().observe(this, new Observer<ArrayList<Card>>() {
                    @Override
                    public void onChanged(ArrayList<Card> cards) {
                        arrCard = cards;
                        adapter = new MyCardAdapter(cards, viewPager2);
                        viewPager2.setAdapter(adapter);
                    }
                });

//        myCardViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
//            @Override
//            public void onChanged(List<Card> cards) {
////                Toast.makeText(getContext(), cards.get(0).getName(), Toast.LENGTH_SHORT).show();
//                if(cards != null){
//                    Log.d("CardFragmentTag", cards.get(0).getName());
//                }
//                else{
//                    Log.d("CardFragmentTag", "Fail");
//
//                }
//            }
//        });
//                new Observer<Card>() {
//            @Override
//            public void onChanged(Card card) {
//                Log.d("MyCardFragment", card.getName());
//                tv_mycardname.setText(card.getName());
//                tv_myPosition.setText(card.getPosition());
//                tv_myOccupation.setText(card.getOccupation());
//                tv_myTeamName.setText(card.getDepart());
//                tv_myCompany_Name.setText(card.getCompany());
////                tv_myGroupName.setText(card.getGroupname());
//                tv_myPhoneNum.setText(card.getPhone());
//                Pnum = card.getPhone();
//                tv_my_Email.setText(card.getEmail());
//                tv_myCompany_Address.setText(card.getAddress());
//                tv_myMemo.setText(card.getMemo());
//                //이미지
//                tv_name.setText(card.getName());
//                tv_position.setText(card.getPosition());
//                tv_depart.setText(card.getOccupation());
//                tv_company.setText(card.getCompany());
//                tv_Phone.setText(card.getPhone());
//                tv_Email.setText(card.getEmail());
//                tv_Address.setText(card.getAddress());
//
//            }
//    });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_card, container, false);
        // 요소 초기화
        init(view);
        myCardViewModel.userInfo();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                tv_mycardname.setText(arrCard.get(position).getName());
                tv_myPosition.setText(arrCard.get(position).getPosition());
                tv_myOccupation.setText(arrCard.get(position).getOccupation());
                tv_myTeamName.setText(arrCard.get(position).getDepart());
                tv_myCompany_Name.setText(arrCard.get(position).getCompany());
//              tv_myGroupName.setText(card.getGroupname());
                tv_myPhoneNum.setText(arrCard.get(position).getPhone());
                tv_my_Email.setText(arrCard.get(position).getEmail());
                tv_myCompany_Address.setText(arrCard.get(position).getAddress());
                tv_myMemo.setText(arrCard.get(position).getMemo());




                // Fragment에서 Toolbar 셋업
                toolbar.setOnMenuItemClickListener(item -> { // 메뉴 눌렀을때 뭐할지 정해주는 코드
                    switch (item.getItemId()) {
                        case R.id.mycard_del:{
                            prevCard.setName(tv_mycardname.getText().toString());
                            prevCard.setPosition(tv_myPosition.getText().toString());
                            prevCard.setOccupation(tv_myOccupation.getText().toString());
                            prevCard.setDepart(tv_myTeamName.getText().toString());
                            prevCard.setCompany(tv_myCompany_Name.getText().toString());
                            prevCard.setPhone(tv_myPhoneNum.getText().toString());
                            prevCard.setEmail(tv_my_Email.getText().toString());
                            prevCard.setAddress(tv_myCompany_Address.getText().toString());
                            prevCard.setMemo(tv_myMemo.getText().toString());

                            myCardViewModel.delInfo(prevCard);
                            reset();

                            return true;
                        }
                        case R.id.mycard_share: {
                            // 공유 뭘로 할지 다이얼로그 창 띄움
                            showDialog(str_card_info);
                            // navigate to settings screen
//                    StartRecord();
//
//                    Intent WriteTest = new Intent(getContext(), Nfc_Write_Activity.class);
//                    startActivity(WriteTest);
                            Toast.makeText(getActivity(), "공유", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        case R.id.mycard_edit: {

                            toolbar.setVisibility(View.GONE);
                            tb_mycard_commit.setVisibility(View.VISIBLE);
                            tb_mycard_commit.setOnMenuItemClickListener(item1 -> {
                                switch (item1.getItemId()){
                                    case R.id.commit:{
                                        toolbar.setVisibility(View.VISIBLE);
                                        tb_mycard_commit.setVisibility(View.GONE);

                                        prevCard.setName(tv_mycardname.getText().toString());
                                        String myname = et_mycardname.getText().toString();
                                        et_mycardname.setVisibility(View.GONE);
                                        tv_mycardname.setVisibility(View.VISIBLE);
//                                tv_mycardname.setText(myname);
                                        arrCard.get(position).setName(myname);
//                                        tv_mycardname.setText(arrCard.get(position).getName());
                                        newCard.setName(myname);


                                        prevCard.setPosition(tv_myPosition.getText().toString());
                                        et_myPosition.setVisibility(View.GONE);
                                        tv_myPosition.setVisibility(View.VISIBLE);
                                        String myPosition = et_myPosition.getText().toString();
//                                tv_myPosition.setText(myPosition);
                                        arrCard.get(position).setPosition(myPosition);
//                                        tv_myPosition.setText(arrCard.get(position).getPosition());
                                        newCard.setPosition(myPosition);

                                        prevCard.setOccupation(tv_myOccupation.getText().toString());
                                        et_myOccupation.setVisibility(View.GONE);
                                        tv_myOccupation.setVisibility(View.VISIBLE);
                                        String myOccupation = et_myOccupation.getText().toString();
//                                tv_myOccupation.setText(myOccupation);
                                        arrCard.get(position).setOccupation(myOccupation);
//                                        tv_myOccupation.setText(arrCard.get(position).getOccupation());
                                        newCard.setOccupation(myOccupation);

                                        prevCard.setDepart(tv_myTeamName.getText().toString());
                                        et_myTeamName.setVisibility(View.GONE);
                                        tv_myTeamName.setVisibility(View.VISIBLE);
                                        String myTeamName = et_myTeamName.getText().toString();
//                                tv_myTeamName.setText(myTeamName);
                                        arrCard.get(position).setDepart(myTeamName);
//                                        tv_myTeamName.setText(arrCard.get(position).getDepart());
                                        newCard.setDepart(myTeamName);

                                        prevCard.setCompany(tv_myCompany_Name.getText().toString());
                                        et_myCompany_Name.setVisibility(View.GONE);
                                        tv_myCompany_Name.setVisibility(View.VISIBLE);
                                        String myCompany_Name = et_myCompany_Name.getText().toString();
//                                tv_myCompany_Name.setText(myCompany_Name);
                                        arrCard.get(position).setCompany(myCompany_Name);
//                                        tv_myCompany_Name.setText(arrCard.get(position).getCompany());
                                        newCard.setCompany(myCompany_Name);

//                                prevCard.setGroupname(tv_myGroupName.getText().toString());
//                                et_myGroupName.setVisibility(View.GONE);
//                                tv_myGroupName.setVisibility(View.VISIBLE);
//                                String myGroupName = et_myGroupName.getText().toString();
//                                tv_myGroupName.setText(myGroupName);
//                                newCard.setGroupname(myGroupName);

                                        prevCard.setPhone(tv_myPhoneNum.getText().toString());
                                        et_myPhoneNum.setVisibility(View.GONE);
                                        tv_myPhoneNum.setVisibility(View.VISIBLE);
                                        String myPhoneNum = et_myPhoneNum.getText().toString();
//                                tv_myPhoneNum.setText(myPhoneNum);
                                        arrCard.get(position).setPhone(myPhoneNum);
//                                        tv_myPhoneNum.setText(arrCard.get(position).getPhone());
                                        newCard.setPhone(myPhoneNum);

                                        prevCard.setEmail(tv_my_Email.getText().toString());
                                        et_my_Email.setVisibility(View.GONE);
                                        tv_my_Email.setVisibility(View.VISIBLE);
                                        String my_Email = et_my_Email.getText().toString();
//                                tv_my_Email.setText(my_Email);
                                        arrCard.get(position).setEmail(my_Email);
//                                        tv_my_Email.setText(arrCard.get(position).getEmail());
                                        newCard.setEmail(my_Email);

                                        prevCard.setAddress(tv_myCompany_Address.getText().toString());
                                        String myCompany_Address = tv_myCompany_Address.getText().toString();
//                                tv_myCompany_Address.setText(myCompany_Address);
                                        arrCard.get(position).setAddress(myCompany_Address);
//                                        tv_myCompany_Address.setText(arrCard.get(position).getAddress());
                                        tv_myCompany_Address.setEnabled(false);
                                        newCard.setAddress(myCompany_Address);

                                        prevCard.setMemo(tv_myMemo.getText().toString());
                                        et_myMemo.setVisibility(View.GONE);
                                        tv_myMemo.setVisibility(View.VISIBLE);
                                        String myMemo = et_myMemo.getText().toString();
//                                tv_myMemo.setText(myMemo);
                                        arrCard.get(position).setMemo(myMemo);
//                                        tv_myMemo.setText(arrCard.get(position).getMemo());
                                        newCard.setMemo(myMemo);



//                                tv_name.setText(myname);
//                                tv_position.setText(myPosition);
//                                tv_depart.setText(myOccupation);
//                                tv_company.setText(myCompany_Name);
//                                tv_Phone.setText(myPhoneNum);
//                                tv_Email.setText(my_Email);
//                                tv_Address.setText(myCompany_Address);




                                        //편집데이터업데이트
                                        myCardViewModel.changeInfo(prevCard, newCard);
                                        reset();



                                        myCardViewModel.getUpdateInfo().observe(getActivity(), new Observer<Card>() {
                                            @Override
                                            public void onChanged(Card card) {
                                                Log.d("MyCardFragment", card.getName());
//                                        arrCard.get(pagenum).setName(myname);
//                                                tv_mycardname.setText(arrCard.get(position).getName());
//                                                tv_myPosition.setText(card.getPosition());
//                                                tv_myOccupation.setText(card.getOccupation());
//                                                tv_myTeamName.setText(card.getDepart());
//                                                tv_myCompany_Name.setText(card.getCompany());
////                                        tv_myGroupName.setText(card.getGroupname());
//                                                tv_myPhoneNum.setText(card.getPhone());
//                                                Pnum = card.getPhone();
//                                                tv_my_Email.setText(card.getEmail());
//                                                tv_myCompany_Address.setText(card.getAddress());
//                                                tv_myMemo.setText(card.getMemo());

//                                                tv_name.setText(arrCard.get(position).getName());
//                                                tv_position.setText(arrCard.get(position).getPosition());
//                                                tv_depart.setText(arrCard.get(position).getOccupation());
//                                                tv_company.setText(arrCard.get(position).getCompany());
//                                                tv_Phone.setText(arrCard.get(position).getPhone());
//                                                tv_Email.setText(arrCard.get(position).getEmail());
//                                                tv_Address.setText(arrCard.get(position).getAddress());

                                            }
                                        });
                                    }
                                    default:
                                        return onOptionsItemSelected(item);
                                }



                            });

                            //수정
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

//                    tv_myGroupName.setVisibility(view.GONE);
//                    et_myGroupName.setVisibility(view.VISIBLE);
//                    String myGroupName = (String)tv_myGroupName.getText();
//                    et_myGroupName.setText(myGroupName);

                            tv_myPhoneNum.setVisibility(view.GONE);
                            et_myPhoneNum.setVisibility(view.VISIBLE);
                            String myPhoneNum = (String)tv_myPhoneNum.getText();
                            et_myPhoneNum.setText(myPhoneNum);

                            tv_my_Email.setVisibility(view.GONE);
                            et_my_Email.setVisibility(view.VISIBLE);
                            String my_Email = (String)tv_my_Email.getText();
                            et_my_Email.setText(my_Email);

                            tv_myCompany_Address.setEnabled(true);

                            tv_myMemo.setVisibility(view.GONE);
                            et_myMemo.setVisibility(view.VISIBLE);
                            String myMemo = (String)tv_myMemo.getText();
                            et_myMemo.setText(myMemo);

                        }
                        default:
                            return onOptionsItemSelected(item);
                    }
                });


            }
        });



//
//        // Fragment에서 Toolbar 셋업
//        toolbar.setOnMenuItemClickListener(item -> { // 메뉴 눌렀을때 뭐할지 정해주는 코드
//            switch (item.getItemId()) {
//                case R.id.mycard_share: {
//                    // 공유 뭘로 할지 다이얼로그 창 띄움
//                    showDialog(str_card_info);
//                    // navigate to settings screen
////                    StartRecord();
////
////                    Intent WriteTest = new Intent(getContext(), Nfc_Write_Activity.class);
////                    startActivity(WriteTest);
//                    Toast.makeText(getActivity(), "공유", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                case R.id.mycard_edit: {
//
//                    toolbar.setVisibility(View.GONE);
//                    tb_mycard_commit.setVisibility(View.VISIBLE);
//                    tb_mycard_commit.setOnMenuItemClickListener(item1 -> {
//                        switch (item1.getItemId()){
//                            case R.id.commit:{
//                                toolbar.setVisibility(View.VISIBLE);
//                                tb_mycard_commit.setVisibility(View.GONE);
//
//                                prevCard.setName(tv_mycardname.getText().toString());
//                                String myname = et_mycardname.getText().toString();
//                                et_mycardname.setVisibility(View.GONE);
//                                tv_mycardname.setVisibility(View.VISIBLE);
////                                tv_mycardname.setText(myname);
//                                arrCard.get(pagenum).setName(myname);
//                                tv_mycardname.setText(arrCard.get(pagenum).getName());
//                                newCard.setName(myname);
//
//                                prevCard.setPosition(tv_myPosition.getText().toString());
//                                et_myPosition.setVisibility(View.GONE);
//                                tv_myPosition.setVisibility(View.VISIBLE);
//                                String myPosition = et_myPosition.getText().toString();
////                                tv_myPosition.setText(myPosition);
//                                arrCard.get(pagenum).setPosition(myPosition);
//                                tv_myPosition.setText(arrCard.get(pagenum).getPosition());
//                                newCard.setPosition(myPosition);
//
//                                prevCard.setOccupation(tv_myOccupation.getText().toString());
//                                et_myOccupation.setVisibility(View.GONE);
//                                tv_myOccupation.setVisibility(View.VISIBLE);
//                                String myOccupation = et_myOccupation.getText().toString();
////                                tv_myOccupation.setText(myOccupation);
//                                arrCard.get(pagenum).setOccupation(myOccupation);
//                                tv_myOccupation.setText(arrCard.get(pagenum).getOccupation());
//                                newCard.setOccupation(myOccupation);
//
//                                prevCard.setDepart(tv_myTeamName.getText().toString());
//                                et_myTeamName.setVisibility(View.GONE);
//                                tv_myTeamName.setVisibility(View.VISIBLE);
//                                String myTeamName = et_myTeamName.getText().toString();
////                                tv_myTeamName.setText(myTeamName);
//                                arrCard.get(pagenum).setDepart(myTeamName);
//                                tv_myTeamName.setText(arrCard.get(pagenum).getDepart());
//                                newCard.setDepart(myTeamName);
//
//                                prevCard.setCompany(tv_myCompany_Name.getText().toString());
//                                et_myCompany_Name.setVisibility(View.GONE);
//                                tv_myCompany_Name.setVisibility(View.VISIBLE);
//                                String myCompany_Name = et_myCompany_Name.getText().toString();
////                                tv_myCompany_Name.setText(myCompany_Name);
//                                arrCard.get(pagenum).setCompany(myCompany_Name);
//                                tv_myCompany_Name.setText(arrCard.get(pagenum).getCompany());
//                                newCard.setCompany(myCompany_Name);
//
////                                prevCard.setGroupname(tv_myGroupName.getText().toString());
////                                et_myGroupName.setVisibility(View.GONE);
////                                tv_myGroupName.setVisibility(View.VISIBLE);
////                                String myGroupName = et_myGroupName.getText().toString();
////                                tv_myGroupName.setText(myGroupName);
////                                newCard.setGroupname(myGroupName);
//
//                                prevCard.setPhone(tv_myPhoneNum.getText().toString());
//                                et_myPhoneNum.setVisibility(View.GONE);
//                                tv_myPhoneNum.setVisibility(View.VISIBLE);
//                                String myPhoneNum = et_myPhoneNum.getText().toString();
////                                tv_myPhoneNum.setText(myPhoneNum);
//                                arrCard.get(pagenum).setPhone(myPhoneNum);
//                                tv_myPhoneNum.setText(arrCard.get(pagenum).getPhone());
//                                newCard.setPhone(myPhoneNum);
//
//                                prevCard.setEmail(tv_my_Email.getText().toString());
//                                et_my_Email.setVisibility(View.GONE);
//                                tv_my_Email.setVisibility(View.VISIBLE);
//                                String my_Email = et_my_Email.getText().toString();
////                                tv_my_Email.setText(my_Email);
//                                arrCard.get(pagenum).setEmail(my_Email);
//                                tv_my_Email.setText(arrCard.get(pagenum).getEmail());
//                                newCard.setEmail(my_Email);
//
//                                prevCard.setAddress(tv_myCompany_Address.getText().toString());
//                                String myCompany_Address = tv_myCompany_Address.getText().toString();
////                                tv_myCompany_Address.setText(myCompany_Address);
//                                arrCard.get(pagenum).setAddress(myCompany_Address);
//                                tv_myCompany_Address.setText(arrCard.get(pagenum).getAddress());
//                                tv_myCompany_Address.setEnabled(false);
//                                newCard.setAddress(myCompany_Address);
//
//                                prevCard.setMemo(tv_myMemo.getText().toString());
//                                et_myMemo.setVisibility(View.GONE);
//                                tv_myMemo.setVisibility(View.VISIBLE);
//                                String myMemo = et_myMemo.getText().toString();
////                                tv_myMemo.setText(myMemo);
//                                arrCard.get(pagenum).setMemo(myMemo);
//                                tv_myMemo.setText(arrCard.get(pagenum).getMemo());
//                                newCard.setMemo(myMemo);
//
////                                tv_name.setText(myname);
////                                tv_position.setText(myPosition);
////                                tv_depart.setText(myOccupation);
////                                tv_company.setText(myCompany_Name);
////                                tv_Phone.setText(myPhoneNum);
////                                tv_Email.setText(my_Email);
////                                tv_Address.setText(myCompany_Address);
//
//
//
//
//                                //편집데이터업데이트
//                                myCardViewModel.changeInfo(prevCard, newCard);
//
//
//                                myCardViewModel.getUpdateInfo().observe(getActivity(), new Observer<Card>() {
//                                    @Override
//                                    public void onChanged(Card card) {
//                                        Log.d("MyCardFragment", card.getName());
////                                        arrCard.get(pagenum).setName(myname);
//                                        tv_mycardname.setText(arrCard.get(pagenum).getName());
//                                        tv_myPosition.setText(card.getPosition());
//                                        tv_myOccupation.setText(card.getOccupation());
//                                        tv_myTeamName.setText(card.getDepart());
//                                        tv_myCompany_Name.setText(card.getCompany());
////                                        tv_myGroupName.setText(card.getGroupname());
//                                        tv_myPhoneNum.setText(card.getPhone());
//                                        Pnum = card.getPhone();
//                                        tv_my_Email.setText(card.getEmail());
//                                        tv_myCompany_Address.setText(card.getAddress());
//                                        tv_myMemo.setText(card.getMemo());
//
//                                        tv_name.setText(arrCard.get(pagenum).getName());
//                                        tv_position.setText(card.getPosition());
//                                        tv_depart.setText(card.getOccupation());
//                                        tv_company.setText(card.getCompany());
//                                        tv_Phone.setText(card.getPhone());
//                                        tv_Email.setText(card.getEmail());
//                                        tv_Address.setText(card.getAddress());
//
//                                    }
//                                });
//                            }
//                            default:
//                                return super.onOptionsItemSelected(item);
//                        }
//
//
//
//                    });
//
//                    //수정
//                    tv_mycardname.setVisibility(view.GONE);
//                    et_mycardname.setVisibility(view.VISIBLE);
//                    String myname = (String)tv_mycardname.getText();
//                    et_mycardname.setText(myname);
//
//                    tv_myPosition.setVisibility(view.GONE);
//                    et_myPosition.setVisibility(view.VISIBLE);
//                    String myposition = (String)tv_myPosition.getText();
//                    et_myPosition.setText(myposition);
//
//                    tv_myOccupation.setVisibility(view.GONE);
//                    et_myOccupation.setVisibility(view.VISIBLE);
//                    String myOccupation = (String)tv_myOccupation.getText();
//                    et_myOccupation.setText(myOccupation);
//
//                    tv_myTeamName.setVisibility(view.GONE);
//                    et_myTeamName.setVisibility(view.VISIBLE);
//                    String myTeamName = (String)tv_myTeamName.getText();
//                    et_myTeamName.setText(myTeamName);
//
//                    tv_myCompany_Name.setVisibility(view.GONE);
//                    et_myCompany_Name.setVisibility(view.VISIBLE);
//                    String myCompany_Name = (String)tv_myCompany_Name.getText();
//                    et_myCompany_Name.setText(myCompany_Name);
//
////                    tv_myGroupName.setVisibility(view.GONE);
////                    et_myGroupName.setVisibility(view.VISIBLE);
////                    String myGroupName = (String)tv_myGroupName.getText();
////                    et_myGroupName.setText(myGroupName);
//
//                    tv_myPhoneNum.setVisibility(view.GONE);
//                    et_myPhoneNum.setVisibility(view.VISIBLE);
//                    String myPhoneNum = (String)tv_myPhoneNum.getText();
//                    et_myPhoneNum.setText(myPhoneNum);
//
//                    tv_my_Email.setVisibility(view.GONE);
//                    et_my_Email.setVisibility(view.VISIBLE);
//                    String my_Email = (String)tv_my_Email.getText();
//                    et_my_Email.setText(my_Email);
//
//                    tv_myCompany_Address.setEnabled(true);
//
//                    tv_myMemo.setVisibility(view.GONE);
//                    et_myMemo.setVisibility(view.VISIBLE);
//                    String myMemo = (String)tv_myMemo.getText();
//                    et_myMemo.setText(myMemo);
//
//                }
//                default:
//                    return super.onOptionsItemSelected(item);
//            }
//        });

        //전화

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
                String number=tv_myPhoneNum.getText().toString();

                Button pnum_call = popupView.findViewById(R.id.pnum_call);

                pnum_call.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        String tel = "tel:" + number;
                        Intent intent =new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
                        startActivity(intent);
                    }
                });

                Button pnum_save = popupView.findViewById(R.id.pnum_save);
                pnum_save.setOnClickListener(new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                        startActivity(intent);
                    }
                });
            }
        });


        //주소
        tv_myCompany_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //주소 검색 웹뷰 화면으로 이동
                Intent intent=new Intent(getContext(), SearchAddressActivity.class);
                getSearchResult.launch(intent);

            }
        });

        return view;
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){
                        String data=result.getData().getStringExtra("data");
                        tv_myCompany_Address.setText(data);
                        myCardViewModel.changeInfo(prevCard, newCard);
                        myCardViewModel.getUpdateInfo().observe(getActivity(), new Observer<Card>() {
                            @Override
                            public void onChanged(Card card) {
                                Log.d("MyCardFragment", card.getName());
                                tv_myCompany_Address.setText(card.getAddress());
                            }
                        });
                    }
                }
            }
    );

    public void reset(){
        Intent intent = ((Activity)getContext()).getIntent();
        ((Activity)getContext()).finish(); //현재 액티비티 종료 실시
        ((Activity)getContext()).overridePendingTransition(0, 0); //효과 없애기
        ((Activity)getContext()).startActivity(intent); //현재 액티비티 재실행 실시
        ((Activity)getContext()).overridePendingTransition(0, 0); //효과 없애기
    }





    //다이얼로그 실행(공유방법 선택창)
    public void showDialog(String str_card_Id) {

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
                        startQR.putExtra("cardId", str_card_Id);
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

    private void init(View view){
        viewPager2 = view.findViewById(R.id.my_card_viewpager);

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

        tb_mycard_commit= view.findViewById(R.id.tb_mycard_commit);
        tb_mycard_commit.inflateMenu(R.menu.menu_edit_mycard);
        toolbar = view.findViewById(R.id.tb_mycard1);
        toolbar.inflateMenu(R.menu.menu_mycard); // 메뉴 어떤거 뜰건지 정하는 코드

        tv_company= view.findViewById(R.id.tv_company);
        tv_depart= view.findViewById(R.id.tv_depart);
        tv_name= view.findViewById(R.id.tv_name);
        tv_position= view.findViewById(R.id.tv_position);
        tv_Phone= view.findViewById(R.id.tv_Phone);
        tv_Email= view.findViewById(R.id.tv_Email);
        tv_Address= view.findViewById(R.id.tv_Address);

//        iv_mycard=view.findViewById(R.id.iv_mycard);
    }

    private int arrSize(ArrayList<Card> arrCard){
        return arrCard.size();
    }

}