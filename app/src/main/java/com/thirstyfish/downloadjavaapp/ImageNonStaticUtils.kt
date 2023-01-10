package com.thirstyfish.downloadjavaapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.thirstyfish.downloadjavaapp.stickerview.StickerTextView
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ImageNonStaticUtils {
//    fun saveImage(context: Context, imageView: ImageView, frameLayout: FrameLayout) {
//        // Create a bitmap with the same dimensions as the ImageView
////        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
//
//        val drawable = imageView.drawable as BitmapDrawable
//        val bitmap = drawable.bitmap
//        // Create a canvas using the bitmap
//        val canvas = Canvas(bitmap)
//        // Draw the ImageView onto the canvas
//        imageView.drawable.draw(canvas)
//        // Iterate through the children of the frameLayout and draw each one onto the canvas
////        for (i in 0 until frameLayout.childCount) {
////            val child = frameLayout.getChildAt(i)
////            if (child is StickerTextView) {
////                val x = child.x
////                val y = child.y
////                canvas.translate(x, y)
////                Log.e("TAG", "saveImage: x: "+child.x+" y:"+child.y )
////                child.draw(canvas)
////            }
////        }
//        val frameWidth = frameLayout.width
//        val frameHeight = frameLayout.height
//        val imageWidth = imageView.width
//        val imageHeight = imageView.height
//
//        for (i in 0 until frameLayout.childCount) {
//            val child = frameLayout.getChildAt(i)
//            if (child is StickerTextView) {
//                //Calculating the relative co-ordinates wrt to ImageView
//                val x = (child.x * imageWidth) / frameWidth
//                val y = (child.y * imageHeight) / frameHeight
//                canvas.translate(x, y)
//                Log.e("TAG", "saveImage: x: "+x+" y:"+y )
//                child.draw(canvas)
//            }
//        }
//
//
//        // Save the bitmap to a file
//        val path_download =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val fileName = UUID.randomUUID().toString() + ".png"
//        val appDir = File(path_download, "MyAImageApp")
//        if (!appDir.exists()) {
//            appDir.mkdir()
//        }
//
//        val file = File(appDir, fileName)
//        val stream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        stream.flush()
//        stream.close()
//        Toast.makeText(context, "Download Successfull", Toast.LENGTH_SHORT).show();
//
//    }

    fun saveImage(context: Context, imageView: ImageView, frameLayout: FrameLayout) {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // Draw the ImageView and its child views onto the canvas
        imageView.draw(canvas)
        frameLayout.draw(canvas)
        // Save the bitmap to a file
        val path_download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = UUID.randomUUID().toString() + ".png"
        val appDir = File(path_download, "MyAImageApp")
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
        Toast.makeText(context, "Download Successfull", Toast.LENGTH_SHORT).show();
    }

}