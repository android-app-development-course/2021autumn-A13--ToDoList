package com.android.librarydb.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.librarybase.basemethod.GetTime;
import com.android.librarybase.basemethod.TipDialog;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.bean__.TagBean;
import com.android.librarydb.function.accountFunc;
import com.android.librarydb.function.cardFunc;
import com.android.librarydb.function.tagFunc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.android.librarydb.SQLite.OrderDBHelperCards.*;
import static com.android.librarydb.SQLite.OrderDBHelperTags.TAG_COLOR;
import static com.android.librarydb.SQLite.OrderDBHelperTags.TAG_NAME;
import static com.android.librarydb.SQLite.OrderDBHelperTags.TAG_TABLE_NAME;

import com.android.librarydb.bean.cardBean;
import com.android.librarydb.bean.tagBean;
import com.android.librarydb.bean.accountBean;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.greenrobot.eventbus.EventBus;

@SuppressLint("Range")
public class SQLiteTools {





    /**
     * 添加新标签
     * @param mContext
     * @param tag_name
     * @param tag_color
     */
    public static boolean insertTagDB(
            Context mContext,
            String tag_name,
            int tag_color){
        if(hasSameTagDB(mContext,tag_name))return false;
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tag_name);
        contentValues.put(TAG_COLOR, tag_color);
        contentValues.put(SQL_NUM, "");
        db.insert(TAG_TABLE_NAME, null, contentValues);
        return true;
    }

    /**
     * 删除标签
     * @param mContext
     * @param tag_name
     * @return
     */
    public static boolean deleteTagDB(
            Context mContext,
            String tag_name){
//        if(!hasSameTagDB(mContext,tag_name))return false;
        if (hasCard(mContext,tag_name))return false;
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getWritableDatabase();
        db.delete(TAG_TABLE_NAME, TAG_NAME + "= ?", new String[] { tag_name });
        return true;
    }

    /**
     * 获取所有标签名
     * @param mContext
     * @return
     */
    public static List<String> getTagNames(
            Context mContext){
        List<String>list=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(TAG_TABLE_NAME, new String[]{
                        TAG_NAME}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex(TAG_NAME)));
        }
        // 关闭游标，释放资源
        cursor.close();
        return list;
    }

    /**
     * 根据标签名获取标签颜色
     * @param mContext
     * @param tag_name
     * @return
     */
    public static int getTagColorDB(
            Context mContext,
            String tag_name){
        int result=0;
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(TAG_TABLE_NAME, new String[]{
                TAG_COLOR}, " tag_name = ?",
                new String[]{tag_name}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            result = cursor.getInt(cursor.getColumnIndex(TAG_COLOR));
        }
        // 关闭游标，释放资源
        cursor.close();
        return result;
    }



    /**
     * 根据标签名查看是否已有该标签
     * @param mContext
     * @param tag_name
     * @return
     */
    public static boolean hasSameTagDB(
            Context mContext,
            String tag_name){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(TAG_TABLE_NAME, new String[]{
                        }, " tag_name = ?",
                new String[]{tag_name}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            cursor.close();
            return true;
        }
        // 关闭游标，释放资源
        cursor.close();
        return false;
    }

    /**
     * 新建计划
     * @param mContext
     * @param tag_name
     * @param card_name
     * @param is_finish
     * @param start_time
     * @param end_time
     * @param content
     */
    public static void insertCardDB(
            Context mContext,
            String tag_name,
            String card_name,
            boolean is_finish,
            long start_time,
            long end_time,
            String content,
            boolean is_all_day
            ){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tag_name);
        contentValues.put(CARD_NAME, card_name);
        contentValues.put(IS_FINISH, is_finish?1:0);
        contentValues.put(START_TIME, String.valueOf(start_time));
        contentValues.put(END_TIME, String.valueOf(end_time));
        contentValues.put(CONTENT, content);
        contentValues.put(IN_GET_BACK, 0);
        contentValues.put(IS_ALL_DAY, is_all_day?1:0);
        contentValues.put(SQL_NUM, "");
        db.insert(CARD_TABLE_NAME, null, contentValues);
    }



    /**
     * 假删除计划
     * @param mContext
     * @param card_id
     */
    public static void fakeDeleteCardDB(
            Context mContext,
            int card_id){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IN_GET_BACK, 1);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }

    /**
     * 真删除计划
     * @param mContext
     * @param card_id
     */
    public static void realDeleteCardDB(
            Context mContext,
            int card_id){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        db.delete(CARD_TABLE_NAME, CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }

    /**
     * 取回回收站的计划
     * @param mContext
     * @param card_id
     */
    public static void getBackCardDB(
            Context mContext,
            int card_id){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IN_GET_BACK, 0);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }

    /**
     * 整一条更新计划
     * @param card_id
     * @param mContext
     * @param tag_name
     * @param card_name
     * @param is_finish
     * @param start_time
     * @param end_time
     * @param content
     */
    public static void updateCardDB(
            Context mContext,
            int card_id,
            String tag_name,
            String card_name,
            boolean is_finish,
            long start_time,
            long end_time,
            String content,
            boolean is_all_day){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tag_name);
        contentValues.put(CARD_NAME, card_name);
        contentValues.put(IS_FINISH, is_finish?1:0);
        contentValues.put(START_TIME, String.valueOf(start_time));
        contentValues.put(END_TIME, String.valueOf(end_time));
        contentValues.put(CONTENT, content);
        contentValues.put(IS_ALL_DAY, is_all_day?1:0);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }



    /**
     * 更新计划
     * @param mContext
     * @param card_id
     * @param key
     * @param value
     */
    public static void updateCardDB(
            Context mContext,
            int card_id,
            String key,
            int value){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }
    public static void updateCardDB(
            Context mContext,
            int card_id,
            String key,
            String value){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }
    public static void updateCardDB(
            Context mContext,
            int card_id,
            String key,
            long value){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }
    public static void updateCardDB(
            Context mContext,
            int card_id,
            String key,
            boolean value){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(key, value?1:0);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }



    /**
     * 获取持久化数据下的所有计划
     * @param mContext
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("Range")
    public static List<CardBean> getAllCards(Context mContext, boolean is_reverse, boolean is_start, int put_is_finish){
        List<CardBean>cards=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID, TAG_NAME, CARD_NAME, IS_FINISH ,START_TIME, END_TIME, CONTENT, IN_GET_BACK ,IS_ALL_DAY, SQL_NUM}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            int in_get_back = cursor.getInt(cursor.getColumnIndex(IN_GET_BACK));
            boolean is_finish = cursor.getInt(cursor.getColumnIndex(IS_FINISH)) == 1;
            if(put_is_finish==1){
                if(!is_finish)continue;
            }
            else if(put_is_finish==0){
                if(is_finish)continue;
            }
            int card_id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            String card_name = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            long start_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(START_TIME)));
            long end_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(END_TIME)));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));


            int tag_color= getTagColorDB(mContext,tag_name);
            boolean is_all_day = cursor.getInt(cursor.getColumnIndex(IS_ALL_DAY)) == 1;
            String sql_num = cursor.getString(cursor.getColumnIndex(SQL_NUM));
            CardBean ca=new CardBean(card_id,tag_name,card_name,start_time,end_time,content,tag_color,is_finish,is_all_day,sql_num);
            ca.setIn_get_back((in_get_back==1?true:false));
            cards.add(ca);
        }
        // 关闭游标，释放资源
        cursor.close();
        if (is_start){
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getStart_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getStart_time));
        }
        else{
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getEnd_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getEnd_time));
        }
        cards.sort(Comparator.comparing(CardBean::getInt_is_finish));
        return cards;
    }

    /**
     * 获取持久化数据下的所有不在回收站的计划
     * @param mContext
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("Range")
    public static List<CardBean> getAllUnInGetBackCards(Context mContext, boolean is_reverse, boolean is_start, int put_is_finish){
        List<CardBean>cards=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                CARD_ID, TAG_NAME, CARD_NAME, IS_FINISH ,START_TIME, END_TIME, CONTENT, IN_GET_BACK ,IS_ALL_DAY, SQL_NUM}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            int in_get_back = cursor.getInt(cursor.getColumnIndex(IN_GET_BACK));
            if(in_get_back==1)continue;
            boolean is_finish = cursor.getInt(cursor.getColumnIndex(IS_FINISH)) == 1;
            if(put_is_finish==1){
                if(!is_finish)continue;
            }
            else if(put_is_finish==0){
                if(is_finish)continue;
            }
            int card_id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            String card_name = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            long start_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(START_TIME)));
            long end_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(END_TIME)));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));


            int tag_color= getTagColorDB(mContext,tag_name);
            boolean is_all_day = cursor.getInt(cursor.getColumnIndex(IS_ALL_DAY)) == 1;
            String sql_num = cursor.getString(cursor.getColumnIndex(SQL_NUM));
            cards.add(new CardBean(card_id,tag_name,card_name,start_time,end_time,content,tag_color,is_finish,is_all_day,sql_num));
        }
        // 关闭游标，释放资源
        cursor.close();
        if (is_start){
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getStart_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getStart_time));
        }
        else{
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getEnd_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getEnd_time));
        }
        cards.sort(Comparator.comparing(CardBean::getInt_is_finish));
        return cards;
    }

    /**
     * 获取持久化数据下的所有回收站的计划
     * @param mContext
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("Range")
    public static List<CardBean> getAllGetBackCards(Context mContext,boolean is_reverse){
        List<CardBean>cards=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID, TAG_NAME, CARD_NAME, IS_FINISH ,START_TIME, END_TIME, CONTENT, IN_GET_BACK,IS_ALL_DAY,SQL_NUM}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            int in_get_back = cursor.getInt(cursor.getColumnIndex(IN_GET_BACK));
            if(in_get_back==0)continue;
            int card_id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            String card_name = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            long start_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(START_TIME)));
            long end_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(END_TIME)));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));
            boolean is_finish = cursor.getInt(cursor.getColumnIndex(IS_FINISH)) == 1;
            int tag_color= getTagColorDB(mContext,tag_name);
            boolean is_all_day = cursor.getInt(cursor.getColumnIndex(IS_ALL_DAY)) == 1;
            String sql_num = cursor.getString(cursor.getColumnIndex(SQL_NUM));
            cards.add(new CardBean(card_id,tag_name,card_name,start_time,end_time,content,tag_color,is_finish,is_all_day,sql_num));
        }
        // 关闭游标，释放资源
        cursor.close();
        if(is_reverse)cards.sort(Comparator.comparing(CardBean::getStart_time).reversed());
        else cards.sort(Comparator.comparing(CardBean::getStart_time));
        cards.sort(Comparator.comparing(CardBean::getInt_is_finish));
        return cards;
    }


    /**
     * 选择指定时间的计划
     * @param mContext
     * @param mTime
     * @param is_reverse
     * @param is_start
     * @param is_pre
     * @param put_is_finish -1为全部 0为未完成 1为已完成
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("Range")
    public static List<CardBean> getSelectTimeCards(Context mContext,long mTime,boolean is_reverse,boolean is_start,boolean is_pre,int put_is_finish){
        List<CardBean>cards=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID, TAG_NAME, CARD_NAME, IS_FINISH ,START_TIME, END_TIME, CONTENT, IN_GET_BACK,IS_ALL_DAY,SQL_NUM}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            int in_get_back = cursor.getInt(cursor.getColumnIndex(IN_GET_BACK));
            if(in_get_back==1)continue;
            boolean is_finish = cursor.getInt(cursor.getColumnIndex(IS_FINISH)) == 1;
            if(put_is_finish==1){
                if(!is_finish)continue;
            }
            else if(put_is_finish==0){
                if(is_finish)continue;
            }
            long start_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(START_TIME)));
            long end_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(END_TIME)));
            //==========================判断时间的包含关系==============================
            int baohanguanxi=-1;
            try {
                baohanguanxi=GetTime.checkTime(start_time,end_time,mTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (baohanguanxi==-1)continue;
            Log.e("time01",GetTime.getNowTimeStr(mTime,"yyyy/MM/dd"));
            Log.e("time02",GetTime.getNowTimeStr(start_time,"yyyy/MM/dd"));
            int card_id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            String card_name = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));
            int tag_color= getTagColorDB(mContext,tag_name);
            boolean is_all_day = cursor.getInt(cursor.getColumnIndex(IS_ALL_DAY)) == 1;
            String sql_num = cursor.getString(cursor.getColumnIndex(SQL_NUM));
            cards.add(new CardBean(card_id,tag_name,card_name,start_time,end_time,content,tag_color,is_finish,is_all_day,sql_num));
        }
        // 关闭游标，释放资源
        cursor.close();
        if (is_start){
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getStart_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getStart_time));
        }
        else{
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getEnd_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getEnd_time));
        }
        if (is_pre){
            cards.sort(Comparator.comparing(CardBean::getInt_is_all_day).reversed());
        }
        else{
            cards.sort(Comparator.comparing(CardBean::getInt_is_all_day));
        }
        cards.sort(Comparator.comparing(CardBean::getInt_is_finish));
        return cards;
    }

    /**
     * 选择指定标签的计划
     * @param mContext
     * @param mTagName
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("Range")
    public static List<CardBean> getSelectTagCards(Context mContext,String mTagName,boolean is_reverse,boolean is_start,int put_is_finish){
        List<CardBean>cards=new ArrayList<>();
        if(mTagName.equals("所有")){
            cards= getAllUnInGetBackCards(mContext,is_reverse,is_start,put_is_finish);
            return cards;
        }
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID, TAG_NAME, CARD_NAME, IS_FINISH ,START_TIME, END_TIME, CONTENT, IN_GET_BACK,IS_ALL_DAY,SQL_NUM}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            int in_get_back = cursor.getInt(cursor.getColumnIndex(IN_GET_BACK));
            if(in_get_back==1)continue;
            boolean is_finish = cursor.getInt(cursor.getColumnIndex(IS_FINISH)) == 1;
            if(put_is_finish==1){
                if(!is_finish)continue;
            }
            else if(put_is_finish==0){
                if(is_finish)continue;
            }
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            if (!tag_name.equals(mTagName))continue;
            long start_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(START_TIME)));
            int card_id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            String card_name = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            long end_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(END_TIME)));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));

            int tag_color= getTagColorDB(mContext,tag_name);
            boolean is_all_day = cursor.getInt(cursor.getColumnIndex(IS_ALL_DAY)) == 1;
            String sql_num = cursor.getString(cursor.getColumnIndex(SQL_NUM));
            cards.add(new CardBean(card_id,tag_name,card_name,start_time,end_time,content,tag_color,is_finish,is_all_day,sql_num));
        }
        // 关闭游标，释放资源
        cursor.close();
        if (is_start){
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getStart_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getStart_time));
        }
        else{
            if(is_reverse)cards.sort(Comparator.comparing(CardBean::getEnd_time).reversed());
            else cards.sort(Comparator.comparing(CardBean::getEnd_time));
        }
        cards.sort(Comparator.comparing(CardBean::getInt_is_finish));
        return cards;
    }


    /**
     * 获取持久化数据下的指定card——id的计划
     * @param mContext
     * @return
     */
    @SuppressLint("Range")
    public static CardBean getCard(Context mContext,int card_id){
        CardBean card = null;
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID, TAG_NAME, CARD_NAME, IS_FINISH ,START_TIME, END_TIME, CONTENT, IN_GET_BACK,IS_ALL_DAY,SQL_NUM}, CARD_ID+" = ? ",
                new String[]{String.valueOf(card_id)}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
