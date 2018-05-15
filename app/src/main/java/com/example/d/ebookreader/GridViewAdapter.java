package com.example.d.ebookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by d on 2018/5/2.
 */

public class GridViewAdapter extends BaseAdapter
{
    private Context context;
    private List<bookGrid> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;

    public GridViewAdapter(Context context, List<bookGrid> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size()+1;//注意此处 change size
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.gridview_item, null);
        mImageView = (ImageView) convertView.findViewById(R.id.img);
        TextView textView=(TextView)convertView.findViewById(R.id.bookname);
       if (position < list.size()) {
           mImageView.setBackgroundResource(list.get(position).getImageId());
            //mImageView.setBackgroundResource(R.drawable.add);
           textView.setText(list.get(position).getName());
        }else{
            mImageView.setBackgroundResource(R.drawable.add);//最后一个显示加号图片
           textView.setText("");
        }
        return convertView;

}}
