package com.example.d.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class sharePic extends AppCompatActivity {
    private TextView textView;//显示图片的文本框
    private Button btnback;//返回键
    private Button btnsave;//下载图片保存到手机
    private Button btnshare;//分享图片到微信

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
        btnsave=(Button)findViewById(R.id.save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               savePic();//生成图片并保存到本地
               saveImageToGallery(sharePic.this,savePic());
                Toast.makeText(sharePic.this, "图片已保存", Toast.LENGTH_SHORT).show();
               // finish();
            }
        });
        btnshare=(Button)findViewById(R.id.share);
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //
                // savePic();//生成图片并分享
                savePic();
              Uri pic= saveImageToGallery(sharePic.this,savePic());
                //Environment.getExternalStorageDirectory()+"/mybook/picName"
                Intent intent = new Intent();
               // ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI");
                    intent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");//微信朋友
              //  intent.setComponent(comp);
                intent.setAction(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM ,pic);//uri为你要分享的图片的uri
                startActivity(intent);

            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
    return true;
    }
    public Bitmap savePic(){
        Bitmap bitmap = Bitmap.createBitmap(textView.getWidth() , textView.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);

        textView.draw(c);
        return bitmap;
    }
    public static Uri  saveImageToGallery(Context context, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "myBook");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        Uri img=Uri.fromFile(file);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);//第二个参数为100表示不压缩即压缩率为0
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //通知系统图库更新
        try {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
        }catch (Exception e){
            e.printStackTrace();
        }
        return img;
    }

}
