package com.hotsauce.meem;

import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hotsauce.meem.db.Meme;

import java.io.File;


public class GalleryActionActivity extends AppCompatActivity {
    /*
    This is an activity for when a user clicks on an image in the gallery.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final MemeViewModel memeViewModel = ViewModelProviders.of(this).get(MemeViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_action);

        // Render meme the user clicked on
        Bundle bundle = getIntent().getExtras();
        final Meme currentMeme = (Meme) bundle.getSerializable("meme");

        ImageView imageView = findViewById(R.id.GalleryActionImage);
        imageView.setImageBitmap(BitmapFactory.decodeFile(currentMeme.getFilepath()));

        final Button button = findViewById(R.id.delete_meme_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new File(currentMeme.getFilepath()).delete()) {
                    memeViewModel.delete(currentMeme);
                }
                finish();
            }
        });

    }

}