package com.example.d.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class noteWrite extends AppCompatActivity {
    private TextView title;//笔记名称
    private EditText note;//笔记
    private Button saveBtn;//保存按钮
    private String bname;//书籍名称
    private String titleofNote;//存储笔记名称
    private String mynote;//存储笔记内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_write);
        title=(TextView)findViewById(R.id.title);
        note=(EditText)findViewById(R.id.mynote);
        saveBtn=(Button)findViewById(R.id.saveBtn);
        Intent intent=getIntent();
    bname=    intent.getStringExtra("bookname");
        Calendar calendar = Calendar.getInstance();//获取系统的日期

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;//从0开始编号
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        title.setText(bname+"\n"+year+"-"+month+"-"+day+"      "+hour+":"+minute+":"+second);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleofNote=title.getText().toString();
                mynote=note.getText().toString();
                if (mynote==null){
                    Toast.makeText(noteWrite.this, "笔记内容为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    //创建数据库，插入数据库
                    Toast.makeText(noteWrite.this, "保存成功"+mynote, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
