package com.hotsauce.meem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.GridLayoutManager;
import java.lang.reflect.Field;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static int RESULT_LOAD_IMG = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private final String image_titles[] = {
            "meme1",
            "meme2",
            "meme3",
            "meme4"
    };

    private final Integer image_ids[] = {
            R.drawable.meme1,
            R.drawable.meme2,
            R.drawable.meme3,
            R.drawable.meme4,
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize RecyclerView for gallery
        recyclerView = (RecyclerView) findViewById(R.id.Gallery);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new GalleryAdapter(getGalleryData());
        recyclerView.setAdapter(recyclerAdapter);
    }

    protected Integer[] getGalleryData() {
        /*
        Returns data to then feed into Gallery.
         */

        // Retrieve all images in drawables folder
        Field[] fields = R.drawable.class.getFields();

        // Filter for images that start with prefix "meme"
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i = 0; i < fields.length; i++) {
            try {
                String name = fields[i].getName();
                if (!name.startsWith("meme")) {
                    continue;
                }
                ids.add(fields[i].getInt(null));
            } catch (Exception e) {
            }
        }

        // Return images
        Utils utils = new Utils();
        return utils.integerArrayListToIntegerList(ids);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                //image_view.setImageBitmap(selectedImage);
                Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            //Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

}
