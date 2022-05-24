package com.example.bica;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.bica.EntireCard.CardDialog;
import com.example.bica.model.Card;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 전화 상태 리시버
 *
 * @author 임성진
 * @version 1.0.0
 * @since 2018-04-05 오전 1:12
 **/
public class CallCheckReceiver extends BroadcastReceiver {

    private final static String TAG = "free_call";
    private AudioManager audioManager;
    // 전화번호
    private static String phoneNumber = "";

    private TextView tv_company, tv_depart, tv_name, tv_position, tv_Phone, tv_Email, tv_Address, tv_group;
    private Button btn_close;
    // incoming 수신 플래그
    private static boolean incomingFlag = false;
    String number = "";
    String title = "";
    String content = "";
    String email = "";
    String company = "";
    String depart = "";
    String name = "";
    String position = "";
    String phone = "";
    String address = "";
    String occupation = "";
    String memo = "";
    String group = "";
    Card card;
    private static String mLastState;
    public CardDao mcardDao;

    protected View rootView;
    WindowManager.LayoutParams params;
    private WindowManager windowManager;

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO Auto-generated method stub

//        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//        if ((state.equals(mLastState)&&(number.equals("null"))||number.isEmpty())) {
//            return;
//        } else {
//            mLastState = state;
//
//        }
        Bundle bundle = intent.getExtras();

        this.audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // TODO 전화를 걸었을 떄이다. (안드로이드 낮은 버전에서는 여기로 들어온다)
            this.incomingFlag = false;
            number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

        }

        // 폰 상태 체크
        processPhoneState(intent, context);

    }

    /**
     * 폰 상태를 체크한다,
     *
     * @author 임성진
     * @version 1.0.0
     * @since 2018-04-05 오전 1:44
     **/

    private void processPhoneState(Intent intent, Context context) {
        Bundle bundle = intent.getExtras();
        HashMap<String, Card> contactsValue = new HashMap<String, Card>();
        ArrayList<Card> arrContactsValue = new ArrayList<>();
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

//        CardRoomDB cardRoomDB = Room.databaseBuilder(context.getApplicationContext(), CardRoomDB.class,"CardRoomDB")
//                .fallbackToDestructiveMigration()
//                .allowMainThreadQueries()
//                .build();
//
//        mcardDao = cardRoomDB.cardDao();
//        LiveData<List<Card>> list =  mcardDao.getCardAll();
//        Log.d(TAG, "2");
//        Log.d(TAG, String.valueOf(list.getValue()));

        switch (tm.getCallState()) {

            case TelephonyManager.CALL_STATE_RINGING:

                // TODO 전화를 왔을때이다. (벨이 울릴때)

                this.incomingFlag = true;

//                this.phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG, String.valueOf(contactsValue.size()));
                Log.d(TAG, title + name + email + company + address + phone + occupation + depart + position + memo);


                if (number != null) {
                     SharedPreferences prefb = context.getSharedPreferences("memo_contain", MODE_PRIVATE);
                Collection<?> col_val = prefb.getAll().values();
                Iterator<?> it_val = ((Collection<?>) col_val).iterator();
                Collection<?> col_key = prefb.getAll().keySet();
                Iterator<?> it_key = col_key.iterator();


                while (it_val.hasNext() && it_key.hasNext()) {
                    String key = (String) it_key.next();
                    String value = (String) it_val.next();

                    // value 값은 다음과 같이 저장되어있다
                    // "{\"title\":\"hi title\",\"content\":\"hi content\"}"
                    try {
                        card = new Card();
                        // String으로 된 value를 JSONObject로 변환하여 key-value로 데이터 추출
                        JSONObject jsonObject = new JSONObject(value);
                        title = (String) jsonObject.getString("title");
                        name = (String) jsonObject.getString("name");
                        email = (String) jsonObject.getString("email");
                        company = (String) jsonObject.getString("company");
                        address = (String) jsonObject.getString("address");
                        phone = (String) jsonObject.getString("phone");
                        occupation = (String) jsonObject.getString("occupation");
                        depart = (String) jsonObject.getString("depart");
                        position = (String) jsonObject.getString("position");
                        memo = (String) jsonObject.getString("memo");

                        card.setName(name);
                        card.setEmail(email);
                        card.setCompany(company);
                        card.setAddress(address);
                        card.setPhone(phone);
                        card.setOccupation(occupation);
                        card.setDepart(depart);
                        card.setPosition(position);
                        card.setMemo(memo);

                        contactsValue.put(title, card);
//                        arrContactsValue.add(card);

                        Log.d(TAG, "where");
                        Log.d(TAG, title + name + email + company + address + phone + occupation + depart + position + memo);
                        // 리사이클러뷰 어뎁터 addItem으로 목록 추가
//                        memoAdapter.addItem(new MemoItem(key, title, content));
                    } catch (JSONException e) {

                    }

                    // 목록 갱신하여 뷰에 띄어줌
//                    memoAdapter.notifyDataSetChanged();

                }
                Log.d(TAG, number);
                Log.d(TAG, contactsValue.get(number).getEmail());
                    Toast.makeText(context, contactsValue.get(number).getName()+"님에게 전화가 왔습니다.", Toast.LENGTH_LONG).show();

                    windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);

                    Display display = windowManager.getDefaultDisplay();

                    int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%


                    params = new WindowManager.LayoutParams(
                            width,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                            PixelFormat.TRANSLUCENT);


                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    rootView = layoutInflater.inflate(R.layout.activity_call_dialog, null);
                    tv_company = rootView.findViewById(R.id.tv_company);
                    tv_depart = rootView.findViewById(R.id.tv_depart);
                    tv_name = rootView.findViewById(R.id.tv_name);
                    tv_position = rootView.findViewById(R.id.tv_position);
                    tv_Phone = rootView.findViewById(R.id.tv_Phone);
                    tv_Email = rootView.findViewById(R.id.tv_Email);
                    tv_Address = rootView.findViewById(R.id.tv_Address);
                    tv_group = rootView.findViewById(R.id.tv_group);
                    btn_close = rootView.findViewById(R.id.btn_close);

                    tv_company.setText(contactsValue.get(number).getCompany());
                    tv_depart.setText(contactsValue.get(number).getDepart());
                    tv_name.setText(contactsValue.get(number).getName());
                    tv_position.setText(contactsValue.get(number).getPosition());
                    tv_Phone.setText(contactsValue.get(number).getPhone());
                    tv_Email.setText(contactsValue.get(number).getEmail());
                    tv_Address.setText(contactsValue.get(number).getAddress());
                    tv_group.setText(contactsValue.get(number).getGroup());
//                    ButterKnife.inject(this, rootView);
                    setDraggable();
                    windowManager.addView(rootView, params);

                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (rootView != null && windowManager != null) windowManager.removeView(rootView);
                        }
                    });

