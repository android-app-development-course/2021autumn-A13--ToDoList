package com.android.librarydb.SQLite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class OrderDBHelperTags extends SQLiteOpenHelper {
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "ToDoListDataTags01.db";
    public static final String TAG_TABLE_NAME = "ToDoListTagData";
    public static final String TAG_NAME = "tag_name";
    public static final String TAG_COLOR = "tag_color";
    public static final String SQL_NUM = "sql_num";
    private Context myContext;
    private SQLiteDatabase myDataBase;
    public OrderDBHelperTags(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public OrderDBHelperTags(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public OrderDBHelperTags(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);
        String sql = "create table if not exists " + TAG_TABLE_NAME + " (" +
                " tag_name TEXT PRIMARY KEY," +
                " tag_color INTEGER," +
                " sql_num TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TAG_TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

}
