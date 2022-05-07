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
import androidx.recyclerview.widget.RecyclerView;

import com.example.bica.R;
import com.example.bica.model.Card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> implements Filterable {

    private String TAG = "CardAdapterTAG";
    ArrayList<Card> cards;
    ArrayList<Card> filterList;
    ArrayList<Card> initList;

    public CardAdapter(ArrayList<Card> cards) {
        this.cards = cards;
        initList = new ArrayList<>();
        initList.addAll(cards);
        filterList = new ArrayList<>();
        filteredList.addAll(cards);

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
    //data set changed
//    public void dataSetChanged(List<Card> exampleList) {
//        initList = cards;
//        notifyDataSetChanged();
//    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.tv_company.setText(initList.get(position).getCompany());
        holder.tv_email.setText(initList.get(position).getEmail());
        holder.tv_phone.setText(initList.get(position).getPhone());
        holder.tv_name.setText(initList.get(position).getName());
        holder.tv_jobTitle.setText(initList.get(position).getDepart());
        holder.tv_position.setText(initList.get(position).getPosition());

//        holder.tv_company.setText("cards.get(position).getCompany()");
//        holder.tv_email.setText("cards.get(position).getEmail()");
//        holder.tv_phone.setText("cards.get(position).getPhone()");
//        holder.tv_name.setText("cards.get(position).getName()");
//        holder.tv_jobTitle.setText("cards.get(position).getDepart()");
//        holder.tv_position.setText("cards.get(position).getPosition()");
    }

    @Override
    public int getItemCount() {
        return initList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    ArrayList<Card> filteredList = new ArrayList<>();
    private Filter exampleFilter = new Filter() {
        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();

            String filterPattern = constraint.toString().toLowerCase().trim();

//            if (filterPattern == null || filterPattern.isEmpty()) {
//                for(Card item : cards){
//                    filteredList.add(item);
//                }
//            } else {
//                for (Card item : cards) {
//                    //TODO filter 대상 setting
//                    if (item.getEmail().toLowerCase().contains(filterPattern)) {
//                        Log.d(TAG, "수신"+item.getEmail());
//                        filteredList.add(item);
//                    }
//                }
//            }
            for (Card item : cards) {
                //TODO filter 대상 setting
                if (item.getEmail().toLowerCase().contains(filterPattern)) {
                    Log.d(TAG, "사이즈"+cards.size());
                    Log.d(TAG, "사이즈"+filterList.size());
                    Log.d(TAG, "사이즈"+initList.size());
                    filteredList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        //Automatic on UI thread
//        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            Log.d(TAG, "빈칸이다다다다다");
            initList.clear();
            initList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

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