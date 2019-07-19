package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hotsauce.meem.TemplateCreator.TemplateEditorActivity;
import com.hotsauce.meem.db.Meme;
import com.hotsauce.meem.state.GreetingContext;

import java.io.File;
import java.util.List;

import static com.hotsauce.meem.PhotoEditor.BaseActivity.READ_WRITE_STORAGE;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    GreetingContext greetingContext;

    private MemeViewModel memeViewModel;
    private int GALLERY_ACTIVITY = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(MainActivity.this, CreateTemplateActivity.class);
                    startActivity(intent);
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(MainActivity.this, TemplateGalleryActivity.class);
                    intent.addFlags(65536); // disables animation
                    startActivityForResult(intent, GALLERY_ACTIVITY);
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
            memeViewModel = ViewModelProviders.of(this).get(MemeViewModel.class);
            memeViewModel.getAllMemes().observe(this, new Observer<List<Meme>>() {
                @Override
                public void onChanged(@Nullable final List<Meme> memes) {
                    for (Meme meme : memes) {
                        File f = new File(meme.getFilepath());
                        if (!f.exists()) {
                            memeViewModel.deleteMeme(meme);
                            return;
                        }
                    }
                    adapter.setMemes(memes);
                    greetingView.setText(greetingContext.getGreetingsString(memes.size()));
                }
            });
        }

        // Sets the bottom text message
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
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
        if (reqCode == GALLERY_ACTIVITY) {
            navigation.getMenu().getItem(0).setChecked(true);
        }
    }
}
