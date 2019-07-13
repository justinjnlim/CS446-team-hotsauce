package com.hotsauce.meem;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hotsauce.meem.db.Meme;
import com.hotsauce.meem.db.MemeRepository;
import com.hotsauce.meem.db.MemeTemplate;

import java.util.List;

public class MemeViewModel extends AndroidViewModel {

    private MemeRepository memeRepository;

    private LiveData<List<Meme>> allMemes;
    private LiveData<List<MemeTemplate>> allTemplates;

    public MemeViewModel(Application application) {
        super(application);
        memeRepository = new MemeRepository(application);
        allMemes = memeRepository.getAllMemes();
        allTemplates = memeRepository.getAllMemeTemplates();
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

    LiveData<List<MemeTemplate>> getAllTemplates() {
        return allTemplates;
    }

    void insertTemplate(MemeTemplate memeTemplate) {
        memeRepository.insertTemplate(memeTemplate);
    }

    void deleteTemplate(MemeTemplate memeTemplate) {
        memeRepository.deleteTemplate(memeTemplate);
    }
}
