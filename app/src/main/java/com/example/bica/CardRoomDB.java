package com.example.bica;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bica.model.Card;

@Database(entities = {Card.class}, version = 1)
public abstract class CardRoomDB extends RoomDatabase {

    private static CardRoomDB instance;

    public abstract CardDao cardDao();

    public static synchronized CardRoomDB getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), CardRoomDB.class, "CardRoomDB") //name: 데이터베이스 이름, 개발자가 지정하고 싶은 이름
                    .fallbackToDestructiveMigration()       // 스키마(Database) 버전 변경 가능
                    .allowMainThreadQueries()               // Main Thread에서 DB에 IO(입출력)를 가능하게 함
                    .build();
        }

        return instance;
    };


}
