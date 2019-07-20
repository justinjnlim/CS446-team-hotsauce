package com.hotsauce.meem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;

public class CreateTemplateActivity extends AppCompatActivity {

    int REQ_LOAD_IMG = 1;
    int REQ_TEMPLATE_EDITOR = 2;
    int REQ_TAKE_IMG = 3;
    Uri imageUri;
    private MemeViewModel memeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memeViewModel = ViewModelProviders.of(this).get(MemeViewModel.class);

        launchImageSelector();
    }

    private void launchImageSelector() {
        final String photoTitle = "Take Photo";
        final String galleryTitle = "Choose from Gallery";
        final String cancelTitle = "Cancel";
        final CharSequence[] titles = { photoTitle, galleryTitle, cancelTitle};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(titles, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                CharSequence title = titles[item];
                if (title.equals(photoTitle)) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQ_TAKE_IMG);
                } else if (title.equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , REQ_LOAD_IMG);
                } else if (title.equals("Cancel")) {
                    dialog.dismiss();
                    onBackPressed();
                }
            }
        });

        builder.show();
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

        if (reqCode == REQ_LOAD_IMG || reqCode == REQ_TAKE_IMG) {
            if (resultCode == RESULT_OK) {

                if (reqCode == REQ_TAKE_IMG) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageUri = getImageUri(photo);
                    Log.d("myTag", "returned from photo" + imageUri.toString());
                } else {
                    imageUri = data.getData();
                }

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

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
