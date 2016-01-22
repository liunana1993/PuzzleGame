package com.puzzle.app.puzzlegame.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.puzzle.app.puzzlegame.R;
import com.puzzle.app.puzzlegame.adapter.PuzzleAdapter;
import com.puzzle.app.puzzlegame.Bean.PuzzleBean;
import com.puzzle.app.puzzlegame.util.PuzzleUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PuzzleActivity extends AppCompatActivity {
    private Context context;
    private List<PuzzleBean> puzzleList = new ArrayList<>();
    private GridView mGridView;
    private PuzzleAdapter mAdapter;
    private Bitmap mLastBitmap = null;
    private int dimension;
    private int blankId;
    private boolean isSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        dimension = intent.getIntExtra("difficulty", 2);
        int flag = intent.getIntExtra("flag", 0);
        Bitmap bitmap = null;

        BitmapFactory.Options measureOptions = new BitmapFactory.Options();
        measureOptions.inJustDecodeBounds = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig =  Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        int scale;

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // Screen Height (px)

        switch (flag){
            case 1: //bitmap source is from system resource
                int res = intent.getIntExtra("res", 0);
                BitmapFactory.decodeResource(
                        getResources(),res, measureOptions);
                scale = measureOptions.outWidth / width;
                scale = Math.max(scale, 1);
                InputStream is = this.getResources().openRawResource(res);
                options.inSampleSize = scale;
                bitmap = BitmapFactory.decodeStream(
                        is,null,options);
                break;
            case 2: //bitmap source is from local file
                String src = intent.getStringExtra("src");
                BitmapFactory.decodeFile(src,measureOptions);
                scale = Math.min(measureOptions.outWidth,measureOptions.outHeight) / width;
                scale = Math.max(scale, 1);
                options.inSampleSize = scale;
                bitmap = BitmapFactory.decodeFile(src, options);
                break;
            default:
                this.finish();
        }
        //init puzzle data
        PuzzleUtil puzzleUtil = new PuzzleUtil(context, dimension);
        puzzleList = puzzleUtil.creatPuzzle(bitmap);
        mLastBitmap = puzzleUtil.getmLastBitmap();
        blankId = puzzleUtil.getBlankId();
        Collections.sort(puzzleList, new PuzzleComparator() {
        });
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.gv_puzzle);
        mAdapter = new PuzzleAdapter(context, puzzleList);
        mGridView.setAdapter(mAdapter);
        mGridView.setNumColumns(dimension);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PuzzleBean bean = puzzleList.get(position);
                int puzzleId = bean.getPuzzleID();
                int diff = puzzleId - blankId;
                int blankRow =(int)Math.ceil(((float) blankId / dimension));
                int puzzleRow =(int)Math.ceil(((float) puzzleId / dimension));
                boolean canMove = false;
                //to judge whether the bitmap can be moved or not
                if(!isSuccess){
                    if(blankRow == puzzleRow && (diff == 1 || diff == -1)){
                        canMove = true;
                    }else if(blankRow != puzzleRow && (diff == dimension || diff == -dimension)){
                        canMove = true;
                    }
                }
                if (canMove){
                    puzzleList.set(position, puzzleList.get(blankId - 1));
                    puzzleList.set(blankId - 1, bean);
                    puzzleList.get(position).setPuzzleID(position + 1);
                    puzzleList.get(blankId -1).setPuzzleID(blankId);
                    mAdapter.notifyDataSetChanged();
                    blankId = puzzleId;
                    isSuccess = true;
                    for (PuzzleBean item:
                            puzzleList) {
                        if (item.getImgID() != item.getPuzzleID()){
                            isSuccess = false;
                            break;
                        }
                    }
                    //do something when the puzzle is finished
                    if (isSuccess){
                        puzzleList.get(puzzleList.size() - 1).setBitmap(mLastBitmap);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(context, "拼图成功！", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }




    public class PuzzleComparator implements Comparator<PuzzleBean> {

        @SuppressLint("DefaultLocale")
        @Override
        public int compare(PuzzleBean p1, PuzzleBean p2) {
            // TODO Auto-generated method stub
            int id1 = p1.getPuzzleID();
            int id2 = p2.getPuzzleID();
            return id1 - id2;
        }
    }
}
