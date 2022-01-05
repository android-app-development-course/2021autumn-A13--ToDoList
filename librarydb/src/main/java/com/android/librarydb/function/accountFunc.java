package com.android.librarydb.function;


import android.content.Context;
import android.util.Log;

import com.android.librarybase.basemethod.TipDialog;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.R;
import com.android.librarydb.bean.accountBean;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarydb.eventBus.EventBusLoginCheckMidMsg;
import com.android.librarydb.eventBus.EventBusMineChangeMsgMidMsg;
import com.android.librarydb.eventBus.EventBusRegisterCheckMidMsg;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class accountFunc {

    //登录
    public void login(String name, String password, Context mContext){
        BmobQuery<accountBean> eq1 = new BmobQuery<accountBean>();
        eq1.addWhereEqualTo("name", name);
        List<BmobQuery<accountBean>> andQuerys = new ArrayList<BmobQuery<accountBean>>();
        andQuerys.add(eq1);
        BmobQuery<accountBean> query = new BmobQuery<accountBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<accountBean>() {
            @Override
            public void done(List<accountBean> object, BmobException e) {
                if(e==null){
                    if(object.size() != 0){
                        //账号存在 进行密码匹配
                        System.out.println("账号存在");
                        BmobQuery<accountBean> eq1 = new BmobQuery<accountBean>();
                        eq1.addWhereEqualTo("name", name);
                        BmobQuery<accountBean> eq2 = new BmobQuery<accountBean>();
                        eq2.addWhereEqualTo("password", password);
                        List<BmobQuery<accountBean>> andQuerys = new ArrayList<BmobQuery<accountBean>>();
                        andQuerys.add(eq1);
                        andQuerys.add(eq2);
                        BmobQuery<accountBean> query = new BmobQuery<accountBean>();
                        query.and(andQuerys);
                        query.findObjects(new FindListener<accountBean>() {
                            @Override
                            public void done(List<accountBean> object, BmobException e) {
                                if(e==null){
                                    Log.e("BmobException","BmobException");
                                    if(object.size() != 0){Log.e("BmobException","BmobException");
                                        //账号密码匹配

                                        EventBus.getDefault().post(new EventBusLoginCheckMidMsg(true,false,true));
                                        SharedStorage.setCurNiCheng(mContext,object.get(0).getNikeName());
                                        SharedStorage.setCurSex(mContext,object.get(0).getSex());
                                        SharedStorage.setCurTouXiangDrawable(mContext, Integer.parseInt(object.get(0).getHead()));
                                        SharedStorage.setCurDescribe(mContext,object.get(0).getDescribe());
                                        EventBus.getDefault().post(new EventBusMineChangeMsgMidMsg(true));
                                        System.out.println("账号密码匹配，登录成功");
                                        System.out.println(object.get(0));
                                    }else{
                                        Log.e("BmobException","BmobException");
                                        //账号或密码错误
                                        EventBus.getDefault().post(new EventBusLoginCheckMidMsg(true,true,false));
                                        System.out.println("账号或密码错误");
                                    }
                                }else{
                                    //失败
                                    System.out.println("查询失败");
                                    TipDialog.showUnFinishDialog(mContext,"无网络连接");
//                                    XToast.error(mContext,"无网络连接").show();
                                }
                            }
                        });
                    }else{
                        //无账号
                        EventBus.getDefault().post(new EventBusLoginCheckMidMsg(false,false,false));
//                        TipDialog.showUnFinishDialog(mContext,"无网络连接");
                    }
                }else{
                    //失败
                    System.out.println("查询失败");
//                    XToast.error(mContext,"无网络连接").show();
                    TipDialog.showUnFinishDialog(mContext,"无网络连接");
                }
            }
        });
    }

    //注册
    public void register(Context mContext,String name,String password) {
        BmobQuery<accountBean> eq1 = new BmobQuery<accountBean>();
        eq1.addWhereEqualTo("name", name);
        List<BmobQuery<accountBean>> andQuerys = new ArrayList<BmobQuery<accountBean>>();
        andQuerys.add(eq1);
        BmobQuery<accountBean> query = new BmobQuery<accountBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<accountBean>() {
            @Override
            public void done(List<accountBean> object, BmobException e) {
                if(e==null){
                    if(object.size() != 0){
                        //存在
                        System.out.println("账号存在");
                        EventBus.getDefault().post(new EventBusRegisterCheckMidMsg(true,false));
                    }else{
                        //不存在 进行注册
                        System.out.println("账号不存在");
                        accountBean acc = new accountBean();
                        acc.setName(name);
                        acc.setPassword(password);
                        acc.setNikeName(name);
                        acc.setDescribe("");
                        acc.setHead(String.valueOf(R.drawable.touxiang01));
                        acc.setSex("保密");
                        acc.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    System.out.println("注册成功");
                                    EventBus.getDefault().post(new EventBusRegisterCheckMidMsg(false,true));
                                } else {
                                    System.out.println("注册失败");
                                }
                            }
                        });
                    }
                }else{
                    //失败
                    System.out.println(e);
                    System.out.println("查询失败");
//                    XToast.error(mContext,"无网络连接").show();
                    TipDialog.showUnFinishDialog(mContext,"无网络连接");
                }
            }
        });
    }

    //修改用户信息
    public void update(Context mContext,accountBean acc){
        BmobQuery<accountBean> eq1 = new BmobQuery<accountBean>();
        eq1.addWhereEqualTo("name", acc.getName());
        List<BmobQuery<accountBean>> andQuerys = new ArrayList<BmobQuery<accountBean>>();
        andQuerys.add(eq1);
        BmobQuery<accountBean> query = new BmobQuery<accountBean>();
        query.and(andQuerys);
        query.findObjects(new FindListener<accountBean>() {
            @Override
            public void done(List<accountBean> object, BmobException e) {
                if(e==null){
                    if(object.size() != 0){
                        //存在,进行更新
                        System.out.println("账号存在");
                        accountBean category = new accountBean();
                        category.setName(acc.getName());
                        category.setPassword(acc.getPassword());
                        category.setDescribe(acc.getDescribe());
                        category.setSex(acc.getSex());
                        category.setHead(acc.getHead());
                        category.setNikeName(acc.getNikeName());
                        category.update(object.get(0).getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    System.out.println("修改成功");
                                } else {
                                    System.out.println("修改失败");
//                                    XToast.error(mContext,"修改失败").show();
                                }
                            }
                        });
                    }else{
                        //不存在
                        System.out.println("账号不存在");
                    }
                }else{
                    //失败
                    System.out.println("查询失败");
//                    XToast.error(mContext,"无网络连接").show();
                    TipDialog.showUnFinishDialog(mContext,"无网络连接");
                }
            }
        });
    }

}
