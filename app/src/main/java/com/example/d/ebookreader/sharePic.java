package com.example.d.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class sharePic extends AppCompatActivity {
    private TextView textView;
    private Button btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_pic);
        textView=(TextView)findViewById(R.id.im_text);
        Intent intent=getIntent();
        String data=intent.getStringExtra("text");
        textView.setText(data);
        btnback=(Button)findViewById(R.id.backfp);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
    return true;
    }
}
