package com.thirstyfish.downloadjavaapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    ArrayList<ImageModel> objectList;
    // Layout Inflater
    LayoutInflater mLayoutInflater;

    int loc;


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, ArrayList<ImageModel> object, int loc) {
        this.context = context;
        this.objectList = object;
        this.loc = loc;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return objectList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item_only_image, container, false);
        // referencing the image view from the item.xml file
        Button btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);


        String imageUrl = objectList.get(position).getImgUrl();
        Glide.with(context)
                .load(imageUrl)
                .into(imageView);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                Bundle args = new Bundle();
                intent.putExtra("imageUrl",imageUrl);
                context.startActivity(intent);
            }
        });
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
