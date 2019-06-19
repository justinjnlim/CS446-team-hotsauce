package com.hotsauce.meem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class GalleryViewHolder extends RecyclerView.ViewHolder{

    public ImageView image;

    public GalleryViewHolder(View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.GalleryCell);
    }
}
