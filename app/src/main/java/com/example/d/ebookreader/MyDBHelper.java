package com.example.d.ebookreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by d on 2018/4/12.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="sqlite.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="bookshelf";
    public static final String BOOK_SHELF="create table Book ("
            + "id integer primary key autoincrement ,"
            +"bName varchar ,"
            +"currentpage integer"
            +"book text )";
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }
    public void onCreate(SQLiteDatabase db){
      //  db.execSQL("create table if not exists user"+"(username varchar primary key,"
             //   +"password varchar)");
        db.execSQL(BOOK_SHELF);

    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newversion){
    }
}
