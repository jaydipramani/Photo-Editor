package com.relish.app.Activity;

import static android.view.WindowManager.LayoutParams.ANIMATION_CHANGED;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
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
import com.relish.app.Adapter.AdjustAdapter;
import com.relish.app.Adapter.ColorAdapter;
import com.relish.app.Adapter.FilterAdapter;
import com.relish.app.Adapter.MagicBrushAdapter;
import com.relish.app.Adapter.OverlayAdapter;
import com.relish.app.Adapter.PolishDrawToolsAdapter;
import com.relish.app.Adapter.PolishEffectToolsAdapter;
import com.relish.app.Adapter.PolishSQToolsAdapter;
import com.relish.app.Adapter.PolishToolsAdapter;
import com.relish.app.Adapter.RecyclerTabLayout;
import com.relish.app.Adapter.StickerAdapter;
import com.relish.app.Adapter.StickersTabAdapter;
import com.relish.app.Fragment.BlurSquareBgFragment;
import com.relish.app.Fragment.ColoredFragment;
import com.relish.app.Fragment.CropFragment;
import com.relish.app.Fragment.FrameFragment;
import com.relish.app.Fragment.HSlFragment;
import com.relish.app.Fragment.MirrorFragment;
import com.relish.app.Fragment.MosaicFragment;
import com.relish.app.Fragment.RatioFragment;
import com.relish.app.Fragment.SaturationSquareBackgroundFragment;
import com.relish.app.Fragment.SaturationSquareFragment;
import com.relish.app.Fragment.SketchSquareBackgroundFragment;
import com.relish.app.Fragment.SketchSquareFragment;
import com.relish.app.Fragment.TextFragment;
import com.relish.app.R;
import com.relish.app.Utils.BitmapTransfer;
import com.relish.app.Utils.DegreeSeekBar;
import com.relish.app.Utils.FilterUtils;
import com.relish.app.Utils.Helper;
import com.relish.app.Utils.NetworkHelper;
import com.relish.app.Utils.SaveFileUtils;
import com.relish.app.Utils.SystemUtil;
import com.relish.app.assets.FilterFileAsset;
import com.relish.app.assets.OverlayFileAsset;
import com.relish.app.assets.StickerFileAsset;
import com.relish.app.constants.StoreManager;
import com.relish.app.draw.DrawModel;
import com.relish.app.draw.Drawing;
import com.relish.app.draw.OnSaveBitmap;
import com.relish.app.event.AlignHorizontallyEvent;
import com.relish.app.event.DeleteIconEvent;
import com.relish.app.event.EditTextIconEvent;
import com.relish.app.event.FlipHorizontallyEvent;
import com.relish.app.event.ZoomIconEvent;
import com.relish.app.keyboardHeight.KeyboardHeightProvider;
import com.relish.app.layout.ArtLayout;
import com.relish.app.layout.BlurLayout;
import com.relish.app.layout.DripLayout;
import com.relish.app.layout.MotionLayout;
import com.relish.app.layout.NeonLayout;
import com.relish.app.layout.PixLabLayout;
import com.relish.app.layout.SplashLayout;
import com.relish.app.layout.WingLayout;
import com.relish.app.listener.AdjustListener;
import com.relish.app.listener.BrushColorListener;
import com.relish.app.listener.BrushMagicListener;
import com.relish.app.listener.FilterListener;
import com.relish.app.listener.OnPolishEditorListener;
import com.relish.app.listener.OverlayListener;
import com.relish.app.model.General;
import com.relish.app.module.Module;
import com.relish.app.picker.PermissionsUtils;
import com.relish.app.polish.PolishEditor;
import com.relish.app.polish.PolishPickerView;
import com.relish.app.polish.PolishStickerIcons;
import com.relish.app.polish.PolishStickerView;
import com.relish.app.polish.PolishText;
import com.relish.app.polish.PolishTextView;
import com.relish.app.polish.PolishView;
import com.relish.app.preference.Preference;
import com.relish.app.sticker.DrawableSticker;
import com.relish.app.sticker.Sticker;

