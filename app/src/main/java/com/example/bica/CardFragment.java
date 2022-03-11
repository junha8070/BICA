package com.example.bica;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CardFragment extends Fragment {
    public ImageView logo_img;
    public EditText find_edt;
    public Spinner spinner_group;
    public TextView edt_tv;
    ArrayList<String> arrayList;
    public CardFragment() {
        // Required empty public constructor
    }
    public static CardFragment newInstance() {
        CardFragment fragment = new CardFragment();
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
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cardFragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new CardFragmentRecyclerViewAdapter());
        edt_tv = (TextView) view.findViewById(R.id.edt_tv);
        logo_img = (ImageView) view.findViewById(R.id.logo_img);
        find_edt = (EditText) view.findViewById(R.id.find_edt);
        spinner_group = (Spinner) view.findViewById(R.id.spinner_group);
        arrayList = new ArrayList<>();
        // 내 uid밑의 arrayList를 가져와 arrayList에 담고 spinner에 보이도록
        arrayList.add("미분류");
        arrayList.add("test1");
        arrayList.add("test2");
        arrayList.add("test3");
        arrayList.add("test4");
        arrayList.add("test5");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_group.setAdapter(arrayAdapter);

        // editText에 글자 채워질 때 반응


        // 편집 글자 눌렀을 때 반응
        edt_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    public class CardFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public CardFragmentRecyclerViewAdapter(){

        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ShowCardActivity.class);
                    startActivity(intent);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView item_card_position;
            public TextView item_card_jobTitle;
            public TextView item_card_company;
            public ImageView item_card_img;
            public CustomViewHolder(View view) {
                super(view);
                item_card_company = (TextView) view.findViewById(R.id.item_card_company);
                item_card_jobTitle = (TextView) view.findViewById(R.id.item_card_jobTitle);
                item_card_position = (TextView) view.findViewById(R.id.item_card_position);
                item_card_img = (ImageView) view.findViewById(R.id.item_card_img);
            }
        }
    }

}