package com.hotsauce.meem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;
import android.content.Context;


public class GalleryAdapter extends RecyclerView.Adapter {
    /*
    Bridge between our model and view.
     */

    private String[] data;
    private Context context;

    public GalleryAdapter(String[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cell, parent,false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GalleryViewHolder vH = (GalleryViewHolder)viewHolder;
        vH.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap memeBitmap = BitmapFactory.decodeFile(this.data[position]);
        vH.image.setImageBitmap(memeBitmap);


        // create touch event
        final String current_image_path = this.data[position];
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
        return this.data.length;
    }
}
