package com.hotsauce.meem.TemplateCreator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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
import com.hotsauce.meem.MemeViewModel;
import com.hotsauce.meem.PhotoEditor.MultiTouchListener;
import com.hotsauce.meem.PhotoEditor.TextEditorDialogFragment;
import com.hotsauce.meem.R;
import com.hotsauce.meem.db.MemeTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TemplateEditorActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private ArrayList<Rect> rectangles;
    private Uri imageUri;
    private RelativeLayout imageLayout;
    private ImageView image;
    boolean isInTextBoxEditingMode = false;
    private ImageButton addTextButton;
    private TextView selectedTextView = null;
    private ArrayList<TextView> textBoxes;

    int REQ_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Strip title and notification bars
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.template_editor);

        Intent intent = getIntent();
        final Uri imageUri = Uri.parse(intent.getStringExtra("imageUri"));
        image = (ImageView)findViewById(R.id.image);
        image.setImageURI(imageUri);
        image.setOnTouchListener(this);

        imageLayout = (RelativeLayout)findViewById(R.id.imageLayout);
        addTextButton = (ImageButton)findViewById(R.id.addTextButton);

        // Set button listeners
        ImageButton closeButton = (ImageButton)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
        ImageButton saveButton = (ImageButton)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        ImageButton addTextButton = (ImageButton)findViewById(R.id.addTextButton);
        addTextButton.setOnClickListener(this);

        rectangles = new ArrayList<Rect>();
        textBoxes = new ArrayList<TextView>(); 
    }

    public TextView createTextBox(int x1, int x2, int y1, int y2) {
        TextView tv = new TextView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                Math.abs(x1-x2),
                Math.abs(y1-y2));

        // size textView to fit width and height
        tv.setAutoSizeTextTypeUniformWithConfiguration(1, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
        tv.setLayoutParams(lp);
        tv.setText("");

        tv.setBackgroundColor(Color.parseColor("#55FF0000"));
        MultiTouchListener multiTouchListener = new MultiTouchListener(null, image, true);

        imageLayout.addView(tv);

        final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams)tv.getLayoutParams();
        lpt.setMargins(x1, y1, 0, 0);
        tv.setOnTouchListener(multiTouchListener);
        return tv;
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
                Log .i("CreateTemplateActivityLog", "x:" + ev.getX());
                Log .i("CreateTemplateActivityLog", "y:" + ev.getY());
                break;
        }
        return true;
    }

    void setIsInTextBoxEditingMode(boolean newIsInTextBoxEditingMode) {
        if (newIsInTextBoxEditingMode) {
            addTextButton.setVisibility(4);
        } else {
            addTextButton.setVisibility(0);
        }
        isInTextBoxEditingMode = newIsInTextBoxEditingMode;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeButton:
                if (isInTextBoxEditingMode) {
                    // delete textbox
                } else {
                    onBackPressed();
                }
                break;
            case R.id.saveButton:
                if (isInTextBoxEditingMode) {
                    setIsInTextBoxEditingMode(false);
                } else {
                    for (TextView tv: textBoxes) {
                        rectangles.add(new Rect(
                                tv.getLeft(), tv.getRight(), tv.getTop(), tv.getBottom()
                        ));
                    }

                    Intent data = new Intent();
                    data.putParcelableArrayListExtra("rectangles", rectangles);
                    setResult(RESULT_OK,data);
                    finish();
                }
                break;
            case R.id.addTextButton:
                setIsInTextBoxEditingMode(true);
                selectedTextView = createTextBox(300, 800, 300, 600);
                textBoxes.add(selectedTextView);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // show save dialogue if user is in editing mode
        super.onBackPressed();
    }
}
