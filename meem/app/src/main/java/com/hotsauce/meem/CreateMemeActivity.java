package com.hotsauce.meem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
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
import com.hotsauce.meem.db.MemeTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateMemeActivity extends AppCompatActivity implements
        View.OnTouchListener,
        View.OnClickListener {

    private RelativeLayout imageLayout;
    private RelativeLayout mainLayout;
    private ImageView image;
    private MemeViewModel memeViewModel;
    private List<Rect> rectangles;
    private List<TextView> textboxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Strip title and notification bars
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.meme_editor);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        MemeTemplate chosenTemplate = (MemeTemplate) bundle.getSerializable("template");

        Uri imageUri = Uri.fromFile(new File(chosenTemplate.getFilepath()));
        image = (ImageView)findViewById(R.id.image);
        image.setImageURI(imageUri);
        image.setOnTouchListener(this);

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        imageLayout = (RelativeLayout)findViewById(R.id.imageLayout);

        // Set button listeners
        ImageButton closeButton = (ImageButton)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(this);
        ImageButton saveButton = (ImageButton)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        textboxes = new ArrayList<TextView>();
        rectangles = chosenTemplate.getRectangles();
        createTextBoxesFromRectangles(rectangles);
        imageLayout.invalidate();

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
                int i = 0;
                for (Rect rect: rectangles) {
                    final int j = i;
                    final int x1 = rect.left;
                    final int x2 = rect.right;
                    final int y1 = rect.top;
                    final int y2 = rect.bottom;
                    if (x1 <= ev.getX() && x2 >= ev.getX() && y1 <= ev.getY() && y2 >= ev.getY()) {
                        TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                            @Override
                            public void onDone(String inputText, int colorCode) {
                                editTextBox(textboxes.get(j), inputText, colorCode);
                            }
                        });
                        break;
                    }
                    i++;
                }

                break;
        }
        return true;
    }

    public void createTextBoxesFromRectangles(List<Rect> rectangles) {
        for (Rect rect: rectangles) {
            textboxes.add(createTextBox("", 0, rect.left, rect.right, rect.top, rect.bottom));
        }
    }

    public TextView createTextBox(String inputText, int colorCode, int x1, int x2, int y1, int y2) {
        TextView tv = new TextView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                Math.abs(x1-x2),
                Math.abs(y1-y2));

        // size textView to fit width and height
        tv.setAutoSizeTextTypeUniformWithConfiguration(1, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
        tv.setTextColor(colorCode);
        tv.setLayoutParams(lp);
        tv.setText(inputText);

        // add TextView border
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.RED);
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Style.STROKE);
        tv.setBackground(sd);

        imageLayout.addView(tv);

        final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams)tv.getLayoutParams();
        lpt.setMargins(x1, y1, 0, 0);

        return tv;
    }

    public void editTextBox(TextView tv, String inputText, int colorCode) {
        tv.setTextColor(colorCode);
        tv.setText(inputText);
    }

    public void saveMeme(Bitmap bitmap){
        final String memeId = Long.toString(System.currentTimeMillis());
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + memeId + ".png");

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            memeViewModel.insertMeme(new Meme(memeId));
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
                // clear textview borders before saving meme
                for (TextView tv: textboxes) {
                    tv.setBackground(null);
                }

                //Bitmap bitmap = loadBitmapFromView(imageLayout, imageLayout.getWidth(), imageLayout.getHeight());
                imageLayout.setDrawingCacheEnabled(true);
                imageLayout.buildDrawingCache(true);
                //Bitmap bitmap = Bitmap.createBitmap(imageLayout.getDrawingCache());
                Bitmap bitmap = loadBitmapFromView(imageLayout, image.getWidth(), image.getHeight());
                saveMeme(bitmap);

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
