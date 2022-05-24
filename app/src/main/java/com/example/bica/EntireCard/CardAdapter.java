package com.example.bica.EntireCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> implements Filterable {

    private String TAG = "CardAdapterTAG";
    ArrayList<Card> cards = new ArrayList<>();
    ArrayList<Card> initList = new ArrayList<>();
    ArrayList<Card> filteredList = new ArrayList<>();
    ArrayList<Card> groupList = new ArrayList<>();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public CardAdapter(ArrayList<Card> cards) {
        this.cards = cards;
        initList = new ArrayList<>();
        initList.addAll(cards);
        groupList.addAll(cards);

    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_card, parent, false);
        CardAdapter.CardViewHolder vh = new CardAdapter.CardViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        int pos = position;
        holder.tv_company.setText(initList.get(position).getCompany());
        holder.tv_email.setText(initList.get(position).getEmail());
        holder.tv_phone.setText(initList.get(position).getPhone());
        holder.tv_name.setText(initList.get(position).getName());
        holder.tv_jobTitle.setText(initList.get(position).getDepart());
        holder.tv_position.setText(initList.get(position).getPosition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardDialog cardDialog = new CardDialog(view.getContext());
                cardDialog.callFunction(cards.get(pos));
            }
        });

        Glide.with(holder.itemView.getContext()).load(initList.get(position).getImage()).into(holder.iv_img);
    }

    @Override
    public int getItemCount() {
        return initList.size();
    }

    // 검색기능 구현
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    // 검색기능중 필터링 작업
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();

            String filterPattern = constraint.toString().toLowerCase().trim();

            for (Card item : cards) {
                //TODO filter 대상 setting
                if (item.getEmail().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        // 검색 결과 화면에 보여주기 위한 코드
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            initList.clear();
            initList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getCardGroup() {
        return cardGroup;
    }

    public Filter cardGroup = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            groupList.clear();
            System.out.println("group test 4 " + initList.size());
            if (constraint == "") {
                initList.clear();
                System.out.println("gp test 1 " + cards.size());
                System.out.println("gp test 2 " + initList.size());
                initList.addAll(cards);
                System.out.println("gp test 3 " + initList.size());
                for (Card item : initList) {
                    groupList.add(item);
                }
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Card item : cards) {
                    System.out.println("그룹 이름 filterP : " + filterPattern);
                    System.out.println("그룹 이름 cards : " + item.getGroup());

                    if (item.getGroup() != null) {
                        if (item.getGroup().equals(filterPattern)) {
                            System.out.println("그룹 email : " + item.getEmail());

                            groupList.add(item);
                            System.out.println("그룹 1 " + groupList.size());
                            System.out.println("그룹 2 " + groupList);
                        }
                    } else {
                        System.out.println("group null");
                    }
                }
            }
            System.out.println("test group1 " + groupList);
            System.out.println("test group2 " + groupList.size());


            FilterResults results = new FilterResults();

            results.values = groupList;
            System.out.println("grr 1 " + groupList.size());
            for (Card item : groupList) {
                System.out.println("grr 2 " + item.getEmail());
            }
            System.out.println("grr " + results.values);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            initList.clear();
            initList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_company, tv_jobTitle, tv_name, tv_position, tv_phone, tv_email;
        private ImageView iv_img;

        public CardViewHolder(@NonNull View view) {
            super(view);

            tv_company = itemView.findViewById(R.id.item_card_company);
            tv_jobTitle = itemView.findViewById(R.id.item_card_jobTitle);
            tv_name = itemView.findViewById(R.id.item_card_name);
            tv_position = itemView.findViewById(R.id.item_card_position);
            tv_phone = itemView.findViewById(R.id.item_card_phone);
            tv_email = itemView.findViewById(R.id.item_card_email);
            iv_img = itemView.findViewById(R.id.item_card_img);
        }
    }
}