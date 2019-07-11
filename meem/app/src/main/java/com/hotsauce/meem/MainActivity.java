package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.hotsauce.meem.state.GreetingContext;

import java.util.List;

import static com.hotsauce.meem.PhotoEditor.BaseActivity.READ_WRITE_STORAGE;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    GreetingContext greetingContext;

    private GalleryViewModel memeViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int RESULT_LOAD_IMG = 1;
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

        if (requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Everything related to populating the gallery with memes
            RecyclerView recyclerView = findViewById(R.id.Gallery);
            final GalleryViewAdapter adapter = new GalleryViewAdapter(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setHasFixedSize(true);

            greetingContext = new GreetingContext();
            final TextView greetingView = findViewById(R.id.greeting);

            // Get a new or existing ViewModel
            memeViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
            memeViewModel.getAllMemes().observe(this, new Observer<List<Meme>>() {
                @Override
                public void onChanged(@Nullable final List<Meme> memes) {
                    adapter.setMemes(memes);
                    greetingView.setText(greetingContext.getGreetingsString(memes.size()));
                }
            });
        }

        // Sets the bottom text message
        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

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
