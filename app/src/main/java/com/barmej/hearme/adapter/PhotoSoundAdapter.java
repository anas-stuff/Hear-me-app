package com.barmej.hearme.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.hearme.R;
import com.barmej.hearme.data.PhotoSound;
import com.barmej.hearme.listener.ItemClickListener;
import com.barmej.hearme.listener.ItemLongClickListener;

import java.util.ArrayList;

public class PhotoSoundAdapter extends RecyclerView.Adapter<PhotoSoundAdapter.PhotoSoundViewHolder> {
    private ArrayList<PhotoSound> mItems;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;

    public PhotoSoundAdapter(ArrayList<PhotoSound> items, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener){
        this.mItems = items;
        this.mItemClickListener = itemClickListener;
        this.mItemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public PhotoSoundAdapter.PhotoSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_sound, parent, false);
        return new PhotoSoundViewHolder(view, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoSoundAdapter.PhotoSoundViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PhotoSound photoSound = mItems.get(position);
        holder.photoImageView.setImageURI(photoSound.getImage());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // View holder class
    static class PhotoSoundViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private int position;

        public PhotoSoundViewHolder(@NonNull View itemView, final ItemClickListener itemClickListener, final ItemLongClickListener itemLongClickListener) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.image_view_item_photo);

            // Set listeners
            itemView.setOnClickListener(listener -> itemClickListener.onItemClickItem(position));
            itemView.setOnLongClickListener(listener -> {
                itemLongClickListener.onLongClickItem(position);
                return true;
            });

        }
    }
}
