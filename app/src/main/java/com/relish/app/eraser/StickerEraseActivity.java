package com.relish.app.eraser;

import static android.view.WindowManager.LayoutParams.ANIMATION_CHANGED;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.relish.app.Activity.PhotoShareActivity;
import com.relish.app.Activity.PolishBaseActivity;
import com.relish.app.Activity.PolishEditorActivity;
import com.relish.app.R;
import com.relish.app.Utils.SaveFileUtils;
import com.relish.app.eraser.eraser.EraseView;
import com.relish.app.eraser.eraser.MultiTouchListener;
import com.relish.app.layout.ArtLayout;
import com.relish.app.layout.DripLayout;
import com.relish.app.layout.MotionLayout;
import com.relish.app.layout.NeonLayout;
import com.relish.app.layout.WingLayout;
import com.relish.app.picker.PermissionsUtils;
import com.relish.app.support.Constants;
import com.relish.app.support.MyExceptionHandlerPix;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class StickerEraseActivity extends PolishBaseActivity implements View.OnClickListener {
    public static Bitmap b = null;
    public static Bitmap bgCircleBit = null;
    public static Bitmap bitmap = null;
    public static int curBgType = 1;
    public static int orgBitHeight;
    public static int orgBitWidth;
    public static BitmapShader patternBMPshader;
    public Animation animSlideDown;
    public Animation animSlideUp;
    public ImageView back_btn;
    public EraseView dv;
    public ImageView dv1;
    public int height;
    public ImageView imageViewBackgroundCover;
    public RelativeLayout inside_cut_lay;
    public boolean isTutOpen = true;
    private LinearLayout lay_lasso_cut;
    private LinearLayout linearLayoutAuto;
    private LinearLayout linearLayoutEraser;
    public RelativeLayout main_rel;
    private String openFrom;
    public Bitmap orgBitmap;
    public RelativeLayout outside_cut_lay;
    private SeekBar radius_seekbar;
    ImageView redo_btn;
    public RelativeLayout relativeLayoutAuto;
    RelativeLayout relativeLayoutBackground;
    public RelativeLayout relativeLayoutEraser;
    public RelativeLayout relativeLayoutExtract;
    public RelativeLayout relativeLayoutRestore;
    public RelativeLayout relativeLayoutSeekBar;
    private RelativeLayout relativeLayoutLoading;
    public RelativeLayout relativeLayoutZoom;
    public ImageView save_btn;
    public Animation scale_anim;
    private SeekBar seekBarBrushOffset;
    private SeekBar seekBarExtractOffset;
    private SeekBar seekBarOffset;
    private SeekBar seekBarThreshold;
    public boolean showDialog = false;
    private TextView textViewAutoOffset;
    private TextView textViewBrushOffset;
    private TextView textViewBrushSize;
    private TextView textViewExtractOffset;
    ImageView undo_btn;
    public int width;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_polish_eraser);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(this));
        this.openFrom = getIntent().getStringExtra(Constants.KEY_OPEN_FROM);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_loading);
        this.relativeLayoutLoading = relativeLayout;
        this.animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
        this.animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down);
        this.scale_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_scale_anim);
        initUI();
        this.isTutOpen = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.height = i - ImageUtils.dpToPx((Context) this, 120.0f);
        curBgType = 1;
        this.main_rel.postDelayed(new Runnable() {
            /* class com.photoz.photoeditor.pro.eraser.StickerEraseActivity.AnonymousClass1 */

            public void run() {
                if (StickerEraseActivity.this.isTutOpen) {
                    ImageView imageView = StickerEraseActivity.this.imageViewBackgroundCover;
                    StickerEraseActivity stickerEraseActivity = StickerEraseActivity.this;
                    imageView.setImageBitmap(ImageUtils.getTiledBitmap(stickerEraseActivity, R.drawable.tbg3, stickerEraseActivity.width, StickerEraseActivity.this.height));
                    StickerEraseActivity.bgCircleBit = ImageUtils.getBgCircleBit(StickerEraseActivity.this, R.drawable.tbg3);
                } else {
                    ImageView imageView2 = StickerEraseActivity.this.imageViewBackgroundCover;
                    StickerEraseActivity stickerEraseActivity2 = StickerEraseActivity.this;
                    imageView2.setImageBitmap(ImageUtils.getTiledBitmap(stickerEraseActivity2, R.drawable.tbg, stickerEraseActivity2.width, StickerEraseActivity.this.height));
                    StickerEraseActivity.bgCircleBit = ImageUtils.getBgCircleBit(StickerEraseActivity.this, R.drawable.tbg);
                }
                StickerEraseActivity.this.importImageFromUri();
            }
        }, 1000);
    }

    private void initUI() {
        this.relativeLayoutSeekBar = (RelativeLayout) findViewById(R.id.relativeLayoutSeekBar);
        this.relativeLayoutAuto = (RelativeLayout) findViewById(R.id.relativeLayoutAuto);
        this.relativeLayoutEraser = (RelativeLayout) findViewById(R.id.relativeLayoutEraser);
        this.relativeLayoutRestore = (RelativeLayout) findViewById(R.id.relativeLayoutRestore);
        this.relativeLayoutExtract = (RelativeLayout) findViewById(R.id.relativeLayoutExtract);
        this.relativeLayoutZoom = (RelativeLayout) findViewById(R.id.relativeLayoutZoom);
        this.main_rel = (RelativeLayout) findViewById(R.id.main_rel);
        this.linearLayoutAuto = (LinearLayout) findViewById(R.id.linearLayoutAuto);
        this.linearLayoutEraser = (LinearLayout) findViewById(R.id.linearLayoutEraser);
        this.lay_lasso_cut = (LinearLayout) findViewById(R.id.lay_lasso_cut);
        this.inside_cut_lay = (RelativeLayout) findViewById(R.id.inside_cut_lay);
        this.outside_cut_lay = (RelativeLayout) findViewById(R.id.outside_cut_lay);
        this.undo_btn = (ImageView) findViewById(R.id.imageViewUndo);
        this.redo_btn = (ImageView) findViewById(R.id.imageViewRedo);
        this.back_btn = (ImageView) findViewById(R.id.btn_back);
        this.save_btn = (ImageView) findViewById(R.id.save_image_btn);
        this.relativeLayoutBackground = (RelativeLayout) findViewById(R.id.relativeLayoutBackground);
        this.imageViewBackgroundCover = (ImageView) findViewById(R.id.imageViewBackgroundCover);
        this.textViewBrushSize = (TextView) findViewById(R.id.textViewBrushSize);
        this.textViewBrushOffset = (TextView) findViewById(R.id.textViewBrushOffset);
        this.textViewAutoOffset = (TextView) findViewById(R.id.textViewOffset);
        this.textViewExtractOffset = (TextView) findViewById(R.id.textViewExtractOffset);
        this.back_btn.setOnClickListener(this);
        this.undo_btn.setOnClickListener(this);
        this.redo_btn.setOnClickListener(this);
        this.undo_btn.setEnabled(false);
        this.redo_btn.setEnabled(false);
        this.save_btn.setOnClickListener(this);
        this.relativeLayoutBackground.setOnClickListener(this);
        this.relativeLayoutEraser.setOnClickListener(this);
        this.relativeLayoutAuto.setOnClickListener(this);
        this.relativeLayoutExtract.setOnClickListener(this);
        this.relativeLayoutZoom.setOnClickListener(this);
        this.relativeLayoutRestore.setOnClickListener(this);
        this.inside_cut_lay.setOnClickListener(this);
        this.outside_cut_lay.setOnClickListener(this);
        this.seekBarBrushOffset = (SeekBar) findViewById(R.id.seekBarBrushOffset);
        this.seekBarOffset = (SeekBar) findViewById(R.id.seekBarOffset);
        this.seekBarExtractOffset = (SeekBar) findViewById(R.id.seekBarExtractOffset);
        this.seekBarBrushOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StickerEraseActivity.this.dv != null) {
                    StickerEraseActivity.this.dv.setOffset(i - 150);
                    StickerEraseActivity.this.dv.invalidate();
                    StickerEraseActivity.this.textViewBrushOffset.setText(String.valueOf(i));
                }
            }
        });
        this.seekBarOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StickerEraseActivity.this.dv != null) {
                    StickerEraseActivity.this.dv.setOffset(i - 150);
                    StickerEraseActivity.this.dv.invalidate();
                    StickerEraseActivity.this.textViewAutoOffset.setText(String.valueOf(i));
                }
            }
        });
        this.seekBarExtractOffset.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StickerEraseActivity.this.dv != null) {
                    StickerEraseActivity.this.dv.setOffset(i - 150);
                    StickerEraseActivity.this.dv.invalidate();
                    StickerEraseActivity.this.textViewExtractOffset.setText(String.valueOf(i));
                }
            }
        });
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarSize);
        this.radius_seekbar = seekBar;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StickerEraseActivity.this.dv != null) {
                    StickerEraseActivity.this.dv.setRadius(i + 2);
                    StickerEraseActivity.this.dv.invalidate();
                    StickerEraseActivity.this.textViewBrushSize.setText(String.valueOf(i));
                }
            }
        });
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBarThreshold);
        this.seekBarThreshold = seekBar2;
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (StickerEraseActivity.this.dv != null) {
                    StickerEraseActivity.this.dv.setThreshold(seekBar.getProgress() + 10);
                    StickerEraseActivity.this.dv.updateThreshHold();
                }
            }
        });
    }

    public void onClick(View view) {
        if (this.dv != null || view.getId() == R.id.btn_back) {
            switch (view.getId()) {
                case R.id.btn_back:
                    onBackPressed();
                    return;
                case R.id.imageViewRedo:
                    final ProgressDialog show = ProgressDialog.show(this, "", getString(R.string.redoing) + "...", true);
                    show.setCancelable(false);
                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                StickerEraseActivity.this.runOnUiThread(new Runnable() {

                                    public void run() {
                                        StickerEraseActivity.this.dv.redoChange();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            show.dismiss();
                        }
                    }).start();
                    return;
                case R.id.imageViewUndo:
                    final ProgressDialog show2 = ProgressDialog.show(this, "", getString(R.string.undoing) + "...", true);
                    show2.setCancelable(false);
                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                StickerEraseActivity.this.runOnUiThread(new Runnable() {

                                    public void run() {
                                        StickerEraseActivity.this.dv.undoChange();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            show2.dismiss();
                        }
                    }).start();
                    return;
                case R.id.inside_cut_lay:
                    this.dv.enableInsideCut(true);
                    this.inside_cut_lay.clearAnimation();
                    this.outside_cut_lay.clearAnimation();
                    return;
                case R.id.outside_cut_lay:
                    this.dv.enableInsideCut(false);
                    this.inside_cut_lay.clearAnimation();
                    this.outside_cut_lay.clearAnimation();
                    return;
                case R.id.relativeLayoutAuto:
                    setSelected(R.id.relativeLayoutAuto);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(2);
                    this.dv.invalidate();
                    return;
                case R.id.relativeLayoutBackground:
                    changeBG();
                    return;
                case R.id.relativeLayoutEraser:
                    setSelected(R.id.relativeLayoutEraser);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(1);
                    this.dv.invalidate();
                    return;
                case R.id.relativeLayoutExtract:
                    setSelected(R.id.relativeLayoutExtract);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(3);
                    this.dv.invalidate();
                    return;
                case R.id.relativeLayoutRestore:
                    setSelected(R.id.relativeLayoutRestore);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(4);
                    this.dv.invalidate();
                    return;
                case R.id.relativeLayoutZoom:
                    this.dv.enableTouchClear(false);
                    this.main_rel.setOnTouchListener(new MultiTouchListener());
                    setSelected(R.id.relativeLayoutZoom);
                    this.dv.setMODE(0);
                    this.dv.invalidate();
                    return;
                case R.id.save_image_btn:
                    Bitmap finalBitmap = this.dv.getFinalBitmap();
                    bitmap = finalBitmap;
                    if (finalBitmap != null) {
                        try {
                            int dpToPx = ImageUtils.dpToPx((Context) this, 42.0f);
                            Bitmap resizeBitmap = ImageUtils.resizeBitmap(bitmap, orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx);
                            bitmap = resizeBitmap;
                            int i = dpToPx + dpToPx;
                            Bitmap createBitmap = Bitmap.createBitmap(resizeBitmap, dpToPx, dpToPx, resizeBitmap.getWidth() - i, bitmap.getHeight() - i);
                            bitmap = createBitmap;
                            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(createBitmap, orgBitWidth, orgBitHeight, true);
                            bitmap = createScaledBitmap;
                            bitmap = ImageUtils.bitmapmasking(this.orgBitmap, createScaledBitmap);
                            if (this.openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_NEON)) {
                                NeonLayout.resultBitmap = bitmap;
                            } else if (this.openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_DRIP)) {
                                DripLayout.resultBmp = bitmap;
                            } else if (this.openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_MOTION)) {
                                MotionLayout.resultBmp = bitmap;
                            } else if (this.openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_ART)) {
                                ArtLayout.resultBitmap = bitmap;
                            } else if (this.openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_WING)) {
                                WingLayout.resultBmp = bitmap;
                            }else if(this.openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_BACKGROUND_REMOVER)){
                                if (PermissionsUtils.checkWriteStoragePermission(this)) {
                                    new SaveEditingBitmap(bitmap).execute(new Void[0]);
                                }
                                return;
                            }
                            setResult(-1);
                            finish();
                            return;
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        finish();
                        return;
                    }
                default:
                    return;
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.import_img_warning), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveEditingBitmap(bitmap).execute(new Void[0]);
        }
    }

    public class SaveEditingBitmap extends AsyncTask<Void, String, String> {
        Bitmap bitmap;
        SaveEditingBitmap(Bitmap bitmap) {
            this.bitmap=bitmap;
        }

        public void onPreExecute() {
            showLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                StickerEraseActivity stickerEraseActivity = StickerEraseActivity.this;
                String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                return SaveFileUtils.saveBitmapFileEditor(stickerEraseActivity,bitmap , format).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
           showLoading(false);
            if (string == null) {
                Toast.makeText(getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            MediaScannerConnection.scanFile(StickerEraseActivity.this, new String[]{string}, null, null);
            Intent i = new Intent(StickerEraseActivity.this, PhotoShareActivity.class);
            i.putExtra("path", string);
            StickerEraseActivity.this.startActivity(i);
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

    private void changeBG() {
        int i = curBgType;
        if (i == 1) {
            curBgType = 2;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg1, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg1);
        } else if (i == 2) {
            curBgType = 3;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg2, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg2);
        } else if (i == 3) {
            curBgType = 4;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg3, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg3);
        } else if (i == 4) {
            curBgType = 5;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg4, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg4);
        } else if (i == 5) {
            curBgType = 6;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg5, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg5);
        } else if (i == 6) {
            curBgType = 1;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg);
        }
    }

    public void importImageFromUri() {
        this.showDialog = false;
        final ProgressDialog show = ProgressDialog.show(this, "", getResources().getString(R.string.importing_image), true);
        show.setCancelable(false);
        new Thread(new Runnable() {

            public void run() {
                try {
                    if (StickerEraseActivity.b == null) {
                        StickerEraseActivity.this.showDialog = true;
                    } else {
                        StickerEraseActivity.this.orgBitmap = StickerEraseActivity.b.copy(StickerEraseActivity.b.getConfig(), true);
                        int dpToPx = ImageUtils.dpToPx((Context) StickerEraseActivity.this, 42.0f);
                        StickerEraseActivity.orgBitWidth = StickerEraseActivity.b.getWidth();
                        StickerEraseActivity.orgBitHeight = StickerEraseActivity.b.getHeight();
                        Bitmap createBitmap = Bitmap.createBitmap(StickerEraseActivity.b.getWidth() + dpToPx + dpToPx, StickerEraseActivity.b.getHeight() + dpToPx + dpToPx, StickerEraseActivity.b.getConfig());
                        Canvas canvas = new Canvas(createBitmap);
                        canvas.drawColor(0);
                        float f = (float) dpToPx;
                        canvas.drawBitmap(StickerEraseActivity.b, f, f, (Paint) null);
                        StickerEraseActivity.b = createBitmap;
                        if (StickerEraseActivity.b.getWidth() > StickerEraseActivity.this.width || StickerEraseActivity.b.getHeight() > StickerEraseActivity.this.height || (StickerEraseActivity.b.getWidth() < StickerEraseActivity.this.width && StickerEraseActivity.b.getHeight() < StickerEraseActivity.this.height)) {
                            StickerEraseActivity.b = ImageUtils.resizeBitmap(StickerEraseActivity.b, StickerEraseActivity.this.width, StickerEraseActivity.this.height);
                        }
                    }
                    Thread.sleep(1000);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    StickerEraseActivity.this.showDialog = true;
                    show.dismiss();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    StickerEraseActivity.this.showDialog = true;
                    show.dismiss();
                }
                show.dismiss();
            }
        }).start();
        show.setOnDismissListener(new DialogInterface.OnDismissListener() {

            public void onDismiss(DialogInterface dialogInterface) {
                if (StickerEraseActivity.this.showDialog) {
                    StickerEraseActivity stickerRemoveActivity = StickerEraseActivity.this;
                    Toast.makeText(stickerRemoveActivity, stickerRemoveActivity.getResources().getString(R.string.import_error), Toast.LENGTH_SHORT).show();
                    StickerEraseActivity.this.finish();
                    return;
                }
                ConstantsApp.rewid = "";
                ConstantsApp.uri = "";
                ConstantsApp.bitmapSticker = null;
                StickerEraseActivity.this.setImageBitmap();
            }
        });
    }

    public void setImageBitmap() {
        this.dv = new EraseView(this);
        this.dv1 = new ImageView(this);
        this.dv.setImageBitmap(b);
        this.dv1.setImageBitmap(getGreenLayerBitmap(b));
        this.dv.enableTouchClear(false);
        this.dv.setMODE(0);
        this.dv.invalidate();
        this.seekBarBrushOffset.setProgress(225);
        this.radius_seekbar.setProgress(18);
        this.seekBarThreshold.setProgress(20);
        this.main_rel.removeAllViews();
        this.main_rel.setScaleX(1.0f);
        this.main_rel.setScaleY(1.0f);
        this.main_rel.addView(this.dv1);
        this.main_rel.addView(this.dv);
        this.dv.invalidate();
        this.dv1.setVisibility(View.GONE);
        this.dv.setUndoRedoListener(new EraseView.UndoRedoListener() {

            @Override
            public void enableUndo(boolean z, int i) {
                if (z) {
                    StickerEraseActivity.this.setBGDrawable(i, StickerEraseActivity.this.undo_btn, R.drawable.ic_undo, z);
                } else {
                    StickerEraseActivity.this.setBGDrawable(i, StickerEraseActivity.this.undo_btn, R.drawable.ic_undo, z);
                }
            }

            @Override
            public void enableRedo(boolean z, int i) {
                if (z) {
                    StickerEraseActivity.this.setBGDrawable(i, StickerEraseActivity.this.redo_btn, R.drawable.ic_redo, z);
                } else {
                    StickerEraseActivity.this.setBGDrawable(i, StickerEraseActivity.this.redo_btn, R.drawable.ic_redo, z);
                }
            }
        });
        b.recycle();
        this.dv.setActionListener(new EraseView.ActionListener() {

            @Override
            public void onActionCompleted(final int i) {
                StickerEraseActivity.this.runOnUiThread(new Runnable() {

                    public void run() {
                    }
                });
            }

            @Override
            public void onAction(final int i) {
                StickerEraseActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (i == 0) {
                            StickerEraseActivity.this.relativeLayoutSeekBar.setVisibility(View.GONE);
                        }
                        if (i == 1) {
                            StickerEraseActivity.this.relativeLayoutSeekBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void setBGDrawable(int i, final ImageView imageView, final int i2, final boolean z) {
        runOnUiThread(new Runnable() {

            public void run() {
                imageView.setImageResource(i2);
                imageView.setEnabled(z);
            }
        });
    }

    public Bitmap getGreenLayerBitmap(Bitmap bitmap2) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAlpha(80);
        int dpToPx = ImageUtils.dpToPx((Context) this, 42.0f);
        Bitmap createBitmap = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        float f = (float) dpToPx;
        canvas.drawBitmap(this.orgBitmap, f, f, (Paint) null);
        canvas.drawRect(f, f, (float) (orgBitWidth + dpToPx), (float) (orgBitHeight + dpToPx), paint);
        Bitmap createBitmap2 = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawColor(0);
        canvas2.drawBitmap(this.orgBitmap, f, f, (Paint) null);
        patternBMPshader = new BitmapShader(ImageUtils.resizeBitmap(createBitmap2, this.width, this.height), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        return ImageUtils.resizeBitmap(createBitmap, this.width, this.height);
    }

    public void setSelected(int i) {
        if (i == R.id.relativeLayoutEraser) {
            this.seekBarBrushOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.VISIBLE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
        if (i == R.id.relativeLayoutAuto) {
            this.seekBarOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.GONE);
            this.linearLayoutAuto.setVisibility(View.VISIBLE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
        if (i == R.id.relativeLayoutExtract) {
            this.seekBarExtractOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.GONE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.VISIBLE);
        }
        if (i == R.id.relativeLayoutRestore) {
            this.seekBarBrushOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.VISIBLE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
        if (i == R.id.relativeLayoutZoom) {
            this.linearLayoutEraser.setVisibility(View.GONE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        Bitmap bitmap2 = b;
        if (bitmap2 != null) {
            bitmap2.recycle();
            b = null;
        }
        try {
            if (!isFinishing() && this.dv.pd != null && this.dv.pd.isShowing()) {
                this.dv.pd.dismiss();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
    }
}
