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
 * Created by d on 2018/5/25.
 */

public class noteAdapter extends BaseAdapter {
    private Context context;
    private List<mynote> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    private boolean isShowDelete;
    private ImageView delete;
    public noteAdapter(Context context, List<mynote> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setIsShowdelete(boolean isShowDelete){
        this.isShowDelete=isShowDelete;
        notifyDataSetChanged();
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

        convertView = layoutInflater.inflate(R.layout.note_items, null);
        mImageView = (ImageView) convertView.findViewById(R.id.note_imageView1);
        TextView textView=(TextView)convertView.findViewById(R.id.note_textView1);
      //  delete=(ImageView)convertView.findViewById(R.id.delete);
           // mImageView.setBackgroundResource(R.drawable.book_brown);
            //mImageView.setBackgroundResource(R.drawable.add);
           // textView.setText(list.get(position).getNoteName());



        return convertView;
}}
