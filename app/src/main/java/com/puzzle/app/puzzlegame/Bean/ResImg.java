package com.puzzle.app.puzzlegame.Bean;

import android.graphics.Bitmap;

/**
 * Created by LiuNana on 2016/1/20.
 */
public class ResImg {
    private String src;
    private int flag;
    private Bitmap bitmap;
    private int res;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
