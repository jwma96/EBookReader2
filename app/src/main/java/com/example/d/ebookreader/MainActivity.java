package com.example.d.ebookreader;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public MyDBHelper helper;
    public SQLiteDatabase db;
    private CheckBox ck;
    private SharedPreferences pref;
    public static final String TABLE_NAME="user";
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper=new MyDBHelper(this,"bookreader.db",null,1);
        TextView t1 = (TextView) findViewById(R.id.name);//butterKnife优化
        TextView t2 = (TextView) findViewById(R.id.pass);
        Button login = (Button) findViewById(R.id.login);
        Button reg = (Button) findViewById(R.id.reg);

        ck=(CheckBox)findViewById(R.id.ck1);
        login.setOnClickListener(this);
        reg.setOnClickListener(this);
        ck.setOnClickListener(this);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRem=pref.getBoolean("remember_password",false);
        if(isRem){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            t1.setText(account);
            t2.setText(password);

        }

    }
    public void onClick(View v){
        boolean flag=false;
        Button btn=(Button)v;
        TextView t1 = (TextView) findViewById(R.id.name);
        TextView t2 = (TextView) findViewById(R.id.pass);
        String name=t1.getText().toString();
        String pass=t2.getText().toString();
        Log.d("MainActivity","ceshi "+name);
        Log.d("MainActivity","ceshi "+pass);
        db=helper.getWritableDatabase();
        Cursor cursor=db.query("user",null,null,null,null,null,null);
        if(btn.getId()==R.id.reg){
            //if username exists
            if(name.length()<6||pass.length()<6||name.length()>12||pass.length()>12){
                Toast.makeText(getApplicationContext(), "用户名或密码不能少于六位或大于十二位",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            ContentValues values=new ContentValues();
            values.put("username",name);
            values.put("password",pass);
            db.insert("user",null,values);
            values.clear();
            Toast.makeText(getApplicationContext(), "success",
                    Toast.LENGTH_SHORT).show();
        }
        if(btn.getId()==R.id.login){
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                String name1=cursor.getString(cursor.getColumnIndex("username"));
                String pass1=cursor.getString(cursor.getColumnIndex("password"));

                if(name.equals(name1)&&pass.equals(pass1)){
                    //success cursor.getString(cursor.getColumnIndex("username"))
                    //==is error use equals
                    flag=true;
                }
            }
            if(flag){
                editor=pref.edit();
                if(ck.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("account",name);
                    editor.putString("password",pass);
                }
                else{
                    editor.clear();
                }
                editor.commit();
                Intent intent=new Intent(MainActivity.this,secBookShelf.class);
                // Intent intent=new Intent(MainActivity.this,filetest.class);
                //创建数据库
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "username or password error!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
