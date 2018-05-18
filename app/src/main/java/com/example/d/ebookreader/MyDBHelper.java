package com.example.d.ebookreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by d on 2018/4/12.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="sqlite.db";
    private static final int DATABASE_VERSION=1;
    private Context mcontext;
   // private static final String TABLE_NAME="bookshelf";
    public static final String BOOK_SHELF="create table  if not exists  myBook "
            + "(id integer primary key autoincrement ,"
            +"bName text ,"
            +"currentpage integer,"
           + "position integer,"
            +"uri text )";
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mcontext=context;
    }
    public void onCreate(SQLiteDatabase db){
      //  db.execSQL("create table if not exists user"+"(username varchar primary key,"
             //   +"password varchar)");
        db.execSQL(BOOK_SHELF);
        db.execSQL("create table if not exists user"+"(username varchar primary key,"
                        +"password varchar)");
        Toast.makeText(mcontext,"succeeded",Toast.LENGTH_SHORT).show();


    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newversion){
    }
}
