package com.hotsauce.meem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.hotsauce.meem.PhotoEditor.TextEditorDialogFragment;
import com.hotsauce.meem.db.Meme;

import java.io.File;
import java.io.FileOutputStream;

public class CreateMemeActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private RelativeLayout imageLayout;
    private ImageView image;
    private MemeViewModel memeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_create_meme);

        Intent intent = getIntent();
        Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        image = (ImageView)findViewById(R.id.image);
        image.setImageURI(imageUri);
        image.setOnTouchListener(this);

        imageLayout = (RelativeLayout)findViewById(R.id.imageLayout);

        // Set button listeners
        ImageButton closeButton = (ImageButton)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
        ImageButton saveButton = (ImageButton)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        memeViewModel = ViewModelProviders.of(this).get(MemeViewModel.class);
    }

    public boolean onTouch (View v, MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN :
                // TODO
                Log .i("CreateTemplateActivityLog", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP :
                // TODO
                Log .i("CreateTemplateActivityLog", " " + ev.getX() + "," + ev.getY());
                final int x1 = 125;
                final int x2 = 1200;
                final int y1 = 200;
                final int y2 = 691;
                if (x1 <= ev.getX() && x2 >= ev.getX() && y1 <= ev.getY() && y2 >= ev.getY()) {
                    TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                    textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                        @Override
                        public void onDone(String inputText, int colorCode) {
                            // TODO
                            createTextBox(inputText, colorCode, x1, x2, y1, y2);
                            Log .i("CreateTemplateActivityLog", "inputText:" + inputText);
                        }
                    });
                }

                break;
        }
        return true;
    }

    public void createTextBox(String inputText, int colorCode, int x1, int x2, int y1, int y2) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                Math.abs(x1-x2),
                Math.abs(y1-y2));
        TextView tv = new TextView(this);

        // size textView to fit width and height
        tv.setAutoSizeTextTypeUniformWithConfiguration(1, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
        tv.setTextColor(colorCode);
        tv.setLayoutParams(lp);
        tv.setText(inputText);
        imageLayout.addView(tv);

        final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams)tv.getLayoutParams();
        Log .i("CreateTemplateActivityLog", "margins: " + x1 + "," + y1);
        lpt.setMargins(x1, y1, 0, 0);
    }

    public void saveImage(Bitmap bitmap){
        final String memeId = Long.toString(System.currentTimeMillis());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + memeId + ".png");

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            memeViewModel.insert(new Meme(memeId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.TRANSPARENT);
        v.draw(c);
        return b;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                onBackPressed();
                break;
            case R.id.saveButton:
                //Bitmap bitmap = loadBitmapFromView(imageLayout, imageLayout.getWidth(), imageLayout.getHeight());
                imageLayout.setDrawingCacheEnabled(true);
                imageLayout.buildDrawingCache(true);
                //Bitmap bitmap = Bitmap.createBitmap(imageLayout.getDrawingCache());
                Bitmap bitmap = loadBitmapFromView(imageLayout, image.getWidth(), image.getHeight());
                saveImage(bitmap);

                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // show save dialogue if user is in editing mode
        super.onBackPressed();
    }
}
