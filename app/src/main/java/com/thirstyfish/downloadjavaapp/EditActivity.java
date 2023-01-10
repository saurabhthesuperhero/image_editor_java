package com.thirstyfish.downloadjavaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.jetbrains.annotations.NotNull;

public class EditActivity extends AppCompatActivity {
    String imageUrl = "";
    String imageBackUrl = "";
    ImageView imageViewMain;
    LinearLayout bt_1_1;
    LinearLayout bt_16_9;
    LinearLayout bt_3_4;
    LinearLayout bt_3_2;
    LinearLayout ll_color_picker;
    LinearLayout ll_menu_background;
    LinearLayout ll_menu_aspect_ratio;
    LinearLayout ll_ratio;
    LinearLayout ll_background;
    LinearLayout ll_share;
    LinearLayout ll_download;
    LinearLayout bt_transparent_back;
    LinearLayout bt_select_image;
    LinearLayout ll_text;
    FrameLayout frame_layout;
    Button bt_original;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("imageUrl");
        imageViewMain = (ImageView) findViewById(R.id.imageViewMain);
        bt_1_1 = (LinearLayout) findViewById(R.id.bt_1_1);
        bt_16_9 = (LinearLayout) findViewById(R.id.bt_16_9);
        bt_3_4 = (LinearLayout) findViewById(R.id.bt_3_4);
        bt_3_2 = (LinearLayout) findViewById(R.id.bt_3_2);
        ll_color_picker = (LinearLayout) findViewById(R.id.ll_color_picker);
        bt_select_image = (LinearLayout) findViewById(R.id.bt_select_image);
        ll_menu_background = (LinearLayout) findViewById(R.id.ll_menu_background);
        ll_menu_aspect_ratio = (LinearLayout) findViewById(R.id.ll_menu_aspect_ratio);
        ll_ratio = (LinearLayout) findViewById(R.id.ll_ratio);
        ll_text = (LinearLayout) findViewById(R.id.ll_text);
        ll_download = (LinearLayout) findViewById(R.id.ll_download);
        ll_share = (LinearLayout) findViewById(R.id.ll_share);
        ll_background = (LinearLayout) findViewById(R.id.ll_background);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
        bt_transparent_back = (LinearLayout) findViewById(R.id.bt_transparent_back);
        bt_original = (Button) findViewById(R.id.bt_original);

        ll_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_menu_aspect_ratio.setVisibility(View.GONE);
                ll_menu_background.setVisibility(View.VISIBLE);
            }
        });
        ll_ratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_menu_aspect_ratio.setVisibility(View.VISIBLE);
                ll_menu_background.setVisibility(View.GONE);
            }
        });

        Glide.with(EditActivity.this)
                .load(imageUrl)
                .into(imageViewMain);


        bt_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(EditActivity.this)
                        .load(imageUrl)
                        .into(imageViewMain);
            }
        });


        bt_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageUtils.INSTANCE.resizeSquareImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 1F);


            }
        });


        bt_16_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.INSTANCE.resizeImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 16.0F / 9.0F);
            }
        });

        bt_3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.INSTANCE.resizeImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 3.0F / 2.0F);

            }
        });

        bt_3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.INSTANCE.resizeImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 3.0F / 4.0F);

            }
        });
        final int[] DefaultColor = {Color.WHITE};
        ll_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog
                        .Builder(EditActivity.this)
                        .setTitle("Pick Color")
                        .setColorShape(ColorShape.SQAURE)
                        .setDefaultColor(DefaultColor[0])
                        .setColorListener(new ColorListener() {
                            @Override
                            public void onColorSelected(int color, @NotNull String colorHex) {
                                // Handle Color Selection
                                ImageUtils.INSTANCE.setCanvasColor(color);
                                ImageUtils.INSTANCE.setBackBitmap(null);
                                ImageUtils.INSTANCE.setMode("COLOR");
                                ImageUtils.INSTANCE.resizeSquareImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 1F);

                            }
                        })
                        .show();
            }
        });

        bt_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(EditActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        bt_transparent_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.INSTANCE.setCanvasColor(Color.TRANSPARENT);
                ImageUtils.INSTANCE.setBackBitmap(null);
                ImageUtils.INSTANCE.setMode("COLOR");
                ImageUtils.INSTANCE.resizeSquareImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 1F);
            }
        });
        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageUtils.INSTANCE.downloadImageFromImageView(EditActivity.this,imageViewMain);
                ImageNonStaticUtils imageNonStaticUtils= new ImageNonStaticUtils();
                imageNonStaticUtils.saveImage(EditActivity.this,imageViewMain,frame_layout);
            }
        });

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.INSTANCE.shareImage(EditActivity.this,imageViewMain);
            }
        });


        ll_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.INSTANCE.opendialogtext(EditActivity.this,frame_layout);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            // Use Uri object instead of File to avoid storage permissions
            imageBackUrl = uri.toString();
            ImageUtils.INSTANCE.setMode("IMAGE");
            ImageUtils.INSTANCE.resizeSquareImage(imageUrl, imageBackUrl, imageViewMain, EditActivity.this, 1F);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}