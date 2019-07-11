package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.hotsauce.meem.db.Meme;
import com.hotsauce.meem.db.MemeRepository;
import com.hotsauce.meem.state.GreetingContext;

import java.io.File;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.hotsauce.meem.PhotoEditor.BaseActivity.READ_WRITE_STORAGE;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView greetingMessage;
    private static int RESULT_LOAD_IMG = 1;

    private MemeViewModel memeViewModel;

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

        RecyclerView recyclerView = findViewById(R.id.Gallery);
        final MemeViewAdapter adapter = new MemeViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        // Get a new or existing ViewModel
        memeViewModel = ViewModelProviders.of(this).get(MemeViewModel.class);

        memeViewModel.getAllMemes().observe(this, new Observer<List<Meme>>() {
            @Override
            public void onChanged(@Nullable final List<Meme> memes) {
                adapter.setMemes(memes);
            }
        });

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //setGreetingMessage();

        // Initialize RecyclerView for gallery

        /*
        recyclerAdapter = new GalleryAdapter(getMemes(), this);
        */
    }

    /*
    public void setGreetingMessage() {
        GreetingContext greetingContext = new GreetingContext();
        greetingMessage = findViewById(R.id.greeting);
        List<Meme> memes = memeViewModel.getAllMemes().getValue();
        greetingMessage.setText(greetingContext.getGreetingsString(memes.size()));
    }
    */

    /*
    @Override
    protected void onResume() {
        super.onResume();
        dataSetChanged();
    }

    @UiThread
    protected void dataSetChanged() {
        //recyclerAdapter.setMemes(getMemes());
        recyclerAdapter.notifyDataSetChanged();
        //setGreetingMessage();
    }
    */

    public boolean requestPermission(String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        if (!isGranted) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    READ_WRITE_STORAGE);
        }
        return isGranted;
    }

    /*
    protected List<Meme> getMemes() {
        if (requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            List<Meme> val = memeRepository.getAllMemes().getValue();
            if (val == null) {
                return Collections.emptyList();
            }
            return val;

            File directory = new File(Environment.getExternalStorageDirectory() + File.separator);
            File[] files = directory.listFiles();
            ArrayList<String> memes = new ArrayList<String>();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".png")) {
                    memes.add(files[i].getAbsolutePath());
                }
            }
            return ;

        } else {
            return Collections.emptyList();
        }


    }*/

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
