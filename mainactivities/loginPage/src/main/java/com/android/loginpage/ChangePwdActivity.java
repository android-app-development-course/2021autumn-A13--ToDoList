package com.android.loginpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.validation.RegexpValidator;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route(path = ARouterActivityPath.ChangePwd.CHANGE_PWD)
public class ChangePwdActivity extends BaseActivity {

    //=================================控件声明===============================
    private Button btn_change;
    private MaterialEditText edit_old_password;
    private MaterialEditText edit_new_password;
    private MaterialEditText edit_new_confirm;

    //=================================其他变量声明============================
    private boolean ok_username=false;
    private boolean ok_password=false;
    private boolean ok_confirm=false;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initView() {
//=========================定义============================
        ARouter.getInstance().inject(this);
        TopBar.setTopBarColor(ChangePwdActivity.this);
        TitleBar change_top_bar=findViewById(R.id.change_top_bar);
        change_top_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_change = findViewById(R.id.btn_change);
        TopBar.setTopBarColor(ChangePwdActivity.this);
        edit_old_password = findViewById(R.id.edit_old_password);
        edit_new_password = findViewById(R.id.edit_new_password);
        edit_new_confirm = findViewById(R.id.edit_new_confirm);

        //=========================设置注册按钮跳转=========================
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!SharedStorage.getCurPassword(ChangePwdActivity.this).equals(edit_old_password.getText().toString().trim())){
                    XToast.error(ChangePwdActivity.this,"原密码不正确").show();
                }
                else{
                    SharedStorage.setCurPassword(ChangePwdActivity.this,edit_new_confirm.getText().toString().trim());
                    SQLiteTools.updateUserMsgToYun(ChangePwdActivity.this);
//                    new MaterialDialog.Builder(ChangePwdActivity.this)
//                            .content("修改密码后需重新登录")
//                            .positiveText("确认")
//                            .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                                }
//                            })
//                            .show();
                    XToast.success(ChangePwdActivity.this,"修改完成").show();
                    finish();
                }
            }
        });
        //=========================设置注册按钮被禁止=========================
        btn_change.setEnabled(false);
        //手机号正则表达式
        String regExPassword = "^[a-zA-Z]\\w{5,17}$";
        //==========================错误提示============================
        //==========================用户名==============================
//        edit_register_username.addTextChangedListener(usernameTextWatcher());
//        edit_register_username.addValidator(new RegexpValidator(getResources().getString(R.string.error_wrong_phone_number), regExPhone));
        edit_old_password.addTextChangedListener(usernameTextWatcher());
        //===========================密码===============================
//        edit_new_password.addValidator(new RegexpValidator(getResources().getString(R.string.error_password_regular), regExPassword));
        edit_new_password.addTextChangedListener(passwordTextWatcher());
        //===========================重复===============================
        edit_new_confirm.addTextChangedListener(confirmTextWatcher());

//        edit_register_confirm.addValidator(new RegexpValidator(getResources().getString(R.string.error_wrong_phone_number), "^"+edit_new_password.getText().toString().trim()+"$"));
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
                editStart = edit_old_password.getSelectionStart();
                editEnd = edit_old_password.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_new_password.setText(s);//显示限制内的内容
                    edit_new_password.setSelection(tempSelection);//光标焦点设置在行末
                }
                if(temp.length()>0)ok_username=true;
                if(ok_password&&ok_confirm){
                    btn_change.setEnabled(true);
                }
                else{
                    btn_change.setEnabled(false);
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
                editStart = edit_new_password.getSelectionStart();
                editEnd = edit_new_password.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_new_password.setText(s);//显示限制内的内容
                    edit_new_password.setSelection(tempSelection);//光标焦点设置在行末
                }
                String regEx = "^[a-zA-Z]\\w{5,17}$";
                // 编译正则表达式
                Pattern pattern = Pattern.compile(regEx);
                // 忽略大小写的写法
                Matcher matcher = pattern.matcher(s.toString().trim());
                // 字符串是否与正则表达式相匹配
                if(edit_new_confirm.getText().toString().trim().equals(s.toString())){
                    ok_confirm=true;
                    edit_new_confirm.setError(null,null);
                }
                else{
                    ok_confirm=false;
//                    getResources().getString(R.string.error_not_same_with_above)
                    edit_new_confirm.setError(getResources().getString(R.string.error_not_same_with_above));
                }
                if(!matcher.matches()){
//                    getResources().getString(R.string.error_password_regular)
                    edit_new_password.setError(getResources().getString(R.string.error_password_regular));
                    ok_password=false;
                }
                else{
                    ok_password=true;
                }
                if(matcher.matches()&&ok_confirm&&ok_username){
                    btn_change.setEnabled(true);
                }
                else{
                    btn_change.setEnabled(false);
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
                editStart = edit_new_confirm.getSelectionStart();
                editEnd = edit_new_confirm.getSelectionEnd();
                if (temp.length() > 18) {//输入字数限制
                    s.delete(editStart - 1, editEnd);//删除限制外的内容
                    int tempSelection = editStart;
                    edit_new_confirm.setText(s);//显示限制内的内容
                    edit_new_confirm.setSelection(tempSelection);//光标焦点设置在行末
                }

                edit_new_confirm.addValidator(new RegexpValidator(getResources().getString(R.string.error_wrong_phone_number), "^"+edit_new_password.getText().toString().trim()+"$"));

                if(ok_username&&ok_password&&s.toString().trim().equals(edit_new_password.getText().toString().trim())){
                    ok_confirm=true;
                    btn_change.setEnabled(true);
                }
                else{
//                    getResources().getString(R.string.error_not_same_with_above)
                    edit_new_confirm.setError(getResources().getString(R.string.error_not_same_with_above));
                    ok_confirm=false;
                    btn_change.setEnabled(false);
                }
            }
        };
    }

}