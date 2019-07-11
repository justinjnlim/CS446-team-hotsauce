package com.hotsauce.meem;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hotsauce.meem.db.Meme;
import com.hotsauce.meem.db.MemeRepository;

import java.util.List;

public class MemeViewModel extends AndroidViewModel {

    private MemeRepository memeRepository;

    private LiveData<List<Meme>> allMemes;

    public MemeViewModel(Application application) {
        super(application);
        memeRepository = new MemeRepository(application);
        allMemes = memeRepository.getAllMemes();
    }

    LiveData<List<Meme>> getAllMemes() {
        return allMemes;
    }

    void insert(Meme meme) {
        memeRepository.insert(meme);
    }

    void delete(Meme meme) {
        memeRepository.delete(meme);
    }
}
