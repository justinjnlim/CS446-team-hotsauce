package com.hotsauce.meem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
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
import com.hotsauce.meem.PhotoEditor.MultiTouchListener;
import com.hotsauce.meem.PhotoEditor.TextEditorDialogFragment;
import com.hotsauce.meem.TemplateCreator.TemplateEditorActivity;
import com.hotsauce.meem.db.MemeTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTemplateActivity extends AppCompatActivity {

    int REQ_LOAD_IMG = 1;
    int REQ_TEMPLATE_EDITOR = 2;
    Uri imageUri;
    private MemeViewModel memeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        memeViewModel = ViewModelProviders.of(this).get(MemeViewModel.class);

        // Calls image selector
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQ_LOAD_IMG);
    }

    @Override
    public void onBackPressed() {
        // show save dialogue if user is in editing mode
        super.onBackPressed();
    }

    // save a copy of the template image file
    void saveTemplateImageFile(String memeTemplateId, Uri imageUri)
    {
        String sourceFilename = imageUri.getPath();
        String destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + memeTemplateId + ".png";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveTemplate(List<Rect> rectangles, Uri imageUri) {
        String memeTemplateId = Long.toString(System.currentTimeMillis());
        saveTemplateImageFile(memeTemplateId, imageUri);
        memeViewModel.insertTemplate(new MemeTemplate(memeTemplateId, rectangles));
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == REQ_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();

                // call template editor activity
                Intent intent = new Intent(getApplicationContext(), TemplateEditorActivity.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivityForResult(intent, REQ_TEMPLATE_EDITOR);
            } else {
                onBackPressed();
            }
        } else if (reqCode == REQ_TEMPLATE_EDITOR) {
            if (resultCode == RESULT_OK) {
                List<Rect> rectangles = data.getParcelableArrayListExtra("rectangles");
                saveTemplate(rectangles, imageUri);
            }
            onBackPressed();
        }
    }
}
