package com.hotsauce.meem.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MemeRepository {
    private MemeDao mMemeDao;
    private LiveData<List<Meme>> mAllMemes;

    public MemeRepository(Application application) {
        MemeRoomDatabase db = MemeRoomDatabase.getDatabase(application);
        mMemeDao = db.memeDao();
        mAllMemes = mMemeDao.getAllMemes();
    }

    public LiveData<List<Meme>> getAllMemes() {
        return mAllMemes;
    }

    public void insert(Meme meme) {
        new insertAsyncTask(mMemeDao).execute(meme);
    }

    public void delete(Meme meme) {
        new deleteMemeAsyncTask(mMemeDao).execute(meme);
    }

    private static class insertAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao mAsyncTaskDao;

        insertAsyncTask(MemeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Meme... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteMemeAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao mAsyncTaskDao;

        deleteMemeAsyncTask(MemeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Meme... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
