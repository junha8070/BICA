package com.example.bica.mycard;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bica.R;

public class MyCardEditFragment extends Fragment {

    Toolbar toolbar;

    public MyCardEditFragment() {
        // Required empty public constructor
    }

    public static MyCardEditFragment newInstance() {
        MyCardEditFragment fragment = new MyCardEditFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_mycard, container, false);

        // Fragment에서 Toolbar 셋업
        toolbar = view.findViewById(R.id.tb_mycard3);
        toolbar.inflateMenu(R.menu.menu_edit_mycard); // 메뉴 어떤거 뜰건지 정하는 코드
        toolbar.setOnMenuItemClickListener(item -> { // 메뉴 눌렀을때 뭐할지 정해주는 코드
            switch (item.getItemId()) {
                case R.id.commit: {
                    // navigate to settings screen
                    Toast.makeText(getActivity(), "완료", Toast.LENGTH_SHORT).show();
                    return true;
                }
                default:
                    return super.onOptionsItemSelected(item);
            }
        });

        return view;
    }

/*
    public View onOptionsItemSelected(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_card, container, false);

        // Fragment에서 Toolbar 셋업
        toolbar = view.findViewById(R.id.tb_mycard);
        toolbar.inflateMenu(R.menu.menu_mycard); // 메뉴 어떤거 뜰건지 정하는 코드
        toolbar.setOnMenuItemClickListener(item -> { // 메뉴 눌렀을때 뭐할지 정해주는 코드
            switch (item.getItemId()) {
                case R.id.mycard_share: {
                    // navigate to settings screen
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
*/

}