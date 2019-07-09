package com.example.project.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    /**
     * Database name
     */

    public static final String DATABASE_NAME="DB";

    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE="table_user";


    private static final String DATABASE_POST_TABLE="table_post";


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



    //userTable sql
    String userSql="create table "+DATABASE_USER_TABLE+"(" +
            "id integer primary key autoincrement," +
            "Username text," +
            "Password text," +
            "Email text)";

    String dropUser="drop table if exists "+DATABASE_USER_TABLE;


    //postTable sql
    private static final String postSql=" create table "+DATABASE_POST_TABLE+"("+
            "_id integer primary key autoincrement," +
            "username text,"+
            "photos blob,"+
            "comment text,"+
            "latitude real,"+
            "longitude real,"+
            "cityname text,"+
            "time datetime)";

    String dropPost="drop table if exists "+DATABASE_POST_TABLE;


    //context:上下文
    //name:数据库名称
    //factory:游标工厂
    //version:数据库版本号，并且版本号大于0
    public static final int VERSION=2;


    public DB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(userSql);

        db.execSQL(postSql);

    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==2||oldVersion==1){
            //Update UserInfo
            //1.delete table
            db.execSQL(dropUser);
            //2.create table
            db.execSQL(userSql);

            db.execSQL(dropPost);
            db.execSQL(postSql);
        }
    }



}
