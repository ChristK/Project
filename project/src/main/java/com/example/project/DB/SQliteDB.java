package com.example.project.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class SQliteDB extends SQLiteOpenHelper {
    //context:上下文
    //name:数据库名称
    //factory:游标工厂
    //version:数据库版本号，并且版本号大于0
    public static final int VERSION=2;
    public SQliteDB(Context context) {
        super(context, "ProjectDB", null, VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserInfo(id integer primary key autoincrement," +
                "Username text," +
                "Password text," +
                "Email text," +
                "Image blob," +
                "Location text," +
                "Longitute real," +
                "Latitute real," +
                "Time integer," +
                "Text text)");

    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion==2||oldVersion==1){
            //Update UserInfo
            //1.delete table
            String sql=("drop table if exists UserInfo");
            db.execSQL(sql);
            //2.create table
            String sql2=("create table UserInfo(id integer primary key autoincrement," +
                    "Username text," +
                    "Password text," +
                    "Email text," +
                    "Image blob," +
                    "Location text," +
                    "Longitute real," +
                    "Latitute real," +
                    "Time integer," +
                    "Text text)");
            db.execSQL(sql2);

        }
    }
}
