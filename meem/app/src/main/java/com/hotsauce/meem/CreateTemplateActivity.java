package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import com.hotsauce.meem.TemplateCreator.TemplateEditorActivity;
import com.hotsauce.meem.db.MemeTemplate;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import static com.hotsauce.meem.PhotoEditor.BaseActivity.READ_WRITE_STORAGE;

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

        if (!requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return;
        }
        if (!requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return;
        }

        String destinationFilename = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + memeTemplateId + ".png";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            FileInputStream fis = new FileInputStream(getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor());
            bis = new BufferedInputStream(fis);
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
}
