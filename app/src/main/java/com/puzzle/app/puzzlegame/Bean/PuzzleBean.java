package com.puzzle.app.puzzlegame.Bean;

import android.graphics.Bitmap;

/**
 * Created by LiuNana on 2016/1/21.
 */
public class PuzzleBean {
    private int imgID;
    private int puzzleID;
    private Bitmap bitmap;
    public PuzzleBean() {

    }
    public PuzzleBean(Bitmap bitmap, int imgID, int puzzleID) {
        this.bitmap = bitmap;
        this.imgID = imgID;
        this.puzzleID = puzzleID;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public int getPuzzleID() {
        return puzzleID;
    }

    public void setPuzzleID(int puzzleID) {
        this.puzzleID = puzzleID;
    }
}
