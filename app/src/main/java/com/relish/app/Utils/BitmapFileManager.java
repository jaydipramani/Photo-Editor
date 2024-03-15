package com.relish.app.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;


import com.relish.app.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BitmapFileManager {
    private static String BITMAP_CROPED_FILE_NAME = "temp_croped_bitmap.png";
    private static String BITMAP_CROPED_MASK_FILE_NAME = "temp_croped_mask_bitmap.png";
    private static String BITMAP_FILE_NAME = "temp_bitmap.png";
    private static String BITMAP_ORIGINAL_FILE_NAME = "temp_original_bitmap.png";
    public static int croppedLeft = 0;
    public static int croppedTop = 0;
    public static String file_path;
    public static Bitmap globalBitmap;
    public static boolean isNull = false;

    public static Bitmap getCurrentCroppedMaskBitmap(Activity activity) {
        if (isNull) {
            return null;
        }
        return getBitmapByFileName(activity, BITMAP_CROPED_MASK_FILE_NAME);
    }

    public static Bitmap getCurrentCropedBitmap(Activity activity) {
        if (isNull) {
            return null;
        }
        return getBitmapByFileName(activity, BITMAP_CROPED_FILE_NAME);
    }

    public static Bitmap getCurrentOriginalBitmap(Activity activity) {
        return getBitmapByFileName(activity, BITMAP_ORIGINAL_FILE_NAME);
    }

    private static Bitmap getBitmapByFileName(Activity r1, String r2) {
        System.out.println("BefeorrrergetBitmapByFileName");
        if (Build.VERSION.SDK_INT >= 26) {
            System.out.println("ineeerererrrerrrrr");
            return globalBitmap;
        }
        System.out.println("afterrrrrgetBitmapByFileName");
        String file_path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + r1.getResources().getString(R.string.directory) + "/" + r2;
        File dir = new File(file_path2);
        File image = null;
        File file = new File(dir.getAbsolutePath());
        boolean success = false;
        if (!file.exists()) {
            success = file.mkdirs();
        }
        if (success) {
            try {
                System.out.println("SuccesscreatedgetBitmapByFileName");
                image = new File(dir, BITMAP_ORIGINAL_FILE_NAME + ".jpeg");
                OutputStream fos2 = new FileOutputStream(image);
                globalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
                fos2.flush();
                fos2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ffffffffff" + file_path2);
        return BitmapFactory.decodeFile(image.getAbsolutePath());
    }

    public static void saveFile(Context r2, Bitmap r3, String r4) {
        if (r3 != null) {
            try {
                globalBitmap = r3;
                if (Build.VERSION.SDK_INT >= 24) {
                    file_path = SaveFileUtils.saveBitmapFileEditor(r2, r3, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date())).getAbsolutePath();
                    FileOutputStream fOut = null;
                    try {
                        fOut = new FileOutputStream(new File(file_path));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    r3.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    try {
                        fOut.flush();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        fOut.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                } else {
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + r2.getResources().getString(R.string.directory));
                    File file = new File(dir.getAbsolutePath());
                    boolean success = false;
                    if (!file.exists()) {
                        success = file.mkdirs();
                    }
                    if (success) {
                        try {
                            System.out.println("Successcreated foldernowaveimage");
                            OutputStream fos2 = new FileOutputStream(new File(dir, BITMAP_ORIGINAL_FILE_NAME + ".jpeg"));
                            r3.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
                            fos2.flush();
                            fos2.close();
                        } catch (FileNotFoundException e4) {
                            e4.printStackTrace();
                        }
                    }
                }
            } catch (Exception e5) {
            }
        }
    }

    public static void deleteFile(Context context, String str) {
        File file = new File(context.getFilesDir().getAbsolutePath() + "/" + str);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void setCurrentCropedBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, BITMAP_CROPED_FILE_NAME);
            isNull = true;
        } else {
            isNull = false;
        }
        saveFile(activity, bitmap, BITMAP_CROPED_FILE_NAME);
    }

    public static void setCurrentCroppedMaskBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, BITMAP_CROPED_MASK_FILE_NAME);
        }
        saveFile(activity, bitmap, BITMAP_CROPED_MASK_FILE_NAME);
    }

    public static void setCurrentOriginalBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, BITMAP_ORIGINAL_FILE_NAME);
        }
        PrintStream printStream = System.out;
        printStream.println("NowwwwsetCurrentOriginalBitmap" + bitmap);
        saveFile(activity, bitmap, BITMAP_ORIGINAL_FILE_NAME);
    }
}
