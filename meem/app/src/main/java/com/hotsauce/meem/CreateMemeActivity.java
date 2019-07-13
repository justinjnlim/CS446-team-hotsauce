package com.hotsauce.meem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.hotsauce.meem.PhotoEditor.TextEditorDialogFragment;

public class CreateMemeActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
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
        ImageView image = findViewById(R.id.image);
        image.setImageURI(imageUri);
        image.setOnTouchListener(this);

        // Set button listeners
        ImageButton closeButton = (ImageButton)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
        ImageButton saveButton = (ImageButton)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
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
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        mainLayout.addView(tv);

        final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams)tv.getLayoutParams();
        Log .i("CreateTemplateActivityLog", "margins: " + x1 + "," + y1);
        lpt.setMargins(x1, y1, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                onBackPressed();
                break;
            case R.id.saveButton:
                // TODO
                break;
            case R.id.addTextButton:
                // TODO
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        // TODO
                        Log .i("CreateTemplateActivityLog", "inputText:" + inputText);
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // show save dialogue if user is in editing mode
        super.onBackPressed();
    }
}
