package com.thirstyfish.downloadjavaapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.thirstyfish.downloadjavaapp.databinding.ActivityMainBinding;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static ActivityMainBinding binding;
    private ImageAdapter mAdapter;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        requestPermission();
        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = binding.etUrl.getText().toString();
                if (!url.equals("")) {
                    FileUtils.downloadFileFromUrl(MainActivity.this, url);
                }
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/qjgWmVS/pexels-tom-fisk-1519753.jpg");
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/LnHBjPG/pexels-lisa-fotios-1540258.jpg");
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/rbZ3FgH/pexels-dominika-mazur-14446666.jpg");
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/XjMRMD0/pexels-linda-gschwentner-10924810.jpg");
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/jvCMKT5/pexels-irina-varanovich-14686142.jpg");
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/PMXcwSt/pexels-yura-forrat-11987710.jpg");
//                FileUtils.downloadFileFromUrl(MainActivity.this, "https://i.ibb.co/M8JZMZb/pexels-bade-saba-13945391.jpg");
            }
        });

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the list of image file paths
        List<String> imageFilePaths = getImageFilePaths();
        List<ImageModel> imageModels = new ArrayList<>();

        for (String filePath : imageFilePaths) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImgUrl(filePath);
            imageModels.add(imageModel);
        }
        // Create the adapter
        mAdapter = new ImageAdapter(imageFilePaths, new ImageAdapter.OnClickListener() {
            @Override
            public void onRowClick(int position) {
                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)imageModels);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("imageLoc",position);
                startActivity(intent);
            }
        });

        // Set the adapter for the RecyclerView
        binding.rvList.setAdapter(mAdapter);
    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast toast = Toast.makeText(ctxt,
                    "Download Completed",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            onResume();

        }
    };

    public static void rvScrolltoPosition(int pos){
        binding.rvList.scrollToPosition(pos);
    }
    private List<String> getImageFilePaths() {
        File directory = new File(Environment.getExternalStorageDirectory(), "Download/DownloadApp");
        File[] files = directory.listFiles();
        List<String> imageFilePaths = new ArrayList<>();
        if (files==null) return imageFilePaths;
        for (File file : files) {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                imageFilePaths.add(file.getAbsolutePath());
            }
        }
        return imageFilePaths;
    }


    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work
                } else {
                    // permission denied, disable the functionality that depends on this permission
                }
                return;
            }
        }
    }
}