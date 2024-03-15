package com.relish.app.Activity;

import static android.view.WindowManager.LayoutParams.ANIMATION_CHANGED;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.relish.app.Ad.AdUtils;
import com.relish.app.Adapter.AspectAdapter;
import com.relish.app.Adapter.CollageBackgroundAdapter;
import com.relish.app.Adapter.CollageColorAdapter;
import com.relish.app.Adapter.FilterAdapter;
import com.relish.app.Adapter.GridAdapter;
import com.relish.app.Adapter.GridItemToolsAdapter;
import com.relish.app.Adapter.GridToolsAdapter;
import com.relish.app.Adapter.RecyclerTabLayout;
import com.relish.app.Adapter.StickerAdapter;
import com.relish.app.Adapter.StickersTabAdapter;
import com.relish.app.Fragment.CropFragment;
import com.relish.app.Fragment.FilterFragment;
import com.relish.app.Fragment.TextFragment;
import com.relish.app.R;
import com.relish.app.Utils.CollageUtils;
import com.relish.app.Utils.FilterUtils;
//import com.relish.app.Utils.Helper;
//import com.relish.app.Utils.NetworkHelper;
import com.relish.app.Utils.Helper;
import com.relish.app.Utils.NetworkHelper;
import com.relish.app.Utils.SaveFileUtils;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.assets.FilterFileAsset;
import com.relish.app.assets.StickerFileAsset;
import com.relish.app.event.AlignHorizontallyEvent;
import com.relish.app.event.DeleteIconEvent;
import com.relish.app.event.EditTextIconEvent;
import com.relish.app.event.FlipHorizontallyEvent;
import com.relish.app.event.ZoomIconEvent;
import com.relish.app.keyboardHeight.KeyboardHeightProvider;
import com.relish.app.listener.FilterListener;
import com.relish.app.model.General;
import com.relish.app.module.Module;
import com.relish.app.picker.PermissionsUtils;
import com.relish.app.polish.PolishGridView;
import com.relish.app.polish.PolishPickerView;
import com.relish.app.polish.PolishStickerIcons;
import com.relish.app.polish.PolishStickerView;
import com.relish.app.polish.PolishText;
import com.relish.app.polish.PolishTextView;
import com.relish.app.polish.grid.PolishGrid;
import com.relish.app.polish.grid.PolishLayout;
import com.relish.app.polish.grid.PolishLayoutParser;
import com.relish.app.preference.Preference;
import com.relish.app.sticker.DrawableSticker;
import com.relish.app.sticker.Sticker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.steelkiwi.cropiwa.AspectRatio;

