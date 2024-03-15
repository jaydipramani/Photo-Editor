package com.relish.app.Activity;

import static android.view.WindowManager.LayoutParams.ANIMATION_CHANGED;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;

import com.relish.app.Adapter.SmallImageAdapter;
import com.relish.app.R;
import com.relish.app.Utils.SaveFileUtils;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.picker.PermissionsUtils;
import com.relish.app.polish.PolishPickerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SpliterActivity extends PolishBaseActivity {
    ConstraintLayout constraintSplit, constraintImage, constraintLayout;
    int row = 3, column = 3, dialogColumn, dialogRow;
    ImageView image, saveImage, backButton, layoutImage;
    SmallImageAdapter smallImageAdapter;
    boolean showingBorder = true;
    ArrayList<Bitmap> smallImage;
    RelativeLayout relativeLayoutLoading;
    Bitmap mainBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_spliter);
        image = findViewById(R.id.image);
        image.setAlpha(0);

        constraintSplit = findViewById(R.id.constraint_split);
        constraintImage = findViewById(R.id.constraint_image);
        constraintLayout = findViewById(R.id.constraint_layout);
        layoutImage = findViewById(R.id.layout);
        relativeLayoutLoading = (RelativeLayout) findViewById(R.id.relative_layout_loading);
        backButton = findViewById(R.id.image_view_exit);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        saveImage = findViewById(R.id.text_view_save);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionsUtils.checkWriteStoragePermission(SpliterActivity.this)) {
                    showLoading(true);
                    for (int i = 0; i < smallImage.size(); i++) {
                        new SpliterActivity.SaveEditingBitmap(smallImage.get(i)).execute(new Void[0]);
                    }
                }
                showLoading(false);
                Toast.makeText(SpliterActivity.this, "Images Downloaded!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        constraintSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSplit();
            }
        });
        constraintImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardSplitter(true).start(SpliterActivity.this);
                finish();
            }
        });
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showingBorder) {
                    layoutImage.setImageResource(R.drawable.ic_hide_layout);
                    smallImageAdapter.SetBoarder(false);
                    showingBorder = false;
                } else {
                    layoutImage.setImageResource(R.drawable.ic_show);
                    smallImageAdapter.SetBoarder(true);
                    showingBorder = true;
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            new SpliterActivity.loadBitmapUri().execute(bundle.getString(PolishPickerView.KEY_SELECTED_PHOTOS));
        }
    }


    @Override
    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            showLoading(true);
            for (int i = 0; i < smallImage.size(); i++) {
                new SpliterActivity.SaveEditingBitmap(smallImage.get(i)).execute(new Void[0]);
            }
            showLoading(false);
            Toast.makeText(this, "Images Downloaded!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void showLoading(boolean z) {
        if (z) {
            getWindow().setFlags(ANIMATION_CHANGED, ANIMATION_CHANGED);
            this.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(ANIMATION_CHANGED);
        this.relativeLayoutLoading.setVisibility(View.GONE);
    }

    public class loadBitmapUri extends AsyncTask<String, Bitmap, Bitmap> {
        loadBitmapUri() {
        }

        public void onPreExecute() {
            SpliterActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(String... string) {
            try {
                Uri fromFile = Uri.fromFile(new File(string[0]));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(SpliterActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(SpliterActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (bitmap1 != bitmap) {
                    bitmap.recycle();
                }
                return bitmap1;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            mainBitmap = bitmap;
            splitImage(bitmap);
            showLoading(false);
        }
    }

    public class SaveEditingBitmap extends AsyncTask<Void, String, String> {
        Bitmap bitmap;

        SaveEditingBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void onPreExecute() {

        }

        public String doInBackground(Void... voids) {
            try {
                SpliterActivity spliterActivity = SpliterActivity.this;
                String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                return SaveFileUtils.saveBitmapFileEditor(spliterActivity, bitmap, format).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            if (string == null) {
                Toast.makeText(getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void splitImage(Bitmap bitmap) {

        int rows, cols;
        int smallimage_Height, smallimage_Width;
        smallImage = new ArrayList<>();
//        BitmapDrawable mydrawable = (BitmapDrawable) image.getDrawable();
//        Bitmap bitmap = mydrawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        rows = row;
        cols = column;
        smallimage_Height = bitmap.getHeight() / rows;
        smallimage_Width = bitmap.getWidth() / cols;


        int yCo = 0;


        for (int x = 0; x < rows; x++) {
            int xCo = 0;
            for (int y = 0; y < cols; y++) {
                try {
                    Bitmap bmp = Bitmap.createBitmap(scaledBitmap, xCo, yCo, smallimage_Width, smallimage_Height);
                    String filename = "bitmap" + x + "" + y + ".png";
                    FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    stream.close();
//                    bmp.recycle();
                    try {
                        FileInputStream is = this.openFileInput(filename);
                        bmp = BitmapFactory.decodeStream(is);
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    smallImage.add(bmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                xCo += smallimage_Width;
            }
            yCo += smallimage_Height;
        }
        GridView image_grid = (GridView) findViewById(R.id.gridView);
        smallImageAdapter = new SmallImageAdapter(this, smallImage, showingBorder);
        image_grid.setAdapter(smallImageAdapter);

        image_grid.setNumColumns((int) Math.sqrt(smallImage.size()));
        image_grid.setColumnWidth(smallImage.get(0).getWidth());

    }

    public void showDialogSplit() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_split_number, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();

        dialogColumn = column;
        dialogRow = row;
        TextView textColumnNumber, textRowNumber;
        ImageView plushColumn, plushRow, minusColumn, minusRow;
        textColumnNumber = customLayout.findViewById(R.id.textColumnNumber);
        textRowNumber = customLayout.findViewById(R.id.textRowNumber);
        textColumnNumber.setText(String.valueOf(dialogColumn));
        textRowNumber.setText(String.valueOf(dialogRow));
        plushColumn = customLayout.findViewById(R.id.plushColumn);
        minusColumn = customLayout.findViewById(R.id.minusColumn);
        plushRow = customLayout.findViewById(R.id.plushRow);
        minusRow = customLayout.findViewById(R.id.minusRow);
        plushColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogColumn != 5) {
                    dialogColumn++;
                    textColumnNumber.setText(String.valueOf(dialogColumn));
                }
            }
        });
        minusColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogColumn != 1) {
                    dialogColumn--;
                    textColumnNumber.setText(String.valueOf(dialogColumn));
                }
            }
        });
        plushRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogRow != 5) {
                    dialogRow++;
                    textRowNumber.setText(String.valueOf(dialogRow));
                }
            }
        });
        minusRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogRow != 1) {
                    dialogRow--;
                    textRowNumber.setText(String.valueOf(dialogRow));
                }
            }
        });


        TextView btnOk = customLayout.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                column = dialogColumn;
                row = dialogRow;
                splitImage(mainBitmap);
                dialog.dismiss();

            }
        });


        dialog.show();
    }
}
