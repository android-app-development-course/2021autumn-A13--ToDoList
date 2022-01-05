package com.android.todolist.Main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.constant.ARouterFragmentPath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarybase.topbarcolor.TopBar;
import com.android.librarydb.eventBus.EventBusCardMidMsg;
import com.android.todolist.AppActivity;
import com.android.todolist.R;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimplePopup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


//@Route(path= ARouterActivityPath.Main.PAGER_MAIN)
public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{

    private TitleBar main_top_bar;
    private QMUILinearLayout bottom_bar;
    //右上角的设置
//    private Button btn_set;
    //声明ViewPager
    private MainViewPager mainViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    //四个Tab对应的布局
    private LinearLayout tab_home;
    private LinearLayout tab_plan;
    private LinearLayout tab_mine;
    private LinearLayout mTab4;

    //四个Tab对应的ImageView
    private ImageView img_home;
    private ImageView img_plan;
    private ImageView img_mine;
    private ImageView mImg4;

    //四个Tab对应的TextView
    private TextView tv_home;
    private TextView tv_plan;
    private TextView tv_mine;
    private TextView mTv04;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        TopBar.setTopBarColor(MainPageActivity.this);

        initViews();//初始化控件
        initEvents();//初始化事件
        initDatas();//初始化数据

        ARouter.getInstance().inject(this);
    }



    private void initViews() {
        mainViewPager =findViewById(R.id.view_pager);
        mainViewPager.setOffscreenPageLimit(3);
        tab_home = (LinearLayout) findViewById(R.id.l_layout_home);
        tab_plan = (LinearLayout) findViewById(R.id.l_layout_plan);
        tab_mine = (LinearLayout) findViewById(R.id.l_layout_mine);
//        mTab4 = (LinearLayout) findViewById(R.id.l_layout04);
//
        img_home = (ImageView) findViewById(R.id.img_home);
        img_plan = (ImageView) findViewById(R.id.img_plan);
        img_mine = (ImageView) findViewById(R.id.img_mine);
//        mImg4 = (ImageView) findViewById(R.id.img04);

        tv_home= findViewById(R.id.tv_home);
        tv_plan= findViewById(R.id.tv_plan);
        tv_mine= findViewById(R.id.tv_mine);
//        mTv04= findViewById(R.id.tv04);

        main_top_bar=findViewById(R.id.main_top_bar);


        bottom_bar=findViewById(R.id.bottom_bar);
        bottom_bar.onlyShowTopDivider(0,0,1,getResources().getColor(R.color.main_grad));

        tv_home.setTextColor(getResources().getColor(R.color.main_green));

        img_home.setImageDrawable(getResources().getDrawable(R.drawable.home_green));
        AdapterItem[] menuItems = new AdapterItem[]{
                new AdapterItem("添加计划", R.drawable.add_plan_item_green)
        };
        XUISimplePopup mMenuPopup;
        mMenuPopup = new XUISimplePopup(this, menuItems)
                .create((adapter, item, position) -> {
                }).setOnPopupItemClickListener(new XUISimplePopup.OnPopupItemClickListener() {
                    @Override
                    public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                        if(position==0){
//                                    startActivityForResult(new Intent(MainPageActivity.this, AddPlanActivity.class),1);
                            ARouter.getInstance().build(ARouterActivityPath.PlanDetail.PLAN_DETAIL)
                                    .withInt("card_id",-1)
                                    .navigation(MainPageActivity.this,1);
                        }
                    }
                });
        main_top_bar.setTitle("首页");

        for(int j=0;j<main_top_bar.getActionCount();++j){
            main_top_bar.removeActionAt(j);
        }
        main_top_bar.disableLeftView().addAction(new TitleBar.ImageAction(R.drawable.plan_plus_black) {
            @Override
            public void performAction(View view) {
                mMenuPopup.showDown(view);
            }
        });

    }



    private void initEvents() {
        //设置四个Tab的点击事件
        tab_home.setOnClickListener(this);
        tab_plan.setOnClickListener(this);
        tab_mine.setOnClickListener(this);
        tab_mine.setOnClickListener(this);
    }


    private void initDatas() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中

        mFragments.add((Fragment) ARouter.getInstance().build(ARouterFragmentPath.Home.FRAG_HOME).navigation());
        mFragments.add((Fragment) ARouter.getInstance().build(ARouterFragmentPath.Plan.FRAG_PLAN).navigation());
        mFragments.add((Fragment) ARouter.getInstance().build(ARouterFragmentPath.Mine.FRAG_MINE).navigation());
//        Log.e("xianshi:", String.valueOf((Fragment) ARouter.getInstance().build(ARouterFragmentPath.Home.FRAG_HOME).navigation()));
        //初始化适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//从集合中获取对应位置的Fragment
                return mFragments.get(position);
            }

            @Override
            public int getCount() {//获取集合中Fragment的总数
                return mFragments.size();
            }
        };
        //不要忘记设置ViewPager的适配器
        mainViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                mainViewPager.setCurrentItem(position);
                resetImgs();
                selectTab(position);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }




    @SuppressLint({"ResourceAsColor"})
    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.home_green));
                tv_home.setTextColor(getResources().getColor(R.color.main_green));
