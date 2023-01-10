package com.thirstyfish.downloadjavaapp;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.core.content.FileProvider;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {

    public static final String DOCUMENTS_DIR = "documents";
    public static final String TAG = "FileUtils";

    @SuppressLint("Range")
    public static File getFileFromUri(final Context context, final Uri uri) throws Exception {

        String path = null;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) { // TODO: 2015. 11. 17. KITKAT


                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];


                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes

                } else if (isDownloadsDocument(uri)) { // DownloadsProvider

                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(
//                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//                    path = getDataColumn(context, contentUri, null, null);

//                    final String[] split = id.split(":");
//                    final String type = split[0];
//                    if ("raw".equalsIgnoreCase(type)) {
//                        path = split[1];
//                    }
//                    else if ("msf".equalsIgnoreCase(type)) { //like msf:7755
//                        String strRealId = split[1];
//                        final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
//                                Long.valueOf(strRealId));
//                        //Uri myUri = ContentResolver
//                        path = getDataColumn(context, contentUri, null, null);
//                    }

                        path= Environment.getExternalStorageDirectory() + "/Download/" +  getFileName(context,uri);

                } else if (isMediaDocument(uri)) { // MediaProvider


                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    path = getDataColumn(context, contentUri, selection, selectionArgs);

                } else if (isGoogleDrive(uri)) { // Google Drive
                    String TAG = "isGoogleDrive";
                    path = TAG;
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(";");
                    final String acc = split[0];
                    final String doc = split[1];

                    /*
                     * @details google drive document data. - acc , docId.
                     * */

                    return saveFileIntoExternalStorageByUri(context, uri);


                } // MediaStore (and general)
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                path = getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            }

            return new File(path);
        } else {

            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            return new File(cursor.getString(cursor.getColumnIndex("_data")));
        }

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is GoogleDrive.
     */

    public static boolean isGoogleDrive(Uri uri) {
        return uri.getAuthority().equalsIgnoreCase("com.google.android.apps.docs.storage");
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static File makeEmptyFileIntoExternalStorageWithTitle(String title) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        return new File(root, title);
    }


    @SuppressLint("Range")
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public static void saveBitmapFileIntoExternalStorageWithTitle(Bitmap bitmap, String title) throws Exception {

        FileOutputStream fileOutputStream = new FileOutputStream(makeEmptyFileIntoExternalStorageWithTitle(title + ".png"));
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();
    }


    public static File saveFileIntoExternalStorageByUri(Context context, Uri uri) throws Exception {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        int originalSize = inputStream.available();

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String fileName = getFileName(context, uri);
        File file = makeEmptyFileIntoExternalStorageWithTitle(fileName);
        bis = new BufferedInputStream(inputStream);
        bos = new BufferedOutputStream(new FileOutputStream(
                file, false));

        byte[] buf = new byte[originalSize];
        bis.read(buf);
        do {
            bos.write(buf);
        } while (bis.read(buf) != -1);

        bos.flush();
        bos.close();
        bis.close();

        return file;

    }

    public static int checkDownloadStatus(Model_FileDownloader model_file_download, Context context) {
        DownloadManager downloadManager;
        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        myDownloadQuery.setFilterById(model_file_download.getdRef());
        Cursor cursor = downloadManager.query(myDownloadQuery);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            return status;
        }


        return 0;
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return (file.exists() && file.getAbsoluteFile().exists());
//        return file.exists();
    }


    public static void downloadFileFromUrl(Context context, String url) {
        DownloadManager downloadManager;
        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();

        // Extract the file name and extension from the URL
        String fileName = "";
        String fileExt = "";
        try {
            URL urlObject = new URL(url);
            String[] parts = urlObject.getFile().split("/");
            fileName = parts[parts.length - 1];
            fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (MalformedURLException e) {
            // Handle invalid URL
        }

        String file = Environment.getExternalStorageDirectory() + "/Download/DownloadApp/" + fileName + "." + fileExt;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription("Downloading");
        request.setDestinationUri(Uri.fromFile(new File(file)));
        request.setAllowedOverMetered(true);

        long downloadId = downloadManager.enqueue(request);

        myDownloadQuery.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(myDownloadQuery);
        if (cursor.moveToFirst()) {
            checkStatus(cursor, context);
        }
    }

    public static void openFileFomPath(Context context, String url) throws IOException {
        // Create URI
        File f = new File(url);
        Log.e("TAG", "openFile: " + url);
        Uri uri = Uri.parse(f.getPath());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx") || url.toString().contains(".csv")) {
            // Excel file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/zip");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".webp") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "image/*");
//            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "video/*");
        } else {

            intent.setDataAndType(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                            FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f) : Uri.fromFile(f),
                    "*/*");
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void checkStatus(Cursor cursor, Context context) {
//        DownloadManager downloadManager;
//        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Log.e(TAG, "checkStatus: cursor"+cursor);
        //column for status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
        int reason = cursor.getInt(columnReason);
        //get the download filename
        int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
        String filename = cursor.getString(filenameIndex);

        String statusText = "";
        String reasonText = "";

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                statusText = "STATUS_FAILED";
                switch (reason) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonText = "ERROR_CANNOT_RESUME";
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonText = "ERROR_DEVICE_NOT_FOUND";
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonText = "ERROR_FILE_ALREADY_EXISTS";
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonText = "ERROR_FILE_ERROR";
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonText = "ERROR_HTTP_DATA_ERROR";
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonText = "ERROR_INSUFFICIENT_SPACE";
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonText = "ERROR_TOO_MANY_REDIRECTS";
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        reasonText = "ERROR_UNKNOWN";
                        break;
                }
                break;
            case DownloadManager.STATUS_PAUSED:
                statusText = "STATUS_PAUSED";
                switch (reason) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        reasonText = "PAUSED_QUEUED_FOR_WIFI";
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        reasonText = "PAUSED_UNKNOWN";
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        reasonText = "PAUSED_WAITING_FOR_NETWORK";
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        reasonText = "PAUSED_WAITING_TO_RETRY";
                        break;
                }
                break;
            case DownloadManager.STATUS_PENDING:
                statusText = "Download_started";
                break;
            case DownloadManager.STATUS_RUNNING:
                statusText = "Download_Running";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusText = "Successful download";
                reasonText = "Filename:" + filename;
                break;
        }

        Log.e(TAG, "checkStatus: statusText"+statusText+" reasonText "+reasonText );
        Toast toast = Toast.makeText(context,
                statusText + "\n" +
                        reasonText,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();

    }



}