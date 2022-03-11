package com.example.bica.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bica.R;
import com.example.bica.model.Card;

import org.w3c.dom.Text;

import java.util.List;

public class FavoriteAdapter extends PagerAdapter {

    private List<Card> cardModel;
    private LayoutInflater layoutInflater;
    private Context context;

    public FavoriteAdapter(List<Card> cardModel, Context context){
        this.cardModel = cardModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardModel.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_favorite_card, container, false);

        ImageView iv_profile;
        TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone,tv_Email, tv_Address;

        iv_profile = view.findViewById(R.id.iv_profile);
        tv_company = view.findViewById(R.id.tv_company);
        tv_depart = view.findViewById(R.id.tv_depart);
        tv_name = view.findViewById(R.id.tv_name);
        tv_position = view.findViewById(R.id.tv_position);
        tv_Phone = view.findViewById(R.id.tv_Phone);
        tv_Email = view.findViewById(R.id.tv_Email);
        tv_Address = view.findViewById(R.id.tv_Address);

        iv_profile.setImageResource(cardModel.get(position).getImage());
        tv_company.setText(cardModel.get(position).getCompany());
        tv_depart.setText(cardModel.get(position).getDepart());
        tv_name.setText(cardModel.get(position).getName());
        tv_position.setText(cardModel.get(position).getPosition());
        tv_Phone.setText(cardModel.get(position).getPhone());
        tv_Email.setText(cardModel.get(position).getEmail());
        tv_Address.setText(cardModel.get(position).getAddress());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
