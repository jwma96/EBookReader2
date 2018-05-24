package com.example.d.ebookreader;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class ReadingBook extends AppCompatActivity {
    private File file;
   // private int position;//在gridview中的位置，待删除
    private int currentPage;//书签
    private int bId;//书籍在数据库表格中的位置
    private String Uri;
    private String bookname;
    private RandomAccessFile raf;
    private long bcount,pagenum;//总字节数 总页面数
    private TextView readingText;//显示文本的TextView
    private int height;
    private int width;
    private LinearLayout linearLayout;
    private float dx,dy,ux,uy;
    private  byte [] buff;
    private byte [] b;
    private int size;
    private MyDBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);
        readingText=(TextView)findViewById(R.id.readingText);
       /* DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        ViewGroup.LayoutParams params = readingText.getLayoutParams();
        params.height=height/10;
        //params.width =width/10;
        readingText.setLayoutParams(params);//设置textview的高度和宽度*/
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
       Uri=bundle.getString("Uri");
     bookname=bundle.getString("bName");
      //   position=bundle.getInt("position");
        currentPage=bundle.getInt("currentPage");
       //  bId=bundle.getInt("bookId");
        file=new File(Uri);
        String a= ProcessText(file,1);//页码从1开始
        readingText.setText(a);
        initView();

       //readingText.setCustomSelectionActionModeCallback(new MyActionModeCallback());
        readingText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                readingText.setTextIsSelectable(true);
                //Toast.makeText(ReadingBook.this, "hahahah", Toast.LENGTH_SHORT).show();
               readingText.setCustomSelectionActionModeCallback(new MyActionModeCallback());
                return false;
                //如果将onLongClick返回false，那么执行完长按事件后，还有执行单击事件。
               // 如果返回true，只执行长按事件
            }
        });
    }
    protected void onStart() {
        super.onStart();
        final TextView tv1 = (TextView) findViewById(R.id.readingText);
        tv1.post(new Runnable() {
            @Override
            public void run() {
                size=getCharNum();
                pagenum=bcount/(size*2);
            }
        });

    }
    //重写返回键方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
           // String deleteData="DELETE FROM myBook WHERE id =1";
            dbHelper=new MyDBHelper(this,"bookreader.db",null,1);
            db=dbHelper.getWritableDatabase();
            db.delete("myBook","bName=?",new String[]{bookname} );
            //db.delete("Score", "name = ?", new String[] { name });
            //  db.execSQL(deleteData);//删除某条数据后重新插入，再次加载bookshelf的时候就会刷新书架的顺序
            values=new ContentValues();
            values.put("bName",bookname);
            values.put("currentpage",currentPage);//0还是1？？？
            values.put("position",0);//记录在gridview中的位置，可能没用了
            values.put("uri",Uri);
            db.insert("myBook",null,values);
            Intent intent=new Intent(ReadingBook.this,secBookShelf.class);
            startActivity(intent);
            db.close();
            ReadingBook.this.finish();

        }
        return true;
    }
    private void initView(){
        //show = (TextView)super.findViewById(R.id.show);
        linearLayout = (LinearLayout)super.findViewById(R.id.line1);
        //final      TextView t=(TextView)findViewById(R.id.txt);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("---action down-----");
                        dx=event.getX();
                        dy=event.getY();
                        //   show.setText("起始位置为："+"("+event.getX()+" , "+event.getY()+")");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //System.out.println("---action move-----");
                        //show.setText("移动中坐标为："+"("+event.getX()+" , "+event.getY()+")");
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("---action up-----");
                        ux=event.getX();
                        uy=event.getY();

                        if(dx>readingText.getWidth()/2){//页面三等分，中间跳出设置 设置字体大小时应重新加载页面
                            //  Toast.makeText(MainActivity.this, "next", Toast.LENGTH_SHORT).show();
                            readingText.setText(getNext());
                        }
                        else {
                            readingText.setText(getPre());

                            //加载上一页
                            //Toast.makeText(MainActivity.this, "pre", Toast.LENGTH_SHORT).show();
                            //show.setText("pre："+"("+event.getX()+" , "+event.getY()+")");
                        }
                        break;
                    //  show.setText("最后位置为："+"("+event.getX()+" , "+event.getY()+")");
                }
                return true;
            }
        });
    }
    private void seek(long page){
        try {
            //if(pages)
            raf.seek(page*size*2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getPre(){
        String content = null;
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        if(currentPage <= 1){
            seek(currentPage-1);
            content =read();
        }else{
            //其它页 则定位到当前页-2 处 在读取指定字节内容 例如当前定位到第二页的末尾 2*SIZE  上一页应该是第一页 也就是从0位置 开始读取SIZE个字节
            seek(currentPage-2);
            content = read();
            currentPage = currentPage - 1;
        }

        return content;
    }
    public String getNext(){
        String content = null;
        if(currentPage >= pagenum){//当前页为最后一页时候,显示的还是 最后一页
            seek(currentPage-1);
            content = read();
        }else{
            seek(currentPage);
            content = read();
            currentPage = currentPage +1;
        }

        return content;
    }
    private String read(){
        //内容重叠防止 末尾字节乱码
        b= new byte[size*2];
        try {

            raf.read(b);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //String res=EncodingUtils.get
        return new String(b, Charset.forName("GBK"));//gbk中一个字占2个字节
    }
    //读取一些文本数据
    public String  ProcessText(File file, int currentpage)  {//currentPage实现书签

        try {
            //  size=getEstimatedLength();

            raf = new RandomAccessFile(file,"r");


            bcount = raf.length();//获得字节总数
            //pagenum = bcount/size;//计算得出文本的页数  有问题，需要改
            this.currentPage=currentpage;
            raf.seek(0);//调整读取文本的起始位置
            buff= new byte[8000];//2*byte=一个字节
            raf.read(buff);
            return new String(buff, Charset.forName("GBK"));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 获取当前页总字数
     */
    public int getCharNum() {
        return readingText.getLayout().getLineEnd(getLineNum());//最后一行最后一个字在整个字符串中的位置
    }

    /**
     * 获取当前页总行数
     */
    public int getLineNum() {
        Layout layout = readingText.getLayout();
        int topOfLastLine = readingText.getHeight() - readingText.getPaddingTop() - readingText.getPaddingBottom() - readingText.getLineHeight();//高度-上下的padding-一行的高度   得到最后一行的纵坐标
        int res=layout.getLineForVertical(topOfLastLine);//根据纵坐标得到对应的行数
        return res;
    }
    private class MyActionModeCallback implements ActionMode.Callback {
        private Menu mMenu;
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

            return true;
        }
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

            MenuInflater menuInflater = actionMode.getMenuInflater();
            menu.clear();//屏蔽其他item
            menuInflater.inflate(R.menu.menu,menu);
            return true;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.it_copy:
                    String selectText = getSelectText(SelectMode.COPY);
                    Toast.makeText(ReadingBook.this, "选中的内容已复制到剪切板", Toast.LENGTH_SHORT).show();
                    readingText.setTextIsSelectable(false);
                    actionMode.finish();
                    break;
                case R.id.it_cut:
                    //剪藏
                    String txt = getSelectText(SelectMode.COPY);
                    ClipboardManager cbs = (ClipboardManager)ReadingBook.this.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData=cbs.getPrimaryClip();
                    CharSequence mytext;
                    if (clipData != null && clipData.getItemCount() > 0) {
                         mytext = clipData.getItemAt(0).getText();//从数据集获取已复制的第一条数据文本？？？

						 //剪贴板中保存的所有剪贴数据集（剪贴板可同时复制/保存多条多种数据条目）
						 //剪贴数据集中的一个数据条目
                        Intent intent=new Intent(ReadingBook.this,sharePic.class);
                      //  intent.putCharSequenceArrayListExtra("text",mytext);
                        intent.putExtra("text",mytext);
                        //书名也应该传递过去
                        startActivity(intent);
                        readingText.setTextIsSelectable(false);
                        actionMode.finish();
                    }else {
                        Toast.makeText(ReadingBook.this, "剪藏失败", Toast.LENGTH_SHORT).show();
                    }

                   // readingText.setText(txt);
                  // Toast.makeText(ReadingBook.this, txt, Toast.LENGTH_SHORT).show();
                    //跳转到另一个生成图片的界面

                    break;
                case R.id.it_write:
                    Toast.makeText(ReadingBook.this, bookname, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ReadingBook.this,noteWrite.class);
                   // Bundle bundle=new Bundle();
                   // bundle.putString("bookName",bookname);
                    intent.putExtra("bookname",bookname);
                    startActivity(intent);
                    actionMode.finish();
                    //this.mMenu.close();
                    break;



            }
            return true;//只有自定义的item有响应，系统的无效
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }

    /**
     *  统一处理复制和剪切的操作
     * @param mode 用来区别是复制还是剪切
     * @return
     */
    private String getSelectText(SelectMode mode) {
        //获取剪切板管理者
        ClipboardManager cbs = (ClipboardManager)ReadingBook.this.getSystemService(CLIPBOARD_SERVICE);
        //获取选中的起始位置
        int selectionStart = readingText.getSelectionStart();
        int selectionEnd = readingText.getSelectionEnd();
        Log.i("选择文本","selectionStart="+selectionStart+",selectionEnd="+selectionEnd);
        //截取选中的文本
        String txt = readingText.getText().toString();
        String substring = txt.substring(selectionStart, selectionEnd);
        Log.i("选择文本","substring="+substring);
        //将选中的文本放到剪切板
        cbs.setPrimaryClip(ClipData.newPlainText(null,substring));
        //如果是复制就不往下操作了
        if (mode==SelectMode.COPY)
            return txt;
        txt = txt.replace(substring, "");
        return txt;
    }

    /**
     * 用枚举来区分是复制还是剪切
     */
    public enum SelectMode{
        COPY,CUT;
    }
    }
