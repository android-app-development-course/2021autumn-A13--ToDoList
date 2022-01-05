package com.android.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.librarybase.basemethod.TipDialog;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.eventBus.EventBusLoginCheckMidMsg;
import com.android.librarydb.eventBus.EventBusLoginOrLogoutMidMsg;
import com.android.librarydb.eventBus.EventBusRegisterCheckMidMsg;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity {

    //=================================控件声明===============================
    private Button btn_register;
    private MaterialEditText edit_register_username;
    private MaterialEditText edit_register_password;
    private MaterialEditText edit_register_confirm;

    //=================================其他变量声明============================
    private boolean ok_username=false;
    private boolean ok_password=false;
    private boolean ok_confirm=false;
//    private int btn_enable=0;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_register;
    }

    @Subscribe
    @Override
    protected void initView() {
//=========================定义============================
        EventBus.getDefault().register(this);
        btn_register = findViewById(R.id.btn_register);
        TopBar.setTopBarColor(RegisterActivity.this);
        edit_register_username = findViewById(R.id.edit_register_username);
        edit_register_password = findViewById(R.id.edit_register_password);
        edit_register_confirm = findViewById(R.id.edit_register_confirm);

        //=========================设置注册按钮跳转=========================
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteTools.registerFromYun(RegisterActivity.this,edit_register_username.getText().toString().trim(),edit_register_password.getText().toString().trim());
            }
        });
        //=========================设置注册按钮被禁止=========================
        btn_register.setEnabled(false);
        //手机号正则表达式
        String regExPhone = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
        String regExPassword = "^[a-zA-Z]\\w{5,17}$";
        //==========================错误提示============================
        //==========================用户名==============================
//        edit_register_username.addTextChangedListener(usernameTextWatcher());
//        edit_register_username.addValidator(new RegexpValidator(getResources().getString(R.string.error_wrong_phone_number), regExPhone));
        edit_register_username.addTextChangedListener(usernameTextWatcher());
        //===========================密码===============================
//        edit_register_password.addValidator(new RegexpValidator(getResources().getString(R.string.error_password_regular), regExPassword));
        edit_register_password.addTextChangedListener(passwordTextWatcher());
        //===========================重复===============================
        edit_register_confirm.addTextChangedListener(confirmTextWatcher());

//        edit_register_confirm.addValidator(new RegexpValidator(getResources().getString(R.string.error_wrong_phone_number), "^"+edit_register_password.getText().toString().trim()+"$"));
    }

    @Override
    protected void initData() {

    }





    private TextWatcher usernameTextWatcher() {
        return new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp=s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = edit_register_username.getSelectionStart();
                editEnd = edit_register_username.getSelectionEnd();
                if (temp.length() > 11) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_register_username.setText(s);//显示限制内的内容
                    edit_register_username.setSelection(tempSelection);//光标焦点设置在行末
                }
                String regEx = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
                // 编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                // 忽略大小写的写法
                Matcher matcher = pattern.matcher(s.toString().trim());
                // 字符串是否与正则表达式相匹配
                if(!matcher.matches()){
//                    getResources().getString(R.string.error_wrong_phone_number)
                    edit_register_username.setError(getResources().getString(R.string.error_wrong_phone_number));
//                    ok_username=false;
                    decEnable(0);

                }
                else {
//                    ok_username=true;
                    incEnable(0);
                }
//                if(matcher.matches()&&ok_password&&ok_confirm){
//                    btn_register.setEnabled(true);
//                }
//                else{
//                    btn_register.setEnabled(false);
//                }
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
                temp=s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = edit_register_password.getSelectionStart();
                editEnd = edit_register_password.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_register_password.setText(s);//显示限制内的内容
                    edit_register_password.setSelection(tempSelection);//光标焦点设置在行末
                }
                String regEx = "^[a-zA-Z]\\w{5,17}$";
                // 编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                // 忽略大小写的写法
                Matcher matcher = pattern.matcher(s.toString().trim());
                // 字符串是否与正则表达式相匹配
                if(edit_register_confirm.getText().toString().trim().equals(s.toString())){
//                    ok_confirm=true;
                    edit_register_confirm.setError(null);
                    incEnable(2);
                }
                else{
//                    ok_confirm=false;
//                    getResources().getString(R.string.error_not_same_with_above)
                    edit_register_confirm.setError(getResources().getString(R.string.error_not_same_with_above));
                    decEnable(2);
                }
                if(!matcher.matches()){
//                    getResources().getString(R.string.error_password_regular)
                    edit_register_password.setError(getResources().getString(R.string.error_password_regular));
//                    ok_password=false;
                    decEnable(1);
                }
                else{
//                    ok_password=true;
                    incEnable(1);
                }
//                if(matcher.matches()&&ok_confirm&&ok_username){
//                    btn_register.setEnabled(true);
//                }
//                else{
//                    btn_register.setEnabled(false);
//                }
            }
        };
    }

    private TextWatcher confirmTextWatcher() {
        return new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp=s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = edit_register_confirm.getSelectionStart();
                editEnd = edit_register_confirm.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_register_confirm.setText(s);//显示限制内的内容
                    edit_register_confirm.setSelection(tempSelection);//光标焦点设置在行末
                }

                edit_register_confirm.addValidator(new RegexpValidator(getResources().getString(R.string.error_wrong_phone_number), "^"+edit_register_password.getText().toString().trim()+"$"));

//                if(ok_username&&ok_password&&s.toString().trim().equals(edit_register_password.getText().toString().trim())){
//                    ok_confirm=true;
//                    btn_register.setEnabled(true);
//                }
//                else{
//                    edit_register_confirm.setError(getResources().getString(R.string.error_not_same_with_above));
//                    ok_confirm=false;
//                    btn_register.setEnabled(false);
//                }
                if(s.toString().trim().equals(edit_register_password.getText().toString().trim())){
                    incEnable(2);
                }
                else {
                    decEnable(2);
                    edit_register_confirm.setError(getResources().getString(R.string.error_not_same_with_above));
                }
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusRegisterCheckMidMsg eventBusRegisterCheckMidMsg){
        Log.e("登录","登录");
        if (eventBusRegisterCheckMidMsg.isHas_username()){
//            XToast.error(RegisterActivity.this,"该账号已存在").show();
            TipDialog.showUnFinishDialog(RegisterActivity.this,"该账号已存在");
        }
        if (eventBusRegisterCheckMidMsg.isRegister_success()){
            Intent intent = new Intent();
            intent.putExtra("username", edit_register_username.getText().toString().trim());
            intent.putExtra("password", edit_register_password.getText().toString().trim());
            setResult(1, intent);
            finish();
        }
    }

    private void incEnable(int num){
        if (num==0)ok_username=true;
        else if(num==1)ok_password=true;
        else if(num==2)ok_confirm=true;
        if(ok_username&&ok_password&&ok_confirm)btn_register.setEnabled(true);
        else btn_register.setEnabled(false);
    }
    private void decEnable(int num){
        if (num==0)ok_username=false;
        else if(num==1)ok_password=false;
        else if(num==2)ok_confirm=false;
        btn_register.setEnabled(false);
    }
}