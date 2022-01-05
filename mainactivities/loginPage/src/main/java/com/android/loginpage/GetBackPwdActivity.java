package com.android.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.librarybase.topbarcolor.TopBar;
import com.xuexiang.xui.XUI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetBackPwdActivity extends AppCompatActivity {

    //=================================控件声明===============================
    private Button btn_finish_get_back;
    private EditText edit_get_back_username;
    private EditText edit_get_back_password;
    private EditText edit_get_back_confirm;

    //=================================其他变量声明============================
    private boolean ok_username=false;
    private boolean ok_password=false;
    private boolean ok_confirm=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_back_pwd);
        TopBar.setTopBarColor(GetBackPwdActivity.this);
        //=========================定义============================
        btn_finish_get_back = findViewById(R.id.btn_finish_get_back);
        edit_get_back_username = findViewById(R.id.edit_get_back_username);
        edit_get_back_password = findViewById(R.id.edit_get_back_password);
        edit_get_back_confirm = findViewById(R.id.edit_get_back_confirm);

        //=========================设置注册按钮跳转=========================
        btn_finish_get_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("username", edit_get_back_username.getText().toString().trim());
                intent.putExtra("password", edit_get_back_password.getText().toString().trim());
                setResult(2, intent);
                finish();
            }
        });
        //=========================设置注册按钮被禁止=========================
        btn_finish_get_back.setEnabled(false);
        //==========================错误提示============================
        //==========================用户名==============================
        edit_get_back_username.addTextChangedListener(usernameTextWatcher());
        //===========================密码===============================
        edit_get_back_password.addTextChangedListener(passwordTextWatcher());
        //===========================重复===============================
        edit_get_back_confirm.addTextChangedListener(confirmTextWatcher());
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
                editStart = edit_get_back_username.getSelectionStart();
                editEnd = edit_get_back_username.getSelectionEnd();
                if (temp.length() > 11) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_get_back_username.setText(s);//显示限制内的内容
                    edit_get_back_username.setSelection(tempSelection);//光标焦点设置在行末
                }
                String regEx = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
                // 编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                // 忽略大小写的写法
                Matcher matcher = pattern.matcher(s.toString().trim());
                // 字符串是否与正则表达式相匹配
                if(!matcher.matches()){
//                    getResources().getString(R.string.error_wrong_phone_number)
                    edit_get_back_username.setError("");
                    ok_username=false;
                }
                else {
                    ok_username=true;
                }
                if(matcher.matches()&&ok_password&&ok_confirm){
                    btn_finish_get_back.setEnabled(true);
                }
                else{
                    btn_finish_get_back.setEnabled(false);
                }
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
                editStart = edit_get_back_password.getSelectionStart();
                editEnd = edit_get_back_password.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_get_back_password.setText(s);//显示限制内的内容
                    edit_get_back_password.setSelection(tempSelection);//光标焦点设置在行末
                }
                String regEx = "^[a-zA-Z]\\w{5,17}$";
                // 编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                // 忽略大小写的写法
                Matcher matcher = pattern.matcher(s.toString().trim());
                // 字符串是否与正则表达式相匹配
                if(edit_get_back_confirm.getText().toString().trim().equals(s.toString())){
                    ok_confirm=true;
                    edit_get_back_confirm.setError(null,null);
                }
                else{
                    ok_confirm=false;
//                    getResources().getString(R.string.error_not_same_with_above)
                    edit_get_back_confirm.setError("");
                }
                if(!matcher.matches()){
//                    getResources().getString(R.string.error_password_regular)
                    edit_get_back_password.setError("");
                    ok_password=false;
                }
                else{
                    ok_password=true;
                }
                if(matcher.matches()&&ok_confirm&&ok_username){
                    btn_finish_get_back.setEnabled(true);
                }
                else{
                    btn_finish_get_back.setEnabled(false);
                }
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
                editStart = edit_get_back_confirm.getSelectionStart();
                editEnd = edit_get_back_confirm.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_get_back_confirm.setText(s);//显示限制内的内容
                    edit_get_back_confirm.setSelection(tempSelection);//光标焦点设置在行末
                }

                if(ok_username&&ok_password&&s.toString().trim().equals(edit_get_back_password.getText().toString().trim())){
                    ok_confirm=true;
                    btn_finish_get_back.setEnabled(true);
                }
                else{
//                    getResources().getString(R.string.error_not_same_with_above)
                    edit_get_back_confirm.setError("");
                    ok_confirm=false;
                    btn_finish_get_back.setEnabled(false);
                }
            }
        };
    }
}