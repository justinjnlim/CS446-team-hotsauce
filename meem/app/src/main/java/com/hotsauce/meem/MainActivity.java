package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hotsauce.meem.state.GreetingContext;

import java.io.File;
import java.util.ArrayList;

import java.util.Arrays;
import static com.hotsauce.meem.PhotoEditor.BaseActivity.READ_WRITE_STORAGE;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView greetingMessage;
    private static int RESULT_LOAD_IMG = 1;
    private RecyclerView recyclerView;
    private GalleryAdapter recyclerAdapter;
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

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setGreetingMessage();

        // Initialize RecyclerView for gallery
        recyclerView = findViewById(R.id.Gallery);
        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new GalleryAdapter(getMemeFilepaths(), this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void setGreetingMessage() {
        GreetingContext greetingContext = new GreetingContext();
        greetingMessage = findViewById(R.id.greeting);
        String[] filePaths = getMemeFilepaths();
        greetingMessage.setText(greetingContext.getGreetingsString(filePaths.length));
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSetChanged();
    }

    @UiThread
    protected void dataSetChanged() {
        recyclerAdapter.data = getMemeFilepaths();
        recyclerAdapter.notifyDataSetChanged();
        setGreetingMessage();
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
