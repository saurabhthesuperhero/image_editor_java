package com.thirstyfish.downloadjavaapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.thirstyfish.downloadjavaapp.stickerview.StickerTextView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutionException

object ImageUtils {
    var canvasColor = Color.WHITE
    var backBitmap: Bitmap? = null

    //COLOR, IMAGE
    var mode = "COLOR"
    fun resizeImage(
        imageUrl: String,
        imageBackUrl: String,
        imageView: ImageView,
        context: Context,
        targetAspectRatio: Float
    ) {
        class ResizeImageTask : AsyncTask<String, Void, Bitmap>() {
            override fun doInBackground(vararg imageUrls: String): Bitmap? {
                val imageUrl = imageUrls[0]

                try {
                    val originalBitmap = Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()
                    backBitmap = if (imageBackUrl != "" && mode.equals("IMAGE")) {
                        Glide.with(context)
                            .asBitmap()
                            .load(imageBackUrl)
                            .submit()
                            .get()
                    } else {
                        null
                    }
                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height

                    val originalAspectRatio = originalWidth.toFloat() / originalHeight

                    val canvasWidth: Int
                    val canvasHeight: Int

                    if (originalAspectRatio > targetAspectRatio) {
                        // The width will change, calculate the new width and height
                        canvasWidth = originalWidth
                        canvasHeight = (canvasWidth / targetAspectRatio).toInt()
                    } else {
                        // The height will change, calculate the new width and height
                        canvasHeight = originalHeight
                        canvasWidth = (canvasHeight * targetAspectRatio).toInt()
                    }

                    val resizedBitmap =
                        Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(resizedBitmap)
                    if (canvasColor.equals(Color.TRANSPARENT)) {
                        val paint = Paint()
                        paint.setAlpha(0) // Set the alpha value to 0 (fully transparent)
                        canvas.drawColor(
                            paint.color,
                            PorterDuff.Mode.CLEAR
                        ) // Draw the transparent background
                    } else {
                        canvas.drawColor(canvasColor)
                    }


                    val x: Float = ((canvasWidth - originalWidth) / 2).toFloat()
                    val y: Float = ((canvasHeight - originalHeight) / 2).toFloat()

                    if (backBitmap != null) {
//                        canvas.drawBitmap(backBitmap!!, 0F, 0F, null)
                        val src = Rect(0, 0, backBitmap!!.width, backBitmap!!.height)
                        val dst = Rect(0, 0, canvas.width, canvas.height)
                        canvas.drawBitmap(backBitmap!!, src, dst, null)
                    }

                    canvas.drawBitmap(originalBitmap, x, y, null)
                    return resizedBitmap
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(resizedBitmap: Bitmap?) {
                if (resizedBitmap != null) {
                    // Load the resized Bitmap into the ImageView using Glide
                    Glide.with(context)
                        .load(resizedBitmap)
                        .into(imageView)
                }
            }
        }

        val resizeTask = ResizeImageTask()
        resizeTask.execute(imageUrl)
    }


    fun resizeSquareImage(
        imageUrl: String,
        imageBackUrl: String,
        imageView: ImageView,
        context: Context,
        targetAspectRatio: Float
    ) {
        class ResizeImageTask : AsyncTask<String, Void, Bitmap>() {
            override fun doInBackground(vararg imageUrls: String): Bitmap? {
                val imageUrl = imageUrls[0]

                try {
                    val originalBitmap = Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()

                    backBitmap = if (imageBackUrl != "" && mode == "IMAGE") {
                        Glide.with(context)
                            .asBitmap()
                            .load(imageBackUrl)
                            .submit()
                            .get()
                    } else {
                        null
                    }
                    val originalWidth = originalBitmap.width
                    val originalHeight = originalBitmap.height

                    val originalAspectRatio = originalWidth.toFloat() / originalHeight

                    if (originalAspectRatio > targetAspectRatio) {
                        // The width will change, calculate the new width
                        val newWidth = (originalHeight * targetAspectRatio).toInt()
                        val changeInPixels = newWidth - originalWidth
                    } else {
                        // The height will change, calculate the new height
                        val newHeight = (originalWidth / targetAspectRatio).toInt()
                        val changeInPixels = newHeight - originalHeight
                    }

                    val canvasSize = Math.max(originalWidth, originalHeight)
                    val resizedBitmap =
                        Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(resizedBitmap)
                    if (canvasColor.equals(Color.TRANSPARENT)) {
                        val paint = Paint()
                        paint.setAlpha(0) // Set the alpha value to 0 (fully transparent)
                        canvas.drawColor(
                            paint.color,
                            PorterDuff.Mode.CLEAR
                        ) // Draw the transparent background
                    } else {
                        canvas.drawColor(canvasColor)
                    }

                    val x = (canvasSize - originalWidth) / 2
                    val y = (canvasSize - originalHeight) / 2
                    if (backBitmap != null) {
//                        canvas.drawBitmap(backBitmap!!, 0F, 0F, null)
                        val src = Rect(0, 0, backBitmap!!.width, backBitmap!!.height)
                        val dst = Rect(0, 0, canvas.width, canvas.height)
                        canvas.drawBitmap(backBitmap!!, src, dst, null)
                    }
                    canvas.drawBitmap(originalBitmap, x.toFloat(), y.toFloat(), null)
                    return resizedBitmap
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(resizedBitmap: Bitmap?) {
                if (resizedBitmap != null) {
                    // Load the resized Bitmap into the ImageView using Glide
                    Glide.with(context)
                        .load(resizedBitmap)
                        .into(imageView)
                }
            }
        }

        val resizeTask = ResizeImageTask()
        resizeTask.execute(imageUrl)
    }

    fun downloadImageFromImageView(context: Context, imageView: ImageView) {
        // create a bitmap from the ImageView's drawable
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        // get the SD card's root directory
        val path_download =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = UUID.randomUUID().toString() + ".png"
        // create a directory for the app
        val appDir = File(path_download, "MyAImageApp")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        // create a file to save the image
        val file = File(appDir, fileName)

        try {
            // create a file output stream and save the image
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            Toast.makeText(context, "Download Successfull", Toast.LENGTH_SHORT).show();

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Unsuccessfull: " + e.localizedMessage, Toast.LENGTH_SHORT)
                .show();

        }
    }

    fun shareImage(context: Context, imageView: ImageView) {
        // create a bitmap from the ImageView's drawable
        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap

        // generate a random file name
        val fileName = UUID.randomUUID().toString() + ".png"

        // create a file to save the image
        val file = File(context.cacheDir, fileName)

        try {
            // create a file output stream and save the image
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // create the share intent and start the activity
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        val uri =
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(intent, "Share image"))
    }

    fun opendialogtext(activity: Activity, frameLayout: FrameLayout) {

        var dialog = Dialog(activity)
//        dialog.setContentView(R.layout.textdialog_layout)
        val inflater = LayoutInflater.from(activity)
        val subview = inflater.inflate(R.layout.textdialog_layout, null)

        val editText = subview.findViewById(R.id.dialogEditText) as EditText
        val btn_done = subview.findViewById(R.id.btn_done) as Button

        var alert: AlertDialog.Builder = AlertDialog.Builder(activity)
        alert.setView(subview)
        alert.setCancelable(true)


        btn_done.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var tv_sticker = StickerTextView(activity)

                tv_sticker.tv_main!!.setText(editText.text.toString())
                tv_sticker.tv_main!!.setTypeface(editText.typeface)
                tv_sticker.tv_main!!.setTextColor(editText.textColors)
                frameLayout.addView(tv_sticker)
                dialog.dismiss()
            }
        })

        dialog = alert.create()
        dialog.show()
    }




}



