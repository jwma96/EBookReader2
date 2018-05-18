package com.example.d.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_book);
        readingText=(TextView)findViewById(R.id.readingText);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
       Uri=bundle.getString("Uri");
      //   position=bundle.getInt("position");
        currentPage=bundle.getInt("currentPage");
         bId=bundle.getInt("bookId");
        file=new File(Uri);
        String a= ProcessText(file,1);//页码从1开始
        readingText.setText(a);
        initView();
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

}
