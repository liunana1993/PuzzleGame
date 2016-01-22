package com.puzzle.app.puzzlegame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.puzzle.app.puzzlegame.R;
import com.puzzle.app.puzzlegame.Bean.PuzzleBean;

import java.util.List;

/**
 * Created by LiuNana on 2016/1/22.
 */
public class PuzzleAdapter extends BaseAdapter {
    List<PuzzleBean> list;
    Context context;
    public PuzzleAdapter(Context context, List<PuzzleBean> members2) {
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
                    R.layout.item_puzzle, parent, false);
            holder=new ViewHolder();
            holder.img =  (ImageView)convertView.findViewById(R.id.iv_pic);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        PuzzleBean res = list.get(position);
        holder.img.setImageBitmap(res.getBitmap());
        return convertView;
    }
}
