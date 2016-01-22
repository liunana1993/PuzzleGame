package com.puzzle.app.puzzlegame.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.puzzle.app.puzzlegame.Bean.ResImg;
import com.puzzle.app.puzzlegame.R;
import com.puzzle.app.puzzlegame.util.FileUtil;
import com.puzzle.app.puzzlegame.util.WheelDialogShowUtil;
import com.puzzle.app.puzzlegame.view.DialogView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WheelDialogShowUtil wheelUtil ;
    private Context context;
    private String[] diff_array;
    private List<ResImg> mPicList = new ArrayList<>();
    private GridView mGridView;
    private GridAdapter adapter;
    private Activity activity;
    private String imageName;
    private FileUtil fileUtil;
    private int difficulty_index = 3;
    private static int IMG_SCALE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        activity = this;
        fileUtil = new FileUtil(context, Constant.IMAGE_PATH);
        fileUtil.getAbsolutePath();
        int density = (int)context.getResources().getDisplayMetrics().density;
        IMG_SCALE = density * 40;
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView tv_difficulty = (TextView) findViewById(R.id.tv_difficulty);
        mGridView = (GridView) findViewById(R.id.gv_img_list);
        setSupportActionBar(toolbar);
        setTitle(R.string.puzzle_game);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crop.pickImage(activity);
            }
        });

        diff_array = getResources().getStringArray(R.array.difficulty);
        tv_difficulty.setText(diff_array[1]);
        tv_difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelUtil = new WheelDialogShowUtil(
                        context,
                        getWindowManager().getDefaultDisplay(),
                        diff_array,
                        "选择拼图难度");
                wheelUtil.setWheelHint(1);
                difficulty_index = 3;
                wheelUtil.dialogView.setBtnPosClick(new DialogView.onWheelBtnPosClick() {
                    @Override
                    public void onClick(String text, int position) {
                        // TODO Auto-generated method stub
                        wheelUtil.dissmissWheel();
                        wheelUtil.setTextToView(tv_difficulty, text);
                        difficulty_index = position + 2;
                    }
                });
                wheelUtil.showWheel();
            }
        });

        adapter = new GridAdapter(context, mPicList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResImg res = mPicList.get(position);
                startActivity(new Intent()
                        .setClass(context, PuzzleActivity.class)
                        .putExtra("src", res.getSrc())
                        .putExtra("flag", res.getFlag())
                        .putExtra("res", res.getRes())
                        .putExtra("difficulty", difficulty_index)
                        );
            }
        });

        int[] mResPicId = new int[]{
               R.drawable.pic1, R.drawable.pic2, R.drawable.pic3,
               R.drawable.pic4, R.drawable.pic5, R.drawable.pic6,
               R.drawable.pic7

        };

        for (int i=0; i < mResPicId.length; i++){
            BitmapFactory.Options measureOptions = new BitmapFactory.Options();
            /**
             * most important:  options.inJustDecodeBounds = true;
             * decodeFile()，return bitmap=null，but options.outHeight = img's height
             */
            measureOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(
                    getResources(),mResPicId[i], measureOptions); // 此
            int scale = Math.min(measureOptions.outWidth,measureOptions.outHeight) / IMG_SCALE;
            scale = Math.max(scale, 1);

            InputStream is = this.getResources().openRawResource(mResPicId[i]);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig =  Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            Bitmap bitmaps = BitmapFactory.decodeStream(
                    is,null,options);
            ResImg res = new ResImg();
            res.setBitmap(bitmaps);
            res.setFlag(1);
            res.setRes(mResPicId[i]);
            mPicList.add(res);
        }
        adapter.notifyDataSetChanged();

        File file = new File(Constant.IMAGE_PATH);
        if(file.exists()){
            File[] subFile = file.listFiles();
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                if (!subFile[iFileLength].isDirectory()) {
                    String filePath = subFile[iFileLength].getAbsolutePath();

                    try{
                        BitmapFactory.Options measureOptions = new BitmapFactory.Options();
                        /**
                         * most important:  options.inJustDecodeBounds = true;
                         * decodeFile()，return bitmap=null，but options.outHeight = img's height
                         */
                        measureOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(filePath,measureOptions);
                        int scale = Math.min(measureOptions.outWidth,measureOptions.outHeight) / IMG_SCALE;
                        scale = Math.max(scale, 1);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig =  Bitmap.Config.RGB_565;
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scale;
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
                        if(bitmap != null){
                            ResImg res = new ResImg();
                            res.setBitmap(bitmap);
                            res.setFlag(2);
                            res.setSrc(filePath);
                            mPicList.add(res);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }


    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void beginCrop(Uri source) {
        if(TextUtils.isEmpty(imageName)){
            imageName = System.currentTimeMillis() + ".png";
        }
        Uri destination = Uri.fromFile(new File(fileUtil.getAbsolutePath(), imageName));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK&&!TextUtils.isEmpty(imageName)) {
            String filePath = Constant.IMAGE_PATH+imageName;
            try{
                BitmapFactory.Options measureOptions = new BitmapFactory.Options();
                /**
                 * most important:  options.inJustDecodeBounds = true;
                 * decodeFile()，return bitmap=null，but options.outHeight = img's height
                 */
                measureOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath,measureOptions);
                int scale = Math.min(measureOptions.outWidth,measureOptions.outHeight) / IMG_SCALE;
                scale = Math.max(scale, 1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig =  Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                options.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
                if(bitmap != null){
                    ResImg res = new ResImg();
                    res.setBitmap(bitmap);
                    res.setFlag(1);
                    res.setSrc(filePath);
                    mPicList.add(res);

                    adapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private class GridAdapter extends BaseAdapter {
        List<ResImg> list;
        Context context;
        public GridAdapter(Context context, List<ResImg> members2) {
            this.list = members2;
            this.context = context;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public final class ViewHolder {
            ImageView img;
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            ViewHolder holder ;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_init_pic, parent, false);
                holder=new ViewHolder();
                holder.img =  (ImageView)convertView.findViewById(R.id.iv_pic);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            ResImg res = list.get(position);
            holder.img.setImageBitmap(res.getBitmap());
            return convertView;
        }
    }
}
