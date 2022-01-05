package com.android.minefragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.librarybase.basepage.BaseFragment;
import com.android.librarybase.constant.ARouterActivityPath;
import com.android.librarybase.constant.ARouterFragmentPath;
import com.android.librarybase.storage.SharedStorage;
import com.android.librarydb.eventBus.EventBusLoginOrLogoutMidMsg;
import com.android.librarydb.eventBus.EventBusMineChangeMsgMidMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

@Route(path= ARouterFragmentPath.Mine.FRAG_MINE)
public class MineFragment extends BaseFragment {

    private CircleImageView img_header_mine;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Subscribe
    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        img_header_mine=mRootView.findViewById(R.id.img_header_mine);
        if (SharedStorage.isLogin(mContext)){
            TextView text_header=mRootView.findViewById(R.id.text_header);
            text_header.setText(SharedStorage.getCurNiCheng(mContext));
            img_header_mine.setImageDrawable(getResources().getDrawable(SharedStorage.getCurTouXiangDrawable(mContext)));
        }
        else {
            TextView text_header=mRootView.findViewById(R.id.text_header);
            text_header.setText("点击登录");
            img_header_mine.setImageDrawable(getResources().getDrawable(R.drawable.touxiang01));
        }
        mRootView.findViewById(R.id.fan_kui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mRootView.getContext(),FanKuiActivity.class));
            }
        });
        mRootView.findViewById(R.id.set_labels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mRootView.getContext(),SetLabelsActivity.class));
            }
        });
        mRootView.findViewById(R.id.get_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mRootView.getContext(),GetBackActivity.class));
            }
        });
        mRootView.findViewById(R.id.to_personal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedStorage.isLogin(mContext))
                    startActivity(new Intent(mRootView.getContext(),PersonalActivity.class));
                else {
                    ARouter.getInstance().build(ARouterActivityPath.Login.PAGER_LOGIN).navigation();
                }
            }
        });

        mRootView.findViewById(R.id.set_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ARouterActivityPath.Setting.SETTINGACTIVITY).navigation();
            }
        });
        mRootView.findViewById(R.id.set_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/Help/HelpActivity").navigation();
            }
        });
    }

    @Override
    protected void initData() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBusReceive(EventBusLoginOrLogoutMidMsg eventBusLoginOrLogoutMidMsg){
//        Log.e("退出登录","退出登录");
        TextView text_header=mRootView.findViewById(R.id.text_header);
        if (eventBusLoginOrLogoutMidMsg.isIs_login()){
            text_header.setText(SharedStorage.getCurNiCheng(mContext));
            img_header_mine.setImageDrawable(getResources().getDrawable(SharedStorage.getCurTouXiangDrawable(mContext)));

        }
        else {
            text_header.setText("点击登录");
            img_header_mine.setImageDrawable(getResources().getDrawable(R.drawable.touxiang01));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventChangeBusReceive(EventBusMineChangeMsgMidMsg eventBusMineChangeMsgMidMsg){
        TextView text_header=mRootView.findViewById(R.id.text_header);
        if (eventBusMineChangeMsgMidMsg.isIs_success()){
            text_header.setText(SharedStorage.getCurNiCheng(mContext));
            img_header_mine.setImageDrawable(getResources().getDrawable(SharedStorage.getCurTouXiangDrawable(mContext)));
        }

    }
}
