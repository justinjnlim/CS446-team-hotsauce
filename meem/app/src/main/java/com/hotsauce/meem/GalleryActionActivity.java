package com.hotsauce.meem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;


public class GalleryActionActivity extends AppCompatActivity {
    /*
    This is an activity for when a user clicks on an image in the gallery.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_action);

        // Render meme the user clicked on
        Bundle bundle = getIntent().getExtras();
        int image_id = bundle.getInt("image_id");
        ImageView imageView = (ImageView)findViewById(R.id.GalleryActionImage);
        imageView.setImageResource(image_id);
    }

}