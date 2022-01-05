package com.android.librarydb.function;


import android.content.Context;
import android.util.Log;

import com.android.librarybase.basemethod.TipDialog;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.SQLite.OrderDBHelperCards;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean.cardBean;
import com.android.librarydb.bean__.CardBean;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class cardFunc {
    //有序返回指定用户的所有计划

    public void query_sort(Context mContext, String name, int cardId){
        BmobQuery<cardBean> eq1 = new BmobQuery<cardBean>();
        eq1.addWhereEqualTo("accountName", name);
        BmobQuery<cardBean> eq2 = new BmobQuery<cardBean>();
        eq2.addWhereEqualTo("card_id", cardId);
        List<BmobQuery<cardBean>> andQuerys = new ArrayList<BmobQuery<cardBean>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        BmobQuery<cardBean> query = new BmobQuery<cardBean>();
        query.and(andQuerys);
        query.order("createdAt");
        query.findObjects(new FindListener<cardBean>() {
            @Override
            public void done(List<cardBean> object, BmobException e) {
                if(e==null){
                    //成功
                    System.out.println("card查询成功，记录数量："+object.size());
//                    System.out.println(object.get(object.size()-1).getAccountName()+object.get(object.size()-1).getCard_id());
                    if(object.size()>=1) {
//                        SharedStorage.setBeifenObjectId(mContext, object.get(object.size() - 1).getObjectId());
                        SQLiteTools.updateCardDB(mContext,cardId, OrderDBHelperCards.SQL_NUM,object.get(object.size() - 1).getObjectId());
                    }
                }else{
                    //失败
                    System.out.println(e);
                }
            }
        });
    }
    //查询指定用户的所有计划
    public void query(String name, Context mContext){
        BmobQuery<cardBean> eq1 = new BmobQuery<cardBean>();
        eq1.addWhereEqualTo("accountName", name);
        List<BmobQuery<cardBean>> andQuerys = new ArrayList<BmobQuery<cardBean>>();
        andQuerys.add(eq1);
        BmobQuery<cardBean> query = new BmobQuery<cardBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<cardBean>() {
            @Override
            public void done(List<cardBean> object, BmobException e) {
                if(e==null){
                    //成功
                    System.out.println("查询成功，记录数量："+object.size());
                    for (cardBean card : object) {
                        if(!SQLiteTools.hasCardSqlNum(mContext,card.getObjectId())){
                            SQLiteTools.insertCardDB(mContext,card.getTag_name(),card.getDay_title(),card.isIs_finish(),card.getStart_time(),card.getEnd_time(),card.getDescribe(),card.isIs_all_day(),card.getObjectId(),card.isIn_get_back());
                        }
                        else{
                            SQLiteTools.updateCardDB(mContext,card.getCard_id(),card.getTag_name(),card.getDay_title(),card.isIs_finish(),card.getStart_time(),card.getEnd_time(),card.getDescribe(),card.isIs_all_day(),card.getObjectId());
                        }
                        System.out.println(card+card.getObjectId());
                    }
                    TipDialog.showFinishDialog(mContext,"获取成功");
//                    for (cardBean card : object){
//                        query_sort(mContext,SharedStorage.getCurUsername(mContext),card.getCard_id());
//                        System.out.println("OrderDBHelperCards.SQL_NUM:::"+OrderDBHelperCards.SQL_NUM);
//                        SQLiteTools.updateCardDB(mContext,card.getCard_id(), OrderDBHelperCards.SQL_NUM,SharedStorage.getBeifenObjectId(mContext));
//                    }
                }else{
                    TipDialog.showUnFinishDialog(mContext,"获取失败");
                    //失败
                    System.out.println("cao::::::::::");
                    System.out.println(e);
                }
            }
        });
    }

    //添加任务 存在则更新
    public void update(Context mContext,cardBean card){
        cardBean category = new cardBean();
        category.setCard_id(card.getCard_id());
        category.setDay_title(card.getDay_title());
        category.setDescribe(card.getDescribe());
        category.setEnd_time(card.getEnd_time());
        category.setStart_time(card.getStart_time());
        category.setIn_get_back(card.isIn_get_back());
        category.setIs_all_day(card.isIs_all_day());
        category.setIs_finish(card.isIs_finish());
        category.setTag_name(card.getTag_name());
        category.setAccountName(card.getAccountName());
//        category.setObjectId(card.getObjectId());
        System.out.println("update "+ card.getObjectId());
        if(card.getObjectId().equals("")){
            category.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
//                        System.out.println("添加成功");
                        System.out.println("添加成功OrderDBHelperCards.SQL_NUM:::"+category.getObjectId());
                        query_sort(mContext,SharedStorage.getCurUsername(mContext),category.getCard_id());
//                        SQLiteTools.updateCardDB(mContext,category.getCard_id(), OrderDBHelperCards.SQL_NUM,SharedStorage.getBeifenObjectId(mContext));
                        System.out.println("现在的getBeifenObjectId:::"+SharedStorage.getBeifenObjectId(mContext));
                    } else {
                        System.out.println("添加失败");
                    }
                }
            });
        }
        else{
            category.update(card.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
//                        System.out.println("更新成功");
                        System.out.println("更新成功OrderDBHelperCards.SQL_NUM:::"+category.getObjectId());
                    } else {
                        System.out.println(e+"更新失败");
                    }
                }
            });
        }



    }

    //删除任务
    public void delete(cardBean cardBean){
        cardBean.delete(cardBean.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    System.out.println("删除成功");
                } else {
                    System.out.println("删除失败");
                }
            }
        });
    }


    //查询指定用户的所有计划
    public void query_test(String name, Context mContext){
        BmobQuery<cardBean> eq1 = new BmobQuery<cardBean>();
        eq1.addWhereEqualTo("accountName", name);
        List<BmobQuery<cardBean>> andQuerys = new ArrayList<BmobQuery<cardBean>>();
        andQuerys.add(eq1);
        BmobQuery<cardBean> query = new BmobQuery<cardBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<cardBean>() {
            @Override
            public void done(List<cardBean> object, BmobException e) {
                if(e==null){
                    //成功
                    System.out.println("查询成功，记录数量："+object.size());
                    TipDialog.showFinishDialog(mContext,"备份成功");
                }else{
                    TipDialog.showUnFinishDialog(mContext,"备份失败");
                    //失败
                    System.out.println("cao::::::::::");
                    System.out.println(e);
                }
            }
        });
    }
}
