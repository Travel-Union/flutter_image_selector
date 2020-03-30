package com.travelunion.image_selector.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.travelunion.image_selector.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>{

    private ArrayList<GalleryImage> images;
    private Context context;
    private ImageClickListener listenToClick;

    public ImageAdapter(ArrayList<GalleryImage> images, Context context, ImageClickListener listen) {
        this.images = images;
        this.context = context;
        this.listenToClick = listen;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.image_thumbnail_holder, parent, false);
        return new ImageHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, final int position) {
        final GalleryImage image = images.get(position);

        Glide.with(context)
                .load(image.getData())
                .apply(new RequestOptions().centerCrop())
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenToClick.onPicClicked(holder, position, images);

                if(holder.selector == null) return;

                if(holder.selector.getAlpha() > 0.0f) {
                    holder.selector.setAlpha(0.0f);
                } else {
                    holder.selector.setAlpha(0.5f);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public class ImageHolder extends RecyclerView.ViewHolder{
        ImageView image;
        CardView card;
        View selector;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.thumbnail_image);
            card = itemView.findViewById(R.id.thumbnail_card);
            selector = itemView.findViewById(R.id.thumbnail_selector);
        }
    }

}
