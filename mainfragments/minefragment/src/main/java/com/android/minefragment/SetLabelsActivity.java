package com.android.minefragment;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basepage.BaseActivity;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.SQLite.SQLiteTools;
import com.android.librarydb.bean__.TagBean;
import com.android.librarydb.eventBus.EventBusSetNewTagMidMsg;
import com.android.minefragment.Adapter.TagsAdapter;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

@Route(path= ARouterActivityPath.SetLabels.SET_LABELS)
public class SetLabelsActivity extends BaseActivity implements SmoothCheckBox.OnCheckedChangeListener{

    private RecyclerView mRecyclerView;
    private EditText edit_tag_name;
    private int select_color;
    private TagsAdapter mAdapter;
    TitleBar add_plan_top_bar;
    private CardView btn_add_tag;
    private SmoothCheckBox green,blue,purple,pink,yellow;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_labels;
    }


    @Override
    protected void initView() {
        TopBar.setTopBarColor(SetLabelsActivity.this);
        ARouter.getInstance().inject(this);
        mRecyclerView = findViewById(R.id.tags_recycleView);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        add_plan_top_bar=findViewById(R.id.set_labels_top_bar);
        btn_add_tag = findViewById(R.id.btn_add_tag);
        add_plan_top_bar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
        /**
         * 添加新标签按钮
         */
        btn_add_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 显示自定义对话框
                 */
                MaterialDialog dialog_layout = new MaterialDialog.Builder(SetLabelsActivity.this)
                        .customView(R.layout.add_tag_dialog, true)
                        .title("新建标签")
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
                                if(edit_tag_name.getText().toString().trim().isEmpty()){
                                    XToast.warning(SetLabelsActivity.this,"标签名称未填写").show();
                                    return;
                                }
                                else{
                                    boolean is_success=SQLiteTools.insertTagDB(SetLabelsActivity.this,
                                            edit_tag_name.getText().toString().trim(),
                                            select_color);
                                    if(is_success) {
                                        XToast.success(SetLabelsActivity.this, "添加成功").show();
                                        mAdapter.add(0,new TagBean(edit_tag_name.getText().toString().trim(), select_color));
                                        EventBus.getDefault().post(new EventBusSetNewTagMidMsg(true));
                                    }
                                    else XToast.warning(SetLabelsActivity.this,"已有相同名称的标签").show();
                                    dialog_layout.dismiss();

                                }
                            }
                        });
                    }
                });
                edit_tag_name=dialog_layout.findViewById(R.id.edit_tag_name);
                green = dialog_layout.findViewById(R.id.green_tag);
                blue = dialog_layout.findViewById(R.id.blue_tag);
                yellow = dialog_layout.findViewById(R.id.yellow_tag);
                pink = dialog_layout.findViewById(R.id.pink_tag);
                purple = dialog_layout.findViewById(R.id.purple_tag);
                green.setCheckedSilent(true);
                select_color=R.color.main_green;
                singleCheck();
            }
        });
        /**
         * 删除
         */
        setDeleteOnClick();
        /**
         * 初始化adapter
         */
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SetLabelsActivity.this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new TagsAdapter(SetLabelsActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }

    @Override
    protected void initData() {

    }

    /**
     * 预加载
     */
    private void onDataLoaded() {
        List<TagBean> data = SQLiteTools.getAllTags(SetLabelsActivity.this);
        mAdapter.setData(data);
    }

    /**
     * 删除一条
     */
    private void setDeleteOnClick(){
        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                /**
                 * 显示自定义对话框
                 */
                MaterialDialog dialog_layout = new MaterialDialog.Builder(SetLabelsActivity.this)
                        .content("是否删除？删除后不可撤销")
                        .positiveText("确认")
                        .negativeText("取消")
                        .positiveColor(getResources().getColor(com.android.commonrecyclelist.R.color.xui_config_color_red))
                        .negativeColor(getResources().getColor(com.android.commonrecyclelist.R.color.main_green))
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
                                TextView set_tag_name=viewHolder.itemView.findViewById(R.id.set_tag_name);
                                if (set_tag_name.getText().toString().trim().equals("其他")){
                                    XToast.error(SetLabelsActivity.this,"默认标签不可删除").show();
                                    dialog.dismiss();
                                    return;
                                }
                                else{
                                    boolean is_success = SQLiteTools.deleteTagDB(SetLabelsActivity.this,set_tag_name.getText().toString().trim());
                                    if (!is_success)XToast.error(SetLabelsActivity.this,"有包含该标签的计划，删除失败").show();
                                    else {
                                        mAdapter.remove(viewHolder.getAdapterPosition());
                                        EventBus.getDefault().post(new EventBusSetNewTagMidMsg(true));
                                    }
                                }
                                dialog.dismiss();
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                        dialog_layout.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                if(action == mAdapter.mDeleteAction){
                    /**
                     * 显示自定义对话框
                     */
                    MaterialDialog dialog_layout = new MaterialDialog.Builder(SetLabelsActivity.this)
                            .content("是否删除？删除后不可撤销")
                            .positiveText("确认")
                            .negativeText("取消")
                            .positiveColor(getResources().getColor(com.android.commonrecyclelist.R.color.xui_config_color_red))
                            .negativeColor(getResources().getColor(com.android.commonrecyclelist.R.color.main_green))
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
                                    TextView set_tag_name=selected.itemView.findViewById(R.id.set_tag_name);
                                    if (set_tag_name.getText().toString().trim().equals("其他")){
                                        XToast.error(SetLabelsActivity.this,"默认标签不可删除").show();
                                        dialog.dismiss();
                                        return;
                                    }
                                    else{
                                        boolean is_success = SQLiteTools.deleteTagDB(SetLabelsActivity.this,set_tag_name.getText().toString().trim());
                                        if (!is_success)XToast.error(SetLabelsActivity.this,"有包含该标签的计划，删除失败").show();
                                        else {
                                            mAdapter.remove(selected.getAdapterPosition());
                                            EventBus.getDefault().post(new EventBusSetNewTagMidMsg(true));
                                        }
                                    }
                                    dialog.dismiss();
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            dialog_layout.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                }else{
                    swipeAction.clear();
                }
            }
        });
        swipeAction.attachToRecyclerView(mRecyclerView);
    }
    private void singleCheck(){
        green.setOnCheckedChangeListener(this);
        purple.setOnCheckedChangeListener(this);
        yellow.setOnCheckedChangeListener(this);
        blue.setOnCheckedChangeListener(this);
        pink.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
        if (green.getId()!=checkBox.getId())green.setCheckedSilent(false);
        if (purple.getId()!=checkBox.getId())purple.setCheckedSilent(false);
        if (yellow.getId()!=checkBox.getId())yellow.setCheckedSilent(false);
        if (pink.getId()!=checkBox.getId())pink.setCheckedSilent(false);
        if (blue.getId()!=checkBox.getId())blue.setCheckedSilent(false);

        if (green.getId()==checkBox.getId()) {
            green.setCheckedSilent(true);
            select_color=R.color.main_green;
        }
        if (purple.getId()==checkBox.getId()) {
            purple.setCheckedSilent(true);
            select_color=R.color.main_purple;
        }
        if (yellow.getId()==checkBox.getId()) {
            yellow.setCheckedSilent(true);
            select_color=R.color.main_yellow;
        }
        if (pink.getId()==checkBox.getId()) {
            pink.setCheckedSilent(true);
            select_color=R.color.main_pink;
        }
        if (blue.getId()==checkBox.getId()) {
            blue.setCheckedSilent(true);
            select_color=R.color.main_blue;
        }
    }
}