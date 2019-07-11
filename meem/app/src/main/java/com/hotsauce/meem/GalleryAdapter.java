package com.hotsauce.meem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.content.Context;

import com.hotsauce.meem.db.Meme;

import java.io.File;
import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter {
    /*
    Bridge between our model and view.
     */

    private List<Meme> memes;
    private Context context;

    public GalleryAdapter(List<Meme> memes, Context context) {
        this.memes = memes;
        this.context = context;
    }

    public void setMemes(List<Meme> memes) {
        this.memes = memes;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cell, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GalleryViewHolder vH = (GalleryViewHolder) viewHolder;
        vH.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap memeBitmap = BitmapFactory.decodeFile(getFilepath(this.memes.get(position)));
        vH.image.setImageBitmap(memeBitmap);


        // create touch event
        final String current_image_path = getFilepath(this.memes.get(position));
        vH.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GalleryActionActivity.class);
                intent.putExtra("image_path", current_image_path);
                Log.d("memePath", current_image_path);
                context.startActivity(intent);
            }
        });
    }

    //
    @Override
    public int getItemCount() {
        return this.memes.size();
    }

    public static String getFilepath(Meme meme) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + meme.getId() + ".png";
    }
}
