package com.android.plandetail;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.cardview.widget.CardView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.Screen.ScreenDatas;
import com.android.librarybase.basemethod.GetTime;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarydb.bean__.CardBean;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.eventBus.EventBusSetNewTagMidMsg;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.switchbutton.SwitchButton;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;

@Route(path= ARouterActivityPath.PlanDetail.PLAN_DETAIL)
public class PlanDetailActivity extends BaseActivity {
    private EditText edit_name;
    private ButtonView btn_start_date;
    private ButtonView btn_start_time;
    private ButtonView btn_end_date;
    private ButtonView btn_end_time;
    private MultiLineEditText edit_content;
    private CardView btn_add;
    private TitleBar titleBar;
    private TextView btn_add_text_detail;
    private SwitchButton btn_all_day;
    private FrameLayout fl_add_tag;
    private TextView tv_add_tag;


    private FlowTagLayout mSingleFlowTagLayout;
    private FlowTagAdapter tagAdapter = new FlowTagAdapter(this);


    @Autowired(name = "card_id")
    int card_id;

    private String select_tag_name;

    @Subscribe
    @Override
    protected int getLayoutId() {
        EventBus.getDefault().register(this);
        return R.layout.activity_plan_detail;
    }

    @Override
    protected void initView() {
        TopBar.setTopBarColor(this);
        ARouter.getInstance().inject(this);


        edit_name=findViewById(R.id.edit_name_detail);

        btn_all_day = findViewById(R.id.btn_all_day);

        btn_start_date = findViewById(R.id.btn_start_date);
        btn_start_time = findViewById(R.id.btn_start_time);
        btn_end_date = findViewById(R.id.btn_end_date);
        btn_end_time = findViewById(R.id.btn_end_time);

        int radius=ScreenDatas.dpToPx(PlanDetailActivity.this,5);
        btn_start_date.setRadius(radius,radius,radius,radius);
        btn_start_time.setRadius(radius,radius,radius,radius);
        btn_end_date.setRadius(radius,radius,radius,radius);
        btn_end_time.setRadius(radius,radius,radius,radius);

        long nowTime01=GetTime.getNowTimeLong();
        long nowTime02=nowTime01+60*60*1000;
        btn_start_date.setText(GetTime.getNowTimeStr(nowTime01,"yyyy/MM/dd"));
        btn_start_time.setText(GetTime.getNowTimeStr(nowTime01,"HH:mm"));
        btn_end_date.setText(GetTime.getNowTimeStr(nowTime02,"yyyy/MM/dd"));
        btn_end_time.setText(GetTime.getNowTimeStr(nowTime02,"HH:mm"));

        edit_content=findViewById(R.id.edit_content_detali);
        btn_add=findViewById(R.id.btn_add_detail);
        titleBar = findViewById(R.id.top_bar_detail);
        btn_add_text_detail = findViewById(R.id.btn_add_text_detail);
        mSingleFlowTagLayout = findViewById(R.id.flowlayout_single_select);


        fl_add_tag = findViewById(R.id.fl_add_tag);
        tv_add_tag = findViewById(R.id.tv_add_tag);
        tv_add_tag.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        fl_add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ARouterActivityPath.SetLabels.SET_LABELS).navigation();
            }
        });


