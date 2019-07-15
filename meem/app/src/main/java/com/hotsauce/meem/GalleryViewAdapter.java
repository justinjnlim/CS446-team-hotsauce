package com.hotsauce.meem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.hotsauce.meem.db.Meme;
import com.hotsauce.meem.db.MemeTemplate;

import java.util.Collections;
import java.util.List;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.MemeViewHolder> {

    class MemeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView memeItemView;

        private MemeViewHolder(View itemView) {
            super(itemView);
            memeItemView = itemView.findViewById(R.id.GalleryCell);
        }
    }

    private final LayoutInflater inflater;
    private List<Meme> memes = Collections.emptyList();
    private List<MemeTemplate> templates = Collections.emptyList();
    private Context context;

    GalleryViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallery_cell, parent, false);
        return new MemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemeViewHolder holder, int position) {

        if (memes.size() > 0) {
            final Meme meme = this.memes.get(position);
            holder.memeItemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap memeBitmap = BitmapFactory.decodeFile(meme.getFilepath());
            holder.memeItemView.setImageBitmap(memeBitmap);

            // create touch event
            final String meme_id = meme.getId();
            holder.memeItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GalleryActionActivity.class);
                    intent.putExtra("meme", meme);
                    Log.d("current_meme", meme_id);
                    context.startActivity(intent);
                }
            });
        } else if (templates.size() > 0) {
            final MemeTemplate template = this.templates.get(position);
            holder.memeItemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap memeBitmap = BitmapFactory.decodeFile(template.getFilepath());
            holder.memeItemView.setImageBitmap(memeBitmap);

            // create touch event
            holder.memeItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CreateMemeActivity.class);
                    intent.putExtra("template", template);
                    context.startActivity(intent);
                }
            });
        }
    }

    void setMemes(List<Meme> memes) {
        this.memes = memes;
        notifyDataSetChanged();
    }

    void setTemplates(List<MemeTemplate> templates) {
        this.templates = templates;
        notifyDataSetChanged();
    }
    //
    @Override
    public int getItemCount() {
        return Math.max(this.memes.size(), this.templates.size());
    }


}
