package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.GridLayoutManager;

import java.io.File;
import java.util.ArrayList;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import static com.hotsauce.meem.PhotoEditor.BaseActivity.READ_WRITE_STORAGE;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private static int RESULT_LOAD_IMG = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

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
        recyclerAdapter = new GalleryAdapter(getMemeFilepaths(), this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public boolean requestPermission(String permission) {
        // TODO remove this and somehow re-use the implementation in BaseActivity
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    READ_WRITE_STORAGE);
        }
        return isGranted;
    }

    protected String[] getMemeFilepaths() {
        if (requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator);
            File[] files = directory.listFiles();
            ArrayList<String> memes = new ArrayList<String>();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".png")) {
                    memes.add(files[i].getAbsolutePath());
                }
            }
            return Arrays.copyOf(memes.toArray(), memes.size(), String[].class);
        } else {
            return new String[0];
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
        }
    }
}
