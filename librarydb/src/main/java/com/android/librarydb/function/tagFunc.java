package com.android.librarydb.function;



import android.content.Context;

import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean.tagBean;
import com.android.librarydb.bean__.TagBean;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class tagFunc {

    //查询当前用户所有tag
    public void queryTag(String accountName, Context mContext){
        BmobQuery<tagBean> eq1 = new BmobQuery<tagBean>();
        eq1.addWhereEqualTo("accountName", accountName);
        List<BmobQuery<tagBean>> andQuerys = new ArrayList<BmobQuery<tagBean>>();
        andQuerys.add(eq1);
        BmobQuery<tagBean> query = new BmobQuery<tagBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<tagBean>() {
            @Override
            public void done(List<tagBean> object, BmobException e) {
                if(e==null){
                    //成功
                    System.out.println("查询成功，记录数量："+object.size());
                    for (tagBean tagBean : object) {
                        if(!SQLiteTools.hasTagSqlNum(mContext,tagBean.getObjectId())){
                            SQLiteTools.insertTagDB(mContext,tagBean.getTag_name(),tagBean.getTag_color(),tagBean.getObjectId());
                        }
                        else{
                            SQLiteTools.updateTagDB(mContext,tagBean.getTag_name(),tagBean.getTag_color(),tagBean.getObjectId());
                        }
                        System.out.println(tagBean);

                    }
//                    XToast.success(mContext,"获取成功").show();
                }else{
                    //失败
                    System.out.println("失败");
//                    XToast.error(mContext,"获取失败").show();
                }
            }
        });
    }

    //查询当前用户的指定tag如存在则更新不存在则插入
    public void addTag(Context mContext,tagBean tag){
        BmobQuery<tagBean> eq1 = new BmobQuery<tagBean>();
        eq1.addWhereEqualTo("accountName", tag.getAccountName());
        BmobQuery<tagBean> eq2 = new BmobQuery<tagBean>();
        eq2.addWhereEqualTo("tag_name", tag.getTag_name());
        List<BmobQuery<tagBean>> andQuerys = new ArrayList<BmobQuery<tagBean>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        BmobQuery<tagBean> query = new BmobQuery<tagBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<tagBean>() {
            @Override
            public void done(List<tagBean> object, BmobException e) {
                if(e==null){
                    if(object.size() != 0){
                        System.out.println("存在");
                        //存在则更新
                        tagBean category = new tagBean();
                        category.setTag_name(tag.getTag_name());
                        category.setTag_color(tag.getTag_color());
                        category.setAccountName(tag.getAccountName());
                        category.update(object.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    System.out.println("更新成功");
//                                    XToast.success(mContext,"备份成功").show();
                                } else {
                                    System.out.println("更新失败");
//                                    XToast.error(mContext,"备份失败").show();
                                }
                            }
                        });
                    }else{
                        System.out.println("不存在");
                        //不存在则添加
                        tagBean category = new tagBean();
                        category.setTag_name(tag.getTag_name());
                        category.setTag_color(tag.getTag_color());
                        category.setAccountName(tag.getAccountName());
                        category.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    System.out.println("添加成功");
//                                    XToast.success(mContext,"备份成功").show();
                                } else {
                                    System.out.println(e);
                                    System.out.println("添加失败");
//                                    XToast.error(mContext,"备份失败").show();
                                }
                            }
                        });
                    }
                }else{
                    System.out.println("异常");
//                    XToast.error(mContext,"备份失败").show();
                }
            }
        });
    }
}
