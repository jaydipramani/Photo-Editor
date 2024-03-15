package com.relish.app.polish;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.relish.app.Utils.SystemUtil;
import com.relish.app.sticker.Sticker;


public class PolishTextView extends Sticker {
    private int backgroundAlpha;
    private int backgroundBorder;
    private int backgroundColor;
    private BitmapDrawable backgroundDrawable;
    private final Context context;
    private Drawable drawable;
    private boolean isShowBackground;
    private float lineSpacingExtra = 0.0f;
    private float lineSpacingMultiplier = 1.0f;
    private float maxTextSizePixels;
    private float minTextSizePixels;
    private int paddingHeight;
    private int paddingWidth;
    private PolishText polishText;
    private StaticLayout staticLayout;
    private String text;
    private Layout.Alignment textAlign;
    private int textAlpha;
    private int textColor;
    private int textHeight;
    private final TextPaint textPaint;
    private PolishText.TextShadow textShadow;
    private int textWidth;

    public PolishTextView(Context paramContext, PolishText paramAddTextProperties) {
        this.context = paramContext;
        this.polishText = paramAddTextProperties;
        this.textPaint = new TextPaint(1);
        PolishTextView textSticker = setTextSize(paramAddTextProperties.getTextSize()).setTextWidth(paramAddTextProperties.getTextWidth()).setTextHeight(paramAddTextProperties.getTextHeight()).setText(paramAddTextProperties.getText()).setPaddingWidth(SystemUtil.dpToPx(paramContext, paramAddTextProperties.getPaddingWidth())).setBackgroundBorder(SystemUtil.dpToPx(paramContext, paramAddTextProperties.getBackgroundBorder())).setTextShadow(paramAddTextProperties.getTextShadow()).setTextColor(paramAddTextProperties.getTextColor()).setTextAlpha(paramAddTextProperties.getTextAlpha()).setBackgroundColor(paramAddTextProperties.getBackgroundColor()).setBackgroundAlpha(paramAddTextProperties.getBackgroundAlpha()).setShowBackground(paramAddTextProperties.isShowBackground()).setTextColor(paramAddTextProperties.getTextColor());
        AssetManager assetManager = paramContext.getAssets();
        textSticker.setTypeface(Typeface.createFromAsset(assetManager, "fonts/" + paramAddTextProperties.getFontName())).setTextAlign(paramAddTextProperties.getTextAlign()).setTextShare(paramAddTextProperties.getTextShader()).resizeText();
    }

    private float convertSpToPx(float paramFloat) {
        return this.context.getResources().getDisplayMetrics().scaledDensity * paramFloat;
    }

    @Override 
    public void draw(Canvas paramCanvas) {
        Matrix matrix = getMatrix();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        if (this.isShowBackground) {
            Paint paint = new Paint();
            if (this.backgroundDrawable != null) {
                paint.setShader(new BitmapShader(this.backgroundDrawable.getBitmap(), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
                paint.setAlpha(this.backgroundAlpha);
            } else {
                paint.setARGB(this.backgroundAlpha, Color.red(this.backgroundColor), Color.green(this.backgroundColor), Color.blue(this.backgroundColor));
            }
            float f = (float) this.textWidth;
            float f2 = (float) this.textHeight;
            int i = this.backgroundBorder;
            paramCanvas.drawRoundRect(0.0f, 0.0f, f, f2, (float) i, (float) i, paint);
            paramCanvas.restore();
            paramCanvas.save();
            paramCanvas.concat(matrix);
        }
        paramCanvas.restore();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        paramCanvas.translate((float) this.paddingWidth, (float) ((this.textHeight / 2) - (this.staticLayout.getHeight() / 2)));
        this.staticLayout.draw(paramCanvas);
        paramCanvas.restore();
        paramCanvas.save();
        paramCanvas.concat(matrix);
        paramCanvas.restore();
    }

    public PolishText getPolishText() {
        return this.polishText;
    }

    @Override 
    public int getAlpha() {
        return this.textPaint.getAlpha();
    }

    @Override 
    public Drawable getDrawable() {
        return this.drawable;
    }

    @Override 
    public int getHeight() {
        return this.textHeight;
    }

    public String getText() {
        return this.text;
    }

    @Override 
    public int getWidth() {
        return this.textWidth;
    }

    @Override 
    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    public PolishTextView resizeText() {
        String text2 = getText();
        if (text2 == null || text2.length() <= 0) {
            return this;
        }
        PolishText.TextShadow textShadow2 = this.textShadow;
        if (textShadow2 != null) {
            this.textPaint.setShadowLayer((float) textShadow2.getRadius(), (float) this.textShadow.getDx(), (float) this.textShadow.getDy(), this.textShadow.getColorShadow());
        }
        this.textPaint.setTextAlign(Paint.Align.LEFT);
        this.textPaint.setARGB(this.textAlpha, Color.red(this.textColor), Color.green(this.textColor), Color.blue(this.textColor));
        int i = this.textWidth - (this.paddingWidth * 2);
        this.staticLayout = new StaticLayout(this.text, this.textPaint, i <= 0 ? 100 : i, this.textAlign, this.lineSpacingMultiplier, this.lineSpacingExtra, true);
        return this;
    }

    @Override 
    public PolishTextView setAlpha(int paramInt) {
        this.textPaint.setAlpha(paramInt);
        return this;
    }

    public PolishTextView setBackgroundAlpha(int paramInt) {
        this.backgroundAlpha = paramInt;
        return this;
    }

    public PolishTextView setBackgroundBorder(int paramInt) {
        this.backgroundBorder = paramInt;
        return this;
    }

    public PolishTextView setBackgroundColor(int paramInt) {
        this.backgroundColor = paramInt;
        return this;
    }

    @Override 
    public PolishTextView setDrawable(Drawable paramDrawable) {
        this.drawable = paramDrawable;
        return this;
    }

    public PolishTextView setPaddingWidth(int paramInt) {
        this.paddingWidth = paramInt;
        return this;
    }

    public PolishTextView setShowBackground(boolean paramBoolean) {
        this.isShowBackground = paramBoolean;
        return this;
    }

    public PolishTextView setText(String paramString) {
        this.text = paramString;
        return this;
    }

    public PolishTextView setTextAlign(int paramInt) {
        switch (paramInt) {
            case 2:
                this.textAlign = Layout.Alignment.ALIGN_NORMAL;
                break;
            case 3:
                this.textAlign = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case 4:
                this.textAlign = Layout.Alignment.ALIGN_CENTER;
                break;
        }
        return this;
    }

    public PolishTextView setTextAlpha(int paramInt) {
        this.textAlpha = paramInt;
        return this;
    }

    public PolishTextView setTextColor(int paramInt) {
        this.textColor = paramInt;
        return this;
    }

    public PolishTextView setTextHeight(int paramInt) {
        this.textHeight = paramInt;
        return this;
    }

    public PolishTextView setTextShadow(PolishText.TextShadow paramTextShadow) {
        this.textShadow = paramTextShadow;
        return this;
    }

    public PolishTextView setTextShare(Shader paramShader) {
        this.textPaint.setShader(paramShader);
        return this;
    }

    public PolishTextView setTextSize(int paramInt) {
        this.textPaint.setTextSize(convertSpToPx((float) paramInt));
        return this;
    }

    public PolishTextView setTextWidth(int paramInt) {
        this.textWidth = paramInt;
        return this;
    }

    public PolishTextView setTypeface(Typeface paramTypeface) {
        this.textPaint.setTypeface(paramTypeface);
        return this;
    }
}
