package com.example.d.ebookreader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class secBookShelf extends AppCompatActivity {
    private View view1,view2;
    private GridView gridView;
    private List<View> viewList;//view数组
    private ViewPager viewPager;
    private List<String> titleList;//标题栏
    private PagerTabStrip pagerTabStrip;
    //gridview数据
  private List<bookGrid> mDatas;
    private List<Map<String, Object>> dataList;
    private GridViewAdapter adapter;
    private boolean iso=false;//判断这个活动是否是第一次打开

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_book_shelf);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        pagerTabStrip=(PagerTabStrip)findViewById(R.id.pagertab);
        pagerTabStrip.setTabIndicatorColor(Color.BLUE);
        LayoutInflater inflater=getLayoutInflater();
        view1=inflater.inflate(R.layout.paga1_shelf,null);
        view2=inflater.inflate(R.layout.page2_my,null);
     gridView=(GridView) view1.findViewById(R.id.gridview);//从装有gridview的view里面找到gridview
        //设置gridview
        mDatas=new ArrayList<bookGrid>();
      /*  mDatas.add(R.drawable.ic_launcher);//book pic
        mDatas.add(R.drawable.ic_launcher);
        mDatas.add(R.drawable.ic_launcher);
        mDatas.add(R.drawable.ic_launcher);
        mDatas.add(R.drawable.ic_launcher);*/
        adapter=new GridViewAdapter(view1.getContext(),mDatas);
        gridView.setAdapter(adapter);
        viewList=new ArrayList<View>();//初始化viewpager中的view
        viewList.add(view1);
        viewList.add(view2);
        titleList=new ArrayList<String>();
        titleList.add("书架");
        titleList.add("我的");
        Intent intent=getIntent();
        String data= intent.getStringExtra("name");
      // Log.d("bs",data);
        if (iso==false){
            iso=true;
        }
        else{
            iso=false;
            Toast.makeText(getApplicationContext(), data,
                    Toast.LENGTH_SHORT).show();
            fileList fl=new fileList();
            bookFile bf=fl.list_map.get(Integer.parseInt(data));
            bookGrid bg=new bookGrid(bf.getName(),R.drawable.ic_launcher,bf.getUri());
          //  bookGrid bg=new bookGrid(fl.list_map.get(Integer.parseInt(data));
            mDatas.add(bg);
            adapter=new GridViewAdapter(view1.getContext(), mDatas);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==parent.getChildCount()-1){
                    Intent intent=new Intent(secBookShelf.this,fileList.class);
                    startActivity(intent);
                   // finish();

//写入Intent传递回来的数据
                    //mDatas.add();//new bool pic 0add to head
                    //Toast.makeText(MainActivity.this, "您点击了添加",Toast.LENGTH_SHORT).show();

                }

            }
        });

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));

                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titleList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
}