//        labels.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
//            @Override
//            public void onLabelClick(TextView label, Object data, int position) {
//                select_tag_name=label.getText().toString().trim();
//            }
//        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!writeToDB())return;
                if(card_id!=-1){
                    XToast.success(XUI.getContext(), "保存成功").show();
                }
                else {
                    XToast.success(XUI.getContext(), "添加成功").show();
                }
                EventBus.getDefault().post(new EventBusCardMidMsg(true));
                String str_start_time=btn_start_date.getText().toString().trim();
                if (btn_start_time.getVisibility()==View.VISIBLE){
                    str_start_time+=" "+btn_start_time.getText().toString().trim();
                }
                String str_end_time=btn_end_date.getText().toString().trim();
                if (btn_end_time.getVisibility()==View.VISIBLE){
                    str_end_time+=" "+btn_end_time.getText().toString().trim();
                }
                setResult(1);
                finish();
            }
        });

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(card_id!=-1){
                    XToast.warning(XUI.getContext(), "取消添加").show();
                }
                else {
                    XToast.warning(XUI.getContext(), "取消保存").show();
                }
                setResult(1);
                finish();
            }
        });
        if(card_id!=-1){
            titleBar.setTitle("编辑计划");
            btn_add_text_detail.setText("保存");
        }
        else {
            titleBar.setTitle("新建计划");
            btn_add_text_detail.setText("添加计划");
        }

        btn_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(PlanDetailActivity.this,  btn_start_date, Calendar.getInstance());
            }
        });
        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(PlanDetailActivity.this,  btn_start_time, Calendar.getInstance());
            }
        });
        btn_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(PlanDetailActivity.this,  btn_end_date, Calendar.getInstance());
            }
        });
        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(PlanDetailActivity.this,  btn_end_time, Calendar.getInstance());
            }
        });

        btn_all_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    btn_end_time.setVisibility(View.GONE);
                    btn_start_time.setVisibility(View.GONE);
                }
                else{
                    btn_end_time.setVisibility(View.VISIBLE);
                    btn_start_time.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {
//        initTags();
        initCardNotOneSingleFlowTagLayout();
    }


    /**
     * 日期选择
     */
    private void showDatePickerDialog(Context context, final ButtonView edit, Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context
                , R.style.MyDatePickerDialogTheme
                , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edit.setText(String.format("%d/%02d/%02d", year, (month + 1), dayOfMonth));
                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        /**
         * 确认取消按钮颜色
         */
        datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.main_black));
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.main_black));
    }

    /**
     * 时间选择
     */
    private void showTimePickerDialog(Context context, final ButtonView edit, Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context
                , R.style.MyDatePickerDialogTheme
                , new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edit.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , false);
        timePickerDialog.show();
        /**
         * 确认取消按钮颜色
         */
        timePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.main_black));
        timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.main_black));
    }

    private boolean writeToDB(){
        String name=edit_name.getText().toString().trim();
        if (name.isEmpty()){
            XToast.warning(XUI.getContext(), "计划名不能为空").show();
            return false;
        }
        String content=edit_content.getContentText().trim();

        String str_start_time=btn_start_date.getText().toString().trim();
        long start_time;
        if(btn_start_time.getVisibility()==View.VISIBLE) {
            str_start_time += " " + btn_start_time.getText().toString().trim();
            start_time= GetTime.timeToStamp(str_start_time,"yyyy/MM/dd HH:mm");
        }
        else {
            str_start_time += " " + "00:00";
            start_time= GetTime.timeToStamp(str_start_time,"yyyy/MM/dd HH:mm");
        }

        long end_time;
        String str_end_time=btn_end_date.getText().toString().trim();
        if(btn_end_time.getVisibility()==View.VISIBLE) {
            str_end_time += " " + btn_end_time.getText().toString().trim();
            end_time = GetTime.timeToStamp(str_end_time,"yyyy/MM/dd HH:mm");
        }
        else{
            str_end_time += " " + "23:59";
            end_time = GetTime.timeToStamp(str_end_time,"yyyy/MM/dd HH:mm");
        }
        if(start_time>=end_time){
            XToast.error(XUI.getContext(), "结束时间不可早于开始时间").show();
            return false;
        }
        if(card_id==-1)SQLiteTools.insertCardDB(PlanDetailActivity.this,select_tag_name,name,false,start_time,end_time,content,btn_all_day.isChecked());
        else {
            CardBean mCardIntent=SQLiteTools.getCard(PlanDetailActivity.this,card_id);
            SQLiteTools.updateCardDB(PlanDetailActivity.this,card_id,select_tag_name,name,mCardIntent.isIs_finish(),start_time,end_time,content,btn_all_day.isChecked());
        }
        return true;
    }

    private void initSingleFlowTagLayout() {
        mSingleFlowTagLayout.setAdapter(tagAdapter);
        mSingleFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        mSingleFlowTagLayout.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, int position, List<Integer> selectedList) {
                select_tag_name=parent.getAdapter().getItem(position).toString().trim();

            }
        });
        List<String> label_list = SQLiteTools.getTagNames(this);
        Log.e("label_list", String.valueOf(label_list));
        tagAdapter.clearAndAddTags(label_list);
        if(!label_list.isEmpty()) {
            select_tag_name = label_list.get(0);
            tagAdapter.setSelectedPositions(0);
        }
        Log.e("select_tag_name",select_tag_name);
    }

    private void initCardNotOneSingleFlowTagLayout() {
        initSingleFlowTagLayout();
        if(card_id!=-1){
            CardBean mCardIntent=SQLiteTools.getCard(PlanDetailActivity.this,card_id);
            int pos=0;
            Log.e("sizesize", String.valueOf(mSingleFlowTagLayout.getChildCount()));
            for (;pos<mSingleFlowTagLayout.getChildCount();++pos){
                String tagtag=mSingleFlowTagLayout.getAdapter().getItem(pos).toString().trim();
                if(mCardIntent.getTag_name().equals(tagtag)){
//                    Log.e("asdas", mCardIntent.getTag_name()+" "+tv.getText().toString().trim());
                    select_tag_name=tagtag;
                    break;
                }
            }
            tagAdapter.setSelectedPosition(pos);
            edit_name.setText(mCardIntent.getDay_title());
            btn_start_date.setText(GetTime.getNowTimeStr(mCardIntent.getStart_time(),"yyyy/MM/dd"));
            btn_start_time.setText(GetTime.getNowTimeStr(mCardIntent.getStart_time(),"HH:mm"));
            btn_end_date.setText(GetTime.getNowTimeStr(mCardIntent.getEnd_time(),"yyyy/MM/dd"));
            btn_end_time.setText(GetTime.getNowTimeStr(mCardIntent.getEnd_time(),"HH:mm"));
            edit_content.setContentText(mCardIntent.getDescribe());
            btn_all_day.setChecked(mCardIntent.isIs_all_day());
            //=======================全天为yes=====================
            if (btn_all_day.isChecked()){
                btn_start_time.setVisibility(View.GONE);
                btn_end_time.setVisibility(View.GONE);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusSetNewTagMidMsg eventBusCardMidMsg){
        if(eventBusCardMidMsg.isIs_success()){
            initCardNotOneSingleFlowTagLayout();
        }
    }
}