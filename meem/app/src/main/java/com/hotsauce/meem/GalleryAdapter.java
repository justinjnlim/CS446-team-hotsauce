package com.hotsauce.meem;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class GalleryAdapter extends RecyclerView.Adapter {

    private Integer[] data;

    public GalleryAdapter(Integer[] data) {
        this.data = data;
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
        vH.image.setImageResource(this.data[position]);
    }
//
    @Override
    public int getItemCount() {
        return this.data.length;
    }
}
