package com.thirstyfish.downloadjavaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> mImageFilePaths;
    private final OnClickListener onClickListener;

    public ImageAdapter(List<String> imageFilePaths,OnClickListener onClickListener) {
        mImageFilePaths = imageFilePaths;
        this.onClickListener = onClickListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view,onClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String filePath = mImageFilePaths.get(position);
        Glide.with(holder.itemView.getContext())
                .load(filePath)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageFilePaths.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onRowClick(getAdapterPosition());
                }
            });
        }

    }

    public interface OnClickListener {
        void onRowClick(int position);
    }
}
