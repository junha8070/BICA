package com.example.bica.favorite;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.bica.AddCard.QR_Make_Activity;
import com.example.bica.EntireCard.CardFragment;
import com.example.bica.EntireCard.CardViewModel;
import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private String TAG = "FavoriteAdapterTAG";

    private ArrayList<Card> cardModel;
    private LayoutInflater layoutInflater;
    private ViewPager2 viewPager2;
    private AlertDialog.Builder builder;
    private CardViewModel cardViewModel;
    Application application;

    RecyclerViewEmptySupport recyclerViewEmptySupport;
    MaterialCardView default_card;

    public FavoriteAdapter(ArrayList<Card> cardModel, ViewPager2 viewPager2, Application application) {
        Log.d(TAG, String.valueOf(cardModel.isEmpty()));
        this.cardModel = cardModel;
        this.viewPager2 = viewPager2;
        this.application = application;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(cardModel.size()==0){
            Log.d(TAG,"값 없음");
            return new FavoriteViewHolder(layoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_favorite_card, parent, false));
        }else{
            return new FavoriteViewHolder(layoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        int i = position;
        Glide.with(holder.itemView.getContext()).load(cardModel.get(i).getImage()).fitCenter().into(holder.iv_profile);
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
                showDialog(i, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(cardModel.isEmpty()){
            viewPager2.setVisibility(View.GONE);
            recyclerViewEmptySupport = new RecyclerViewEmptySupport(viewPager2.getContext());
            recyclerViewEmptySupport.setEmptyView(default_card);
            return cardModel.size();
        }else{
            return cardModel.size();
        }
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone, tv_Email, tv_Address;
        Context context;

        FavoriteViewHolder(@NonNull View view) {
            super(view);
            context = view.getContext();
            cardViewModel = new CardViewModel(application);
            iv_profile = view.findViewById(R.id.iv_profile);
            tv_company = view.findViewById(R.id.tv_company);
            tv_depart = view.findViewById(R.id.tv_depart);
            tv_name = view.findViewById(R.id.tv_name);
            tv_position = view.findViewById(R.id.tv_position);
            tv_Phone = view.findViewById(R.id.tv_Phone);
            tv_Email = view.findViewById(R.id.tv_Email);
            tv_Address = view.findViewById(R.id.tv_Address);
            default_card = view.findViewById(R.id.default_card);
        }
    }

    //다이얼로그 실행(공유방법 선택창)
    public void showDialog(int position, View view) {

        String[] navigate = {"전화걸기", "메세지 보내기", "즐겨찾기 해제", "명함 삭제"};

        builder = new AlertDialog.Builder(viewPager2.getContext());

        //다이얼로그에 리스트 담기
        builder.setItems(navigate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(builder.getContext(), "선택" + navigate[which], Toast.LENGTH_SHORT).show();
                switch (which) {
                    case 0:
                        String tel = "tel:" + cardModel.get(position).getPhone();
                        //startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
                        builder.getContext().startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(builder.getContext(),cardModel.get(position).getPhone(), Toast.LENGTH_SHORT).show();
                        sendSmsIntent(cardModel.get(position).getPhone(), view);
                        break;
                    case 2:
                        Toast.makeText(builder.getContext(), "3개발중", Toast.LENGTH_SHORT).show();
                        cardViewModel.delFavorite(cardModel.get(position));
                        cardModel.remove(position);
                        notifyDataSetChanged();
                        break;
                    case 3:
                        Toast.makeText(builder.getContext(), "4개발중", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void sendSmsIntent(String number, View view){
        try{
            Log.d(TAG, number);
            Uri smsUri = Uri.parse("sms:"+number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
//            sendIntent.putExtra("sms_body", "");

            view.getContext().startActivity(sendIntent);

//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("address", number);
//        sendIntent.putExtra("sms_body", editBody.getText().toString());
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        startActivity(sendIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
