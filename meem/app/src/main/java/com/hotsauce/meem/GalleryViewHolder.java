package com.hotsauce.meem;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;


public class GalleryViewHolder extends RecyclerView.ViewHolder{
    /*
    View for each cell in the Gallery
     */

    public ImageView image;

    public GalleryViewHolder(View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.GalleryCell);
    }
}
