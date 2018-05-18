package com.example.d.ebookreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class secBookShelf extends AppCompatActivity {
    private View view1,view2;
    private GridView gridView;
    private List<View> viewList;//view数组
    private ViewPager viewPager;
    private List<String> titleList;//标题栏
    private PagerTabStrip pagerTabStrip;
    //private MyDBHelper helper;
    //gridview数据
  private List<bookGrid> mDatas;
 //   private List<Map<String, Object>> dataList;
    private GridViewAdapter adapter;
    private boolean iso=false;//判断这个活动是否是第一次打开
    private EditText searchText;//搜索框
    private Button serachBtn;//搜索按钮
    private Button user;//用户按钮
    private String searchName;
    private MainActivity ma;
    private MyDBHelper dbHelper;
    private SQLiteDatabase db;
    private    ContentValues values;
    private Cursor cursor;
    private bookGrid bg;
    private int   []currentPage;
    private int []bookId;//存储记录在数据库中的id，方便修改
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sec_book_shelf);
        searchText=(EditText) findViewById(R.id.searchText);
        serachBtn=(Button)findViewById(R.id.searchBtn);
        serachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchName=searchText.getText().toString();
                //根据searchName搜索书架
            }
        });
        user=(Button)findViewById(R.id.my);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(secBookShelf.this,user.class);
                startActivity(intent);//跳转到用户界面
            }
        });
        viewPager=(ViewPager)findViewById(R.id.viewpager);
     //   helper=new MyDBHelper(this,"user.db",null,1);
        pagerTabStrip=(PagerTabStrip)findViewById(R.id.pagertab);
        pagerTabStrip.setTabIndicatorColor(Color.BLUE);
        LayoutInflater inflater=getLayoutInflater();
        view1=inflater.inflate(R.layout.paga1_shelf,null);
        view2=inflater.inflate(R.layout.page2_my,null);
     gridView=(GridView) view1.findViewById(R.id.gridview);//从装有gridview的view里面找到gridview
        //设置gridview
       // ma=new MainActivity();
        dbHelper=new MyDBHelper(this,"bookreader.db",null,1);
         //db=ma.db;
        db=dbHelper.getWritableDatabase();
        values=new ContentValues();
        init();

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
       // int num=bundle.getInt("num");
        if(data!=null){
            Bundle bundle=intent.getExtras();
            String name=bundle.getString("name");
            String Uri=bundle.getString("Uri");
            iso=false;
            Toast.makeText(getApplicationContext(), "hahaha",
                    Toast.LENGTH_SHORT).show();

            fileList fl=new fileList();
           // int i=Integer.parseInt(data);
           // bookFile bf=fl.list_map.get(i);
            bookGrid bg=new bookGrid(name,R.drawable.books,Uri);
          //  bookGrid bg=new bookGrid(fl.list_map.get(Integer.parseInt(data));
            //添加到数据库
          //  values.put("id",1);
            values.put("bName",name);
            values.put("currentpage",0);//0还是1？？？
            values.put("position",0);//记录在gridview中的位置，可能没用了
           values.put("uri",Uri);
            db.insert("myBook",null,values);//将新增的数据插入数据库
            mDatas.add(0,bg);
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
                   finish();

//写入Intent传递回来的数据
                    //mDatas.add();//new bool pic 0add to head
                    //Toast.makeText(MainActivity.this, "您点击了添加",Toast.LENGTH_SHORT).show();

                }
                else {
                //if(position==0){//不加if会出现Exception in MessageQueue callback: handleReceiveCallback
                    //多次触发触摸事件
                   // Toast.makeText(secBookShlf.this, position,Toast.LENGTH_SHORT).show();
                   // Log.i("po","hahah");
                   // searchText.setText("text");
                    String Uri=mDatas.get(position).getUri();
                    int cupage=currentPage[position];
                    int myd=bookId[position];
                    Bundle bundle=new Bundle();
                   // bundle.putInt("position",position);
                    bundle.putInt("bookId",myd);
                    bundle.putString("Uri",Uri);
                    bundle.putInt("currentPage",cupage);
                    Intent intent=new Intent(secBookShelf.this,ReadingBook.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
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
    public void init(){
        mDatas=new ArrayList<bookGrid>();
   //     bookGrid bg;
      //  SQLiteDatabase db=helper.getWritableDatabase();
        //  bookGrid bg=new bookGrid(name,R.drawable.ic_launcher,Uri);
       // mDatas.add(0,bg);
        cursor=db.query("myBook",null,null,null,null,null,null);//都为null表示查询全部数据
        int num=cursor.getCount();//获取数据库中的数据条数
        currentPage=new int[num];
       bookId=new int[num];
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex("bName"));
            String uri=cursor.getString(cursor.getColumnIndex("uri"));
            //获取当前页
            int cpage=cursor.getInt(cursor.getColumnIndex("currentpage"));
            int bId=cursor.getInt(cursor.getColumnIndex("id"));
            currentPage[num-1]=cpage;
            bookId[num-1]=bId;
            num--;
            bg=new bookGrid(name,R.drawable.books,uri);
            mDatas.add(0,bg);//保证倒叙插入
           // num--;用add（num，bg）会出现第一个插入的数据在listview为空的情况下插入，会报错 越界
        }

    }
}
