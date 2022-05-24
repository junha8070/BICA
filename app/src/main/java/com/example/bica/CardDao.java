package com.example.bica;

import com.example.bica.model.Card;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

// Data Access Object

@Dao
public interface CardDao {

    @Insert // 삽입
    void setInsertCard(Card card);

    @Update // 수정
    void setUpdateCard(Card card);

    @Delete // 삭제
    void setDeleteCard(Card card);

    // 전체 삭제 쿼리
    @Query("DELETE FROM Card")
    void deleteCardAll();

    // 조회 쿼리
    @Query("SELECT * FROM Card ORDER BY roomID ASC")  // 쿼리 : 데이터베이스에 요청하는 명령문 , * : 전체 조회
    LiveData<List<Card>> getCardAll();

    // 번호 조회 쿼리
//    @Query("SELECT phone, name, company  FROM Card")
//    void getInfo();

}
