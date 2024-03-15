package com.relish.app.support;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.exifinterface.media.ExifInterface;


import com.relish.app.Utils.ImageUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Constants {
    public static String KEY_OPEN_FROM = "openFrom";
    public static final int PIX_CATEGORY_SIZE = 38;
    public static final String PIX_ICON_FILE_NAME = "small_";
    public static final String PIX_MASK_FILE_NAME = "effect_";
    public static String VALUE_OPEN_FROM_ART = "openFromArt";
    public static String VALUE_OPEN_FROM_DRIP = "openFromDrip";
    public static String VALUE_OPEN_FROM_MOTION = "openFromMotion";
    public static String VALUE_OPEN_FROM_NEON = "openFromNeon";
    public static String VALUE_OPEN_FROM_WING = "openFromWing";
    public static String VALUE_OPEN_FROM_BACKGROUND_REMOVER = "openFromBackgroundRemover";

    public Constants(Context context) {
    }

    public static String convertMediaUriToPath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT < 24) {
            return getPath(context, uri);
        }
        return getFilePathForN(uri, context);
    }

    private static String getFilePathForN(Uri uri, Context context) {
        Exception e;
        InputStream inputStream;
        FileOutputStream outputStream;
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex("_display_name");
        int sizeIndex = returnCursor.getColumnIndex("_size");
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        Long.toString(returnCursor.getLong(sizeIndex));
        File file = new File(context.getFilesDir(), name);
        try {
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                outputStream = new FileOutputStream(file);
            } catch (Exception e2) {
                e = e2;
                Log.e("Exception", e.getMessage());
                return file.getPath();
            }
            try {
                byte[] buffers = new byte[Math.min(inputStream.available(), 1048576)];
                while (true) {
                    try {
                        int read = inputStream.read(buffers);
                        if (read != -1) {
                            try {
                                outputStream.write(buffers, 0, read);
                                returnUri = returnUri;
                                nameIndex = nameIndex;
                            } catch (Exception e3) {
                                e = e3;
                                Log.e("Exception", e.getMessage());
                                return file.getPath();
                            }
                        } else {
                            try {
                                break;
                            } catch (Exception e4) {
                                e = e4;
                                Log.e("Exception", e.getMessage());
                                return file.getPath();
                            }
                        }
                    } catch (Exception e5) {
                        e = e5;
                        Log.e("Exception", e.getMessage());
                        return file.getPath();
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Size ");
                try {
                    sb.append(file.length());
                    Log.e("File Size", sb.toString());
                    inputStream.close();
                    outputStream.close();
                    Log.e("File Path", "Path " + file.getPath());
                    Log.e("File Size", "Size " + file.length());
                } catch (Exception e6) {
                    e = e6;
                }
            } catch (Exception e7) {
                e = e7;
                Log.e("Exception", e.getMessage());
                return file.getPath();
            }
        } catch (Exception e8) {
            e = e8;
            Log.e("Exception", e.getMessage());
            return file.getPath();
        }
        return file.getPath();
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri, float f, float f2) throws IOException {
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = openFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            if (f <= f2) {
                f = f2;
            }
            int i = (int) f;
            options2.inSampleSize = ImageUtils.getClosestResampleSize(options.outWidth, options.outHeight, i);
            Bitmap decodeFileDescriptor = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options2);
            Matrix matrix = new Matrix();
            if (decodeFileDescriptor.getWidth() > i || decodeFileDescriptor.getHeight() > i) {
                BitmapFactory.Options resampling = ImageUtils.getResampling(decodeFileDescriptor.getWidth(), decodeFileDescriptor.getHeight(), i);
                matrix.postScale(((float) resampling.outWidth) / ((float) decodeFileDescriptor.getWidth()), ((float) resampling.outHeight) / ((float) decodeFileDescriptor.getHeight()));
            }
            openFileDescriptor.close();
            if (convertMediaUriToPath(context, uri) != null) {
                return modifyOrientation(decodeFileDescriptor, convertMediaUriToPath(context, uri));
            }
            return decodeFileDescriptor;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String str) throws IOException {
        int attributeInt = new ExifInterface(str).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        if (attributeInt == 2) {
            return flip(bitmap, true, false);
        }
        if (attributeInt == 3) {
            return rotate(bitmap, 180.0f);
        }
        if (attributeInt == 4) {
            return flip(bitmap, false, true);
        }
        if (attributeInt == 6) {
            return rotate(bitmap, 90.0f);
        }
        if (attributeInt != 8) {
            return bitmap;
        }
        return rotate(bitmap, 270.0f);
    }

    public static Bitmap flip(Bitmap bitmap, boolean z, boolean z2) {
        float f;
        Matrix matrix = new Matrix();
        float f2 = z ? -1.0f : 1.0f;
        if (!z2) {
            f = 1.0f;
        } else {
            f = -1.0f;
        }
        matrix.preScale(f2, f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static String getPath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public static Bitmap rotate(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }
}
