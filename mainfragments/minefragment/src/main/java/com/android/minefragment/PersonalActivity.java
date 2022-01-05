package com.android.minefragment;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.eventBus.EventBusMineChangeMsgMidMsg;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout change_header,change_name,change_sex,change_pwd;
    private CircleImageView img_header;
    private TextView text_name,text_sex,text_phone_number;
    private CircleImageView img1,img2,img3,img4,img5,img6,img7,img8;
    private CircleImageView yes1,yes2,yes3,yes4,yes5,yes6,yes7,yes8;
    private EditText edit_nicheng;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        change_header = findViewById(R.id.change_header);
        change_name = findViewById(R.id.change_name);
        change_sex = findViewById(R.id.change_sex);
        change_pwd = findViewById(R.id.change_pwd);
        img_header = findViewById(R.id.img_header);
        text_name = findViewById(R.id.text_name);
        text_sex = findViewById(R.id.text_sex);
        text_phone_number = findViewById(R.id.text_phone_number);


        img_header.setImageDrawable(getResources().getDrawable(SharedStorage.getCurTouXiangDrawable(this)));
        text_name.setText(SharedStorage.getCurNiCheng(this));
        text_sex.setText(SharedStorage.getCurSex(this));
        text_phone_number.setText(SharedStorage.getCurUsername(this));

        TopBar.setTopBarColor(PersonalActivity.this);
        TitleBar add_plan_top_bar=findViewById(R.id.personal_top_bar);
        add_plan_top_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        setOnClick();
    }

    @Override
    protected void initData() {

    }

    private void setOnClick(){
        change_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHeader();
            }
        });
        change_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSex();
            }
        });
        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ARouterActivityPath.ChangePwd.CHANGE_PWD).navigation();
            }
        });
    }

    private void changeHeader(){

        /**
         * 显示自定义对话框
         */
        MaterialDialog dialog_layout = new MaterialDialog.Builder(PersonalActivity.this)
                .customView(R.layout.change_header_dialog, true)
                .title("选择头像")
                .positiveText("确认")
                .negativeText("取消")
                .positiveColor(getResources().getColor(R.color.main_green))
                .negativeColor(getResources().getColor(R.color.xui_config_color_red))
                .show();
        /**
         * 自定义确认按钮
         */
        dialog_layout.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                dialog_layout.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        img_header.setImageDrawable(getResources().getDrawable(SharedStorage.getCurTouXiangDrawable(PersonalActivity.this)));
                        EventBus.getDefault().post(new EventBusMineChangeMsgMidMsg(true));
                        dialog_layout.dismiss();
                    }
                });
            }
        });
        img1 = dialog_layout.findViewById(R.id.img_1);
        img2 = dialog_layout.findViewById(R.id.img_2);
        img3 = dialog_layout.findViewById(R.id.img_3);
        img4 = dialog_layout.findViewById(R.id.img_4);
        img5 = dialog_layout.findViewById(R.id.img_5);
        img6 = dialog_layout.findViewById(R.id.img_6);
        img7 = dialog_layout.findViewById(R.id.img_7);
        img8 = dialog_layout.findViewById(R.id.img_8);

        yes1 = dialog_layout.findViewById(R.id.yes_1);
        yes2 = dialog_layout.findViewById(R.id.yes_2);
        yes3 = dialog_layout.findViewById(R.id.yes_3);
        yes4 = dialog_layout.findViewById(R.id.yes_4);
        yes5 = dialog_layout.findViewById(R.id.yes_5);
        yes6 = dialog_layout.findViewById(R.id.yes_6);
        yes7 = dialog_layout.findViewById(R.id.yes_7);
        yes8 = dialog_layout.findViewById(R.id.yes_8);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        img8.setOnClickListener(this);

        setTouXiangBackground(SharedStorage.getCurTouXiangDrawable(this));
    }
    private void changeSex(){
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .setGravityCenter(true)
                .setTitle("性别")
                .addItem("保密")
                .addItem("男")
                .addItem("女")
                .setAddCancelBtn(true)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
//                                Toast.makeText(MainActivity.this, "Item " + (position + 1), Toast.LENGTH_SHORT).show();
                        if (position==0){
                            text_sex.setText("保密");
                            SharedStorage.setCurSex(PersonalActivity.this,"保密");
                            SQLiteTools.updateUserMsgToYun(PersonalActivity.this);
                        }
                        else if(position==1){
                            text_sex.setText("男");
                            SharedStorage.setCurSex(PersonalActivity.this,"男");
                            SQLiteTools.updateUserMsgToYun(PersonalActivity.this);
                        }
                        else if(position==2){
                            text_sex.setText("女");
                            SharedStorage.setCurSex(PersonalActivity.this,"女");
                            SQLiteTools.updateUserMsgToYun(PersonalActivity.this);
                        }
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int drawable_ids[]={R.drawable.touxiang01,R.drawable.touxiang02,R.drawable.touxiang03,R.drawable.touxiang04
                ,R.drawable.touxiang05,R.drawable.touxiang06,R.drawable.touxiang07,R.drawable.touxiang08};
        int view_ids[]={R.id.img_1,R.id.img_2,R.id.img_3,R.id.img_4,
                R.id.img_5,R.id.img_6,R.id.img_7,R.id.img_8};
        int index=0;
        for (;index<8;++index){
            if (view_ids[index]==id)break;
        }
        if (id == R.id.img_1 || id == R.id.img_2 || id == R.id.img_3 || id == R.id.img_4 || id == R.id.img_5 || id == R.id.img_6 || id == R.id.img_7 || id == R.id.img_8) {
            setTouXiangBackground(drawable_ids[index]);
        }
    }

    private void setTouXiangBackground(int mDrawableId){
        int drawable_ids[]={R.drawable.touxiang01,R.drawable.touxiang02,R.drawable.touxiang03,R.drawable.touxiang04
                ,R.drawable.touxiang05,R.drawable.touxiang06,R.drawable.touxiang07,R.drawable.touxiang08};
        int view_ids[]={R.id.img_1,R.id.img_2,R.id.img_3,R.id.img_4,
                R.id.img_5,R.id.img_6,R.id.img_7,R.id.img_8};
        int mViewId;
        int index=0;
        for (;index<8;++index){
            if (drawable_ids[index]==mDrawableId)break;
        }
        mViewId=view_ids[index];
        img1.setAlpha(1f);
        img2.setAlpha(1f);
        img3.setAlpha(1f);
        img4.setAlpha(1f);
        img5.setAlpha(1f);
        img6.setAlpha(1f);
        img7.setAlpha(1f);
        img8.setAlpha(1f);
        yes1.setVisibility(View.GONE);
        yes2.setVisibility(View.GONE);
        yes3.setVisibility(View.GONE);
        yes4.setVisibility(View.GONE);
        yes5.setVisibility(View.GONE);
        yes6.setVisibility(View.GONE);
        yes7.setVisibility(View.GONE);
        yes8.setVisibility(View.GONE);

        CircleImageView view=img1;
        CircleImageView viewYes=yes1;
        int img_id=R.drawable.touxiang01;
        if(img2.getId()==mViewId) {
            view = img2;
            viewYes = yes2;
            img_id=R.drawable.touxiang02;
        }
        else if(img3.getId()==mViewId) {
            view = img3;
            viewYes = yes3;
            img_id=R.drawable.touxiang03;
        }
        else if(img4.getId()==mViewId) {
            view = img4;
            viewYes = yes4;
            img_id=R.drawable.touxiang04;
        }
        else if(img5.getId()==mViewId) {
            view = img5;
            viewYes = yes5;
            img_id=R.drawable.touxiang05;
        }
        else if(img6.getId()==mViewId) {
            view = img6;
            viewYes = yes6;
            img_id=R.drawable.touxiang06;
        }
        else if(img7.getId()==mViewId) {
            view = img7;
            viewYes = yes7;
            img_id=R.drawable.touxiang07;
        }
        else if(img8.getId()==mViewId) {
            view = img8;
            viewYes = yes8;
            img_id=R.drawable.touxiang08;
        }
        view.setAlpha(0.4f);
        view.setFillColor(getResources().getColor(R.color.main_deep_grad));
        viewYes.setVisibility(View.VISIBLE);

        SharedStorage.setCurTouXiangDrawable(this,img_id);
        SQLiteTools.updateUserMsgToYun(PersonalActivity.this);
    }

    private void changeName(){
        /**
         * 显示自定义对话框
         */
        MaterialDialog dialog_layout = new MaterialDialog.Builder(PersonalActivity.this)
                .customView(R.layout.change_nicheng_dialog, true)
                .title("更改昵称")
                .positiveText("确认")
                .negativeText("取消")
                .positiveColor(getResources().getColor(R.color.main_green))
                .negativeColor(getResources().getColor(R.color.xui_config_color_red))
                .show();
        /**
         * 自定义确认按钮
         */
        dialog_layout.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                dialog_layout.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edit_nicheng.getText().toString().trim().isEmpty()){
                            XToast.warning(PersonalActivity.this,"昵称不能为空").show();
                            return;
                        }
                        else{
                            SharedStorage.setCurNiCheng(PersonalActivity.this,edit_nicheng.getText().toString().trim());
                            text_name.setText(edit_nicheng.getText().toString().trim());
                            EventBus.getDefault().post(new EventBusMineChangeMsgMidMsg(true));
                            dialog_layout.dismiss();
                            SQLiteTools.updateUserMsgToYun(PersonalActivity.this);
                        }
                    }
                });
            }
        });
        edit_nicheng=dialog_layout.findViewById(R.id.edit_nicheng);
    }
}