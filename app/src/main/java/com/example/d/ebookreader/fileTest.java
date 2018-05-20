package com.example.d.ebookreader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class fileTest extends AppCompatActivity {
    private TextView textView;
    private LinearLayout l1;
    private String str ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_test);
        textView=(TextView)findViewById(R.id.test_text);
        l1=(LinearLayout)findViewById(R.id.L1);
        //Uri a= Uri.fromFile(new File("/storage/emulated/0/A/a.txt"));
       // textView.setText("hahahha");
   Cursor cursor = fileTest.this.getContentResolver().query(
//数据源
               // MediaStore.Files.getContentUri("external"),//由于版本问题无法使用
                Uri.fromFile(new File("/storage/emulated/0")),

//查询ID和名称
                new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE},
//条件为文件类型
                MediaStore.Files.FileColumns.MIME_TYPE + "= ?",
//类型为“video/mp4”
                new String[]{"txt"},
//默认排序
                null);
        // if(cursor==null)
        //   return;
        //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
        if(cursor.moveToLast())
        {
            do{
                //输出文件的完整路径
                String data=cursor.getString(0);
                //str=str+data;
                textView.append(data);
            }while(cursor.moveToPrevious());
        }
        cursor.close();

        //loadTask lt= new loadTask();
        //lt.execute();
       // textView.setText(str);
    }
  /*  class loadTask extends AsyncTask<Void,Void,Void> {
        protected  void onPreExecute(){
            l1.setVisibility(View.VISIBLE);

        }
        protected  Void doInBackground(Void... parms) {
            //ContentResolver resolver=fileTest.this.getContentResolver();

            return null;
        }
        protected void onPostExecute(Void avoid){
            Cursor cursor = fileTest.this.getContentResolver().query(
//数据源
                    MediaStore.Files.getContentUri("external"),
//查询ID和名称
                    new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.TITLE},
//条件为文件类型
                    MediaStore.Files.FileColumns.MIME_TYPE + "= ?",
//类型为“video/mp4”
                    new String[]{"txt"},
//默认排序
                    null);
            // if(cursor==null)
            //   return;
            //游标从最后开始往前递减，以此实现时间递减顺序（最近访问的文件，优先显示）
            if(cursor.moveToLast())
            {
                do{
                    //输出文件的完整路径
                    String data=cursor.getString(0);
                    //str=str+data;
                    textView.append(data);
                }while(cursor.moveToPrevious());
            }
            cursor.close();


            l1.setVisibility(View.GONE);
           textView.setVisibility(View.VISIBLE);
        }
        // }
    }*/
}
