package com.example.bica;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.bica.model.Card;

import java.util.List;

public class CardRepository {
    private CardDao cardDao;
    private LiveData<List<Card>> allCards;
    private Application application;

    public CardRepository(Application application){
        this.application = application;

        CardRoomDB database = CardRoomDB.getInstance(application);
        cardDao = database.cardDao();
        allCards = cardDao.getCardAll();

    }

    public void setInsertCard(Card card){
        new InsertCardAsyncTask(cardDao).execute(card);
    }

    public void setUpdateCard(Card card){
        new UpdateCardAsyncTask(cardDao).execute(card);
    }

    public void setDeleteCard(Card card){
        new DeleteCardAsyncTask(cardDao).execute(card);
    }

    public void deleteCardAll(){
        new DeleteAllCardsAsyncTask(cardDao).execute();
    }

    private static class InsertCardAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        private InsertCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards){
            cardDao.setInsertCard(cards[0]);
            return null;
        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        private UpdateCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards){
            cardDao.setUpdateCard(cards[0]);
            return null;
        }
    }

    private static class DeleteCardAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        private DeleteCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards){
            cardDao.setDeleteCard(cards[0]);
            return null;
        }
    }

    private static class DeleteAllCardsAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        private DeleteAllCardsAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards){
            cardDao.deleteCardAll();
            return null;
        }
    }
}