//                btn_set.setVisibility(View.GONE);
                AdapterItem[] menuItems = new AdapterItem[]{
                        new AdapterItem("添加计划", R.drawable.add_plan_item_green)
                };
                XUISimplePopup mMenuPopup;
                mMenuPopup = new XUISimplePopup(this, menuItems)
                        .create((adapter, item, position) -> {
                        }).setOnPopupItemClickListener(new XUISimplePopup.OnPopupItemClickListener() {
                            @Override
                            public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                                if(position==0){
//                                    startActivityForResult(new Intent(MainPageActivity.this, AddPlanActivity.class),1);
                                    ARouter.getInstance().build(ARouterActivityPath.PlanDetail.PLAN_DETAIL)
                                            .withInt("card_id",-1)
                                            .navigation(MainPageActivity.this,1);
                                }
                            }
                        });
                main_top_bar.setTitle("首页");

                for(int j=0;j<main_top_bar.getActionCount();++j){
                    main_top_bar.removeActionAt(j);
                }
                main_top_bar.disableLeftView().addAction(new TitleBar.ImageAction(R.drawable.plan_plus_black) {
                    @Override
                    public void performAction(View view) {
                        mMenuPopup.showDown(view);
                    }
                });
                EventBus.getDefault().post(new EventBusCardMidMsg(true));
                break;
            case 1:
                img_plan.setImageDrawable(getResources().getDrawable(R.drawable.plan_green));
                tv_plan.setTextColor(getResources().getColor(R.color.main_green));
//                btn_set.setVisibility(View.GONE);
                AdapterItem[] menuItems01 = new AdapterItem[]{
                        new AdapterItem("添加计划", R.drawable.add_plan_item_green)
                };
                XUISimplePopup mMenuPopup01;
                mMenuPopup01 = new XUISimplePopup(this, menuItems01)
                        .create((adapter, item, position) -> {
                        }).setOnPopupItemClickListener(new XUISimplePopup.OnPopupItemClickListener() {
                            @Override
                            public void onItemClick(XUISimpleAdapter adapter, AdapterItem item, int position) {
                                if(position==0){
//                                    startActivityForResult(new Intent(MainPageActivity.this, AddPlanActivity.class),1);
                                    ARouter.getInstance().build(ARouterActivityPath.PlanDetail.PLAN_DETAIL)
                                            .withInt("card_id",-1)
                                            .navigation(MainPageActivity.this,1);
                                }
                            }
                        });
                main_top_bar.setTitle("计划");

                for(int j=0;j<main_top_bar.getActionCount();++j){
                    main_top_bar.removeActionAt(j);
                }
                main_top_bar.disableLeftView().addAction(new TitleBar.ImageAction(R.drawable.plan_plus_black) {
                    @Override
                    public void performAction(View view) {
                        mMenuPopup01.showDown(view);
                    }
                });
                EventBus.getDefault().post(new EventBusCardMidMsg(true));
                break;
            case 2:
                img_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine_green));
                tv_mine.setTextColor(getResources().getColor(R.color.main_green));
                main_top_bar.setTitle("我的");

                for(int j=0;j<main_top_bar.getActionCount();++j){
                    main_top_bar.removeActionAt(j);
                }
                main_top_bar.disableLeftView().addAction(new TitleBar.ImageAction(R.drawable.setting_black) {
                    @Override
                    public void performAction(View view) {
                        startActivityForResult(new Intent(MainPageActivity.this,SettingActivity.class),2);
                    }
                });
//                btn_set.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new EventBusCardMidMsg(true));
                break;
        }
        //设置当前点击的Tab所对应的页面
        mainViewPager.setCurrentItem(i,false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            //下面的1为startActivityForResult(intent, 1);中的1
            case 2:
                //这里的1为setResult(1, intent);中的1
                if (resultCode==2){
                    SharedStorage.storeLogoutMsg(MainPageActivity.this);
//                    startActivity(new Intent(MainPageActivity.this,AppActivity.class));
//                    finish();
                }
                break;
            default:
                break;

        }
    }

    //将四个ImageButton设置为灰色
    @SuppressLint("ResourceType")
    private void resetImgs() {
        img_home.setImageDrawable(getResources().getDrawable(R.drawable.home_black));
        img_plan.setImageDrawable(getResources().getDrawable(R.drawable.plan_black));
        img_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine_black));

        tv_home.setTextColor(getResources().getColor(R.color.main_black));
        tv_plan.setTextColor(getResources().getColor(R.color.main_black));
        tv_mine.setTextColor(getResources().getColor(R.color.main_black));
    }

    @Override
    public void onClick(View v) {
        //先将四个ImageButton置为白色
        resetImgs();
        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (v.getId()) {
            case R.id.l_layout_home:
                selectTab(0);
                break;
            case R.id.l_layout_plan:
                selectTab(1);
                break;
            case R.id.l_layout_mine:
                selectTab(2);
                break;
        }
    }


    //用户按返回按钮不关闭页面，而是返回到系统桌面。相当于按home键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}