//                    CallDialogActivity callDialogActivity = new CallDialogActivity(context.getApplicationContext());
//                    callDialogActivity.callFunction(contactsValue.get(number));
//                    Toast.makeText(context, list.getValue().size(), Toast.LENGTH_SHORT).show();
                }
                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:

                if (this.incomingFlag) {

                    // TODO 전화가 왔을때이다. (통화 시작)

                } else {

                    // TODO 안드로이드 8.0 으로 테스트했을때는 ACTION_NEW_OUTGOING_CALL 거치지 않고, 이쪽으로 바로 온다.
                    this.phoneNumber = intent.getStringExtra("incoming_number");

                }

                if (this.incomingFlag) {
                    // TODO 전화가 왔고, 통화를 시작했을때 그에 맞는 프로세스를 실행한다.
                    windowManager.removeView(rootView);
//                    Toast.makeText(context, "전화가 와서 받았음 " + phoneNumber, Toast.LENGTH_SHORT).show();
                } else {
                    // TODO 전화를 했을때 그에 맞는 프로세스를 실행한다.
//                    Toast.makeText(context, "전화 했음 " + phoneNumber, Toast.LENGTH_SHORT).show();
                }

                break;

            case TelephonyManager.CALL_STATE_IDLE:
                if (this.incomingFlag) {
                    // TODO 전화가 왔을때이다. (전화를 끊었을 경우)
//                    windowManager.removeView(rootView);
//                    Toast.makeText(context, "전화가 왔고, 끊었음 " + phoneNumber, Toast.LENGTH_SHORT).show();

                } else {
                    // TODO 전화를 걸었을 떄이다. (전화를 끊었을 경우)
//                    Toast.makeText(context, "전화를 했고, 끊었음 " + phoneNumber, Toast.LENGTH_SHORT).show();

                }

                break;

        }

    }

    private void setDraggable() {

        rootView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        if (rootView != null)
                            windowManager.updateViewLayout(rootView, params);
                        return true;
                }
                return false;
            }
        });

    }


}