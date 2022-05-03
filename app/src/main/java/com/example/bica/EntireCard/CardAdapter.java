package com.example.bica.EntireCard;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bica.R;
import com.example.bica.model.Card;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    ArrayList<Card> cards;

    public CardAdapter(ArrayList<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.tv_company.setText(cards.get(position).getCompany());
        holder.tv_email.setText(cards.get(position).getEmail());
        holder.tv_phone.setText(cards.get(position).getPhone());
        holder.tv_name.setText(cards.get(position).getName());
        holder.tv_jobTitle.setText(cards.get(position).getDepart());
        holder.tv_position.setText(cards.get(position).getPosition());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder{

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
