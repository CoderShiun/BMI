package com.shiun.bmi;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shiun on 9/15/2016.
 */
public class DB {

    //將db欄位定義成參數,好取得額外的修改彈性
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ITME = "item";
    public static final String KEY_CREATED = "created";

    private static final String DATABASE_TABLE = "history";

    private Context mContext = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    //Constructor
    public DB(Context context){
        this.mContext = context;
    }

    //先建立一個DatabaseHelper物件,並呼叫getWritableDatabase方法,
    //該方法會根據我們在DatabaseHelper類別中的設定,若db不存在就呼叫create,若已存在就根據db版本來決定是否要更新db
    //如果執行中發生錯誤就拋出SQLException錯誤.
    public DB open() throws SQLException{
        dbHelper = new DatabaseHelper(mContext);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        /*
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        */

        private static final String DATABASE_NAME = "notes.db";
        private static final int DATABASE_VERSION = 3;
        private static final String DATABASE_CREATE =
                "CREATE TABLE notes(_id INTEGER PRIMARY KEY, note TEXT NOT NULL,"
                        + "created TIMESTAMP);";

        //將上述改寫成將constructor(DatabaseHelper)改爲只需要軟如負責控制db的開啓和關閉的Context參數;
        //其它三個都改爲使用super語法來在每次調用到constructor時,自動傳入這些固定的參數.
        //參數context:可以控制db的開啓和關閉;name:傳入db的名稱;factory:可以做複雜的查詢;version:傳入db的版本
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //TODO Auto-generated constructor stub
        }


        //建立資料表
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //TODO Auto-generated method stub
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        //更新資料表
        @Override
        //oldVersion:目前應用程式在機器上的db版本號;newVersion:目前開發的版本指定的db版本號;
        //當程式運行onUpgrade方法時,表示機器上的db版本與開發版本的db版本號已不相同
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            //TODO Auto-generated method stub
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS"+DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    //查詢的結果爲一個Cursor類別,作用是用來存取查詢的結果
    public Cursor getAll(){
        return db.rawQuery("SELECT * FROM history ORDER BY created DESC", null);
    }
}
