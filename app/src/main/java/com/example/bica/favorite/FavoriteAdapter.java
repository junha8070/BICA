package com.example.bica.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bica.R;
import com.example.bica.model.Card;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {


    private ArrayList<Card> cardModel;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewPager2 viewPager2;

    public FavoriteAdapter(ArrayList<Card> cardModel, ViewPager2 viewPager2) {
        this.cardModel = cardModel;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(layoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
//        holder.iv_profile.setImageResource(cardModel.get(position).getImage());
        holder.tv_company.setText(cardModel.get(position).getCompany());
        holder.tv_depart.setText(cardModel.get(position).getDepart());
        holder.tv_name.setText(cardModel.get(position).getName());
        holder.tv_position.setText(cardModel.get(position).getPosition());
        holder.tv_Phone.setText(cardModel.get(position).getPhone());
        holder.tv_Email.setText(cardModel.get(position).getEmail());
        holder.tv_Address.setText(cardModel.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return cardModel.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profile;
        TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone,tv_Email, tv_Address;
        FavoriteViewHolder(@NonNull View view){
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
