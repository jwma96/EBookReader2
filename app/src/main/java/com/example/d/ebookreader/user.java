package com.example.d.ebookreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class user extends AppCompatActivity {
    private Button btn;//返回键
    private TextView userName;//用户名
    private SharedPreferences pref;//
    private SharedPreferences.Editor editor;
    private Button btnexit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.activity_user);
        btn=(Button)findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        String name=pref.getString("account","");
        userName=(TextView)findViewById(R.id.user_name);
        userName.setText("用户"+name);
        btnexit=(Button)findViewById(R.id.exit);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor=pref.edit();
                editor.putBoolean("isLogin",false);//记录是否登录
                editor.commit();
                Intent intent=new Intent(user.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
