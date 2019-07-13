package com.hotsauce.meem.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MemeRepository {
    private MemeDao mMemeDao;
    private LiveData<List<Meme>> mAllMemes;
    private LiveData<List<MemeTemplate>> mAllMemeTemplates;

    public MemeRepository(Application application) {
        MemeRoomDatabase db = MemeRoomDatabase.getDatabase(application);
        mMemeDao = db.memeDao();
        mAllMemes = mMemeDao.getAllMemes();
        mAllMemeTemplates = mMemeDao.getAllMemeTemplates();
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

    public LiveData<List<MemeTemplate>> getAllMemeTemplates() {
        return mAllMemeTemplates;
    }

    public void insertTemplate(MemeTemplate memeTemplate) {
        new insertTemplateAsyncTask(mMemeDao).execute(memeTemplate);
    }

    private static class insertTemplateAsyncTask extends AsyncTask<MemeTemplate, Void, Void> {
        private MemeDao mAsyncTaskDao;

        insertTemplateAsyncTask(MemeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MemeTemplate... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deleteTemplate(MemeTemplate memeTemplate) {
        new deleteTemplateAsyncTask(mMemeDao).execute(memeTemplate);
    }

    private static class deleteTemplateAsyncTask extends AsyncTask<MemeTemplate, Void, Void> {
        private MemeDao mAsyncTaskDao;

        deleteTemplateAsyncTask(MemeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MemeTemplate... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
