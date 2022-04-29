package com.example.bica.mycard;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bica.R;
import com.example.bica.favorite.FavoriteAdapter;
import com.example.bica.model.Card;

import java.util.ArrayList;

public class MyCardAdapter extends RecyclerView.Adapter<MyCardAdapter.MycardViewHolder>{
    private ArrayList<Card> cardModel;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewPager2 viewPager2;
    private AlertDialog.Builder builder;

    public MyCardAdapter(ArrayList<Card> cardModel, ViewPager2 viewPager2) {
        this.cardModel = cardModel;
        this.viewPager2 = viewPager2;
    }
    @NonNull

    public MyCardAdapter.MycardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCardAdapter.MycardViewHolder(layoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_card, parent, false));
    }

    public void onBindViewHolder(@NonNull MyCardAdapter.MycardViewHolder holder, int position) {
        int i = position;
//        holder.iv_profile.setImageResource(cardModel.get(position).getImage());
        holder.tv_company.setText(cardModel.get(i).getCompany());
        holder.tv_depart.setText(cardModel.get(i).getDepart());
        holder.tv_name.setText(cardModel.get(i).getName());
        holder.tv_position.setText(cardModel.get(i).getPosition());
        holder.tv_Phone.setText(cardModel.get(i).getPhone());
        holder.tv_Email.setText(cardModel.get(i).getEmail());
        holder.tv_Address.setText(cardModel.get(i).getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public int getItemCount() {
        return cardModel.size();
    }

    class MycardViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profile;
        TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone,tv_Email, tv_Address;
        MycardViewHolder(@NonNull View view){
            super(view);

            iv_profile = view.findViewById(R.id.iv_profile);
            tv_company = view.findViewById(R.id.tv_company);
            tv_depart = view.findViewById(R.id.tv_depart);
            tv_name = view.findViewById(R.id.tv_name);
            tv_position = view.findViewById(R.id.tv_position);
            tv_Phone = view.findViewById(R.id.tv_Phone);
            tv_Email = view.findViewById(R.id.tv_Email);
            tv_Address = view.findViewById(R.id.tv_Address);
        }
    }

}
