package com.example.bica;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

public class CardInfoFragment extends Fragment {

    Toolbar toolbar;

    public CardInfoFragment() {
        // Required empty public constructor
    }
    //TODO: Git test 223
    public static CardInfoFragment newInstance() {
        CardInfoFragment fragment = new CardInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_card_info, container, false);

        // Fragment에서 Toolbar 셋업
        toolbar = view.findViewById(R.id.tb_mycard2);
        toolbar.inflateMenu(R.menu.menu_card); // 메뉴 어떤거 뜰건지 정하는 코드
        toolbar.setOnMenuItemClickListener(item -> { // 메뉴 눌렀을때 뭐할지 정해주는 코드
            switch (item.getItemId()) {
                case R.id.card_share: {
                    // navigate to settings screen
                    Toast.makeText(getActivity(), "공유", Toast.LENGTH_SHORT).show();
                    return true;
                }
                case R.id.card_del: {
                    // save profile changes
                    Toast.makeText(getActivity(), "삭제", Toast.LENGTH_SHORT).show();
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