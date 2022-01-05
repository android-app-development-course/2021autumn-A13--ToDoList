package com.android.librarydb.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class OrderDBHelperCards extends SQLiteOpenHelper {
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "ToDoListDataCards_new05.db";
    public static final String CARD_TABLE_NAME = "ToDoListCardData";
    public static final String CARD_ID = "card_id";
    public static final String TAG_NAME = "tag_name";
    public static final String CARD_NAME = "card_name";
    public static final String IS_FINISH = "is_finish";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String CONTENT = "content";
    public static final String IN_GET_BACK = "in_get_back";
    public static final String IS_ALL_DAY = "is_all_day";
    public static final String SQL_NUM = "sql_num";
    private Context myContext;
    private SQLiteDatabase myDataBase;
    public OrderDBHelperCards(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public OrderDBHelperCards(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public OrderDBHelperCards(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + CARD_TABLE_NAME + " (" +
                " card_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " tag_name TEXT," +
                " card_name TEXT," +
                " is_finish INTEGER," +
                " start_time TEXT," +
                " end_time TEXT," +
                " content TEXT," +
                " in_get_back INTEGER," +
                " is_all_day INTEGER," +
                " sql_num TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + CARD_TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

}
