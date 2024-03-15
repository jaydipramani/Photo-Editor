package com.relish.app.Utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.relish.app.R;
import com.relish.app.constants.Constants;
import com.relish.app.polish.PolishGridView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class SaveFileUtils {

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static File saveBitmapFileEditor(Context context, Bitmap bitmap, String str) throws IOException {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str);
            contentValues.put("mime_type", "image/png");
            contentValues.put("relative_path", "Pictures/Relish");
            OutputStream openOutputStream = contentResolver.openOutputStream(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, openOutputStream);
            openOutputStream.flush();
            openOutputStream.close();
            String str2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + context.getString(R.string.app_name);
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
            return new File(str2, str + ".png");
        }
        String str3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + context.getString(R.string.app_name);
        File file2 = new File(str3);
        if (!file2.exists()) {
            file2.mkdir();
        }
        File file3 = new File(str3, str + ".png");
        FileOutputStream fileOutputStream = new FileOutputStream(file3);
        MediaScannerConnection.scanFile(context, new String[]{file3.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {

            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String str, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file3;

//        boolean saved;
//        OutputStream fos;
//        String str = context.getString(R.string.app_file) + System.currentTimeMillis() + Constants.KEY_JPG;
//        String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + context.getString(R.string.app_folder);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//
//            ContentResolver resolver = context.getContentResolver();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, str);
//            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
//            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + context.getString(R.string.app_folder));
//            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//            fos = resolver.openOutputStream(imageUri);
//
//        } else {
//
//            File file = new File(imagesDir);
//
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            File image = new File(imagesDir, str);
//            fos = new FileOutputStream(image);
//
//        }
//
//        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        fos.flush();
//        fos.close();
//
//        String valueOf = imagesDir + File.separator + str;
//        return new File(valueOf);

    }

    public static File saveBitmapFileCollage(Context context, Bitmap bitmap, String str) throws IOException {
        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str);
            contentValues.put("mime_type", "image/png");
            contentValues.put("relative_path", "Pictures/Relish");
            OutputStream openOutputStream = contentResolver.openOutputStream(contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, openOutputStream);
            openOutputStream.flush();
            openOutputStream.close();
            String str2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + context.getString(R.string.app_name);
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdir();
            }
            return new File(str2, str + ".png");
        }
        String str3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + context.getString(R.string.app_name);
        File file2 = new File(str3);
        if (!file2.exists()) {
            file2.mkdir();
        }
        File file3 = new File(str3, str + ".png");
        FileOutputStream fileOutputStream = new FileOutputStream(file3);
        MediaScannerConnection.scanFile(context, new String[]{file3.getAbsolutePath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {

            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String str, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file3;
    }

    public static Bitmap createBitmap(PolishGridView quShotCollageView, int i) {
        quShotCollageView.clearHandling();
        quShotCollageView.invalidate();
        Bitmap createBitmap = Bitmap.createBitmap(i, (int) (((float) i) / (((float) quShotCollageView.getWidth()) / ((float) quShotCollageView.getHeight()))), Bitmap.Config.ARGB_8888);
        quShotCollageView.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap createBitmap(PolishGridView quShotCollageView) {
        quShotCollageView.clearHandling();
        quShotCollageView.invalidate();
        Bitmap createBitmap = Bitmap.createBitmap(quShotCollageView.getWidth(), quShotCollageView.getHeight(), Bitmap.Config.ARGB_8888);
        quShotCollageView.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}
