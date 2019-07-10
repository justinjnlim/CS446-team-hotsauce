package com.hotsauce.meem;

import android.graphics.BitmapFactory;
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
        String memePath = bundle.getString("image_path");
        ImageView imageView = findViewById(R.id.GalleryActionImage);
        imageView.setImageBitmap(BitmapFactory.decodeFile(memePath));
    }

}