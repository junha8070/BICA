package com.example.bica.EntireCard;

import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.bica.R;
import com.example.bica.model.Card;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-08-07.
 */

public class CardDialog {

    private Context context;

    public CardDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(Card cardInfo) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_card_dialog);

        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes((WindowManager.LayoutParams) params);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final MaterialToolbar cardView_toolbar;
        final ImageView iv_profile;
        final TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone, tv_Email, tv_Address;

        cardView_toolbar = dlg.findViewById(R.id.cardView_toolbar);
        iv_profile = dlg.findViewById(R.id.iv_profile);
        tv_company = dlg.findViewById(R.id.tv_company);
        tv_depart = dlg.findViewById(R.id.tv_depart);
        tv_name = dlg.findViewById(R.id.tv_name);
        tv_position = dlg.findViewById(R.id.tv_position);
        tv_Phone = dlg.findViewById(R.id.tv_Phone);
        tv_Email = dlg.findViewById(R.id.tv_Email);
        tv_Address = dlg.findViewById(R.id.tv_Address);

        tv_company.setText(cardInfo.getCompany());
        tv_depart.setText(cardInfo.getDepart());
        tv_name.setText(cardInfo.getName());
        tv_position.setText(cardInfo.getPosition());
        tv_Phone.setText(cardInfo.getPhone());
        tv_Email.setText(cardInfo.getEmail());
        tv_Address.setText(cardInfo.getAddress());

        cardView_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        cardView_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.delete:
                        Toast.makeText(dlg.getContext(), "삭제 버튼", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.edit:
                        Toast.makeText(dlg.getContext(), "수정 버튼", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.favorite:
                        Toast.makeText(dlg.getContext(), "즐겨찾기 버튼", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }
}
