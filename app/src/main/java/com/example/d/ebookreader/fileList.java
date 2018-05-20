package com.example.d.ebookreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class fileList extends AppCompatActivity {
    private int ch;
    public int currentItem;
    public List<bookFile> list_map=new ArrayList<bookFile>();
    boolean finish=false;
    private bookFile bf;
    private LinearLayout l1;
    private ListView listView;
    private TextView wt;
    private CheckBox cb;
  //  private boolean firstCk;//第一次被选中弹出popup window
    private int cNum=0;// 选中次数
  //  private String[] fl;//存储文件路径
  //  Vector fl=new Vector();
    private int i=0;//文件路径数组
    //private boolean[] isclick;//判断listview的item是否被点击
    public Vector isclick=new Vector();//判断listview的item是否被点击
    private int itemNum;//item的个数
    private PopupWindow mPopup;
    private Boolean isFirst=true;//判断是否应该弹出popup window
    //private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    private   FileAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
       l1=(LinearLayout)findViewById(R.id.L1);//l1加载  可改为gifView
        listView=(ListView)findViewById(R.id.list_view);//lView列表
        cb=(CheckBox)findViewById(R.id.cb_st);//复选框
        wt=(TextView)findViewById(R.id.wT);
        //listView.setOnItemClickListener();

        loadTask lt=new loadTask();
        lt.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //判断当前是不是第一个选中的，若是则弹出popup window
                //利用fl[i]得到选中文件的path
                //设置复选框被选中
                //若不是则判断当前是否被选中的为0，若是则收回popup window
                //list_map.get(1).getIsCho()
//点击后在标题上显示点击了第几行
                // setTitle("你点击了第"+arg2+"行");
               // currentItem=arg2;
                adapter.setSelectedPosition(arg2);
                adapter.notifyDataSetInvalidated();
                Toast.makeText(getApplicationContext(), list_map.get(arg2).getUri(),
                        Toast.LENGTH_SHORT).show();

                if((boolean)isclick.elementAt(arg2)==true){//arg2从0开始计数
                    cNum--;
                    isclick.setElementAt(new Boolean(false),arg2);
                    Log.i("set","cNum: "+cNum);
                    Log.i("set","isclick: "+isclick.elementAt(arg2));
                    adapter.notifyDataSetChanged();
                    //取消复选框
                   // cb.setChecked(false);
                }
                else {
                    cNum++;
                    isclick.setElementAt(new Boolean(true),arg2);

                  //  cb.setChecked(true);
                    //设置复选框被选中
                    Log.i("set","cNum: "+cNum);
                    Log.i("set","isclick: "+isclick.elementAt(arg2));
                    adapter.notifyDataSetChanged();
                }
                if(cNum==1){
                    ch=arg2;
                   // cb.setChecked(true);
                    //popup window
                    if(isFirst==true){
                        isFirst=false;
                    Toast.makeText(getApplicationContext(), "弹出窗口",
                            Toast.LENGTH_SHORT).show();
                    showPopupWindow();
                    }
                }
                if(cNum==0){
                    //cb.setChecked(false);
                    isFirst=true;
                    //收回popup window
                    Toast.makeText(getApplicationContext(), "收回窗口",
                            Toast.LENGTH_SHORT).show();
                    mPopup.dismiss();
                }


            }
        });

        //File sdDir = Environment.getExternalStorageDirectory();
    }

    //显示popup window
    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(fileList.this).inflate(R.layout.popup_window, null);
        mPopup = new PopupWindow(contentView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopup.setContentView(contentView);
        mPopup.setOutsideTouchable(false);//防止popup window 弹出不正常
        mPopup.setFocusable(false);
        //设置popup window的点击响应
        final Button btnIm=(Button)contentView.findViewById(R.id.btn_im);
        btnIm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=list_map.get(ch).getName();
                String Uri=list_map.get(ch).getUri();
                Bundle bundle=new Bundle();
                bundle.putString("name",name);
                bundle.putString("Uri",Uri);
                bundle.putInt("num",ch);
                Toast.makeText(getApplicationContext(), "导入文件",
                        Toast.LENGTH_SHORT).show();
                //导入
                Intent intent=new Intent(fileList.this,secBookShelf.class);
                intent.putExtras(bundle);
                intent.putExtra("num",ch+"");
                mPopup.dismiss();//防止出现窗体泄露的情况
                startActivity(intent);
                fileList.this.finish();//保证按返回键时返回上一个页面
            }
        });
        //显示PopupWindow
        View rootview = LayoutInflater.from(fileList.this).inflate(R.layout.activity_file_list, null);
        mPopup.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }
    private void initFile(){
        Log.i("init","正在加载");
        File path = Environment.getExternalStorageDirectory();
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)) {
            File[] files = path.listFiles();// 读取文件夹下文件
           getFileName(files);

            Log.i("init","加载完毕");
        }
        finish=true;
    }
    private String getFileName(File[] files) {
        float n=1024;
        String str = "";
        String uri="";
        if (files != null) {	// 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()){//检查此路径名的文件是否是一个目录(文件夹)
                    getFileName(file.listFiles());
                } else {
                    String fileName = file.getName();
                   // fl[i]=file.getPath();//??
                    i++;
                    if (fileName.endsWith(".txt")) {
                        if(file.length()/1024>50) {
                            long size=file.length()/1024;
                            String sizes;
                            if(size<1024){
                                sizes=size+"kb";
                            }else {
                                sizes=(float)(Math.round(size/n*100))/100+"M";//保留到小数点后两位
                            }
                            //String s=fileName.substring(0,fileName.lastIndexOf(".")).toString();
                            str = fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                            uri=file.getAbsolutePath();
                           // Map<String, Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
                            bookFile bf=new bookFile(str, R.drawable.txt,sizes,false,uri);
                            //fileList.add(bf);
                           // items.put("pic", R.drawable.txt);
                           // items.put("name", str);
                            list_map.add(bf);
                        }

                    }
                }
            }

        }
        return str;
    }
class loadTask extends AsyncTask<Void,Void,Void>{
    protected  void onPreExecute(){
        l1.setVisibility(View.VISIBLE);

    }
    protected  Void doInBackground(Void... parms) {
        initFile();
        //return null;
     //   Log.i("ope","listview初始化完毕");
        return null;
    }
    protected void onPostExecute(Void avoid){
       // if(list_map.size()==0){
         //  wt.setText("无txt文件");
        //}
       // else {
            adapter = new FileAdapter(fileList.this, R.layout.list_items, list_map);
            // ListView listView=(ListView)findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            itemNum = listView.getCount();//获取item的数量
            //   for(int m=0;m<itemNum;m++){
            //    isclick[m]=false;//初始化是否被选中，注意数组从0开始编号，item可能从1开始编号
            //}
            for (int m = 0; m < itemNum; m++) {
                isclick.addElement(new Boolean(false));//初始化是否被选中，注意数组从0开始编号，item可能从1开始编号
            }

            Log.i("set", "准备切换界面");
            l1.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
   // }
}
}
