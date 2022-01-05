package com.android.librarybase.topview;

import android.view.View;
import android.widget.LinearLayout;

import com.android.librarybase.R;
import com.android.librarybase.Screen.ScreenDatas;
import com.android.librarybase.basepage.BaseFragment;

public class TopViewFragment extends BaseFragment {
    View mViewTop;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_top_view;
    }

    @Override
    protected void initView() {
        mViewTop=mRootView.findViewById(R.id.view_Top);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) mViewTop.getLayoutParams();
        params.weight=LinearLayout.LayoutParams.MATCH_PARENT;
        params.height= ScreenDatas.getStatusHeight(mRootView.getContext());
        mViewTop.setLayoutParams(params);
    }

    @Override
    protected void initData() {

    }
}
