package com.relish.app.loading;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.relish.app.R;
import com.relish.app.loading.render.LoadingDrawable;
import com.relish.app.loading.render.LoadingRenderer;
import com.relish.app.loading.render.LoadingRendererFactory;


public class LoadingView extends ImageView {
    private LoadingDrawable mLoadingDrawable;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        try {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
            setLoadingRenderer(LoadingRendererFactory.createLoadingRenderer(context, ta.getInt(0, 0)));
            ta.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoadingRenderer(LoadingRenderer loadingRenderer) {
        LoadingDrawable loadingDrawable = new LoadingDrawable(loadingRenderer);
        this.mLoadingDrawable = loadingDrawable;
        setImageDrawable(loadingDrawable);
    }


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }


    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }


    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0 && getVisibility() == VISIBLE) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    private void startAnimation() {
        LoadingDrawable loadingDrawable = this.mLoadingDrawable;
        if (loadingDrawable != null) {
            loadingDrawable.start();
        }
    }

    private void stopAnimation() {
        LoadingDrawable loadingDrawable = this.mLoadingDrawable;
        if (loadingDrawable != null) {
            loadingDrawable.stop();
        }
    }
}
