package com.puzzle.app.puzzlegame.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.puzzle.app.puzzlegame.Bean.PuzzleBean;
import com.puzzle.app.puzzlegame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuNana on 2016/1/22.
 */
public class PuzzleUtil {

    private Context context;
    private Bitmap mLastBitmap;
    private int dimension;
    private int blankId;
    private List<PuzzleBean> mPuzzleList = new ArrayList<>();
    public PuzzleUtil(Context context,int dimension) {
        this.context = context;
        this.dimension = dimension;
    }

    public Bitmap getmLastBitmap() {
        return mLastBitmap;
    }

    public int getBlankId() {
        return blankId;
    }

    public List<PuzzleBean> creatPuzzle(Bitmap resBitmap) {
        mPuzzleList = new ArrayList<>();
        Bitmap bitmap;
        int mWidth = resBitmap.getWidth() / dimension;
        int mHeight = resBitmap.getHeight() / dimension;
        PuzzleBean bean;
        for (int i = 0; i< dimension; i++){
            for (int j = 0; j< dimension; j++){
                bitmap= Bitmap.createBitmap(
                        resBitmap,
                        j * mWidth,
                        i * mHeight,
                        mWidth,
                        mHeight);
                bean = new PuzzleBean(bitmap,
                        i * dimension + j +1,
                        i * dimension + j +1);
                mPuzzleList.add(bean);
            }
        }
        mLastBitmap = mPuzzleList.get(mPuzzleList.size()-1).getBitmap();
        Bitmap mBlankBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
        mBlankBitmap = Bitmap.createBitmap(
                mBlankBitmap, 0, 0, mWidth, mHeight);
        mPuzzleList.get(mPuzzleList.size()-1).setBitmap(mBlankBitmap);

        generatePuzzle();

        return mPuzzleList;
    }

    private void generatePuzzle(){
        int index;
        int total = dimension * dimension;
        List<Integer> initIndex = new ArrayList<>();
        List<Integer> puzzleIndex = new ArrayList<>();
        for (int i = 1; i <= total; i++){
            initIndex.add(i);
        }

        while (initIndex.size()>0){
            index = (int)(Math.random() * initIndex.size());
            puzzleIndex.add(initIndex.get(index));
            initIndex.remove(index);
        }

        if(canSolve(puzzleIndex)){
            for (int i = 0; i < total; i++){
                mPuzzleList.get(puzzleIndex.get(i)-1).setPuzzleID(i + 1);
            }
        }else {
            generatePuzzle();
        }
    }

    /**
     * Determine whether there is a solution
     *
     * @param puzzleIndex array data
     * @return boolean
     */

    private boolean canSolve(List<Integer> puzzleIndex) {
        blankId = 1;
        int size = puzzleIndex.size();
        for (Integer index:
                puzzleIndex) {
            if(index == size){
                break;
            }else{
                blankId++;
            }
        }
        if (size % 2 == 1){
            return getInversions(puzzleIndex) % 2 ==0;
        } else{
            if (((blankId - 1)/dimension) % 2 == 1){
                return getInversions(puzzleIndex) % 2 ==0;
            }else {
                return getInversions(puzzleIndex) % 2 ==1;
            }
        }
    }


    private int getInversions(List<Integer> data) {
        int inversions = 0;
        int inversionCount = 0;
        int size = data.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                int index = data.get(i);
                if (index != size){
                    if (data.get(j) != size && data.get(j) < index) {
                        inversionCount++;
                    }
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }

}
