package com.puzzle.app.puzzlegame.view;



import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
/**
 * Created by LiuNana on 2016/1/21.
 */
public class FitImageView extends ImageView {
    private Bitmap mBitmap;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private boolean mReady;
    private boolean mSetupPending;

    public FitImageView(Context context) {
        super(context);
        init();
    }

    public FitImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FitImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    private void reSize() {
        int width = getWidth();
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);

//        int imgH = mBitmapHeight;
//        int imgW = mBitmapWidth;
        height = width * mBitmapHeight / mBitmapWidth;
//        int lastH = imgH;
//        int lastW = imgW;
//        double radio = 1.0;
//        radio = (width * 1.0) / imgW;
//        if (imgH * radio > height) {
//            radio = (height * 1.0) / imgH;
//        }
//        lastH = (int) (radio * imgH);
//        lastW = (int) (radio * imgW);
        LayoutParams lp = this.getLayoutParams();
        lp.width = width;
        lp.height = height;
        this.setLayoutParams(lp);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();

//        reSize();

    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        invalidate();
        Log.e("wwww",  this.getMeasuredWidth() + "--" + getHeight());
    }

}
