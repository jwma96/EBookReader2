package com.example.d.ebookreader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class noteList extends AppCompatActivity {
    private List<mynote> list=new ArrayList<mynote>();
    private MyDBHelper dbHelper;
    private SQLiteDatabase db;
    private ListView notel;
    private ContentValues values;
    private Cursor cursor;
    private mynote mn;
    private noteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        notel=(ListView)findViewById(R.id.note_list);
        dbHelper=new MyDBHelper(this,"bookreader.db",null,1);
        //db=ma.db;
        db=dbHelper.getWritableDatabase();
        init();
        adapter=new noteAdapter(noteList.this,list);
        notel.setAdapter(adapter);
        notel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String t= list.get(i).getNoteName();
                String n=list.get(i).getNote();
                Intent intent=new Intent(noteList.this,readNote.class);
                intent.putExtra("notetitlt",t);
                intent.putExtra("note",n);
                startActivity(intent);
            }
        });
    }
    public  void init(){
        cursor=db.query("myNote",null,null,null,null,null,null);//都为null表示查询全部数据

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {

            String notename = cursor.getString(cursor.getColumnIndex("mytitle"));
            String note = cursor.getString(cursor.getColumnIndex("note"));

            mn = new mynote(notename, note,R.drawable.book_brown);
            list.add(0, mn);//保证倒叙插入
        }

    }
}
