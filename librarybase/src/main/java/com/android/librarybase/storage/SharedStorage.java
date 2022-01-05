package com.android.librarybase.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.librarybase.R;
import com.android.librarybase.constant.FilePath;

public class SharedStorage {
    public static SharedPreferences sharedPreferences;
    public static void storeLoginMsg(Context context,
                                     String username,String password,
                                     boolean is_remember_password){
        //步骤1：创建一个SharedPreferences对象
//        SharedPreferences sharedPreferences= getSharedPreferences("LoginMsgFile", Context.MODE_PRIVATE);
        //步骤2： 实例化SharedPreferences.Editor对象
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //步骤3：将获取过来的值放入文件
        if(!sharedPreferences.getString("username", "").equals(username))
            editor.putString("username", username);
        if(!sharedPreferences.getString("password", "").equals(password))
            editor.putString("password", password);
        if(sharedPreferences.getBoolean("is_remember_password", false)!=is_remember_password)
            editor.putBoolean("is_remember_password", is_remember_password);
        if(!sharedPreferences.getBoolean("is_login", false))
            editor.putBoolean("is_login", true);
        editor.putInt("touxiang", R.drawable.touxiang01);
        editor.putString("nicheng", username);
        editor.putString("sex", "保密");
        //步骤4：提交
        editor.commit();
    }
    public static void storeLogoutMsg(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_login", false);
        //步骤4：提交
        editor.commit();
    }
    public static boolean isLogin(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_login",false);
    }
    public static String getCurUsername(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("username","");
    }
    public static String getCurPassword(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("password","");
    }

    public static boolean getIsRememberPassword(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_remember_password",false);
    }
    public static int getCurTouXiangDrawable(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("touxiang",R.drawable.touxiang01);
    }
    public static String getCurNiCheng(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("nicheng",sharedPreferences.getString("username",""));
    }
    public static String getCurSex(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("sex","保密");
    }

    public static String getCurDescribe(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("describe","");
    }

    /**
     * set
     * @param context
     * @return
     */
    public static void setCurPassword(Context context,String pwd){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", pwd);
        //步骤4：提交
        editor.commit();
    }
    public static void setCurTouXiangDrawable(Context context, int mDrawableId){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("touxiang", mDrawableId);
        //步骤4：提交
        editor.commit();
    }
    public static void setCurNiCheng(Context context,String nicheng){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nicheng", nicheng);
        //步骤4：提交
        editor.commit();
    }
    public static void setCurSex(Context context,String sex){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sex", sex);
        //步骤4：提交
        editor.commit();
    }

    public static void setCurDescribe(Context context,String describe){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("describe", describe);
        //步骤4：提交
        editor.commit();
    }



    public static boolean changeAndIsReverseTimeLine(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("isReverseTimeLine",false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isReverseTimeLine", !is);
        editor.commit();
        return !is;
    }

    public static boolean isReverseTimeLine(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("isReverseTimeLine",false);
        return is;
    }

    public static String getNowTagInHome(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        String tag_name=sharedPreferences.getString("nowTagInHome","所有");
        return tag_name;
    }

    public static void setNowTagInHome(Context context,String tag_name){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nowTagInHome", tag_name);
        editor.commit();
    }

    public static void setNowTagInHomeWhenEmpty(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        String tag_name=sharedPreferences.getString("nowTagInHome","");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(tag_name.equals("")) {
            editor.putString("nowTagInHome", "所有");
            editor.commit();
        }

    }

    public static boolean isStartTimeLine(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("changeStartTimeLine",true);
        return is;
    }

    public static void changeStartTimeLine(Context context,boolean isStart){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("changeStartTimeLine", isStart);
        editor.commit();
    }

    public static boolean isPreTimeLine(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("changePreTimeLine",true);
        return is;
    }

    public static void changePreTimeLine(Context context,boolean isPre){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("changePreTimeLine", isPre);
        editor.commit();

    }

    public static int isFinishTimeline(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        int is=sharedPreferences.getInt("changeFinishTimeline",-1);
        return is;
    }

    public static void changeFinishTimeline(Context context,int isFinish){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("changeFinishTimeline", isFinish);
        editor.commit();
    }



    public static boolean isStartVerticalCommon(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("changeStartVerticalCommon",true);
        return is;
    }

    public static void changeStartVerticalCommon(Context context,boolean isStart){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("changeStartVerticalCommon", isStart);
        editor.commit();
    }

    public static int isFinishVerticalCommon(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        int is=sharedPreferences.getInt("changeFinishVerticalCommon",-1);
        return is;
    }

    public static void changeFinishVerticalCommon(Context context,int isFinish){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("changeFinishVerticalCommon", isFinish);
        editor.commit();
    }

    public static boolean changeAndIsReverseVerticalCommon(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("isReverseVerticalCommon",false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isReverseVerticalCommon", !is);
        editor.commit();
        return !is;
    }

    public static boolean isReverseVerticalCommon(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        boolean is=sharedPreferences.getBoolean("isReverseVerticalCommon",false);
        return is;
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getBeifenObjectId(Context context){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("BeifenObjectId","");
    }

    /**
     * set
     * @param context
     * @return
     */
    public static void setBeifenObjectId(Context context,String object_id){
        sharedPreferences=context.getSharedPreferences(FilePath.LOGIN_MSG_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BeifenObjectId", object_id);
        //步骤4：提交
        editor.commit();
    }
}
