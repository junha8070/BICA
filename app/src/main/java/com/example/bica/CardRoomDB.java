package com.example.bica;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDbAsynTask(instance).execute();
        }
    };

    private static class PopulateDbAsynTask extends AsyncTask<Void,Void,Void>{
        private CardDao cardDao;

        private PopulateDbAsynTask(CardRoomDB db){
            cardDao = db.cardDao();
        }

        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }
    }
}
