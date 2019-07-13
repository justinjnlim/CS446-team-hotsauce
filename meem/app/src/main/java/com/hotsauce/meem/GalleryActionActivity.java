package com.hotsauce.meem;

import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
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

    public Intent createIntent(Uri uri) {
        return ShareCompat.IntentBuilder.from(this).setStream(uri).getIntent();
    }

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

        final Button button2 = findViewById(R.id.share_meme_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(currentMeme.getFilepath());
                Uri uriToImage = FileProvider.getUriForFile(getApplicationContext(), "com.hotsauce.meem.fileprovider", file);
                Intent shareIntent = createIntent(uriToImage);
                shareIntent.setData(uriToImage);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent openInChooser = Intent.createChooser(shareIntent, "Open in");
                startActivity(openInChooser);
            }
        });

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