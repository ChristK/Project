package com.example.project.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class SQliteDB extends SQLiteOpenHelper {



    /**
     * Database name
     */

    public static final String DATABASE_NAME="DB";

    /**
     * Table name
     */
    public static final String DATABASE_USER_TABLE="table_user";


    /**
     *The row name of the table
     * ID
     * Username
     * Password
     * Email
     */
    public static final String KEY_ID="id";
    public static final String KEY_USERNAME="username";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_EMAIL="email";

    //context:上下文
    //name:数据库名称
    //factory:游标工厂
    //version:数据库版本号，并且版本号大于0
    public static final int VERSION=2;


    public SQliteDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DATABASE_USER_TABLE+"(" +
                "id integer primary key autoincrement," +
                "Username text," +
                "Password text," +
                "Email text)");

    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(newVersion==2||oldVersion==1){
            //Update UserInfo
            //1.delete table
            String sql=("drop table if exists "+DATABASE_USER_TABLE);
            db.execSQL(sql);
            //2.create table
            String sql2=("create table "+DATABASE_USER_TABLE+"(" +
                    "id integer primary key autoincrement," +
                    "Username text," +
                    "Password text," +
                    "Email text)");
            db.execSQL(sql2);
        }
    }



}