import org.wysaid.myUtils.MsgUtil;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PolishEditorActivity extends PolishBaseActivity implements OnPolishEditorListener, View.OnClickListener, StickerAdapter.OnClickSplashListener, BlurSquareBgFragment.BlurSquareBgListener, CropFragment.OnCropPhoto, BrushColorListener, SaturationSquareBackgroundFragment.SplashSaturationBackgrundListener, RatioFragment.RatioSaveListener, FrameFragment.RatioSaveListener, SketchSquareBackgroundFragment.SketchBackgroundListener, SaturationSquareFragment.SplashSaturationListener, MosaicFragment.MosaicListener, ColoredFragment.ColoredListener, SketchSquareFragment.SketchListener, PolishToolsAdapter.OnQuShotItemSelected, PolishDrawToolsAdapter.OnQuShotDrawItemSelected, PolishEffectToolsAdapter.OnQuShotEffectItemSelected, PolishSQToolsAdapter.OnQuShotSQItemSelected, FilterListener, AdjustListener, OverlayListener, BrushMagicListener, HSlFragment.OnFilterSavePhoto {
    private static final String TAG = "PolishEditorActivity";
    private DegreeSeekBar adjustFilter;
    private DegreeSeekBar adjustSeekBar;
    public ColorAdapter colorAdapter;
    private ConstraintLayout constraintLayoutAddText;
    private ConstraintLayout constraintLayoutAdjust;
    private ConstraintLayout constraintLayoutBurn;
    private ConstraintLayout constraintLayoutConfirmCompareBurn;
    private ConstraintLayout constraintLayoutConfirmCompareDivide;
    private ConstraintLayout constraintLayoutConfirmCompareDodge;
    private ConstraintLayout constraintLayoutConfirmCompareHardmix;
    private ConstraintLayout constraintLayoutConfirmCompareOverlay;
    private ConstraintLayout constraintLayoutDivide;
    private ConstraintLayout constraintLayoutDodge;
    private ConstraintLayout constraintLayoutDraw;
    private ConstraintLayout constraintLayoutEffects;
    public ConstraintLayout constraintLayoutFilter;
    private ConstraintLayout constraintLayoutMagic;
    private ConstraintLayout constraintLayoutMagicTool;
    private ConstraintLayout constraintLayoutNeon;
    private ConstraintLayout constraintLayoutNeonTool;
    private ConstraintLayout constraintLayoutOverlay;
    private ConstraintLayout constraintLayoutPaint;
    private ConstraintLayout constraintLayoutPaintTool;
    private ConstraintLayout constraintLayoutSave;
    private ConstraintLayout constraintLayoutSaveOverlay;
    private ConstraintLayout constraintLayoutSaveSticker;
    private ConstraintLayout constraintLayoutSaveText;
    private ConstraintLayout constraintLayoutSplash;
    private ConstraintLayout constraintLayoutSticker;
    private ConstraintLayout constraintLayoutView;
    private General generalModel;
    private Guideline guideline;
    private Guideline guidelinePaint;
    public ImageView imageViewAddSticker;
    private ImageView imageViewBrushEraser;
    private ImageView imageViewBrushOpacity;
    private ImageView imageViewBrushSize;
    private ImageView imageViewCleanMagic;
    private ImageView imageViewCleanNeon;
    private ImageView imageViewCleanPaint;
    private ImageView imageViewCompareAdjust;
    public ImageView imageViewCompareFilter;
    public ImageView imageViewCompareOverlay;
    private ImageView imageViewMagicEraser;
    private ImageView imageViewMagicOpacity;
    private ImageView imageViewMagicSize;
    private ImageView imageViewNeonEraser;
    private ImageView imageViewNeonSize;
    private ImageView imageViewRedoMagic;
    private ImageView imageViewRedoNeon;
    private ImageView imageViewRedoPaint;
    private ImageView imageViewUndoMagic;
    private ImageView imageViewUndoNeon;
    private ImageView imageViewUndoPaint;
    public ImageView image_view_exit;
    private KeyboardHeightProvider keyboardProvider;
    private LinearLayout linearLayoutBurn;
    private LinearLayout linearLayoutColor;
    private LinearLayout linearLayoutDodge;
    private LinearLayout linearLayoutHardmix;
    private LinearLayout linearLayoutHue;
    private LinearLayout linearLayoutOverlay;
    public LinearLayout linear_layout_wrapper_sticker_list;
    public ArrayList listBurnEffect = new ArrayList();
    public ArrayList listColorEffect = new ArrayList();
    public ArrayList listDodgeEffect = new ArrayList();
    public ArrayList listFilter = new ArrayList();
    public ArrayList listHardMixEffect = new ArrayList();
    public ArrayList listHueEffect = new ArrayList();
    public ArrayList listOverlayEffect = new ArrayList();
    boolean forwardEffects;
    public CGENativeLibrary.LoadImageCallback loadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        @Override
        public Bitmap loadImage(String string, Object object) {
            try {
                return BitmapFactory.decodeStream(PolishEditorActivity.this.getAssets().open(string));
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        public void loadImageOK(Bitmap bitmap, Object object) {
            bitmap.recycle();
        }
    };
    public AdjustAdapter mAdjustAdapter;
    private final PolishDrawToolsAdapter mEditingDrawToolsAdapter = new PolishDrawToolsAdapter(this);
    private final PolishEffectToolsAdapter mEditingEffectToolsAdapter = new PolishEffectToolsAdapter(this);
    private final PolishSQToolsAdapter mEditingSplashToolsAdapter = new PolishSQToolsAdapter(this);
    private final PolishToolsAdapter mEditingToolsAdapter = new PolishToolsAdapter(this);
    public Module moduleToolsId = Module.NONE;
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        public final boolean onTouch(View view, MotionEvent motionEvent) {
            return PolishEditorActivity.this.onTouchView(view, motionEvent);
        }
    };
    public PolishEditor polishEditor;
    PolishStickerIcons polishStickerIconAlign;
    PolishStickerIcons polishStickerIconClose;
    PolishStickerIcons polishStickerIconEdit;
    PolishStickerIcons polishStickerIconFlip;
    PolishStickerIcons polishStickerIconRotate;
    PolishStickerIcons polishStickerIconScale;
    public PolishView polishView;
    private RecyclerView recyclerViewAdjust;
    public RecyclerView recyclerViewDraw;
    public RecyclerView recyclerViewEffect;
    public RecyclerView recyclerViewFilter;
    private RecyclerView recyclerViewMagicListColor;
    private RecyclerView recyclerViewNeonListColor;
    private RecyclerView recyclerViewPaintListColor;
    public RecyclerView recyclerViewSpalsh;
    public RecyclerView recyclerViewTools;
    public RecyclerView recycler_view_burn_effect;
    public RecyclerView recycler_view_color_effect;
    public RecyclerView recycler_view_dodge_effect;
    public RecyclerView recycler_view_hardmix_effet;
    public RecyclerView recycler_view_hue_effect;
    public RecyclerView recycler_view_overlay_effect;
    public ImageView redo;
    private RelativeLayout relativeLayoutAddText;
    private RelativeLayout relativeLayoutLoading;
    private RelativeLayout relativeLayoutMagic;
    private RelativeLayout relativeLayoutNeon;
    private RelativeLayout relativeLayoutPaint;
    private RelativeLayout relativeLayoutWrapper;
    private SeekBar seekBarBrush;
    private SeekBar seekBarEraser;
    private SeekBar seekBarMagicBrush;
    private SeekBar seekBarMagicEraser;
    private SeekBar seekBarMagicOpacity;
    private SeekBar seekBarNeonBrush;
    private SeekBar seekBarNeonEraser;
    private SeekBar seekBarOpacity;
    private SeekBar seekBarOverlay;
    public SeekBar seekbarSticker;
    private Animation slideDownAnimation;
    private Animation slideUpAnimation;
    public TextFragment.TextEditor textEditor;
    public TextFragment textFragment;
    public TextView textViewCancel;
    public TextView textViewDiscard;
    private TextView textViewMagicBrush;
    private TextView textViewMagicEraser;
    private TextView textViewMagicOpacity;
    private TextView textViewNeonBrush;
    private TextView textViewNeonEraser;
    public TextView text_view_save;
    private TextView textViewTitleBrush;
    private TextView textViewTitleMagic;
    private TextView textViewTitleNeon;
    private TextView textViewValueBrush;
    private TextView textViewValueEraser;
    private TextView textViewValueOpacity;
    private TextView text_view_burn;
    private TextView text_view_color;
    private TextView text_view_dodge;
    private TextView text_view_hardmix;
    private TextView text_view_hue;
    private TextView text_view_overlay;
    public ImageView undo;
    public ViewPager viewPagerStickers;
    private View view_burn;
    private View view_color;
    private View view_dodge;
    private View view_hardmix;
    private View view_hue;
    private View view_overlay;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_polish_editor);
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        this.slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        this.slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        initView();
        checkData();
        onClickListener();
        setView();
        setBottomToolbar(false);

        AdUtils.ShowAmBanner(this, (LinearLayout) findViewById(R.id.ll_banneradmidview), (LinearLayout) findViewById(R.id.lnr_ads));

        return;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.keyboardProvider.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.keyboardProvider.onResume();
        if (forwardEffects) {
            viewSlideUp(constraintLayoutEffects);
        }
        checkData();
    }

    private void initView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_loading);
        this.relativeLayoutLoading = relativeLayout;
        relativeLayout.setVisibility(View.VISIBLE);
        PolishView polishView2 = (PolishView) findViewById(R.id.photo_editor_view);
        this.polishView = polishView2;
        polishView2.setVisibility(View.INVISIBLE);
        this.recyclerViewTools = (RecyclerView) findViewById(R.id.recyclerViewTools);
        this.recyclerViewDraw = (RecyclerView) findViewById(R.id.recyclerViewDraw);
        this.recyclerViewSpalsh = (RecyclerView) findViewById(R.id.recyclerViewBlurSqaure);
        this.constraintLayoutEffects = (ConstraintLayout) findViewById(R.id.constraint_layout_effects);
        this.recyclerViewEffect = (RecyclerView) findViewById(R.id.recyclerViewEffect);
        this.recyclerViewFilter = (RecyclerView) findViewById(R.id.recycler_view_filter);
        this.recyclerViewAdjust = (RecyclerView) findViewById(R.id.recyclerViewAdjust);
        this.constraintLayoutView = (ConstraintLayout) findViewById(R.id.constraint_layout_root_view);
        this.constraintLayoutFilter = (ConstraintLayout) findViewById(R.id.constraint_layout_filter);
        this.constraintLayoutAdjust = (ConstraintLayout) findViewById(R.id.constraintLayoutAdjust);
        this.constraintLayoutOverlay = (ConstraintLayout) findViewById(R.id.constraint_layout_overlay);
        this.constraintLayoutConfirmCompareOverlay = (ConstraintLayout) findViewById(R.id.constraintLayoutConfirmCompareOverlay);
        this.constraintLayoutSaveOverlay = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_save_overlay);
        this.linearLayoutOverlay = (LinearLayout) findViewById(R.id.linearLayoutOverlay);
        this.linearLayoutHue = (LinearLayout) findViewById(R.id.linearLayoutHue);
        this.linearLayoutColor = (LinearLayout) findViewById(R.id.linearLayoutColor);
        this.linearLayoutDodge = (LinearLayout) findViewById(R.id.linearLayoutDodge);
        this.linearLayoutBurn = (LinearLayout) findViewById(R.id.linearLayoutBurn);
        this.linearLayoutHardmix = (LinearLayout) findViewById(R.id.linearLayoutHardmix);
        this.text_view_overlay = (TextView) findViewById(R.id.text_view_overlay);
        this.text_view_color = (TextView) findViewById(R.id.text_view_color);
        this.text_view_dodge = (TextView) findViewById(R.id.text_view_dodge);
        this.text_view_hardmix = (TextView) findViewById(R.id.text_view_hardmix);
        this.text_view_hue = (TextView) findViewById(R.id.text_view_hue);
        this.text_view_burn = (TextView) findViewById(R.id.text_view_burn);
        this.recycler_view_overlay_effect = (RecyclerView) findViewById(R.id.recycler_view_overlay_effect);
        this.recycler_view_color_effect = (RecyclerView) findViewById(R.id.recycler_view_color_effect);
        this.recycler_view_dodge_effect = (RecyclerView) findViewById(R.id.recycler_view_dodge_effect);
        this.recycler_view_hardmix_effet = (RecyclerView) findViewById(R.id.recycler_view_hardmix_effet);
        this.recycler_view_hue_effect = (RecyclerView) findViewById(R.id.recycler_view_hue_effect);
        this.recycler_view_burn_effect = (RecyclerView) findViewById(R.id.recycler_view_burn_effect);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.view_overlay = findViewById(R.id.view_overlay);
        this.view_hue = findViewById(R.id.view_hue);
        this.view_color = findViewById(R.id.view_color);
        this.view_dodge = findViewById(R.id.view_dodge);
        this.view_hardmix = findViewById(R.id.view_hardmix);
        this.view_burn = findViewById(R.id.view_burn);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
        this.constraintLayoutSticker = (ConstraintLayout) findViewById(R.id.constraint_layout_sticker);
        this.constraintLayoutAddText = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_text);
        this.viewPagerStickers = (ViewPager) findViewById(R.id.stickerViewpaper);
        this.linear_layout_wrapper_sticker_list = (LinearLayout) findViewById(R.id.linear_layout_wrapper_sticker_list);
        this.guidelinePaint = (Guideline) findViewById(R.id.guidelinePaint);
        this.guideline = (Guideline) findViewById(R.id.guideline);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbarStickerAlpha);
        this.seekbarSticker = seekBar;
        seekBar.setVisibility(View.GONE);
        this.textViewTitleBrush = (TextView) findViewById(R.id.textViewTitleBrush);
        this.imageViewBrushSize = (ImageView) findViewById(R.id.imageViewSizePaint);
        this.constraintLayoutSaveText = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_save_text);
        this.constraintLayoutSaveSticker = (ConstraintLayout) findViewById(R.id.constraint_layout_confirm_save_sticker);
        this.imageViewBrushOpacity = (ImageView) findViewById(R.id.imageViewOpacityPaint);
        this.imageViewBrushEraser = (ImageView) findViewById(R.id.imageViewEraserPaint);
        this.textViewValueBrush = (TextView) findViewById(R.id.seekbarBrushValue);
        this.textViewValueOpacity = (TextView) findViewById(R.id.seekbarOpacityValue);
        this.textViewValueEraser = (TextView) findViewById(R.id.seekbarEraserValue);
        this.seekBarBrush = (SeekBar) findViewById(R.id.seekbarBrushSize);
        this.seekBarOpacity = (SeekBar) findViewById(R.id.seekbarOpacitySize);
        this.seekBarEraser = (SeekBar) findViewById(R.id.seekbarEraserSize);
        this.textViewTitleMagic = (TextView) findViewById(R.id.textViewTitleMagic);
        this.imageViewMagicSize = (ImageView) findViewById(R.id.imageViewSizeMagic);
        this.imageViewMagicOpacity = (ImageView) findViewById(R.id.imageViewOpacityMagic);
        this.imageViewMagicEraser = (ImageView) findViewById(R.id.imageViewEraserMagic);
        this.textViewMagicBrush = (TextView) findViewById(R.id.seekbarBrushMagicValue);
        this.textViewMagicOpacity = (TextView) findViewById(R.id.seekbarOpacityMagicValue);
        this.textViewMagicEraser = (TextView) findViewById(R.id.seekbarEraserMagicValue);
        this.seekBarMagicBrush = (SeekBar) findViewById(R.id.seekbarMagicSize);
        this.seekBarMagicOpacity = (SeekBar) findViewById(R.id.seekbarOpacityMagic);
        this.seekBarMagicEraser = (SeekBar) findViewById(R.id.seekbarEraserMagic);
        this.textViewTitleNeon = (TextView) findViewById(R.id.textViewTitleNeon);
        this.imageViewNeonSize = (ImageView) findViewById(R.id.imageViewSizeNeon);
        this.imageViewNeonEraser = (ImageView) findViewById(R.id.imageViewEraserNeon);
        this.textViewNeonBrush = (TextView) findViewById(R.id.seekbarNeonValue);
        this.textViewNeonEraser = (TextView) findViewById(R.id.seekbarEraserNeon);
        this.seekBarNeonBrush = (SeekBar) findViewById(R.id.seekbarNeonSize);
        this.seekBarNeonEraser = (SeekBar) findViewById(R.id.seekbarNeonEraser);
        this.relativeLayoutPaint = (RelativeLayout) findViewById(R.id.viewPaint);
        this.relativeLayoutNeon = (RelativeLayout) findViewById(R.id.viewNeon);
        this.relativeLayoutMagic = (RelativeLayout) findViewById(R.id.viewMagic);
        this.redo = (ImageView) findViewById(R.id.redo);
        this.undo = (ImageView) findViewById(R.id.undo);
        this.constraintLayoutPaint = (ConstraintLayout) findViewById(R.id.constraintLayoutPaint);
        this.constraintLayoutPaintTool = (ConstraintLayout) findViewById(R.id.constraintLayoutPaintTool);
        this.recyclerViewPaintListColor = (RecyclerView) findViewById(R.id.recyclerViewColorPaint);
        this.recyclerViewMagicListColor = (RecyclerView) findViewById(R.id.recyclerViewColorMagic);
        this.constraintLayoutMagic = (ConstraintLayout) findViewById(R.id.constraintLayoutMagic);
        this.constraintLayoutMagicTool = (ConstraintLayout) findViewById(R.id.constraintLayoutMagicTool);
        this.constraintLayoutNeon = (ConstraintLayout) findViewById(R.id.constraintLayoutNeon);
        this.constraintLayoutNeonTool = (ConstraintLayout) findViewById(R.id.constraintLayoutNeonTool);
        this.recyclerViewNeonListColor = (RecyclerView) findViewById(R.id.recyclerViewColorNeon);
        ImageView imageView = (ImageView) findViewById(R.id.image_view_undo);
        this.imageViewUndoPaint = imageView;
        imageView.setVisibility(View.GONE);
        ImageView imageView2 = (ImageView) findViewById(R.id.image_view_undo_Magic);
        this.imageViewUndoMagic = imageView2;
        imageView2.setVisibility(View.GONE);
        ImageView imageView3 = (ImageView) findViewById(R.id.image_view_redo);
        this.imageViewRedoPaint = imageView3;
        imageView3.setVisibility(View.GONE);
        ImageView imageView4 = (ImageView) findViewById(R.id.image_view_redo_Magic);
        this.imageViewRedoMagic = imageView4;
        imageView4.setVisibility(View.GONE);
        ImageView imageView5 = (ImageView) findViewById(R.id.image_view_clean);
        this.imageViewCleanPaint = imageView5;
        imageView5.setVisibility(View.GONE);
        ImageView imageView6 = (ImageView) findViewById(R.id.image_view_clean_Magic);
        this.imageViewCleanMagic = imageView6;
        imageView6.setVisibility(View.GONE);
        ImageView imageView7 = (ImageView) findViewById(R.id.image_view_clean_neon);
        this.imageViewCleanNeon = imageView7;
        imageView7.setVisibility(View.GONE);
        ImageView imageView8 = (ImageView) findViewById(R.id.image_view_undo_neon);
        this.imageViewUndoNeon = imageView8;
        imageView8.setVisibility(View.GONE);
        ImageView imageView9 = (ImageView) findViewById(R.id.image_view_redo_neon);
        this.imageViewRedoNeon = imageView9;
        imageView9.setVisibility(View.GONE);
        this.relativeLayoutWrapper = (RelativeLayout) findViewById(R.id.relative_layout_wrapper_photo);
        this.text_view_save = findViewById(R.id.text_view_save);
        this.image_view_exit = (ImageView) findViewById(R.id.image_view_exit);
        this.constraintLayoutSave = (ConstraintLayout) findViewById(R.id.constraintLayoutSave);
        this.constraintLayoutDraw = (ConstraintLayout) findViewById(R.id.constraint_layout_draw);
        this.constraintLayoutSplash = (ConstraintLayout) findViewById(R.id.constraint_layout_blur_sqaure);
        ImageView imageView10 = (ImageView) findViewById(R.id.imageViewCompareAdjust);
        this.imageViewCompareAdjust = imageView10;
        imageView10.setOnTouchListener(this.onTouchListener);
        ImageView imageView11 = (ImageView) findViewById(R.id.image_view_compare_filter);
        this.imageViewCompareFilter = imageView11;
        imageView11.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareFilter.setVisibility(View.GONE);
        ImageView imageView12 = (ImageView) findViewById(R.id.image_view_compare_overlay);
        this.imageViewCompareOverlay = imageView12;
        imageView12.setOnTouchListener(this.onTouchListener);
        this.imageViewCompareOverlay.setVisibility(View.GONE);
        this.relativeLayoutAddText = (RelativeLayout) findViewById(R.id.relative_layout_add_text);
    }

    private void setOnBackPressDialog() {
        Dialog dialogOnBackPressed = new Dialog(this, R.style.UploadDialog);
        dialogOnBackPressed.requestWindowFeature(1);
        dialogOnBackPressed.setContentView(R.layout.dialog_exit);
        dialogOnBackPressed.setCancelable(true);
        dialogOnBackPressed.show();
        this.textViewCancel = (TextView) dialogOnBackPressed.findViewById(R.id.textViewCancel);
        this.textViewDiscard = (TextView) dialogOnBackPressed.findViewById(R.id.textViewDiscard);
        this.textViewCancel.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                dialogOnBackPressed.dismiss();
            }
        });
        this.textViewDiscard.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.discardClick(dialogOnBackPressed, view);
            }
        });
    }

    public void discardClick(Dialog dialogOnBackPressed, View view) {
        dialogOnBackPressed.dismiss();
        this.moduleToolsId = null;
        finish();
    }

    private void setView() {
        this.recyclerViewTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewTools.setAdapter(this.mEditingToolsAdapter);
        this.recyclerViewTools.setHasFixedSize(true);
        this.recyclerViewDraw.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewDraw.setAdapter(this.mEditingDrawToolsAdapter);
        this.recyclerViewDraw.setHasFixedSize(true);
        this.recyclerViewSpalsh.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewSpalsh.setAdapter(this.mEditingSplashToolsAdapter);
        this.recyclerViewSpalsh.setHasFixedSize(true);
        this.recyclerViewEffect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewEffect.setAdapter(this.mEditingEffectToolsAdapter);
        this.recyclerViewFilter.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewFilter.setHasFixedSize(true);
        this.recycler_view_overlay_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_overlay_effect.setHasFixedSize(true);
        this.recycler_view_color_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_color_effect.setHasFixedSize(true);
        this.recycler_view_dodge_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_dodge_effect.setHasFixedSize(true);
        this.recycler_view_hardmix_effet.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_hardmix_effet.setHasFixedSize(true);
        this.recycler_view_hue_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_hue_effect.setHasFixedSize(true);
        this.recycler_view_burn_effect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recycler_view_burn_effect.setHasFixedSize(true);
        new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        this.recyclerViewAdjust.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewAdjust.setHasFixedSize(true);
        AdjustAdapter adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
        this.mAdjustAdapter = adjustAdapter;
        this.recyclerViewAdjust.setAdapter(adjustAdapter);
        this.recyclerViewPaintListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewPaintListColor.setHasFixedSize(true);
        this.recyclerViewPaintListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recyclerViewNeonListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewNeonListColor.setHasFixedSize(true);
        this.recyclerViewNeonListColor.setAdapter(new ColorAdapter(getApplicationContext(), this));
        this.recyclerViewMagicListColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewMagicListColor.setHasFixedSize(true);
        this.recyclerViewMagicListColor.setAdapter(new MagicBrushAdapter(getApplicationContext(), this));
        this.viewPagerStickers.setAdapter(new PagerAdapter() {

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
                View inflate = LayoutInflater.from(PolishEditorActivity.this.getBaseContext()).inflate(R.layout.list_sticker, (ViewGroup) null, false);
                RecyclerView recycler_view_sticker = (RecyclerView) inflate.findViewById(R.id.recyclerViewSticker);
                recycler_view_sticker.setHasFixedSize(true);
                recycler_view_sticker.setLayoutManager(new GridLayoutManager(PolishEditorActivity.this.getApplicationContext(), 7));
                switch (i) {
                    case 0:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.bubbleList(), i, PolishEditorActivity.this));
                        break;
                    case 1:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.cartoonList(), i, PolishEditorActivity.this));
                        break;
                    case 2:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.childList(), i, PolishEditorActivity.this));
                        break;
                    case 3:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.flowerList(), i, PolishEditorActivity.this));
                        break;
                    case 4:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.amojiList(), i, PolishEditorActivity.this));
                        break;
                    case 5:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.deliciousList(), i, PolishEditorActivity.this));
                        break;
                    case 6:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.newyearList(), i, PolishEditorActivity.this));
                        break;
                    case 7:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.popularList(), i, PolishEditorActivity.this));
                        break;
                    case 8:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.emojList(), i, PolishEditorActivity.this));
                        break;
                    case 9:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.rageList(), i, PolishEditorActivity.this));
                        break;
                    case 10:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.christmasList(), i, PolishEditorActivity.this));
                        break;
                    case 11:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.unicornList(), i, PolishEditorActivity.this));
                        break;
                    case 12:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.stickerList(), i, PolishEditorActivity.this));
                        break;
                    case 13:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.plantList(), i, PolishEditorActivity.this));
                        break;
                    case 14:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.birthdayList(), i, PolishEditorActivity.this));
                        break;
                    case 15:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.lovedayList(), i, PolishEditorActivity.this));
                        break;
                    case 16:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.chickenList(), i, PolishEditorActivity.this));
                        break;
                    case 17:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.textneonList(), i, PolishEditorActivity.this));
                        break;
                    case 18:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.thuglifeList(), i, PolishEditorActivity.this));
                        break;
                    case 19:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.sweetList(), i, PolishEditorActivity.this));
                        break;
                    case 20:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.celebrateList(), i, PolishEditorActivity.this));
                        break;
                    case 21:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.happyList(), i, PolishEditorActivity.this));
                        break;
                    case 22:
                        recycler_view_sticker.setAdapter(new StickerAdapter(PolishEditorActivity.this.getApplicationContext(), StickerFileAsset.textcolorList(), i, PolishEditorActivity.this));
                        break;
                }
                viewGroup.addView(inflate);
                return inflate;
            }
        });
        RecyclerTabLayout recycler_tab_layout = (RecyclerTabLayout) findViewById(R.id.recycler_tab_layout);
        recycler_tab_layout.setUpWithAdapter(new StickersTabAdapter(this.viewPagerStickers, getApplicationContext()));
        recycler_tab_layout.setPositionThreshold(0.5f);
        recycler_tab_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.TabColor));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            new loadBitmapUri().execute(bundle.getString(PolishPickerView.KEY_SELECTED_PHOTOS));
            forwardEffects = bundle.getBoolean(PolishPickerView.EFFECTS, false);
        }
        if (forwardEffects) {
            recyclerViewTools.setVisibility(View.GONE);
            constraintLayoutEffects.setVisibility(View.VISIBLE);
        }
        getWindowManager().getDefaultDisplay().getSize(new Point());
        CGENativeLibrary.setLoadImageCallback(this.loadImageCallback, null);
        if (Build.VERSION.SDK_INT < 26) {
            getWindow().setSoftInputMode(48);
        }
        PolishEditor build = new PolishEditor.Builder(this, this.polishView).setPinchTextScalable(true).build();
        this.polishEditor = build;
        build.setOnPhotoEditorListener(this);
        Preference.setKeyboard(getApplicationContext(), 0);

        KeyboardHeightProvider keyboardHeightProvider = new KeyboardHeightProvider(this);
        this.keyboardProvider = keyboardHeightProvider;
        keyboardHeightProvider.addKeyboardListener(new KeyboardHeightProvider.KeyboardListener() {

            @Override
            public final void onHeightChanged(int i) {
                PolishEditorActivity.this.keyboardHeight(i);
            }
        });
    }

    public void keyboardHeight(int i) {
        if (i <= 0) {
            Preference.setHeightOfNotch(getApplicationContext(), -i);
            return;
        }
        TextFragment textFragment2 = this.textFragment;
        if (textFragment2 != null) {
            textFragment2.updateAddTextBottomToolbarHeight(Preference.getHeightOfNotch(getApplicationContext()) + i);
            Preference.setKeyboard(getApplicationContext(), Preference.getHeightOfNotch(getApplicationContext()) + i);
        }
    }

    private void onClickListener() {
        this.text_view_save.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.saveEditing(view);
            }
        });
        this.undo.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.undo(view);
            }
        });
        this.redo.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.redo(view);
            }
        });
        this.linearLayoutOverlay.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.overlay(view);
            }
        });
        this.linearLayoutColor.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.color(view);
            }
        });
        this.linearLayoutDodge.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.dodge(view);
            }
        });
        this.linearLayoutHardmix.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.harmix(view);
            }
        });
        this.linearLayoutHue.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.hue(view);
            }
        });
        this.linearLayoutBurn.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.burn(view);
            }
        });
        this.image_view_exit.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.exit(view);
            }
        });
        this.imageViewBrushEraser.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.eraser(view);
            }
        });
        this.imageViewBrushSize.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.brushSize(view);
            }
        });
        this.imageViewBrushOpacity.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.brushOpacity(view);
            }
        });
        this.imageViewNeonEraser.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.neonEraser(view);
            }
        });
        this.imageViewNeonSize.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.NeonSize(view);
            }
        });
        this.imageViewMagicEraser.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.MagicEraser(view);
            }
        });
        this.imageViewMagicSize.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.MagicSize(view);
            }
        });
        this.imageViewMagicOpacity.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                PolishEditorActivity.this.MagicOpacity(view);
            }
        });
        this.seekBarBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushSize((float) (i + 5));
                PolishEditorActivity.this.textViewValueBrush.setText(String.valueOf(i));
            }
        });
        this.seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setPaintOpacity(i);
                PolishEditorActivity.this.textViewValueOpacity.setText(String.valueOf(i));
            }
        });
        this.seekBarEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushEraserSize((float) i);
                PolishEditorActivity.this.polishEditor.brushEraser();
                PolishEditorActivity.this.textViewValueEraser.setText(String.valueOf(i));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seekBarMagicBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushSize((float) (i + 5));
                PolishEditorActivity.this.textViewMagicBrush.setText(String.valueOf(i));
            }
        });
        this.seekBarMagicOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setMagicOpacity(i);
                PolishEditorActivity.this.textViewMagicOpacity.setText(String.valueOf(i));
            }
        });
        this.seekBarMagicEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushEraserSize((float) i);
                PolishEditorActivity.this.polishEditor.brushEraser();
                PolishEditorActivity.this.textViewMagicEraser.setText(String.valueOf(i));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seekBarNeonBrush.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushSize((float) (i + 5));
                PolishEditorActivity.this.textViewNeonBrush.setText(String.valueOf(i));
            }
        });
        this.seekBarNeonEraser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishEditor.setBrushEraserSize((float) i);
                PolishEditorActivity.this.polishEditor.brushEraser();
                PolishEditorActivity.this.textViewNeonEraser.setText(String.valueOf(i));
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.seekbarSticker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                Sticker currentSticker = PolishEditorActivity.this.polishView.getCurrentSticker();
                if (currentSticker != null) {
                    currentSticker.setAlpha(i);
                }
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.imageViewAddSticker);
        this.imageViewAddSticker = imageView;
        imageView.setVisibility(View.GONE);
        this.imageViewAddSticker.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                PolishEditorActivity.this.addSticker(view);
            }
        });
        this.relativeLayoutAddText.setOnClickListener(new View.OnClickListener() {


            public final void onClick(View view) {
                PolishEditorActivity.this.addText(view);
            }
        });
        DegreeSeekBar degreeSeekBar = (DegreeSeekBar) findViewById(R.id.seekbarAdjust);
        this.adjustSeekBar = degreeSeekBar;
        degreeSeekBar.setCenterTextColor(getResources().getColor(R.color.yellow));
        this.adjustSeekBar.setTextColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setPointColor(getResources().getColor(R.color.white));
        this.adjustSeekBar.setDegreeRange(-50, 50);
        this.adjustSeekBar.setScrollingListener(new DegreeSeekBar.ScrollingListener() {


            @Override
            public void onScrollEnd() {
            }

            @Override
            public void onScrollStart() {
            }

            @Override
            public void onScroll(int i) {
                AdjustAdapter.AdjustModel currentAdjustModel = PolishEditorActivity.this.mAdjustAdapter.getCurrentAdjustModel();
                currentAdjustModel.originValue = (((float) Math.abs(i + 50)) * ((currentAdjustModel.maxValue - ((currentAdjustModel.maxValue + currentAdjustModel.minValue) / 2.0f)) / 50.0f)) + currentAdjustModel.minValue;
                PolishEditorActivity.this.polishEditor.setAdjustFilter(PolishEditorActivity.this.mAdjustAdapter.getFilterConfig());
            }
        });
        DegreeSeekBar degreeSeekBar2 = (DegreeSeekBar) findViewById(R.id.seekbarFilter);
        this.adjustFilter = degreeSeekBar2;
        degreeSeekBar2.setCenterTextColor(getResources().getColor(R.color.yellow));
        this.adjustFilter.setTextColor(getResources().getColor(R.color.white));
        this.adjustFilter.setPointColor(getResources().getColor(R.color.white));
        this.adjustFilter.setDegreeRange(0, 100);
        this.adjustFilter.setScrollingListener(new DegreeSeekBar.ScrollingListener() {

            @Override
            public void onScrollEnd() {
            }

            @Override
            public void onScrollStart() {
            }

            @Override
            public void onScroll(int i) {
                PolishEditorActivity.this.polishView.setFilterIntensity(((float) i) / 100.0f);
            }
        });
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbarOverlay);
        this.seekBarOverlay = seekBar;
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                PolishEditorActivity.this.polishView.setFilterIntensity(((float) i) / 100.0f);
            }
        });

        PolishStickerIcons polishStickerIcons = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_close), 0, PolishStickerIcons.REMOVE);
        this.polishStickerIconClose = polishStickerIcons;
        polishStickerIcons.setIconEvent(new DeleteIconEvent());
        PolishStickerIcons polishStickerIcons2 = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_scale), 3, PolishStickerIcons.ZOOM);
        this.polishStickerIconScale = polishStickerIcons2;
        polishStickerIcons2.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons polishStickerIcons3 = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_flip), 1, PolishStickerIcons.FLIP);
        this.polishStickerIconFlip = polishStickerIcons3;
        polishStickerIcons3.setIconEvent(new FlipHorizontallyEvent());
        PolishStickerIcons polishStickerIcons4 = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_rotate), 3, PolishStickerIcons.ROTATE);
        this.polishStickerIconRotate = polishStickerIcons4;
        polishStickerIcons4.setIconEvent(new ZoomIconEvent());
        PolishStickerIcons polishStickerIcons5 = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_edit), 1, PolishStickerIcons.EDIT);
        this.polishStickerIconEdit = polishStickerIcons5;
        polishStickerIcons5.setIconEvent(new EditTextIconEvent());
        PolishStickerIcons polishStickerIcons6 = new PolishStickerIcons(ContextCompat.getDrawable(this, R.drawable.ic_outline_center), 2, PolishStickerIcons.ALIGN_HORIZONTALLY);
        this.polishStickerIconAlign = polishStickerIcons6;
        polishStickerIcons6.setIconEvent(new AlignHorizontallyEvent());
        this.polishView.setIcons(Arrays.asList(this.polishStickerIconClose, this.polishStickerIconScale, this.polishStickerIconFlip, this.polishStickerIconEdit, this.polishStickerIconRotate, this.polishStickerIconAlign));
        this.polishView.setBackgroundColor(Color.BLACK);
        this.polishView.setLocked(false);
        this.polishView.setConstrained(true);
        this.polishView.setOnStickerOperationListener(new PolishStickerView.OnStickerOperationListener() {

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
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
                PolishEditorActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
            }

            @Override
            public void onStickerSelected(Sticker sticker) {
                if (sticker instanceof PolishTextView) {
                    ((PolishTextView) sticker).setTextColor(SupportMenu.CATEGORY_MASK);
                    PolishEditorActivity.this.polishView.replace(sticker);
                    PolishEditorActivity.this.polishView.invalidate();
                }
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.VISIBLE);
                PolishEditorActivity.this.seekbarSticker.setProgress(sticker.getAlpha());
            }

            @Override
            public void onStickerDeleted(Sticker sticker) {
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.GONE);
            }

            @Override
            public void onStickerTouchOutside() {
                PolishEditorActivity.this.seekbarSticker.setVisibility(View.GONE);
            }

            @Override
            public void onStickerDoubleTap(Sticker sticker) {
                if (sticker instanceof PolishTextView) {
                    sticker.setShow(false);
                    PolishEditorActivity.this.polishView.setHandlingSticker(null);
                    PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
                    polishEditorActivity.textFragment = TextFragment.show(polishEditorActivity, ((PolishTextView) sticker).getPolishText());
                    PolishEditorActivity.this.textEditor = new TextFragment.TextEditor() {

                        @Override
                        public void onDone(PolishText polishText) {
                            PolishEditorActivity.this.polishView.getStickers().remove(PolishEditorActivity.this.polishView.getLastHandlingSticker());
                            PolishEditorActivity.this.polishView.addSticker(new PolishTextView(PolishEditorActivity.this, polishText));
                        }

                        @Override
                        public void onBackButton() {
                            PolishEditorActivity.this.polishView.showLastHandlingSticker();
                        }
                    };
                    PolishEditorActivity.this.textFragment.setOnTextEditorListener(PolishEditorActivity.this.textEditor);
                }
            }
        });
    }

    public void saveEditing(View view) {
        displayInterstitial();
    }

    public void undo(View view) {
        setUndo();
    }

    public void redo(View view) {
        setRedo();
    }

    public void overlay(View view) {
        setOverlayEffect();
    }

    public void color(View view) {
        setColorEffect();
    }

    public void dodge(View view) {
        setDodgeEffect();
    }

    public void harmix(View view) {
        setHardMixEffect();
    }

    public void hue(View view) {
        setHueEffect();
    }

    public void burn(View view) {
        setBurnEffect();
    }

    public void exit(View view) {
        onBackPressed();
    }

    public void eraser(View view) {
        setErasePaint();
    }

    public void brushSize(View view) {
        setColorPaint();
    }

    public void brushOpacity(View view) {
        setPaintOpacity();
    }

    public void neonEraser(View view) {
        setEraseNeon();
    }

    public void NeonSize(View view) {
        setColorNeon();
    }

    public void MagicEraser(View view) {
        setEraseMagic();
    }

    public void MagicSize(View view) {
        setMagicBrush();
    }

    public void MagicOpacity(View view) {
        setMagicOpacity();
    }

    public void addSticker(View view) {
        this.imageViewAddSticker.setVisibility(View.GONE);
        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
    }

    public void addText(View view) {
        this.polishView.setHandlingSticker(null);
        textFragment();
    }

    public void setOverlayEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.VISIBLE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.view_overlay.setVisibility(View.VISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setColorEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.VISIBLE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.VISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setDodgeEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.VISIBLE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.VISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setHardMixEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.VISIBLE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.VISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setHueEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.VISIBLE);
        this.recycler_view_burn_effect.setVisibility(View.GONE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.VISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.INVISIBLE);
    }

    public void setBurnEffect() {
        this.recycler_view_overlay_effect.setVisibility(View.GONE);
        this.recycler_view_color_effect.setVisibility(View.GONE);
        this.recycler_view_dodge_effect.setVisibility(View.GONE);
        this.recycler_view_hardmix_effet.setVisibility(View.GONE);
        this.recycler_view_hue_effect.setVisibility(View.GONE);
        this.recycler_view_burn_effect.setVisibility(View.VISIBLE);
        this.text_view_overlay.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hue.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_color.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_dodge.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_hardmix.setTextColor(ContextCompat.getColor(this, R.color.gray));
        this.text_view_burn.setTextColor(ContextCompat.getColor(this, R.color.white));
        this.view_overlay.setVisibility(View.INVISIBLE);
        this.view_hue.setVisibility(View.INVISIBLE);
        this.view_color.setVisibility(View.INVISIBLE);
        this.view_dodge.setVisibility(View.INVISIBLE);
        this.view_hardmix.setVisibility(View.INVISIBLE);
        this.view_burn.setVisibility(View.VISIBLE);
    }

    private void setBottomToolbar(boolean z) {
        int mVisibility = !z ? View.GONE : View.VISIBLE;
        this.imageViewUndoPaint.setVisibility(mVisibility);
        this.imageViewRedoPaint.setVisibility(mVisibility);
        this.imageViewCleanPaint.setVisibility(mVisibility);
        this.imageViewCleanNeon.setVisibility(mVisibility);
        this.imageViewUndoNeon.setVisibility(mVisibility);
        this.imageViewRedoNeon.setVisibility(mVisibility);
        this.imageViewCleanMagic.setVisibility(mVisibility);
        this.imageViewUndoMagic.setVisibility(mVisibility);
        this.imageViewRedoMagic.setVisibility(mVisibility);
    }

    public void setErasePaint() {
        this.relativeLayoutPaint.setVisibility(View.VISIBLE);
        this.recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        this.polishEditor.brushEraser();
        this.seekBarEraser.setProgress(20);
        this.seekBarEraser.setVisibility(View.VISIBLE);
        this.seekBarOpacity.setVisibility(View.GONE);
        this.seekBarBrush.setVisibility(View.GONE);
        this.textViewValueEraser.setVisibility(View.VISIBLE);
        this.textViewValueBrush.setVisibility(View.GONE);
        this.textViewValueOpacity.setVisibility(View.GONE);
        this.textViewTitleBrush.setText("Eraser");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser_select);

    }

    public void setColorPaint() {
        this.relativeLayoutPaint.setVisibility(View.GONE);
        this.recyclerViewPaintListColor.setVisibility(View.VISIBLE);
        this.recyclerViewPaintListColor.scrollToPosition(0);
        ColorAdapter colorAdapter2 = (ColorAdapter) this.recyclerViewPaintListColor.getAdapter();
        this.colorAdapter = colorAdapter2;
        if (colorAdapter2 != null) {
            colorAdapter2.setSelectedColorIndex(0);
        }
        ColorAdapter colorAdapter3 = this.colorAdapter;
        if (colorAdapter3 != null) {
            colorAdapter3.notifyDataSetChanged();
        }
        this.polishEditor.setBrushMode(1);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarBrush.setProgress(20);
        this.seekBarEraser.setVisibility(View.GONE);
        this.seekBarOpacity.setVisibility(View.GONE);
        this.seekBarBrush.setVisibility(View.VISIBLE);
        this.textViewValueEraser.setVisibility(View.GONE);
        this.textViewValueBrush.setVisibility(View.VISIBLE);
        this.textViewValueOpacity.setVisibility(View.GONE);
        this.textViewTitleBrush.setText("Brush");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser_select);

    }

    public void setPaintOpacity() {
        this.relativeLayoutPaint.setVisibility(View.GONE);
        this.seekBarOpacity.setProgress(100);
        this.seekBarEraser.setVisibility(View.GONE);
        this.seekBarOpacity.setVisibility(View.VISIBLE);
        this.seekBarBrush.setVisibility(View.GONE);
        this.textViewValueEraser.setVisibility(View.GONE);
        this.textViewValueBrush.setVisibility(View.GONE);
        this.textViewValueOpacity.setVisibility(View.VISIBLE);
        this.textViewTitleBrush.setText("Opacity");
        this.imageViewBrushSize.setImageResource(R.drawable.ic_brush);
        this.imageViewBrushOpacity.setImageResource(R.drawable.ic_opacity_select);
        this.imageViewBrushEraser.setImageResource(R.drawable.ic_eraser_select);

    }

    public void setEraseNeon() {
        this.relativeLayoutNeon.setVisibility(View.VISIBLE);
        this.recyclerViewNeonListColor.setVisibility(View.VISIBLE);
        this.polishEditor.brushEraser();
        this.seekBarNeonEraser.setProgress(20);
        this.seekBarNeonEraser.setVisibility(View.VISIBLE);
        this.seekBarNeonBrush.setVisibility(View.GONE);
        this.textViewNeonEraser.setVisibility(View.VISIBLE);
        this.textViewNeonBrush.setVisibility(View.GONE);
        this.textViewTitleNeon.setText("Eraser");
        this.imageViewNeonSize.setImageResource(R.drawable.ic_brush);
        this.imageViewNeonEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setColorNeon() {
        this.relativeLayoutNeon.setVisibility(View.GONE);
        this.recyclerViewNeonListColor.setVisibility(View.VISIBLE);
        this.recyclerViewNeonListColor.scrollToPosition(0);
        ColorAdapter colorAdapter2 = (ColorAdapter) this.recyclerViewNeonListColor.getAdapter();
        this.colorAdapter = colorAdapter2;
        if (colorAdapter2 != null) {
            colorAdapter2.setSelectedColorIndex(0);
        }
        ColorAdapter colorAdapter3 = this.colorAdapter;
        if (colorAdapter3 != null) {
            colorAdapter3.notifyDataSetChanged();
        }
        this.polishEditor.setBrushMode(2);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarNeonBrush.setProgress(20);
        this.seekBarNeonEraser.setVisibility(View.GONE);
        this.seekBarNeonBrush.setVisibility(View.VISIBLE);
        this.textViewNeonEraser.setVisibility(View.GONE);
        this.textViewNeonBrush.setVisibility(View.VISIBLE);
        this.textViewTitleNeon.setText("Brush");
        this.imageViewNeonSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewNeonEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setEraseMagic() {
        this.relativeLayoutMagic.setVisibility(View.VISIBLE);
        this.recyclerViewMagicListColor.setVisibility(View.VISIBLE);
        this.polishEditor.brushEraser();
        this.seekBarMagicEraser.setProgress(20);
        this.seekBarMagicEraser.setVisibility(View.VISIBLE);
        this.seekBarMagicOpacity.setVisibility(View.GONE);
        this.seekBarMagicBrush.setVisibility(View.GONE);
        this.textViewMagicEraser.setVisibility(View.VISIBLE);
        this.textViewMagicBrush.setVisibility(View.GONE);
        this.textViewMagicOpacity.setVisibility(View.GONE);
        this.textViewTitleMagic.setText("Eraser");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser_select);
    }

    public void setMagicBrush() {
        this.relativeLayoutMagic.setVisibility(View.GONE);
        this.recyclerViewMagicListColor.setVisibility(View.VISIBLE);
        this.recyclerViewMagicListColor.scrollToPosition(0);
        this.polishEditor.setBrushMagic(MagicBrushAdapter.lstDrawBitmapModel(getApplicationContext()).get(0));
        MagicBrushAdapter magicBrushAdapter = (MagicBrushAdapter) this.recyclerViewMagicListColor.getAdapter();
        if (magicBrushAdapter != null) {
            magicBrushAdapter.setSelectedColorIndex(0);
        }
        this.recyclerViewMagicListColor.scrollToPosition(0);
        if (magicBrushAdapter != null) {
            magicBrushAdapter.notifyDataSetChanged();
        }
        this.polishEditor.setBrushMode(3);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarMagicBrush.setProgress(20);
        this.seekBarMagicEraser.setVisibility(View.GONE);
        this.seekBarMagicOpacity.setVisibility(View.GONE);
        this.seekBarMagicBrush.setVisibility(View.VISIBLE);
        this.textViewMagicEraser.setVisibility(View.GONE);
        this.textViewMagicBrush.setVisibility(View.VISIBLE);
        this.textViewMagicOpacity.setVisibility(View.GONE);
        this.textViewTitleMagic.setText("Brush");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush_select);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void setMagicOpacity() {
        this.relativeLayoutMagic.setVisibility(View.GONE);
        this.polishEditor.setBrushMode(3);
        this.seekBarMagicOpacity.setProgress(100);
        this.polishEditor.setBrushDrawingMode(true);
        this.seekBarMagicEraser.setVisibility(View.GONE);
        this.seekBarMagicOpacity.setVisibility(View.VISIBLE);
        this.seekBarMagicBrush.setVisibility(View.GONE);
        this.textViewMagicEraser.setVisibility(View.GONE);
        this.textViewMagicBrush.setVisibility(View.GONE);
        this.textViewMagicOpacity.setVisibility(View.VISIBLE);
        this.textViewTitleMagic.setText("Opacity");
        this.imageViewMagicSize.setImageResource(R.drawable.ic_brush);
        this.imageViewMagicOpacity.setImageResource(R.drawable.ic_opacity_select);
        this.imageViewMagicEraser.setImageResource(R.drawable.ic_eraser);
    }

    public void viewSlideUp(View showLayout) {
        showLayout.setVisibility(View.VISIBLE);
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        this.slideUpAnimation = loadAnimation;
        showLayout.startAnimation(loadAnimation);
        this.slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void viewSlideDown(View hideLayout) {
        hideLayout.setVisibility(View.GONE);
        hideLayout.startAnimation(this.slideDownAnimation);
        this.slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public boolean onTouchView(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.polishView.getGLSurfaceView().setAlpha(0.0f);
                return true;
            case 1:
                this.polishView.getGLSurfaceView().setAlpha(1.0f);
                return false;
            default:
                return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] string, int[] i2) {
        super.onRequestPermissionsResult(i, string, i2);
    }

    @Override
    public void onAddViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    @Override
    public void onRemoveViewListener(int i) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + i + "]");
    }

    @Override
    public void onRemoveViewListener(Drawing viewType, int i) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + i + "]");
    }

    @Override
    public void onStartViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(Drawing viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    private void setUndo() {
        this.polishView.undo();
    }

    private void setRedo() {
        this.polishView.redo();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewCloseAdjust:
            case R.id.imageViewCloseFilter:
            case R.id.imageViewCloseOverlay:
            case R.id.imageViewCloseText:
            case R.id.image_view_close_sticker:
                setVisibleSave();
                onBackPressed();
                return;
            case R.id.imageViewSaveAdjust:
                new SaveFilter().execute(new Void[0]);
                this.constraintLayoutAdjust.setVisibility(View.GONE);
                this.recyclerViewTools.setVisibility(View.VISIBLE);
                this.constraintLayoutSave.setVisibility(View.VISIBLE);
                setGuideLine();
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveFilter:
                new SaveFilter().execute(new Void[0]);
                this.imageViewCompareFilter.setVisibility(View.GONE);
                viewSlideUp(this.recyclerViewTools);
                viewSlideDown(this.constraintLayoutFilter);
                setGuideLine();
                this.moduleToolsId = Module.NONE;
                setVisibleSave();
                return;
            case R.id.imageViewSaveMagic:
                showLoading(true);
                runOnUiThread(new Runnable() {

                    public final void run() {
                        PolishEditorActivity.this.saveMagic();
                    }
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveNeon:
                showLoading(true);
                runOnUiThread(new Runnable() {

                    public final void run() {
                        PolishEditorActivity.this.saveNeon();
                    }
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveOverlay:
                new SaveFilter().execute(new Void[0]);
                this.imageViewCompareOverlay.setVisibility(View.GONE);
                this.constraintLayoutSaveOverlay.setVisibility(View.GONE);
                this.seekBarOverlay.setVisibility(View.GONE);
                if (forwardEffects) {
                    viewSlideUp(this.constraintLayoutEffects);
                    viewSlideDown(this.constraintLayoutOverlay);
                } else {
                    viewSlideUp(this.recyclerViewTools);
                    viewSlideDown(this.constraintLayoutOverlay);
                }
                this.moduleToolsId = Module.NONE;
                setGuideLine();
                setVisibleSave();
                return;
            case R.id.imageViewSavePaint:
                showLoading(true);
                runOnUiThread(new Runnable() {

                    public final void run() {
                        PolishEditorActivity.this.savePaint();
                    }
                });
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.imageViewSaveText:
                this.polishView.setHandlingSticker(null);
                this.polishView.setLocked(true);
                this.constraintLayoutSaveText.setVisibility(View.GONE);
                setGuideLine();
                if (!this.polishView.getStickers().isEmpty()) {
                    new SaveSticker().execute(new Void[0]);
                }
                viewSlideUp(this.recyclerViewTools);
                viewSlideDown(this.constraintLayoutAddText);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                return;
            case R.id.image_view_clean:
            case R.id.image_view_clean_Magic:
            case R.id.image_view_clean_neon:
                this.polishEditor.clearBrushAllViews();
                return;
            case R.id.image_view_redo:
            case R.id.image_view_redo_Magic:
            case R.id.image_view_redo_neon:
                this.polishEditor.redoBrush();
                return;
            case R.id.image_view_save_sticker:
                this.polishView.setHandlingSticker(null);
                this.polishView.setLocked(true);
                this.seekbarSticker.setVisibility(View.GONE);
                this.imageViewAddSticker.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                if (!this.polishView.getStickers().isEmpty()) {
                    new SaveSticker().execute(new Void[0]);
                }
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                viewSlideUp(this.recyclerViewTools);
                viewSlideDown(this.constraintLayoutSticker);
                setVisibleSave();
                this.moduleToolsId = Module.NONE;
                setGuideLine();
                return;
            case R.id.image_view_undo:
            case R.id.image_view_undo_Magic:
            case R.id.image_view_undo_neon:
                this.polishEditor.undoBrush();
                return;
            default:
                return;
        }
    }

    public void savePaint() {
        this.polishEditor.setBrushDrawingMode(false);
        this.imageViewUndoPaint.setVisibility(View.GONE);
        this.imageViewRedoPaint.setVisibility(View.GONE);
        this.imageViewCleanPaint.setVisibility(View.GONE);
        this.constraintLayoutPaintTool.setVisibility(View.GONE);
        this.constraintLayoutSave.setVisibility(View.VISIBLE);
        viewSlideUp(this.recyclerViewTools);
        viewSlideDown(this.constraintLayoutPaint);
        setGuideLine();
        this.polishView.setImageSource(this.polishEditor.getBrushDrawingView().getDrawBitmap(this.polishView.getCurrentBitmap()));
        this.polishEditor.clearBrushAllViews();
        showLoading(false);
        reloadingLayout();
    }

    public void saveMagic() {
        this.polishEditor.setBrushDrawingMode(false);
        this.imageViewUndoMagic.setVisibility(View.GONE);
        this.imageViewRedoMagic.setVisibility(View.GONE);
        this.imageViewCleanMagic.setVisibility(View.GONE);
        viewSlideUp(this.recyclerViewTools);
        viewSlideDown(this.constraintLayoutMagic);
        this.constraintLayoutMagicTool.setVisibility(View.GONE);
        this.constraintLayoutSave.setVisibility(View.VISIBLE);
        setGuideLine();
        this.polishView.setImageSource(this.polishEditor.getBrushDrawingView().getDrawBitmap(this.polishView.getCurrentBitmap()));
        this.polishEditor.clearBrushAllViews();
        showLoading(false);
        reloadingLayout();
    }

    public void saveNeon() {
        this.polishEditor.setBrushDrawingMode(false);
        this.imageViewUndoNeon.setVisibility(View.GONE);
        this.imageViewRedoNeon.setVisibility(View.GONE);
        this.constraintLayoutNeonTool.setVisibility(View.GONE);
        this.constraintLayoutSave.setVisibility(View.VISIBLE);
        if (forwardEffects) {
            viewSlideUp(this.constraintLayoutEffects);
            viewSlideDown(this.constraintLayoutNeon);
        } else {
            viewSlideUp(this.recyclerViewTools);
            viewSlideDown(this.constraintLayoutNeon);
        }
        setGuideLine();
        this.polishView.setImageSource(this.polishEditor.getBrushDrawingView().getDrawBitmap(this.polishView.getCurrentBitmap()));
        this.polishEditor.clearBrushAllViews();
        showLoading(false);
        reloadingLayout();
    }

    @Override
    public void isPermissionGranted(boolean z, String string) {
        if (z) {
            new SaveEditingBitmap().execute(new Void[0]);
        }
    }

    public void textFragment() {
        this.textFragment = TextFragment.show(this);
        TextFragment.TextEditor textEditor = new TextFragment.TextEditor() {


            @Override
            public void onDone(PolishText polishText) {
                PolishEditorActivity.this.polishView.addSticker(new PolishTextView(PolishEditorActivity.this.getApplicationContext(), polishText));
            }

            @Override
            public void onBackButton() {
                if (PolishEditorActivity.this.polishView.getStickers().isEmpty()) {
                    PolishEditorActivity.this.onBackPressed();
                }
            }
        };
        this.textEditor = textEditor;
        this.textFragment.setOnTextEditorListener(textEditor);
    }
//jaydip

    @Override
    public void onQuShotToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case TEXT:
                setGoneSave();
                setGuideLine();
                this.polishView.setLocked(false);
                textFragment();
                viewSlideDown(this.recyclerViewTools);
                viewSlideUp(this.constraintLayoutAddText);
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSaveText.setVisibility(View.VISIBLE);
                break;
            case STICKER:
                setGoneSave();
                setGuideLine();
                this.polishView.setLocked(false);
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSaveSticker.setVisibility(View.VISIBLE);
                this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                if (!this.polishView.getStickers().isEmpty()) {
                    this.polishView.getStickers().clear();
                    this.polishView.setHandlingSticker(null);
                }
                viewSlideDown(this.recyclerViewTools);
                viewSlideUp(this.constraintLayoutSticker);
                break;
            case ADJUST:
                setGoneSave();
                setGuideLinePaint();
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                viewSlideDown(this.recyclerViewTools);
                viewSlideUp(this.constraintLayoutAdjust);
                this.adjustSeekBar.setCurrentDegrees(0);
                AdjustAdapter adjustAdapter = new AdjustAdapter(getApplicationContext(), this);
                this.mAdjustAdapter = adjustAdapter;
                this.recyclerViewAdjust.setAdapter(adjustAdapter);
                this.polishEditor.setAdjustFilter(this.mAdjustAdapter.getFilterConfig());
                break;
            case FILTER:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                setGoneSave();
                new openFilters().execute(new Void[0]);
                break;
            case DRAW:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                viewSlideUp(this.constraintLayoutDraw);
                break;
            case EFFECT:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                viewSlideUp(this.constraintLayoutEffects);
                break;
            case RATIO:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openBlurFragment().execute(new Void[0]);
                goneLayout();
                break;
            case BACKGROUND:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openFrameFragment().execute(new Void[0]);
                goneLayout();
                break;
            case HSL:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                HSlFragment.show(this, this, this.polishView.getCurrentBitmap());
                goneLayout();
                break;
            case MIRROR:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                new openMirrorFragment().execute(new Void[0]);
                goneLayout();
                break;
            case SQ_BG:
                viewSlideUp(this.constraintLayoutSplash);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                break;
            case CROP:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                CropFragment.show(this, this, this.polishView.getCurrentBitmap());
                goneLayout();
                break;
            case BLURE:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                this.constraintLayoutEffects.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                BitmapTransfer.bitmap = this.polishView.getCurrentBitmap();
                startActivityForResult(new Intent(this, BlurLayout.class), 900);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    @Override
    public void onQuShotEffectToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case OVERLAY:
                setGoneSave();
                this.constraintLayoutSaveOverlay.setVisibility(View.VISIBLE);
                new effectOvarlay().execute(new Void[0]);
                new effectColor().execute(new Void[0]);
                new effectHardmix().execute(new Void[0]);
                new effectDodge().execute(new Void[0]);
                new effectDivide().execute(new Void[0]);
                new effectBurn().execute(new Void[0]);
                this.seekBarOverlay.setVisibility(View.VISIBLE);
                break;
            case NEON:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new neonEffect().execute(new Void[0]);
                break;
            case PIX:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new pixEffect().execute(new Void[0]);
                break;
            case ART:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new artEffect().execute(new Void[0]);
                break;
            case WINGS:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new wingEffect().execute(new Void[0]);
                break;
            case MOTION:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                new motionEffect().execute(new Void[0]);
                goneLayout();
                break;
            case SPLASH:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                BitmapTransfer.bitmap = this.polishView.getCurrentBitmap();
                startActivityForResult(new Intent(this, SplashLayout.class), 900);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case DRIP:
                this.constraintLayoutEffects.setVisibility(View.GONE);
                goneLayout();
                new dripEffect().execute(new Void[0]);
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    @Override
    public void onQuShotSQToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case SPLASH_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSplashSquareBackgroundFragment(true).execute(new Void[0]);
                break;
            case SPLASH_SQ:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSplashFragment(true).execute(new Void[0]);
                break;
            case SKETCH_SQ:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSketchFragment(true).execute(new Void[0]);
                break;
            case SKETCH_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openSketchBackgroundFragment(true).execute(new Void[0]);
                break;
            case BLUR_BG:
                this.constraintLayoutSplash.setVisibility(View.GONE);
                new openBlurSquareBackgroundFragment(true).execute(new Void[0]);
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    @Override
    public void onQuShotDrawToolSelected(Module module) {
        this.moduleToolsId = module;
        switch (module) {
            case NEON:
                setColorNeon();
                this.polishEditor.setBrushDrawingMode(true);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutNeonTool.setVisibility(View.VISIBLE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSave.setVisibility(View.GONE);
                this.constraintLayoutNeon.setVisibility(View.VISIBLE);
                this.polishEditor.clearBrushAllViews();
                this.polishEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(this.constraintLayoutView);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutNeon.getId(), 3, 0);
                constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet.applyTo(this.constraintLayoutView);
                this.polishEditor.setBrushMode(2);
                reloadingLayout();
                break;
            case PAINT:
                setColorPaint();
                this.polishEditor.setBrushDrawingMode(true);
                this.constraintLayoutPaint.setVisibility(View.VISIBLE);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutSave.setVisibility(View.GONE);
                this.constraintLayoutPaint.setVisibility(View.VISIBLE);
                this.constraintLayoutPaintTool.setVisibility(View.VISIBLE);
                this.polishEditor.clearBrushAllViews();
                this.polishEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                ConstraintSet constraintSet2 = new ConstraintSet();
                constraintSet2.clone(this.constraintLayoutView);
                constraintSet2.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet2.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutPaint.getId(), 3, 0);
                constraintSet2.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet2.applyTo(this.constraintLayoutView);
                this.polishEditor.setBrushMode(1);
                reloadingLayout();
                break;
            case COLORED:
                new openColoredFragment().execute(new Void[0]);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
            case MAGIC:
                setMagicBrush();
                this.polishEditor.setBrushDrawingMode(true);
                this.recyclerViewTools.setVisibility(View.GONE);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                this.constraintLayoutMagic.setVisibility(View.VISIBLE);
                this.constraintLayoutMagicTool.setVisibility(View.VISIBLE);
                this.polishEditor.clearBrushAllViews();
                this.polishEditor.setBrushDrawingMode(false);
                setGoneSave();
                setBottomToolbar(true);
                ConstraintSet constraintSet3 = new ConstraintSet();
                constraintSet3.clone(this.constraintLayoutView);
                constraintSet3.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
                constraintSet3.connect(this.relativeLayoutWrapper.getId(), 4, this.constraintLayoutMagic.getId(), 3, 0);
                constraintSet3.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
                constraintSet3.applyTo(this.constraintLayoutView);
                this.polishEditor.setBrushMode(3);
                reloadingLayout();
                break;
            case MOSAIC:
                new openShapeFragment().execute(new Void[0]);
                this.constraintLayoutDraw.setVisibility(View.GONE);
                goneLayout();
                break;
        }
        this.polishView.setHandlingSticker(null);
    }

    private void goneLayout() {
        setVisibleSave();
    }

    public void setGoneSave() {
        this.constraintLayoutSave.setVisibility(View.GONE);
    }

    public void setGuideLine() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraintLayoutView);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.guideline.getId(), 3, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
        constraintSet.applyTo(this.constraintLayoutView);
    }

    public void setGuideLinePaint() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.constraintLayoutView);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 1, this.constraintLayoutView.getId(), 1, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 4, this.guidelinePaint.getId(), 3, 0);
        constraintSet.connect(this.relativeLayoutWrapper.getId(), 2, this.constraintLayoutView.getId(), 2, 0);
        constraintSet.applyTo(this.constraintLayoutView);
    }

    public void setVisibleSave() {
        this.constraintLayoutSave.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (this.moduleToolsId != null) {
            try {
                switch (moduleToolsId) {
                    case TEXT:
                        if (!this.polishView.getStickers().isEmpty()) {
                            this.polishView.getStickers().clear();
                            this.polishView.setHandlingSticker(null);
                        }
                        viewSlideUp(this.recyclerViewTools);
                        viewSlideDown(this.constraintLayoutAddText);
                        this.constraintLayoutSaveText.setVisibility(View.GONE);
                        this.polishView.setHandlingSticker(null);
                        this.polishView.setLocked(true);
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case STICKER:
                        if (this.polishView.getStickers().size() <= 0) {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            viewSlideUp(this.recyclerViewTools);
                            viewSlideDown(this.constraintLayoutSticker);
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            this.polishView.setHandlingSticker(null);
                            this.polishView.setLocked(true);
                            this.moduleToolsId = Module.NONE;
                        } else if (this.imageViewAddSticker.getVisibility() == View.VISIBLE) {
                            this.polishView.getStickers().clear();
                            this.imageViewAddSticker.setVisibility(View.GONE);
                            viewSlideUp(this.recyclerViewTools);
                            viewSlideDown(this.constraintLayoutSticker);
                            this.polishView.setHandlingSticker(null);
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                            this.moduleToolsId = Module.NONE;
                        } else {
                            this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
                            this.imageViewAddSticker.setVisibility(View.VISIBLE);
                        }
                        this.linear_layout_wrapper_sticker_list.setVisibility(View.VISIBLE);
                        this.moduleToolsId = Module.NONE;
                        setVisibleSave();
                        this.constraintLayoutSaveSticker.setVisibility(View.GONE);
                        setGuideLine();
                        return;
                    case ADJUST:
                        this.polishEditor.setFilterEffect("");
                        viewSlideUp(this.recyclerViewTools);
                        viewSlideDown(this.constraintLayoutAdjust);
                        setGuideLine();
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case FILTER:
                        viewSlideUp(this.recyclerViewTools);
                        viewSlideDown(this.constraintLayoutFilter);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        setGuideLine();
                        setVisibleSave();
                        this.polishEditor.setFilterEffect("");
                        this.imageViewCompareFilter.setVisibility(View.GONE);
                        this.listFilter.clear();
                        if (this.recyclerViewFilter.getAdapter() != null) {
                            this.recyclerViewFilter.getAdapter().notifyDataSetChanged();
                        }
                        this.moduleToolsId = Module.NONE;
                        return;

//                    default:
//                        this.moduleToolsId = Module.NONE;
//                        super.onBackPressed();
//                        return;
                    case NONE:
                    case EFFECT:
                    case DRAW:
                    case SQ_BG:
                    case HSL:
                    case CROP:
                    case RATIO:
                    case BLURE:
                    case MIRROR:
                    case BACKGROUND:
//                    case 19:
//                    case 20:
//                    case 21:
//                    case 22:
//                    case 23:
//                    case 24:
//                    case 25:
//                    case 26:
//                    case 28:
//                    case 30:
//                    case 31:
//                    case 32:
//                    case 33:
//                    case 34:
//                    case 35:
                        setOnBackPressDialog();
                        return;
                    case WINGS:
                    case DRIP:
                    case SPLASH:
                    case ART:
                    case MOTION:
                    case PIX:
                        if (forwardEffects)
                            setOnBackPressDialog();
                        return;
                    case OVERLAY:
                        this.polishEditor.setFilterEffect("");
                        if (forwardEffects) {
                            viewSlideUp(this.constraintLayoutEffects);
                            viewSlideDown(this.constraintLayoutOverlay);
                        } else {
                            viewSlideUp(this.recyclerViewTools);
                            viewSlideDown(this.constraintLayoutOverlay);
                        }
                        this.imageViewCompareOverlay.setVisibility(View.GONE);
                        this.constraintLayoutSaveOverlay.setVisibility(View.GONE);
                        this.constraintLayoutConfirmCompareOverlay.setVisibility(View.GONE);
                        this.listOverlayEffect.clear();
                        if (this.recycler_view_overlay_effect.getAdapter() != null) {
                            this.recycler_view_overlay_effect.getAdapter().notifyDataSetChanged();
                        }
                        setGuideLine();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        return;
                    case NEON:
                        setVisibleSave();
                        if (forwardEffects) {
//                            viewSlideUp(this.constraintLayoutEffects);
                            viewSlideDown(this.constraintLayoutNeon);
                        } else {
                            viewSlideUp(this.recyclerViewTools);
                            viewSlideDown(this.constraintLayoutNeon);
                        }
                        this.imageViewUndoNeon.setVisibility(View.GONE);
                        this.imageViewRedoNeon.setVisibility(View.GONE);
                        this.constraintLayoutNeonTool.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.polishEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.polishEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        if (forwardEffects)
                            setOnBackPressDialog();
                        return;
                    case PAINT:
                        setVisibleSave();
                        viewSlideUp(this.recyclerViewTools);
                        viewSlideDown(this.constraintLayoutPaint);
                        this.imageViewUndoPaint.setVisibility(View.GONE);
                        this.imageViewRedoPaint.setVisibility(View.GONE);
                        this.imageViewCleanPaint.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.constraintLayoutPaintTool.setVisibility(View.GONE);
                        this.polishEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.polishEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                    case MAGIC:
                        setVisibleSave();
                        viewSlideUp(this.recyclerViewTools);
                        viewSlideDown(this.constraintLayoutMagic);
                        this.imageViewUndoMagic.setVisibility(View.GONE);
                        this.imageViewRedoMagic.setVisibility(View.GONE);
                        this.imageViewCleanMagic.setVisibility(View.GONE);
                        this.constraintLayoutMagicTool.setVisibility(View.GONE);
                        this.constraintLayoutSave.setVisibility(View.VISIBLE);
                        this.polishEditor.setBrushDrawingMode(false);
                        setGuideLine();
                        this.polishEditor.clearBrushAllViews();
                        setVisibleSave();
                        this.moduleToolsId = Module.NONE;
                        reloadingLayout();
                        return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAdjustSelected(AdjustAdapter.AdjustModel adjustModel) {
        this.adjustSeekBar.setCurrentDegrees(((int) ((adjustModel.originValue - adjustModel.minValue) / ((adjustModel.maxValue - ((adjustModel.maxValue + adjustModel.minValue) / 2.0f)) / 50.0f))) - 50);
    }

    @Override
    public void addSticker(int item, Bitmap bitmap) {

        this.polishView.addSticker(new DrawableSticker(new BitmapDrawable(getResources(), bitmap)));
        this.linear_layout_wrapper_sticker_list.setVisibility(View.GONE);
        this.imageViewAddSticker.setVisibility(View.VISIBLE);
        return;

    }

    @Override
    public void finishCrop(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    @Override
    public void onColorChanged(String string) {
        this.polishEditor.setBrushColor(Color.parseColor(string));
    }

    @Override
    public void ratioSavedBitmap(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
        reloadingLayout();
    }

    @Override
    public void onSaveFilter(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onSaveSplashBackground(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onSaveBlurBackground(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onSaveSketchBackground(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onSaveSketch(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onSaveMosaic(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onMagicChanged(int item, DrawModel drawBitmapModel) {

        this.polishEditor.setBrushMagic(drawBitmapModel);

    }

    @Override
    public void onSaveSplash(Bitmap bitmap) {
        this.polishView.setImageSource(bitmap);
        this.moduleToolsId = Module.NONE;
    }

    @Override
    public void onFilterSelected(int itemCurrent, String string) {
        this.polishEditor.setFilterEffect(string);
        this.adjustFilter.setCurrentDegrees(50);
        if (this.moduleToolsId == Module.FILTER) {
            this.polishView.getGLSurfaceView().setFilterIntensity(0.5f);
            return;
        }
        return;
    }

    @Override
    public void onOverlaySelected(int itemCurrent, String string) {

        this.polishEditor.setFilterEffect(string);
        this.seekBarOverlay.setProgress(50);
        if (this.moduleToolsId == Module.OVERLAY) {
            this.polishView.getGLSurfaceView().setFilterIntensity(0.5f);
            return;
        }
        return;

    }

    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    class openFilters extends AsyncTask<Void, Void, Void> {
        openFilters() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listFilter.clear();
            PolishEditorActivity.this.listFilter.addAll(FilterFileAsset.getListBitmapFilter(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            Log.d("XXXXXXXX", "allFilters " + PolishEditorActivity.this.listFilter.size());
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recyclerViewFilter;
            ArrayList arrayList = PolishEditorActivity.this.listFilter;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new FilterAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(FilterFileAsset.FILTERS)));
            PolishEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
            PolishEditorActivity.this.adjustFilter.setCurrentDegrees(50);
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity polishEditorActivity2 = PolishEditorActivity.this;
            polishEditorActivity2.viewSlideDown(polishEditorActivity2.recyclerViewTools);
            PolishEditorActivity polishEditorActivity3 = PolishEditorActivity.this;
            polishEditorActivity3.viewSlideUp(polishEditorActivity3.constraintLayoutFilter);
            PolishEditorActivity polishEditorActivity4 = PolishEditorActivity.this;
            polishEditorActivity4.viewSlideUp(polishEditorActivity4.imageViewCompareFilter);
            PolishEditorActivity.this.setGuideLinePaint();
        }
    }

    class openBlurFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openBlurFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            RatioFragment.show(polishEditorActivity, polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap(), bitmap);
        }
    }

    class openMirrorFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openMirrorFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity queShotEditorActivity = PolishEditorActivity.this;
            MirrorFragment.show(queShotEditorActivity, queShotEditorActivity, queShotEditorActivity.polishView.getCurrentBitmap(), bitmap);
        }
    }

    class dripEffect extends AsyncTask<Void, Void, Void> {
        dripEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            Bitmap bitmap = null;
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, bitmap);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, bitmap);
            DripLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            StoreManager.setCurrentOriginalBitmap(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap());
            PolishEditorActivity.this.startActivityForResult(new Intent(PolishEditorActivity.this, DripLayout.class), 900);
            PolishEditorActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class motionEffect extends AsyncTask<Void, Void, Void> {
        motionEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            Bitmap bitmap = null;
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, bitmap);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, bitmap);
            MotionLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            StoreManager.setCurrentOriginalBitmap(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap());
            PolishEditorActivity.this.startActivityForResult(new Intent(PolishEditorActivity.this, MotionLayout.class), 900);
            PolishEditorActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class wingEffect extends AsyncTask<Void, Void, Void> {
        wingEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            Bitmap bitmap = null;
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, bitmap);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, bitmap);
            WingLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            StoreManager.setCurrentOriginalBitmap(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap());
            PolishEditorActivity.this.startActivityForResult(new Intent(PolishEditorActivity.this, WingLayout.class), 900);
            PolishEditorActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class neonEffect extends AsyncTask<Void, Void, Void> {
        neonEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            Bitmap bitmap = null;
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, bitmap);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, bitmap);
            NeonLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            StoreManager.setCurrentOriginalBitmap(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap());
            PolishEditorActivity.this.startActivityForResult(new Intent(PolishEditorActivity.this, NeonLayout.class), 900);
            PolishEditorActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class artEffect extends AsyncTask<Void, Void, Void> {
        artEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            Bitmap bitmap = null;
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, bitmap);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, bitmap);
            ArtLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            StoreManager.setCurrentOriginalBitmap(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap());
            PolishEditorActivity.this.startActivityForResult(new Intent(PolishEditorActivity.this, ArtLayout.class), 900);
            PolishEditorActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class pixEffect extends AsyncTask<Void, Void, Void> {
        pixEffect() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            return null;
        }

        public void onPostExecute(Void voids) {
            Bitmap bitmap = null;
            StoreManager.setCurrentCropedBitmap(PolishEditorActivity.this, bitmap);
            StoreManager.setCurrentCroppedMaskBitmap(PolishEditorActivity.this, bitmap);
            PixLabLayout.setFaceBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap());
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            StoreManager.setCurrentOriginalBitmap(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap());
            PolishEditorActivity.this.startActivityForResult(new Intent(PolishEditorActivity.this, PixLabLayout.class), 900);
            PolishEditorActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openFrameFragment extends AsyncTask<Void, Bitmap, Bitmap> {
        openFrameFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            return FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 5.0f);
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            FrameFragment.show(polishEditorActivity, polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap(), bitmap);
        }
    }

    class openShapeFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openShapeFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            PolishEditorActivity.this.showLoading(false);
            MosaicFragment.show(PolishEditorActivity.this, list.get(0), list.get(1), PolishEditorActivity.this);
        }
    }

    class openColoredFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        openColoredFragment() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(FilterUtils.cloneBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap()));
            arrayList.add(FilterUtils.getBlurImageFromBitmap(PolishEditorActivity.this.polishView.getCurrentBitmap(), 8.0f));
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            PolishEditorActivity.this.showLoading(false);
            ColoredFragment.show(PolishEditorActivity.this, list.get(0), list.get(1), PolishEditorActivity.this);
        }
    }

    class effectDodge extends AsyncTask<Void, Void, Void> {
        effectDodge() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listDodgeEffect.clear();
            PolishEditorActivity.this.listDodgeEffect.addAll(OverlayFileAsset.getListBitmapDodgeEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recycler_view_dodge_effect;
            ArrayList arrayList = PolishEditorActivity.this.listDodgeEffect;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new OverlayAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(OverlayFileAsset.DODGE_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectColor extends AsyncTask<Void, Void, Void> {
        effectColor() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listColorEffect.clear();
            PolishEditorActivity.this.listColorEffect.addAll(OverlayFileAsset.getListBitmapColorEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recycler_view_color_effect;
            ArrayList arrayList = PolishEditorActivity.this.listColorEffect;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new OverlayAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(OverlayFileAsset.COLOR_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectDivide extends AsyncTask<Void, Void, Void> {
        effectDivide() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listHueEffect.clear();
            PolishEditorActivity.this.listHueEffect.addAll(OverlayFileAsset.getListBitmapHueEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recycler_view_hue_effect;
            ArrayList arrayList = PolishEditorActivity.this.listHueEffect;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new OverlayAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(OverlayFileAsset.HUE_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectHardmix extends AsyncTask<Void, Void, Void> {
        effectHardmix() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listHardMixEffect.clear();
            PolishEditorActivity.this.listHardMixEffect.addAll(OverlayFileAsset.getListBitmapHardmixEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recycler_view_hardmix_effet;
            ArrayList arrayList = PolishEditorActivity.this.listHardMixEffect;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new OverlayAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(OverlayFileAsset.HARDMIX_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class effectOvarlay extends AsyncTask<Void, Void, Void> {
        effectOvarlay() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listOverlayEffect.clear();
            PolishEditorActivity.this.listOverlayEffect.addAll(OverlayFileAsset.getListBitmapOverlayEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recycler_view_overlay_effect;
            ArrayList arrayList = PolishEditorActivity.this.listOverlayEffect;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new OverlayAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(OverlayFileAsset.OVERLAY_EFFECTS)));
            PolishEditorActivity.this.imageViewCompareOverlay.setVisibility(View.VISIBLE);
            PolishEditorActivity.this.constraintLayoutSave.setVisibility(View.GONE);
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity polishEditorActivity2 = PolishEditorActivity.this;
            polishEditorActivity2.viewSlideDown(polishEditorActivity2.recyclerViewTools);
            PolishEditorActivity polishEditorActivity3 = PolishEditorActivity.this;
            polishEditorActivity3.viewSlideDown(polishEditorActivity3.constraintLayoutEffects);
            PolishEditorActivity polishEditorActivity4 = PolishEditorActivity.this;
            polishEditorActivity4.viewSlideUp(polishEditorActivity4.constraintLayoutConfirmCompareOverlay);
            PolishEditorActivity polishEditorActivity5 = PolishEditorActivity.this;
            polishEditorActivity5.viewSlideUp(polishEditorActivity5.constraintLayoutOverlay);
            PolishEditorActivity.this.setGuideLinePaint();
        }
    }

    class effectBurn extends AsyncTask<Void, Void, Void> {
        effectBurn() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Void doInBackground(Void... voids) {
            PolishEditorActivity.this.listBurnEffect.clear();
            PolishEditorActivity.this.listBurnEffect.addAll(OverlayFileAsset.getListBitmapBurnEffect(ThumbnailUtils.extractThumbnail(PolishEditorActivity.this.polishView.getCurrentBitmap(), 100, 100)));
            return null;
        }

        public void onPostExecute(Void voids) {
            RecyclerView recyclerView = PolishEditorActivity.this.recycler_view_burn_effect;
            ArrayList arrayList = PolishEditorActivity.this.listBurnEffect;
            PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
            recyclerView.setAdapter(new OverlayAdapter(arrayList, polishEditorActivity, polishEditorActivity.getApplicationContext(), Arrays.asList(OverlayFileAsset.BURN_EFFECTS)));
            PolishEditorActivity.this.seekBarOverlay.setProgress(100);
        }
    }

    class openSplashSquareBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSplashSquareBackgroundFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SaturationSquareBackgroundFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openBlurSquareBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openBlurSquareBackgroundFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlurImageFromBitmap(currentBitmap, 2.5f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                BlurSquareBgFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openSplashFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSplashFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getBlackAndWhiteImageFromBitmap(currentBitmap));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SaturationSquareFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openSketchFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSplashSquared;

        public openSketchFragment(boolean z) {
            this.isSplashSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSplashSquared) {
                arrayList.add(FilterUtils.getSketchImageFromBitmap(currentBitmap, 0.8f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSplashSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SketchSquareFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class openSketchBackgroundFragment extends AsyncTask<Void, List<Bitmap>, List<Bitmap>> {
        boolean isSketchBackgroundSquared;

        public openSketchBackgroundFragment(boolean z) {
            this.isSketchBackgroundSquared = z;
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public List<Bitmap> doInBackground(Void... voids) {
            Bitmap currentBitmap = PolishEditorActivity.this.polishView.getCurrentBitmap();
            List<Bitmap> arrayList = new ArrayList<>();
            arrayList.add(currentBitmap);
            if (this.isSketchBackgroundSquared) {
                arrayList.add(FilterUtils.getSketchImageFromBitmap(currentBitmap, 0.8f));
            }
            return arrayList;
        }

        public void onPostExecute(List<Bitmap> list) {
            if (this.isSketchBackgroundSquared) {
                PolishEditorActivity.this.constraintLayoutSplash.setVisibility(View.GONE);
                SketchSquareBackgroundFragment.show(PolishEditorActivity.this, list.get(0), null, list.get(1), PolishEditorActivity.this, true);
            }
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class SaveFilter extends AsyncTask<Void, Void, Bitmap> {
        SaveFilter() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            Bitmap[] bitmaps = {null};
            PolishEditorActivity.this.polishView.saveGLSurfaceViewAsBitmap(new OnSaveBitmap() {

                @Override
                public final void onBitmapReady(Bitmap bitmap) {
                    bitmaps[0] = bitmap;

                }
            });
            while (bitmaps[0] == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return bitmaps[0];
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.polishView.setFilterEffect("");
            PolishEditorActivity.this.showLoading(false);
        }
    }

    class SaveSticker extends AsyncTask<Void, Void, Bitmap> {
        SaveSticker() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.polishView.getGLSurfaceView().setAlpha(0.0f);
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Void... voids) {
            Bitmap[] bitmaps = {null};
            while (bitmaps[0] == null) {
                try {
                    PolishEditorActivity.this.polishEditor.saveStickerAsBitmap(new OnSaveBitmap() {

                        @Override
                        public final void onBitmapReady(Bitmap bitmap) {
                            bitmaps[0] = bitmap;
                        }
                    });
                    while (bitmaps[0] == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                }
            }
            return bitmaps[0];
        }


        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.polishView.getStickers().clear();
            PolishEditorActivity.this.polishView.getGLSurfaceView().setAlpha(1.0f);
            PolishEditorActivity.this.showLoading(false);
            PolishEditorActivity.this.reloadingLayout();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 13337) {
            super.onActivityResult(i, i2, intent);
            if (i == 123) {
                if (i2 == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        float width = (float) bitmap.getWidth();
                        float height = (float) bitmap.getHeight();
                        float max = Math.max(width / 1280.0f, height / 1280.0f);
                        if (max > 1.0f) {
                            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                        }
                        if (SystemUtil.rotateBitmap(bitmap, new ExifInterface(inputStream).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)) != bitmap) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                        this.polishView.setImageSource(bitmap);
                        reloadingLayout();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MsgUtil.toastMsg(this, "Error: Can not open image");
                    }
                } else {
                    finish();
                }
            } else if (i == 900 && intent != null && intent.getStringExtra("MESSAGE").equals("done") && BitmapTransfer.bitmap != null) {
                new loadBitmap().execute(BitmapTransfer.bitmap);
            }
        } else if (i2 == RESULT_OK) {

        }
    }

    class loadBitmap extends AsyncTask<Bitmap, Bitmap, Bitmap> {
        loadBitmap() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(Bitmap... bitmaps) {
            try {
                Bitmap bitmap = bitmaps[0];
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    return Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.reloadingLayout();
        }
    }

    public class loadBitmapUri extends AsyncTask<String, Bitmap, Bitmap> {
        loadBitmapUri() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public Bitmap doInBackground(String... string) {
            try {
                Uri fromFile = Uri.fromFile(new File(string[0]));
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(PolishEditorActivity.this.getContentResolver(), fromFile);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if (max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap bitmap1 = SystemUtil.rotateBitmap(bitmap, new ExifInterface(PolishEditorActivity.this.getContentResolver().openInputStream(fromFile)).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
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
            PolishEditorActivity.this.polishView.setImageSource(bitmap);
            PolishEditorActivity.this.reloadingLayout();
        }
    }

    public void reloadingLayout() {
        this.polishView.postDelayed(new Runnable() {

            public final void run() {
                PolishEditorActivity.this.reLoading();
            }
        }, 300);
    }

    public void reLoading() {
        try {
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int i = point.x;
            int height = this.relativeLayoutWrapper.getHeight();
            int i2 = this.polishView.getGLSurfaceView().getRenderViewport().width;
            float f = (float) this.polishView.getGLSurfaceView().getRenderViewport().height;
            float f2 = (float) i2;
            if (((int) ((((float) i) * f) / f2)) <= height) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                this.polishView.setLayoutParams(params);
                this.polishView.setVisibility(View.VISIBLE);
            } else {
                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) ((((float) height) * f2) / f), -1);
                params2.addRule(RelativeLayout.CENTER_IN_PARENT);
                this.polishView.setLayoutParams(params2);
                this.polishView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        showLoading(false);
    }

    public class SaveEditingBitmap extends AsyncTask<Void, String, String> {
        SaveEditingBitmap() {
        }

        public void onPreExecute() {
            PolishEditorActivity.this.showLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                PolishEditorActivity polishEditorActivity = PolishEditorActivity.this;
                String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                return SaveFileUtils.saveBitmapFileEditor(polishEditorActivity, polishEditorActivity.polishView.getCurrentBitmap(), format).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            PolishEditorActivity.this.showLoading(false);
            if (string == null) {
                Toast.makeText(PolishEditorActivity.this.getApplicationContext(), "Oop! Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            MediaScannerConnection.scanFile(PolishEditorActivity.this, new String[]{string}, null, null);
            Intent i = new Intent(PolishEditorActivity.this, PhotoShareActivity.class);
            i.putExtra("path", string);
            PolishEditorActivity.this.startActivity(i);
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


    private void SaveView() {
        if (PermissionsUtils.checkWriteStoragePermission(this)) {
            new SaveEditingBitmap().execute(new Void[0]);
        }
    }

    private void checkData() {
        if (NetworkHelper.isConnectedWifi(this) || (NetworkHelper.isConnectedMobile(this))) {
            Helper.generalRef.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    PolishEditorActivity.this.generalModel = (General) snapshot.getValue(General.class);
                    PolishEditorActivity.this.recyclerViewTools.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }
}
