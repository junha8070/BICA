package com.example.bica;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bica.member.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingFragment extends Fragment {

    private Toolbar tb_setting;
    private ListView lv_setting;
    private FirebaseAuth firebaseAuth;

    private CardDao mcardDao;

    public SettingFragment() {
        // Required empty public constructor
    }
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //Fragment에서 Toolbar 셋업
        tb_setting = view.findViewById(R.id.tb_setting);
        lv_setting = view.findViewById(R.id.listview);

        firebaseAuth = FirebaseAuth.getInstance();

        CardRoomDB cardRoomDB = Room.databaseBuilder(getContext(), CardRoomDB.class,"CardRoomDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mcardDao = cardRoomDB.cardDao();

        tb_setting.setTitle(null);
        final String[] mid = {"비밀번호 변경", "로그아웃"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mid);
        lv_setting.setAdapter(adapter);

        lv_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //switch ()
                switch (i){
                    case 0:
                        Intent startEditInfoActivity = new Intent(getContext(), EditInfoActivity.class);
                        startActivity(startEditInfoActivity);
                        break;
                    case 1:
                        new AlertDialog.Builder(getContext())
                                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        firebaseAuth.signOut();
                                       // requireActivity().finish();
                                        Intent startInitialActivity = new Intent(getContext(), InitialActivity.class);
                                        startActivity(startInitialActivity);
                                        getActivity().finish();

                                        // TODO: room DB 전체 삭제 후 로그인하면 다시 데이터 돌아오기
                                        mcardDao.deleteCardAll();

                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .show();
                        break;

                }
            }
        });

       return view;
    }

}