//            int card_id = cursor.getInt(cursor.getColumnIndex(CARD_ID));
            String card_name = cursor.getString(cursor.getColumnIndex(CARD_NAME));
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            long start_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(START_TIME)));
            long end_time = Long.parseLong(cursor.getString(cursor.getColumnIndex(END_TIME)));
            String content = cursor.getString(cursor.getColumnIndex(CONTENT));
            boolean is_finish = cursor.getInt(cursor.getColumnIndex(IS_FINISH)) == 1;
            int tag_color= getTagColorDB(mContext,tag_name);
            boolean is_all_day = cursor.getInt(cursor.getColumnIndex(IS_ALL_DAY)) == 1;
            String sql_num = cursor.getString(cursor.getColumnIndex(SQL_NUM));
            card=new CardBean(card_id,tag_name,card_name,start_time,end_time,content,tag_color,is_finish,is_all_day,sql_num);
        }
        // 关闭游标，释放资源
        cursor.close();
        return card;
    }

    public static List<TagBean> getAllTags(Context mContext) {
        List<TagBean>tags=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(TAG_TABLE_NAME, new String[]{
                        TAG_NAME,TAG_COLOR}, "",
                new String[]{}, null, null, null);
        //利用游标遍历所有数据对象
        while(cursor.moveToNext()){
            String tag_name = cursor.getString(cursor.getColumnIndex(TAG_NAME));
            int tag_color = cursor.getInt(cursor.getColumnIndex(TAG_COLOR));
            tags.add(new TagBean(tag_name,tag_color));
        }
        // 关闭游标，释放资源
        cursor.close();
        return tags;
    }

    /**
     * 根据标签名判断是否有对应的计划
     * @param mContext
     * @param tag_name
     * @return
     */
    public static boolean hasCard(Context mContext, String tag_name) {
        List<TagBean>tags=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID}, TAG_NAME+" = ? ",
                new String[]{tag_name}, null, null, null);
        //利用游标遍历所有数据对象
        boolean has=false;
        while(cursor.moveToNext()){
            has = true;
            break;
        }
        // 关闭游标，释放资源
        cursor.close();
        return has;
    }



    //===================================================云端=================================================================


    /**
     * 从云端拿的card数据插入到本地
     * @param mContext
     * @param tag_name
     * @param card_name
     * @param is_finish
     * @param start_time
     * @param end_time
     * @param content
     * @param is_all_day
     * @param sql_num
     */
    public static void insertCardDB(
            Context mContext,
            String tag_name,
            String card_name,
            boolean is_finish,
            long start_time,
            long end_time,
            String content,
            boolean is_all_day,
            String sql_num,
            boolean in_get_back){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tag_name);
        contentValues.put(CARD_NAME, card_name);
        contentValues.put(IS_FINISH, is_finish?1:0);
        contentValues.put(START_TIME, String.valueOf(start_time));
        contentValues.put(END_TIME, String.valueOf(end_time));
        contentValues.put(CONTENT, content);
        contentValues.put(IN_GET_BACK, 0);
        contentValues.put(IS_ALL_DAY, is_all_day?1:0);
        contentValues.put(SQL_NUM, sql_num);
        contentValues.put(IN_GET_BACK, in_get_back?1:0);
        System.out.println("从云端拿的card数据插入到本地insertCardDB从云端拿的card数据插入到本地"+sql_num);
        db.insert(CARD_TABLE_NAME, null, contentValues);
    }

    /**
     * 整一条更新计划,包括sql_num（从云端拿来数据）
     * @param card_id
     * @param mContext
     * @param tag_name
     * @param card_name
     * @param is_finish
     * @param start_time
     * @param end_time
     * @param content
     */
    public static void updateCardDB(
            Context mContext,
            int card_id,
            String tag_name,
            String card_name,
            boolean is_finish,
            long start_time,
            long end_time,
            String content,
            boolean is_all_day,
            String sql_num){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tag_name);
        contentValues.put(CARD_NAME, card_name);
        contentValues.put(IS_FINISH, is_finish?1:0);
        contentValues.put(START_TIME, String.valueOf(start_time));
        contentValues.put(END_TIME, String.valueOf(end_time));
        contentValues.put(CONTENT, content);
        contentValues.put(IS_ALL_DAY, is_all_day?1:0);
        contentValues.put(SQL_NUM, sql_num);
        db.update(CARD_TABLE_NAME, contentValues,CARD_ID + " = ? ", new String[] { String.valueOf(card_id) });
    }

    /**
     * 从云端获取所有card数据
     * @param mContext
     */
    public static void getAllCardsFromYunToSQLite(Context mContext){
        cardFunc cardF=new cardFunc();
        cardF.query(SharedStorage.getCurUsername(mContext),mContext);
    }

    /**
     * 从云端获取的card数据的sql_num是否已存在在本地数据库
     * @param mContext
     * @param sql_num
     * @return
     */
    public static boolean hasCardSqlNum(Context mContext, String sql_num) {
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperCards().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(CARD_TABLE_NAME, new String[]{
                        CARD_ID}, SQL_NUM+" = ? ",
                new String[]{String.valueOf(sql_num)}, null, null, null);
        //利用游标遍历所有数据对象
        boolean has=false;
        while(cursor.moveToNext()){
            has = true;
            break;
        }
        // 关闭游标，释放资源
        cursor.close();
        return has;
    }

    /**
     * 添加新标签(从云端拿)
     * @param mContext
     * @param tag_name
     * @param tag_color
     * @param sql_num
     */
    public static boolean insertTagDB(
            Context mContext,
            String tag_name,
            int tag_color,
            String sql_num){
        if(hasSameTagDB(mContext,tag_name))return false;
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tag_name);
        contentValues.put(TAG_COLOR, tag_color);
        contentValues.put(SQL_NUM, sql_num);
        db.insert(TAG_TABLE_NAME, null, contentValues);
        return true;
    }

    /**
     * 整一条更新tag,包括sql_num（从云端拿来数据）
     * @param mContext
     * @param tag_name
     * @param tag_color
     * @param sql_num
     */
    public static void updateTagDB(
            Context mContext,
            String tag_name,
            int tag_color,
            String sql_num){
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_COLOR, tag_color);
        contentValues.put(SQL_NUM, sql_num);
        db.update(TAG_TABLE_NAME, contentValues,TAG_NAME + " = ? ", new String[] { tag_name });
    }

    /**
     * 从云端获取所有tag数据
     * @param mContext
     */
    public static void getAllTagsFromYunToSQLite(Context mContext){
        tagFunc tagF=new tagFunc();
        tagF.queryTag(SharedStorage.getCurUsername(mContext),mContext);
    }

    /**
     * 从云端获取的tag数据的sql_num是否已存在在本地数据库
     * @param mContext
     * @param sql_num
     * @return
     */
    public static boolean hasTagSqlNum(Context mContext, String sql_num) {
        List<TagBean>tags=new ArrayList<>();
        SQLiteDatabase db;
        OrderDAO orderDAO =new OrderDAO(mContext);
        db = orderDAO.getOrderDBHelperTags().getReadableDatabase();
        //创建游标对象
        Cursor cursor = db.query(TAG_TABLE_NAME, new String[]{
                        TAG_NAME}, SQL_NUM+" = ? ",
                new String[]{String.valueOf(sql_num)}, null, null, null);
        //利用游标遍历所有数据对象
        boolean has=false;
        while(cursor.moveToNext()){
            has = true;
            break;
        }
        // 关闭游标，释放资源
        cursor.close();
        return has;
    }

    /**
     * 登录
     * @param mContext
     * @param username
     * @param password
     */
    public static void loginFromYun(Context mContext,String username,String password){
        accountFunc accFunc = new accountFunc();
        tagFunc tagF = new tagFunc();
        cardFunc cardF = new cardFunc();
        accFunc.login(username,password,mContext);
    }

    /**
     * 注册
     * @param mContext
     * @param username
     * @param password
     */
    public static void registerFromYun(Context mContext,String username,String password){
        accountFunc accFunc = new accountFunc();
        tagFunc tagF = new tagFunc();
        cardFunc cardF = new cardFunc();
        accFunc.register(mContext,username,password);
    }

    /**
     * 更新用户各种信息
     * @param mContext
     */
    public static void updateUserMsgToYun(Context mContext){
        accountBean acc=new accountBean();
        acc.setHead(String.valueOf(SharedStorage.getCurTouXiangDrawable(mContext)));
        acc.setSex(SharedStorage.getCurSex(mContext));
        acc.setName(SharedStorage.getCurUsername(mContext));
        acc.setNikeName(SharedStorage.getCurNiCheng(mContext));
        acc.setDescribe(SharedStorage.getCurDescribe(mContext));
        acc.setPassword(SharedStorage.getCurPassword(mContext));
        Log.e("setPassword",SharedStorage.getCurPassword(mContext));
        accountFunc accFunc = new accountFunc();
        tagFunc tagF = new tagFunc();
        cardFunc cardF = new cardFunc();
        accFunc.update(mContext,acc);
    }


    /**
     * 备份所有计划到云端
     * @param mContext
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addCardsToYunSql(Context mContext){
        accountFunc accFunc = new accountFunc();
        tagFunc tagF = new tagFunc();
        cardFunc cardF = new cardFunc();
        List<CardBean>cards= getAllCards(mContext,false,false,-1);
        Log.v("getSql_num", String.valueOf(cards));
        for (int i=0;i<cards.size();++i){
            CardBean temp = cards.get(i);
            cardBean card=new cardBean();
            card.setAccountName(SharedStorage.getCurUsername(mContext));
            card.setCard_id(temp.getCard_id());
            card.setDay_title(temp.getDay_title());
            card.setDescribe(temp.getDescribe());
            card.setTag_name(temp.getTag_name());
            card.setStart_time(temp.getStart_time());
            card.setEnd_time(temp.getEnd_time());
            card.setIn_get_back(temp.isIn_get_back());
            card.setIs_all_day(temp.isIs_all_day());
            card.setIs_finish(temp.isIs_finish());
//            if(temp.getSql_num().equals("-1"))
//                temp.setSql_num("");
            card.setObjectId(temp.getSql_num());

//            Log.v("getSql_num",temp.getSql_num());
            System.out.println("temp.getSql_num()"+temp.getSql_num());
            cardF.update(mContext,card);
        }
        cardF.query_test(SharedStorage.getCurUsername(mContext),mContext);
    }


    /**
     * 备份所有TAG到云端
     * @param mContext
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addTagsToYunSql(Context mContext){
        accountFunc accFunc = new accountFunc();
        tagFunc tagF = new tagFunc();
        cardFunc cardF = new cardFunc();
        List<TagBean>tags=getAllTags(mContext);
        for (int i=0;i<tags.size();++i){
            TagBean temp = tags.get(i);
            tagBean tag=new tagBean();
            tag.setAccountName(SharedStorage.getCurUsername(mContext));
            tag.setTag_name(temp.getTag_name());
            tag.setTag_color(temp.getTag_color());
//            tag.setObjectId(temp.getSql_num());
            tagF.addTag(mContext,tag);
        }
    }


}