import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class PolishCollageActivity extends PolishBaseActivity implements GridToolsAdapter.OnItemSelected, AspectAdapter.OnNewSelectedListener, StickerAdapter.OnClickSplashListener, CollageBackgroundAdapter.BackgroundGridListener, FilterListener, CropFragment.OnCropPhoto, CollageColorAdapter.BackgroundColorListener, FilterFragment.OnFilterSavePhoto, GridItemToolsAdapter.OnPieceFuncItemSelected, GridAdapter.OnItemClickListener {
    public static PolishCollageActivity QueShotGridActivityCollage;
    private static PolishCollageActivity QueShotGridActivityInstance;
    public float BorderRadius;
    public float Padding;
    public TextFragment addTextFragment;
    public AspectRatio aspectRatio;
    private LinearLayout bannerContainer;
    private ConstraintLayout constraintLayoutAddText;
    private ConstraintLayout constraintLayoutSaveSticker;
    private ConstraintLayout constraintLayoutSaveText;
    public ConstraintLayout constraint_layout_change_background;
    private ConstraintLayout constraint_layout_collage_layout;
    public ConstraintLayout constraint_layout_filter_layout;
    public ConstraintLayout constraint_layout_sticker;
    private ConstraintLayout constraint_layout_wrapper_collage_view;
    private ConstraintLayout constraint_save_control;
    public ConstraintLayout constrant_layout_change_Layout;
    public CollageBackgroundAdapter.SquareView currentBackgroundState;
    private int deviceHeight = 0;
    public int deviceWidth = 0;
    public List<Drawable> drawableList = new ArrayList();
    private General generalModel;
    private GridItemToolsAdapter gridItemToolsAdapter = new GridItemToolsAdapter(this);
    public GridToolsAdapter gridToolsAdapter = new GridToolsAdapter(this, true);
    private Guideline guideline;
    private Guideline guidelineTools;
    public ImageView imageViewAddSticker;
    private KeyboardHeightProvider keyboardHeightProvider;
    private LinearLayout linearLayoutBlur;
    private LinearLayout linearLayoutBorde;
    private LinearLayout linearLayoutBorder;
    private LinearLayout linearLayoutColor;
    private LinearLayout linearLayoutGradient;
    private LinearLayout linearLayoutLayer;
    private LinearLayout linearLayoutRatio;
    public LinearLayout linear_layout_wrapper_sticker_list;
    public ArrayList listFilterAll = new ArrayList();
    public CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        @Override
        public Bitmap loadImage(String string, Object object) {
            try {
                return BitmapFactory.decodeStream(PolishCollageActivity.this.getAssets().open(string));
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void loadImageOK(Bitmap bitmap, Object object) {
            bitmap.recycle();
        }
    };

    public Module moduleToolsId;
    public View.OnClickListener onClickListener = new View.OnClickListener() {

        public final void onClick(View view) {
            PolishCollageActivity.this.viewClick(view);
        }
    };
    public SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            switch (seekBar.getId()) {
                case R.id.seekbar_border:
                    PolishCollageActivity.this.queShotGridView.setCollagePadding((float) i);
                    PolishCollageActivity.this.textViewSeekBarPadding.setText(String.valueOf(i));
                    break;
                case R.id.seekbar_radius:
                    PolishCollageActivity.this.queShotGridView.setCollageRadian((float) i);
                    PolishCollageActivity.this.textViewSeekBarRadius.setText(String.valueOf(i));
                    break;
            }
            PolishCollageActivity.this.queShotGridView.invalidate();
        }
    };
    PolishStickerView.OnStickerOperationListener onStickerOperationListener = new PolishStickerView.OnStickerOperationListener() {

        @Override
        public void onStickerDrag(Sticker sticker) {
        }

        @Override
        public void onStickerFlip(Sticker sticker) {
        }

        @Override
        public void onStickerTouchedDown(Sticker sticker) {
        }

        @Override
        public void onStickerZoom(Sticker sticker) {
        }

        @Override
        public void onTouchDownBeauty(float f, float f2) {
        }

        @Override
        public void onTouchDragBeauty(float f, float f2) {
        }

        @Override
        public void onTouchUpBeauty(float f, float f2) {
        }

        @Override
        public void onAddSticker(Sticker sticker) {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
        }

        @Override
        public void onStickerSelected(Sticker sticker) {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
        }

        @Override
        public void onStickerDeleted(Sticker sticker) {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
        }

        @Override
        public void onStickerTouchOutside() {
            PolishCollageActivity.this.seekbarSticker.setVisibility(View.GONE);
        }

        @Override
        public void onStickerDoubleTap(Sticker sticker) {
            if (sticker instanceof PolishTextView) {
                sticker.setShow(false);
                PolishCollageActivity.this.queShotGridView.setHandlingSticker(null);
                PolishCollageActivity polishCollageActivity = PolishCollageActivity.this;
                polishCollageActivity.addTextFragment = TextFragment.show(polishCollageActivity, ((PolishTextView) sticker).getPolishText());
                PolishCollageActivity.this.textEditor = new TextFragment.TextEditor() {

                    @Override
                    public void onDone(PolishText addTextProperties) {
                        PolishCollageActivity.this.queShotGridView.getStickers().remove(PolishCollageActivity.this.queShotGridView.getLastHandlingSticker());
                        PolishCollageActivity.this.queShotGridView.addSticker(new PolishTextView(PolishCollageActivity.this, addTextProperties));
                    }

                    @Override
                    public void onBackButton() {
                        PolishCollageActivity.this.queShotGridView.showLastHandlingSticker();
                    }
                };
                PolishCollageActivity.this.addTextFragment.setOnTextEditorListener(PolishCollageActivity.this.textEditor);
            }
        }
    };
    ActivityResultLauncher<Intent> paymentResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {


        @Override
        public final void onActivityResult(Object obj) {
            PolishCollageActivity.this.onResult((ActivityResult) obj);
        }
    });
    public PolishGridView queShotGridView;
    public PolishLayout queShotLayout;
    public RecyclerView recyclerViewFilter;
    public RecyclerView recyclerViewTools;
    public RecyclerView recyclerViewToolsCollage;
    private RecyclerView recycler_view_blur;
    private RecyclerView recycler_view_collage;
    private RecyclerView recycler_view_color;
    private RecyclerView recycler_view_gradient;
    private RecyclerView recycler_view_ratio;
    private RelativeLayout relativeLayoutAddText;
    private RelativeLayout relativeLayoutLoading;
    private SeekBar seekBarPadding;
    private SeekBar seekBarRadius;
    public SeekBar seekbarSticker;
    private Animation slideDownAnimation;
    private Animation slideUpAnimation;
    public List<String> stringList;
    public List<Target> targets = new ArrayList();
    public TextFragment.TextEditor textEditor;
    public TextView textViewCancel;
    public TextView textViewDiscard;
    private TextView textViewListBlur;
    private TextView textViewListBorder;
    private TextView textViewListColor;
    private TextView textViewListGradient;
    private TextView textViewListLayer;
    private TextView textViewListRatio;
    private TextView textViewSeekBarPadding;
    private TextView textViewSeekBarRadius;
    private TextView textViewTitle;
    private TextView text_view_save;
    private View viewBlur;
    private View viewBorder;
    private View viewCollage;
    private View viewColor;
    private View viewGradient;
    private View viewRatio;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setFullScreen();
        setContentView(R.layout.activity_polish_collage);
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().setSoftInputMode(72);
        }
        this.deviceWidth = getResources().getDisplayMetrics().widthPixels;
        this.deviceHeight = getResources().getDisplayMetrics().heightPixels;
        findViewById(R.id.image_view_exit).setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.exitClick(view);
            }
        });
        this.queShotGridView = (PolishGridView) findViewById(R.id.collage_view);
        this.bannerContainer = (LinearLayout) findViewById(R.id.bannerContainer);
        this.constraintLayoutSaveText = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_save_text);
        this.constraintLayoutSaveSticker = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_save_sticker);
        this.constraint_layout_wrapper_collage_view = (ConstraintLayout) findViewById(R.id.constraint_layout_wrapper_collage_view);
        this.constraint_layout_filter_layout = (ConstraintLayout) findViewById(R.id.constraint_layout_filter_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_tools);
        this.recyclerViewTools = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewTools.setAdapter(this.gridToolsAdapter);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recycler_view_tools_collage);
        this.recyclerViewToolsCollage = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewToolsCollage.setAdapter(this.gridItemToolsAdapter);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar_border);
        this.seekBarPadding = seekBar;
        seekBar.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekbar_radius);
        this.seekBarRadius = seekBar2;
        seekBar2.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        this.stringList = getIntent().getStringArrayListExtra(GridPickerActivity.KEY_DATA_RESULT);
        this.relativeLayoutLoading = (RelativeLayout) findViewById(R.id.relative_layout_loading);
        this.recyclerViewFilter = (RecyclerView) findViewById(R.id.recycler_view_filter);
        this.linearLayoutBorder = (LinearLayout) findViewById(R.id.linearLayoutPadding);
        this.guidelineTools = (Guideline) findViewById(R.id.guidelineTools);
        this.guideline = (Guideline) findViewById(R.id.guideline);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_add_text);
        this.relativeLayoutAddText = relativeLayout;
        relativeLayout.setVisibility(View.GONE);
        this.constraintLayoutAddText = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_text);
        PolishLayout polishLayout = CollageUtils.getCollageLayouts(this.stringList.size()).get(0);
        this.queShotLayout = polishLayout;
        this.queShotGridView.setQueShotLayout(polishLayout);
        this.queShotGridView.setTouchEnable(true);
        this.queShotGridView.setNeedDrawLine(false);
        this.queShotGridView.setNeedDrawOuterLine(false);
        this.queShotGridView.setLineSize(4);
        this.queShotGridView.setCollagePadding(6.0f);
        this.queShotGridView.setCollageRadian(15.0f);
        this.queShotGridView.setLineColor(ContextCompat.getColor(this, R.color.white));
        this.queShotGridView.setSelectedLineColor(ContextCompat.getColor(this, R.color.white));
        this.queShotGridView.setHandleBarColor(ContextCompat.getColor(this, R.color.white));
        this.queShotGridView.setAnimateDuration(300);
        this.queShotGridView.setOnQueShotSelectedListener(new PolishGridView.onQueShotSelectedListener() {

            @Override
            public final void onQuShotSelected(PolishGrid polishGrid, int i) {
                PolishCollageActivity.this.quShortSelectedClick(polishGrid, i);
            }
        });
        this.queShotGridView.setOnQueShotUnSelectedListener(new PolishGridView.onQueShotUnSelectedListener() {

            @Override
            public final void onQuShotUnSelected() {
                PolishCollageActivity.this.quShortunSelectedClick();
            }
        });
        this.constraint_save_control = (ConstraintLayout) findViewById(R.id.constraint_save_control);
        this.queShotGridView.post(new Runnable() {

            public final void run() {
                PolishCollageActivity.this.GridViewClick();
            }
        });
        findViewById(R.id.imageViewSaveLayer).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseLayer).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSaveText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseText).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewClosebackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.image_view_close_sticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSaveFilter).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewSavebackground).setOnClickListener(this.onClickListener);
        findViewById(R.id.image_view_save_sticker).setOnClickListener(this.onClickListener);
        findViewById(R.id.imageViewCloseFilter).setOnClickListener(this.onClickListener);
        this.linearLayoutLayer = (LinearLayout) findViewById(R.id.linearLayoutCollage);
        this.linearLayoutBorde = (LinearLayout) findViewById(R.id.linearLayoutBorder);
        this.linearLayoutRatio = (LinearLayout) findViewById(R.id.linearLayoutRatio);
        this.textViewListLayer = (TextView) findViewById(R.id.text_view_collage);
        this.textViewListBorder = (TextView) findViewById(R.id.text_view_border);
        this.textViewListRatio = (TextView) findViewById(R.id.text_view_ratio);
        this.viewCollage = findViewById(R.id.view_collage);
        this.viewBorder = findViewById(R.id.view_border);
        this.viewRatio = findViewById(R.id.view_ratio);
        this.linearLayoutLayer.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.layerClick(view);
            }
        });
        this.linearLayoutBorde.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.borderClick(view);
            }
        });
        this.linearLayoutRatio.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.ratioClick(view);
            }
        });
        this.linearLayoutColor = (LinearLayout) findViewById(R.id.linearLayoutColor);
        this.linearLayoutGradient = (LinearLayout) findViewById(R.id.linearLayoutGradient);
        this.linearLayoutBlur = (LinearLayout) findViewById(R.id.linearLayoutBlur);
        this.textViewListColor = (TextView) findViewById(R.id.text_view_color);
        this.textViewListGradient = (TextView) findViewById(R.id.text_view_gradient);
        this.textViewListBlur = (TextView) findViewById(R.id.text_view_blur);
        this.viewGradient = findViewById(R.id.view_gradient);
        this.viewBlur = findViewById(R.id.view_blur);
        this.viewColor = findViewById(R.id.view_color);
        this.linearLayoutColor.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.colorClick(view);
            }
        });
        this.linearLayoutGradient.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.gradientClick(view);
            }
        });
        this.linearLayoutBlur.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.blurClick(view);
            }
        });
        this.constrant_layout_change_Layout = (ConstraintLayout) findViewById(R.id.constrant_layout_change_Layout);
        this.textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        this.textViewSeekBarPadding = (TextView) findViewById(R.id.seekbarPadding);
        this.textViewSeekBarRadius = (TextView) findViewById(R.id.seekbarRadius);
        GridAdapter collageAdapter = new GridAdapter();
        RecyclerView recyclerView3 = (RecyclerView) findViewById(R.id.recycler_view_collage);
        this.recycler_view_collage = recyclerView3;
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_collage.setAdapter(collageAdapter);
        collageAdapter.refreshData(CollageUtils.getCollageLayouts(this.stringList.size()), null);
        collageAdapter.setOnItemClickListener(this);
        AspectAdapter aspectRatioPreviewAdapter = new AspectAdapter(true);
        aspectRatioPreviewAdapter.setListener(this);
        RecyclerView recyclerView4 = (RecyclerView) findViewById(R.id.recycler_view_ratio);
        this.recycler_view_ratio = recyclerView4;
        recyclerView4.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_ratio.setAdapter(aspectRatioPreviewAdapter);
        this.linear_layout_wrapper_sticker_list = (LinearLayout) findViewById(R.id.linear_layout_wrapper_sticker_list);
        ViewPager stickerViewPager = (ViewPager) findViewById(R.id.stickerViewpaper);
        this.constraint_layout_sticker = (ConstraintLayout) findViewById(R.id.constraint_layout_sticker);
        SeekBar seekBar3 = (SeekBar) findViewById(R.id.seekbarStickerAlpha);
        this.seekbarSticker = seekBar3;
        seekBar3.setVisibility(View.GONE);
        this.seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PolishCollageActivity.this.queShotGridView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        this.relativeLayoutAddText.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.addtextClick(view);
            }
        });

        AdUtils.ShowAmBanner(this, (LinearLayout) findViewById(R.id.ll_banneradmidview), (LinearLayout) findViewById(R.id.lnr_ads));

        TextView textView = (TextView) findViewById(R.id.text_view_save);
        this.text_view_save = textView;
        textView.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.saveClick(view);
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker = imageView;
        imageView.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.addStickerClick(view);
            }
        });
        PolishStickerIcons quShotStickerIconClose = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PolishStickerIcons.REMOVE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        PolishStickerIcons quShotStickerIconScale = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PolishStickerIcons.ZOOM);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons quShotStickerIconFlip = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PolishStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        PolishStickerIcons quShotStickerIconCenter = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PolishStickerIcons.ALIGN_HORIZONTALLY);
        quShotStickerIconCenter.setIconEvent(new AlignHorizontallyEvent());
        PolishStickerIcons quShotStickerIconRotate = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PolishStickerIcons.ROTATE);
        quShotStickerIconRotate.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons quShotStickerIconEdit = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PolishStickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new EditTextIconEvent());
        this.queShotGridView.setIcons(Arrays.asList(quShotStickerIconClose, quShotStickerIconScale, quShotStickerIconFlip, quShotStickerIconEdit, quShotStickerIconRotate, quShotStickerIconCenter));
        this.queShotGridView.setConstrained(true);
        this.queShotGridView.setOnStickerOperationListener(this.onStickerOperationListener);
        stickerViewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 23;
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view.equals(obj);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup viewGroup, int i) {
                View inflate = LayoutInflater.from(PolishCollageActivity.this.getBaseContext()).inflate(R.layout.list_sticker, (ViewGroup) null, false);
                RecyclerView recycler_view_sticker = (RecyclerView) inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PolishCollageActivity.this.getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.bubbleList(), i, PolishCollageActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.cartoonList(), i, PolishCollageActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.childList(), i, PolishCollageActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.flowerList(), i, PolishCollageActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PolishCollageActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.deliciousList(), i, PolishCollageActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.newyearList(), i, PolishCollageActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.popularList(), i, PolishCollageActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PolishCollageActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.rageList(), i, PolishCollageActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PolishCollageActivity.this));
                        break;
                    case 11:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.unicornList(), i, PolishCollageActivity.this));
                        break;
                    case 12:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PolishCollageActivity.this));
                        break;
                    case 13:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.plantList(), i, PolishCollageActivity.this));
                        break;
                    case 14:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.birthdayList(), i, PolishCollageActivity.this));
                        break;
                    case 15:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.lovedayList(), i, PolishCollageActivity.this));
                        break;
                    case 16:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.chickenList(), i, PolishCollageActivity.this));
                        break;
                    case 17:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.textneonList(), i, PolishCollageActivity.this));
                        break;
                    case 18:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.thuglifeList(), i, PolishCollageActivity.this));
                        break;
                    case 19:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.sweetList(), i, PolishCollageActivity.this));
                        break;
                    case 20:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.celebrateList(), i, PolishCollageActivity.this));
                        break;
                    case 21:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.happyList(), i, PolishCollageActivity.this));
                        break;
                    case 22:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishCollageActivity.this.getApplicationContext(), StickerFileAsset.textcolorList(), i, PolishCollageActivity.this));
                        break;
                }
                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = (RecyclerTabLayout) findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickersTabAdapter(stickerViewPager, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));
        Preference.setKeyboard(getApplicationContext(), 0);
        KeyboardHeightProvider keyboardHeightProvider2 = new KeyboardHeightProvider(this);
        this.keyboardHeightProvider = keyboardHeightProvider2;
        keyboardHeightProvider2.addKeyboardListener(new KeyboardHeightProvider.KeyboardListener() {

            @Override
            public final void onHeightChanged(int i) {
                PolishCollageActivity.this.heightChange(i);
            }
        });
        setLoading(false);
        this.constraint_layout_change_background = (ConstraintLayout) findViewById(R.id.constrant_layout_change_background);
        this.constraint_layout_collage_layout = (ConstraintLayout) findViewById(R.id.constraint_layout_collage_layout);
        this.currentBackgroundState = new CollageBackgroundAdapter.SquareView(Color.parseColor("#ffffff"), "", true);
        RecyclerView recyclerView5 = (RecyclerView) findViewById(R.id.recycler_view_color);
        this.recycler_view_color = recyclerView5;
        recyclerView5.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_color.setHasFixedSize(true);
        this.recycler_view_color.setAdapter(new CollageColorAdapter(getApplicationContext(), this));
        RecyclerView recyclerView6 = (RecyclerView) findViewById(R.id.recycler_view_gradient);
        this.recycler_view_gradient = recyclerView6;
        recyclerView6.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_gradient.setHasFixedSize(true);
        this.recycler_view_gradient.setAdapter(new CollageBackgroundAdapter(getApplicationContext(), (CollageBackgroundAdapter.BackgroundGridListener) this, true));
        RecyclerView recyclerView7 = (RecyclerView) findViewById(R.id.recycler_view_blur);
        this.recycler_view_blur = recyclerView7;
        recyclerView7.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.recycler_view_blur.setHasFixedSize(true);
        this.recycler_view_blur.setAdapter(new CollageBackgroundAdapter(getApplicationContext(), (CollageBackgroundAdapter.BackgroundGridListener) this, true));
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.queShotGridView.getLayoutParams();
        layoutParams.height = point.x;
        layoutParams.width = point.x;
        this.queShotGridView.setLayoutParams(layoutParams);
        this.aspectRatio = new AspectRatio(1, 1);
        this.queShotGridView.setAspectRatio(new AspectRatio(1, 1));
        QueShotGridActivityCollage = this;
        this.moduleToolsId = Module.NONE;
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        QueShotGridActivityInstance = this;
        this.recyclerViewToolsCollage.setAlpha(0.0f);
        this.constraint_layout_collage_layout.post(new Runnable() {

            public final void run() {
                PolishCollageActivity.this.collageLayoutClick();
            }
        });
        new Handler().postDelayed(new Runnable() {

            public final void run() {
                PolishCollageActivity.this.collagelayoutClick2();
            }
        }, 1000);
    }

    public void exitClick(View view) {
        onBackPressed();
    }

    public void quShortSelectedClick(PolishGrid collage, int i) {
        this.recyclerViewTools.setVisibility(View.GONE);
        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
        slideUp(this.recyclerViewToolsCollage);
        setGoneSave();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.recyclerViewToolsCollage.getLayoutParams();
        layoutParams.bottomMargin = SystemUtil.dpToPx(getApplicationContext(), 10);
        this.recyclerViewToolsCollage.setLayoutParams(layoutParams);
        this.moduleToolsId = Module.COLLAGE;
    }

    public void quShortunSelectedClick() {
        this.recyclerViewToolsCollage.setVisibility(View.GONE);
        this.recyclerViewTools.setVisibility(View.VISIBLE);
        setVisibleSave();
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.recyclerViewToolsCollage.getLayoutParams();
        layoutParams.bottomMargin = 0;
        this.recyclerViewToolsCollage.setLayoutParams(layoutParams);
        this.moduleToolsId = Module.NONE;
    }

    public void layerClick(View view) {
        setLayer();
    }

    public void borderClick(View view) {
        setBorder();
    }

    public void ratioClick(View view) {
        setRatio();
    }

    public void colorClick(View view) {
        setBackgroundColor();
    }

    public void gradientClick(View view) {
        setBackgroundGradient();
    }

    public void blurClick(View view) {
        selectBackgroundBlur();
    }

    public void addtextClick(View view) {
        this.queShotGridView.setHandlingSticker(null);
        openTextFragment();
    }

    public void saveClick(View view) {
            displayInterstitial();

    }

    public void addStickerClick(View view) {
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
    }

    public void heightChange(int i) {
        if (i <= 0) {
            Preference.setHeightOfNotch(getApplicationContext(), -i);
            return;
        }
        TextFragment textFragment = this.addTextFragment;
        if (textFragment != null) {
            textFragment.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + i);
            Preference.setKeyboard(getApplicationContext(), Preference.getHeightOfNotch(getApplicationContext()) + i);
        }
    }

    public void collageLayoutClick() {
        slideDown(this.recyclerViewToolsCollage);
    }

    public void collagelayoutClick2() {
        this.recyclerViewToolsCollage.setAlpha(1.0f);
    }

    public void onResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            this.recyclerViewTools.setVisibility(View.VISIBLE);
        }
    }

    private void SaveView() {
        if (PermissionsUtils.checkWriteStoragePermission(this)) {
            Bitmap createBitmap = SaveFileUtils.createBitmap(this.queShotGridView, 1920);
            Bitmap createBitmap2 = this.queShotGridView.createBitmap();
            new SaveCollageAsFile().execute(createBitmap, createBitmap2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            this.queShotGridView.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.keyboardHeightProvider.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.keyboardHeightProvider.onResume();
        checkData();
    }

    public void slideDown(View view) {
        ObjectAnimator.ofFloat(view, "translationY", 0.0f, (float) view.getHeight()).start();
    }

    public void slideUp(View view) {
        ObjectAnimator.ofFloat(view, "translationY", (float) view.getHeight(), 0.0f).start();
    }

    private void openTextFragment() {
        this.addTextFragment = TextFragment.show(this);
        TextFragment.TextEditor r0 = new TextFragment.TextEditor() {

            @Override
            public void onDone(PolishText addTextProperties) {
                PolishCollageActivity.this.queShotGridView.addSticker(new PolishTextView(PolishCollageActivity.this.getApplicationContext(), addTextProperties));
            }

            @Override
            public void onBackButton() {
                if (PolishCollageActivity.this.queShotGridView.getStickers().isEmpty()) {
                    PolishCollageActivity.this.onBackPressed();
                }
            }
        };
        this.textEditor = r0;
        this.addTextFragment.setOnTextEditorListener(r0);
    }

    public void viewClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewCloseFilter:
            case R.id.imageViewCloseLayer:
            case R.id.imageViewCloseText:
            case R.id.imageViewClosebackground:
            case R.id.image_view_close_sticker:
                setVisibleSave();
                onBackPressed();
                return;
            case R.id.imageViewSaveFilter:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_filter_layout.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveLayer:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constrant_layout_change_Layout.setVisibility(View.GONE);
                setVisibleSave();
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.queShotGridView.setLocked(true);
                this.queShotGridView.setTouchEnable(true);
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveText:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraintLayoutAddText.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.GONE);
                this.queShotGridView.setHandlingSticker(null);
                this.queShotGridView.setLocked(true);
                this.relativeLayoutAddText.setVisibility(View.GONE);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSavebackground:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_change_background.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                setVisibleSave();
                this.queShotGridView.setLocked(true);
                this.queShotGridView.setTouchEnable(true);
                if (this.queShotGridView.getBackgroundResourceMode() == 0) {
                    this.currentBackgroundState.isColor = true;
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.drawableId = ((ColorDrawable) this.queShotGridView.getBackground()).getColor();
                    this.currentBackgroundState.drawable = null;
                } else if (this.queShotGridView.getBackgroundResourceMode() == 1) {
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                } else {
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.isBitmap = true;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                }
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.image_view_save_sticker:
                setGuideLineTools();
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraint_layout_sticker.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                this.queShotGridView.setHandlingSticker(null);
                this.seekbarSticker.setVisibility(View.GONE);
                this.imageViewAddSticker.setVisibility(View.GONE);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                setVisibleSave();
                this.queShotGridView.setLocked(true);
                this.queShotGridView.setTouchEnable(true);
                this.moduleToolsId = Module.NONE;
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                setVisibleSave();
                return;
            default:
                return;
        }
    }

    public static PolishCollageActivity getQueShotGridActivityInstance() {
        return QueShotGridActivityInstance;
    }

    @Override
    public void isPermissionGranted(boolean z, String str) {
        if (z) {
            Bitmap createBitmap = SaveFileUtils.createBitmap(this.queShotGridView, 1920);
            Bitmap createBitmap2 = this.queShotGridView.createBitmap();
            new SaveCollageAsFile().execute(createBitmap, createBitmap2);
        }
    }

    public void setBackgroundColor() {
        this.recycler_view_color.scrollToPosition(0);
        ((CollageColorAdapter) this.recycler_view_color.getAdapter()).setSelectedIndex(-1);
        this.recycler_view_color.getAdapter().notifyDataSetChanged();
        this.recycler_view_color.setVisibility(View.VISIBLE);
        this.recycler_view_gradient.setVisibility(View.GONE);
        this.recycler_view_blur.setVisibility(View.GONE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.viewColor.setVisibility(View.VISIBLE);
        this.viewGradient.setVisibility(View.INVISIBLE);
        this.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void setBackgroundGradient() {
        this.recycler_view_gradient.scrollToPosition(0);
        ((CollageBackgroundAdapter) this.recycler_view_gradient.getAdapter()).setSelectedIndex(-1);
        this.recycler_view_gradient.getAdapter().notifyDataSetChanged();
        this.recycler_view_color.setVisibility(View.GONE);
        this.recycler_view_gradient.setVisibility(View.VISIBLE);
        this.recycler_view_blur.setVisibility(View.GONE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.viewColor.setVisibility(View.INVISIBLE);
        this.viewGradient.setVisibility(View.VISIBLE);
        this.viewBlur.setVisibility(View.INVISIBLE);
    }

    public void selectBackgroundBlur() {
        ArrayList arrayList = new ArrayList();
        for (PolishGrid drawable : this.queShotGridView.getQueShotGrids()) {
            arrayList.add(drawable.getDrawable());
        }
        CollageBackgroundAdapter backgroundGridAdapter = new CollageBackgroundAdapter(getApplicationContext(), this, arrayList);
        backgroundGridAdapter.setSelectedIndex(-1);
        this.recycler_view_blur.setAdapter(backgroundGridAdapter);
        this.recycler_view_color.setVisibility(View.GONE);
        this.recycler_view_gradient.setVisibility(View.GONE);
        this.recycler_view_blur.setVisibility(View.VISIBLE);
        this.textViewListColor.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListGradient.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListBlur.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.viewColor.setVisibility(View.INVISIBLE);
        this.viewGradient.setVisibility(View.INVISIBLE);
        this.viewBlur.setVisibility(View.VISIBLE);
    }

    public void setLayer() {
        this.recycler_view_collage.setVisibility(View.VISIBLE);
        this.recycler_view_ratio.setVisibility(View.GONE);
        this.linearLayoutBorder.setVisibility(View.GONE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.viewCollage.setVisibility(View.VISIBLE);
        this.viewBorder.setVisibility(View.INVISIBLE);
        this.viewRatio.setVisibility(View.INVISIBLE);
    }

    public void setBorder() {
        this.recycler_view_collage.setVisibility(View.GONE);
        this.recycler_view_ratio.setVisibility(View.GONE);
        this.linearLayoutBorder.setVisibility(View.VISIBLE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.viewCollage.setVisibility(View.INVISIBLE);
        this.viewBorder.setVisibility(View.VISIBLE);
        this.viewRatio.setVisibility(View.INVISIBLE);
        this.seekBarPadding.setProgress((int) this.queShotGridView.getCollagePadding());
        this.seekBarRadius.setProgress((int) this.queShotGridView.getCollageRadian());
    }

    public void setRatio() {
        this.recycler_view_collage.setVisibility(View.GONE);
        this.recycler_view_ratio.setVisibility(View.VISIBLE);
        this.linearLayoutBorder.setVisibility(View.GONE);
        this.textViewListLayer.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListBorder.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.textViewListRatio.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.viewCollage.setVisibility(View.INVISIBLE);
        this.viewBorder.setVisibility(View.INVISIBLE);
        this.viewRatio.setVisibility(View.VISIBLE);
    }

    @Override
    public void onToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case LAYER:
                setLayer();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case PADDING:
                setBorder();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case RATIO:
                setRatio();
                setGuideLine();
                this.constrant_layout_change_Layout.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotLayout = this.queShotGridView.getQueShotLayout();
                this.aspectRatio = this.queShotGridView.getAspectRatio();
                this.BorderRadius = this.queShotGridView.getCollageRadian();
                this.Padding = this.queShotGridView.getCollagePadding();
                this.recycler_view_collage.scrollToPosition(0);
                ((GridAdapter) this.recycler_view_collage.getAdapter()).setSelectedIndex(-1);
                this.recycler_view_collage.getAdapter().notifyDataSetChanged();
                this.recycler_view_ratio.scrollToPosition(0);
                ((AspectAdapter) this.recycler_view_ratio.getAdapter()).setLastSelectedView(-1);
                this.recycler_view_ratio.getAdapter().notifyDataSetChanged();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                return;
            case FILTER:
                if (this.drawableList.isEmpty()) {
                    for (PolishGrid drawable : this.queShotGridView.getQueShotGrids()) {
                        this.drawableList.add(drawable.getDrawable());
                    }
                }
                new allFilters().execute(new Void[0]);
                setGoneSave();
                return;
            case TEXT:
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                setGuideLine();
                this.queShotGridView.setLocked(false);
                openTextFragment();
                this.constraintLayoutAddText.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.VISIBLE);
                this.relativeLayoutAddText.setVisibility(View.VISIBLE);
                return;
            case STICKER:
                setGuideLine();
                this.constraint_layout_sticker.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.VISIBLE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotGridView.updateLayout(this.queShotLayout);
                this.queShotGridView.setCollagePadding(this.Padding);
                this.queShotGridView.setCollageRadian(this.BorderRadius);
                getWindowManager().getDefaultDisplay().getSize(new Point());
                onNewAspectRatioSelected(this.aspectRatio);
                this.queShotGridView.setAspectRatio(this.aspectRatio);
                for (int i = 0; i < this.drawableList.size(); i++) {
                    this.queShotGridView.getQueShotGrids().get(i).setDrawable(this.drawableList.get(i));
                }
                this.queShotGridView.invalidate();
                if (this.currentBackgroundState.isColor) {
                    this.queShotGridView.setBackgroundResourceMode(0);
                    this.queShotGridView.setBackgroundColor(this.currentBackgroundState.drawableId);
                } else {
                    this.queShotGridView.setBackgroundResourceMode(1);
                    if (this.currentBackgroundState.drawable != null) {
                        this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.queShotGridView.setBackgroundResource(this.currentBackgroundState.drawableId);
                    }
                }
                setGoneSave();
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                return;
            case GRADIENT:
                setGuideLine();
                this.constraint_layout_change_background.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.recyclerViewToolsCollage.setVisibility(View.GONE);
                this.queShotGridView.setLocked(false);
                this.queShotGridView.setTouchEnable(false);
                setGoneSave();
                setBackgroundColor();
                if (this.queShotGridView.getBackgroundResourceMode() == 0) {
                    this.currentBackgroundState.isColor = true;
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.drawableId = ((ColorDrawable) this.queShotGridView.getBackground()).getColor();
                    return;
                } else if (this.queShotGridView.getBackgroundResourceMode() == 2 || (this.queShotGridView.getBackground() instanceof ColorDrawable)) {
                    this.currentBackgroundState.isBitmap = true;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                    return;
                } else if (this.queShotGridView.getBackground() instanceof GradientDrawable) {
                    this.currentBackgroundState.isBitmap = false;
                    this.currentBackgroundState.isColor = false;
                    this.currentBackgroundState.drawable = this.queShotGridView.getBackground();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void GridViewClick() {
        final int i;
        final ArrayList arrayList = new ArrayList();
        if (this.stringList.size() > this.queShotLayout.getAreaCount()) {
            i = this.queShotLayout.getAreaCount();
        } else {
            i = this.stringList.size();
        }
        for (int i2 = 0; i2 < i; i2++) {
            Target r4 = new Target() {

                @Override
                public void onBitmapFailed(Exception exc, Drawable drawable) {
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {
                }

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    float f = (float) bitmap.getWidth();
                    float height = (float) bitmap.getHeight();
                    float max = Math.max(f / f, height / f);
                    if (max > 1.0f) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (f / max), (int) (height / max), false);
                    }
                    arrayList.add(bitmap);
                    if (arrayList.size() == i) {
                        if (PolishCollageActivity.this.stringList.size() < PolishCollageActivity.this.queShotLayout.getAreaCount()) {
                            for (int i = 0; i < PolishCollageActivity.this.queShotLayout.getAreaCount(); i++) {
                                PolishCollageActivity.this.queShotGridView.addQuShotCollage((Bitmap) arrayList.get(i % i));
                            }
                        } else {
                            PolishCollageActivity.this.queShotGridView.addPieces(arrayList);
                        }
                    }
                    PolishCollageActivity.this.targets.remove(this);
                }
            };
            Picasso picasso = Picasso.get();
            RequestCreator load = picasso.load("file:///" + this.stringList.get(i2));
            int i3 = this.deviceWidth;
            load.resize(i3, i3).centerInside().config(Bitmap.Config.RGB_565).into(r4);
            this.targets.add(r4);
        }
    }

    private void setOnBackPressDialog() {
        Dialog dialogOnBackPressed = new Dialog(this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(1);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        this.textViewCancel = (TextView) dialogOnBackPressed.findViewById(R.id.textViewCancel);
        TextView textView = (TextView) dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        this.textViewDiscard = textView;
        textView.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishCollageActivity.this.CancleClick(dialogOnBackPressed, view);
            }
        });
        this.textViewCancel.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                dialogOnBackPressed.dismiss();
            }
        });
    }

    public void CancleClick(Dialog dialogOnBackPressed, View view) {
        dialogOnBackPressed.dismiss();
        this.moduleToolsId = null;
        finish();
        finish();
    }

    public void setGuideLineTools() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_collage_layout);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 1, this.constraint_layout_collage_layout.getId(), 1, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 4, this.guidelineTools.getId(), 3, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 2, this.constraint_layout_collage_layout.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_collage_layout);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_collage_layout);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 1, this.constraint_layout_collage_layout.getId(), 1, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.constraint_layout_wrapper_collage_view.getId(), 2, this.constraint_layout_collage_layout.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_collage_layout);
    }

    public void setGoneSave() {
        this.constraint_save_control.setVisibility(View.GONE);
    }

    public void setVisibleSave() {
        this.constraint_save_control.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (this.moduleToolsId == null) {
            super.onBackPressed();
            return;
        }
        try {
            switch (this.moduleToolsId) {
                case LAYER:
                case PADDING:
                case RATIO:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constrant_layout_change_Layout.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    setVisibleSave();
                    this.queShotGridView.updateLayout(this.queShotLayout);
                    this.queShotGridView.setCollagePadding(this.Padding);
                    this.queShotGridView.setCollageRadian(this.BorderRadius);
                    this.moduleToolsId = Module.NONE;
                    getWindowManager().getDefaultDisplay().getSize(new Point());
                    onNewAspectRatioSelected(this.aspectRatio);
                    this.queShotGridView.setAspectRatio(this.aspectRatio);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    return;
                case FILTER:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_filter_layout.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    for (int i = 0; i < this.drawableList.size(); i++) {
                        this.queShotGridView.getQueShotGrids().get(i).setDrawable(this.drawableList.get(i));
                    }
                    this.queShotGridView.invalidate();
                    setVisibleSave();
                    this.moduleToolsId = Module.NONE;
                    return;
                case TEXT:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraintLayoutAddText.setVisibility(View.GONE);
                    this.constraintLayoutSaveText.setVisibility(View.GONE);
                    if (!this.queShotGridView.getStickers().isEmpty()) {
                        this.queShotGridView.getStickers().clear();
                        this.queShotGridView.setHandlingSticker(null);
                    }
                    this.moduleToolsId = Module.NONE;
                    this.relativeLayoutAddText.setVisibility(View.GONE);
                    this.queShotGridView.setHandlingSticker(null);
                    setVisibleSave();
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    return;
                case STICKER:
                    setGuideLineTools();
                    this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                    if (this.queShotGridView.getStickers().size() <= 0) {
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.imageViewAddSticker.setVisibility(View.GONE);
                        this.queShotGridView.setHandlingSticker(null);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                        this.constraint_layout_sticker.setVisibility(View.GONE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.queShotGridView.setLocked(true);
                        this.moduleToolsId = Module.NONE;
                    } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                        this.queShotGridView.getStickers().clear();
                        this.imageViewAddSticker.setVisibility(View.GONE);
                        this.queShotGridView.setHandlingSticker(null);
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                        this.constraint_layout_sticker.setVisibility(View.GONE);
                        this.queShotGridView.setLocked(true);
                        this.queShotGridView.setTouchEnable(true);
                        this.moduleToolsId = Module.NONE;
                    } else {
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                        this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                        this.recyclerViewTools.setVisibility(View.VISIBLE);
                    }
                    this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_sticker.setVisibility(View.GONE);
                    setVisibleSave();
                    return;
                case GRADIENT:
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.constraint_layout_change_background.setVisibility(View.GONE);
                    this.recyclerViewToolsCollage.setVisibility(View.VISIBLE);
                    this.queShotGridView.setLocked(true);
                    this.queShotGridView.setTouchEnable(true);
                    if (this.currentBackgroundState.isColor) {
                        this.queShotGridView.setBackgroundResourceMode(0);
                        this.queShotGridView.setBackgroundColor(this.currentBackgroundState.drawableId);
                    } else if (this.currentBackgroundState.isBitmap) {
                        this.queShotGridView.setBackgroundResourceMode(2);
                        this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                    } else {
                        this.queShotGridView.setBackgroundResourceMode(1);
                        if (this.currentBackgroundState.drawable != null) {
                            this.queShotGridView.setBackground(this.currentBackgroundState.drawable);
                        } else {
                            this.queShotGridView.setBackgroundResource(this.currentBackgroundState.drawableId);
                        }
                    }
                    setVisibleSave();
                    this.moduleToolsId = Module.NONE;
                    return;
                case COLLAGE:
                    setVisibleSave();
                    setGuideLineTools();
                    this.recyclerViewTools.setVisibility(View.VISIBLE);
                    this.recyclerViewToolsCollage.setVisibility(View.GONE);
                    this.moduleToolsId = Module.NONE;
                    this.queShotGridView.setQueShotGrid(null);
                    this.queShotGridView.setPrevHandlingQueShotGrid(null);
                    this.queShotGridView.invalidate();
                    this.moduleToolsId = Module.NONE;
                    return;
                case NONE:
                    setOnBackPressDialog();
                    return;
                default:
                    super.onBackPressed();
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(PolishLayout puzzleLayout2, int i) {
        PolishLayout parse = PolishLayoutParser.parse(puzzleLayout2.generateInfo());
        puzzleLayout2.setRadian(this.queShotGridView.getCollageRadian());
        puzzleLayout2.setPadding(this.queShotGridView.getCollagePadding());
        this.queShotGridView.updateLayout(parse);
    }

    @Override
    public void onNewAspectRatioSelected(AspectRatio aspectRatio2) {
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int[] calculateWidthAndHeight = calculateWidthAndHeight(aspectRatio2, point);
        this.queShotGridView.setLayoutParams(new ConstraintLayout.LayoutParams(calculateWidthAndHeight[0], calculateWidthAndHeight[1]));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraint_layout_wrapper_collage_view);
        constraintSet.connect(this.queShotGridView.getId(), 3, this.constraint_layout_wrapper_collage_view.getId(), 3, 0);
        constraintSet.connect(this.queShotGridView.getId(), 1, this.constraint_layout_wrapper_collage_view.getId(), 1, 0);
        constraintSet.connect(this.queShotGridView.getId(), 4, this.constraint_layout_wrapper_collage_view.getId(), 4, 0);
        constraintSet.connect(this.queShotGridView.getId(), 2, this.constraint_layout_wrapper_collage_view.getId(), 2, 0);
        constraintSet.applyTo(this.constraint_layout_wrapper_collage_view);
        this.queShotGridView.setAspectRatio(aspectRatio2);
    }

    public void replaceCurrentPiece(String str) {
        new OnLoadBitmapFromUri().execute(str);
    }

    private int[] calculateWidthAndHeight(AspectRatio aspectRatio2, Point point) {
        int height = this.constraint_layout_wrapper_collage_view.getHeight();
        if (aspectRatio2.getHeight() > aspectRatio2.getWidth()) {
            int ratio = (int) (aspectRatio2.getRatio() * ((float) height));
            if (ratio < point.x) {
                return new int[]{ratio, height};
            }
            return new int[]{point.x, (int) (((float) point.x) / aspectRatio2.getRatio())};
        }
        int ratio2 = (int) (((float) point.x) / aspectRatio2.getRatio());
        if (ratio2 > height) {
            return new int[]{(int) (((float) height) * aspectRatio2.getRatio()), height};
        }
        return new int[]{point.x, ratio2};
    }

    @Override
    public void addSticker(int item, Bitmap bitmap) {
        this.queShotGridView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
        return;

    }

    @Override
    public void onBackgroundSelected(int item, final CollageBackgroundAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.queShotGridView.setBackgroundColor(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(0);
        } else if (squareView.drawable != null) {
            this.queShotGridView.setBackgroundResourceMode(2);
            new AsyncTask<Void, Bitmap, Bitmap>() {

                public Bitmap doInBackground(Void... voidArr) {
                    return FilterUtils.getBlurImageFromBitmap(((BitmapDrawable) squareView.drawable).getBitmap(), 5.0f);
                }

                public void onPostExecute(Bitmap bitmap) {
                    PolishCollageActivity.this.queShotGridView.setBackground(new BitmapDrawable(PolishCollageActivity.this.getResources(), bitmap));
                }
            }.execute(new Void[0]);
        } else {
            this.queShotGridView.setBackgroundResource(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(1);
        }
    }

    @Override
    public void onBackgroundColorSelected(int item, CollageColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            this.queShotGridView.setBackgroundColor(squareView.drawableId);
            this.queShotGridView.setBackgroundResourceMode(0);
        }
    }

    @Override
    public void onFilterSelected(int item, String str) {
        new LoadBitmapWithFilter().execute(str);
        return;
    }

    @Override
    public void finishCrop(Bitmap bitmap) {
        this.queShotGridView.replace(bitmap, "");
    }

    @Override
    public void onSaveFilter(Bitmap bitmap) {
        this.queShotGridView.replace(bitmap, "");
    }

    @Override
    public void onPieceFuncSelected(Module toolType) {
        switch (toolType) {
            case FILTER:
                new LoadFilterBitmapForCurrentPiece().execute(new Void[0]);
                return;
            case TEXT:
            case STICKER:
            case GRADIENT:
            case COLLAGE:
            case NONE:
            default:
                return;
            case REPLACE:
                PolishPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).setForwardMain(true).start(this);
                return;
            case H_FLIP:
                this.queShotGridView.flipHorizontally();
                return;
            case V_FLIP:
                this.queShotGridView.flipVertically();
                return;
            case ROTATE:
                this.queShotGridView.rotate(90.0f);
                return;
            case CROP:
                CropFragment.show(this, this, ((BitmapDrawable) this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap());
                return;
        }
    }

    class allFilters extends AsyncTask<Void, Void, Void> {
        allFilters() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public Void doInBackground(Void... voidArr) {
            PolishCollageActivity.this.listFilterAll.clear();
            PolishCollageActivity.this.listFilterAll.addAll(FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(((BitmapDrawable) PolishCollageActivity.this.queShotGridView.getQueShotGrids().get(0).getDrawable()).getBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voidR) {
            RecyclerView recyclerView = PolishCollageActivity.this.recyclerViewFilter;
            ArrayList arrayList = PolishCollageActivity.this.listFilterAll;
            PolishCollageActivity polishCollageActivity = PolishCollageActivity.this;
            recyclerView.setAdapter(new FilterAdapter(arrayList, polishCollageActivity, polishCollageActivity.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PolishCollageActivity.this.recyclerViewToolsCollage.setVisibility(View.GONE);
            PolishCollageActivity.this.queShotGridView.setLocked(false);
            PolishCollageActivity.this.queShotGridView.setTouchEnable(false);
            PolishCollageActivity.this.setGuideLine();
            PolishCollageActivity.this.constraint_layout_filter_layout.setVisibility(View.VISIBLE);
            PolishCollageActivity.this.recyclerViewTools.setVisibility(View.GONE);
            PolishCollageActivity.this.setLoading(false);
        }
    }

    class LoadFilterBitmapForCurrentPiece extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        LoadFilterBitmapForCurrentPiece() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voidArr) {
            return FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(((BitmapDrawable) PolishCollageActivity.this.queShotGridView.getQueShotGrid().getDrawable()).getBitmap(), 100, 100));
        }

        public void onPostExecute(List<Bitmap> list) {
            PolishCollageActivity.this.setLoading(false);
            if (PolishCollageActivity.this.queShotGridView.getQueShotGrid() != null) {
                PolishCollageActivity polishCollageActivity = PolishCollageActivity.this;
                FilterFragment.show(polishCollageActivity, polishCollageActivity, ((BitmapDrawable) polishCollageActivity.queShotGridView.getQueShotGrid().getDrawable()).getBitmap(), list);
            }
        }
    }

    class LoadBitmapWithFilter extends AsyncTask<String, List<Bitmap>, List<Bitmap>> {
        LoadBitmapWithFilter() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public List<Bitmap> doInBackground(String... strArr) {
            ArrayList arrayList = new ArrayList();
            Iterator<Drawable> it = PolishCollageActivity.this.drawableList.iterator();
            while (it.hasNext()) {
                arrayList.add(FilterUtils.getBitmapWithFilter(((BitmapDrawable) it.next()).getBitmap(), strArr[0]));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            for (int i = 0; i < list.size(); i++) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(PolishCollageActivity.this.getResources(), list.get(i));
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);
                PolishCollageActivity.this.queShotGridView.getQueShotGrids().get(i).setDrawable(bitmapDrawable);
            }
            PolishCollageActivity.this.queShotGridView.invalidate();
            PolishCollageActivity.this.setLoading(false);
        }
    }

    public class OnLoadBitmapFromUri extends AsyncTask<String, Bitmap, Bitmap> {
        OnLoadBitmapFromUri() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public Bitmap doInBackground(String... strArr) {
            try {
                Uri fromFile = Uri.fromFile(new File(strArr[0]));
                Bitmap rotateBitmap = SystemUtil.rotateBitmap(MediaStore.Images.Media.getBitmap(PolishCollageActivity.this.getContentResolver(), fromFile), new ExifInterface(PolishCollageActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                float width = (float) rotateBitmap.getWidth();
                float height = (float) rotateBitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                return max > 1.0f ? Bitmap.createScaledBitmap(rotateBitmap, (int) (width / max), (int) (height / max), false) : rotateBitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishCollageActivity.this.setLoading(false);
            PolishCollageActivity.this.queShotGridView.replace(bitmap, "");
        }
    }

    public class SaveCollageAsFile extends AsyncTask<Bitmap, String, String> {
        SaveCollageAsFile() {
        }

        public void onPreExecute() {
            PolishCollageActivity.this.setLoading(true);
        }

        public String doInBackground(Bitmap... bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            Bitmap bitmap2 = bitmapArr[1];
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Rect rect = null;
            canvas.drawBitmap(bitmap, rect, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), (Paint) null);
            canvas.drawBitmap(bitmap2, rect, new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), (Paint) null);
            bitmap.recycle();
            bitmap2.recycle();
            try {
                String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                File image = SaveFileUtils.saveBitmapFileCollage(PolishCollageActivity.this, createBitmap, format);
                createBitmap.recycle();
                return image.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String str) {
            PolishCollageActivity.this.setLoading(false);
            MediaScannerConnection.scanFile(PolishCollageActivity.this, new String[]{str}, null, null);
            Intent intent = new Intent(PolishCollageActivity.this, PhotoShareActivity.class);
            intent.putExtra("path", str);
            PolishCollageActivity.this.startActivity(intent);
        }
    }

    public void setLoading(boolean z) {
        if (z) {
            getWindow().setFlags(ANIMATION_CHANGED, ANIMATION_CHANGED);
            this.relativeLayoutLoading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(ANIMATION_CHANGED);
        this.relativeLayoutLoading.setVisibility(View.GONE);
    }

    public void displayInterstitial() {
        AdUtils.showAds_full(this, new AdUtils.OnFinishAds() {
            public void onFinishAds(boolean z) {
                if (z) {

                }
                passActivity();
            }
        });
    }

    private void passActivity() {
        SaveView();
    }

    private void checkData() {
        if (NetworkHelper.isConnectedWifi(this) || (NetworkHelper.isConnectedMobile(this))) {
            Helper.generalRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    PolishCollageActivity.this.generalModel = (General) snapshot.getValue(General.class);
                    PolishCollageActivity.this.recyclerViewTools.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }
}
