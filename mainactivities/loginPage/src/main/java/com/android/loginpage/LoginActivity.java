package com.android.loginpage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basemethod.TipDialog;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.constant.FilePath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.eventBus.EventBusLoginCheckMidMsg;
import com.android.librarydb.eventBus.EventBusLoginOrLogoutMidMsg;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



@Route(path= ARouterActivityPath.Login.PAGER_LOGIN)
public class LoginActivity extends BaseActivity {
    //=================================控件声明===============================
    private Button btn_login;
    private TextView tv_to_register;
    private TextView tv_to_get_back_pwd;
    private EditText edit_login_username;
    private EditText edit_login_password;

    //=================================其他变量声明============================
    private boolean ok_username=false;
    private boolean ok_password=false;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_login;
    }

    @Subscribe
    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        ARouter.getInstance().inject(this);
        TopBar.setTopBarColor(LoginActivity.this);
        //=========================定义============================
        btn_login = findViewById(R.id.btn_login);
        tv_to_register = findViewById(R.id.tv_to_register);
        tv_to_get_back_pwd = findViewById(R.id.tv_to_get_back_pwd);
        edit_login_username = findViewById(R.id.edit_login_username);
        edit_login_password = findViewById(R.id.edit_login_password);

        edit_login_username.setText(SharedStorage.getCurUsername(LoginActivity.this));
        edit_login_password.setText(SharedStorage.getCurPassword(LoginActivity.this));

        //=========================设置注册页面的跳转=========================
        tv_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //=========================设置找回密码页面的跳转=========================
        tv_to_get_back_pwd.setEnabled(false);
        tv_to_get_back_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, GetBackPwdActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        //=========================登录按钮的监听事件=========================
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteTools.loginFromYun(LoginActivity.this
                        ,edit_login_username.getText().toString().trim()
                        ,edit_login_password.getText().toString().trim());
                Log.e("阿斯顿撒大大大伟大","萨法法发文发发访问团范文");
            }
        });
        //=========================设置登录按钮被禁止=========================
        if(edit_login_username.getText().toString().trim().isEmpty()
                ||edit_login_password.getText().toString().trim().isEmpty()){
            btn_login.setEnabled(false);
        }
        if (!edit_login_password.getText().toString().trim().isEmpty())incEnable(1);
        if (!edit_login_username.getText().toString().trim().isEmpty())incEnable(0);

        //==========================错误提示============================
        //==========================用户名==============================
        edit_login_username.addTextChangedListener(usernameTextWatcher());
        //===========================密码===============================
        edit_login_password.addTextChangedListener(passwordTextWatcher());
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            //下面的1为startActivityForResult(intent, 1);中的1
            case 1:
                //这里的1为setResult(1, intent);中的1
                if (resultCode==1){
                    edit_login_username.setText(data.getStringExtra("username"));
                    edit_login_password.setText(data.getStringExtra("password"));
                }
                break;
            case 2:
                //这里的1为setResult(2, intent);中的2
                if (resultCode==2){
                    edit_login_username.setText(data.getStringExtra("username"));
                    edit_login_password.setText(data.getStringExtra("password"));
                }
                break;
            default:
                break;

        }
    }

    private TextWatcher usernameTextWatcher(){
        return new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = edit_login_username.getSelectionStart();
                editEnd = edit_login_username.getSelectionEnd();
                if (temp.length() > 11) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_login_username.setText(s);//显示限制内的内容
                    edit_login_username.setSelection(tempSelection);//光标焦点设置在行末
                }
//                String regEx = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
//                // 编译正则表达式
//                Pattern pattern = Pattern.compile(regEx);
//                // 忽略大小写的写法
//                Matcher matcher = pattern.matcher(s.toString().trim());
//                // 字符串是否与正则表达式相匹配
//                if(!matcher.matches()) {
////                    ok_username=false;
//                    decEnable(0);
//                }
//                else{
////                    ok_username=true;
//                    incEnable(0);
//                }
                if (s.toString().trim().isEmpty())decEnable(0);
                else incEnable(0);
            }
        };
    }

    private TextWatcher passwordTextWatcher() {
        return new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = edit_login_password.getSelectionStart();
                editEnd = edit_login_password.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_login_password.setText(s);//显示限制内的内容
                    edit_login_password.setSelection(tempSelection);//光标焦点设置在行末
                }
                if(s.length()==0){
//                    ok_password=false;
//                    btn_login.setEnabled(false);
                    decEnable(1);
                }
                else{
//                    ok_password=true;
//                    if(ok_username)
//                        btn_login.setEnabled(true);
                    incEnable(1);
                }
            }
        };
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusLoginCheckMidMsg eventBusLoginCheckMidMsg){
        Log.e("登录","登录");
        if (!eventBusLoginCheckMidMsg.isHas_username()){
//            XToast.error(LoginActivity.this,"该账号不存在").show();
            TipDialog.showUnFinishDialog(LoginActivity.this,"该账号不存在");
        }
        if (eventBusLoginCheckMidMsg.isLogin_success()){
            SharedStorage.storeLoginMsg(LoginActivity.this
                    ,edit_login_username.getText().toString().trim()
                    ,edit_login_password.getText().toString().trim()
                    ,false);
            EventBus.getDefault().post(new EventBusLoginOrLogoutMidMsg(true,true));
            setResult(1);
            finish();
        }
        if (eventBusLoginCheckMidMsg.isWrong_pwd()){
            XToast.error(LoginActivity.this,"密码错误").show();
        }
    }
    private void incEnable(int num){
        if (num==0)ok_username=true;
        else if(num==1)ok_password=true;
        if(ok_username&&ok_password)btn_login.setEnabled(true);
        else btn_login.setEnabled(false);
    }
    private void decEnable(int num){
        if (num==0)ok_username=false;
        else if(num==1)ok_password=false;
        btn_login.setEnabled(false);
    }
}