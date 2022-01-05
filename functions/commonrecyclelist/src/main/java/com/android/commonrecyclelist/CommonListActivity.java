package com.android.commonrecyclelist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.librarybase.basepage.BaseRecyclerAdapter;
import com.android.librarybase.basepage.RecyclerViewHolder;
import com.android.librarybase.basepage.BaseActivity;
import com.qmuiteam.qmui.recyclerView.QMUIRVItemSwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonListActivity extends BaseActivity {
    //    @BindView(R.id.pull_layout)
//    QMUIPullLayout mPullLayout;
    //    @BindView(R.id.common_vertical_list)
    RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_vertical_list;
    }

    @Override
    protected void initView() {
//        ButterKnife.bind(mRootView);    //必须要这一句，不然会出现奔溃
//        mPullLayout=findViewById(R.id.pull_layout);
        mRecyclerView=findViewById(R.id.common_vertical_list);

    }

    @Override
    protected void initData() {
//        mPullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
//            @Override
//            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
//                mPullLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
//                            onRefreshData();
//                        } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
//                            onLoadMore();
//                        }
//                        mPullLayout.finishActionRun(pullAction);
//                    }
//                }, 3000);
//            }
//        });
        QMUIRVItemSwipeAction swipeAction = new QMUIRVItemSwipeAction(true, new QMUIRVItemSwipeAction.Callback() {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.remove(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirection(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return QMUIRVItemSwipeAction.SWIPE_LEFT;
            }

            @Override
            public void onClickAction(QMUIRVItemSwipeAction swipeAction, RecyclerView.ViewHolder selected, QMUISwipeAction action) {
                super.onClickAction(swipeAction, selected, action);
                mAdapter.remove(selected.getAdapterPosition());
                Toast.makeText(CommonListActivity.this,
                        "你点击了第 " + selected.getAdapterPosition() + " 个 item 的" + action.getText(),
                        Toast.LENGTH_SHORT).show();
                swipeAction.clear();
            }
        });
        swipeAction.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CommonListActivity.this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        mAdapter = new BaseRecyclerAdapter<String>(CommonListActivity.this, null) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.day_list;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, String item) {
                holder.setText(R.id.day_title, item);
            }
        };
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Toast.makeText(CommonListActivity.this, "click position=" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        onDataLoaded();
    }
    private void onDataLoaded() {
        List<String> data = new ArrayList<>(Arrays.asList("Helps", "Maintain", "Liver", "Health", "Function", "Supports", "Healthy", "Fat",
                "Metabolism", "Nuturally", "Bracket", "Refrigerator", "Bathtub", "Wardrobe", "Comb", "Apron", "Carpet", "Bolster", "Pillow", "Cushion"));
        Collections.shuffle(data);
        mAdapter.setData(data);
    }
    private void onRefreshData() {
        List<String> data = new ArrayList<>();
        long id = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            data.add("onRefreshData-" + id + "-" + i);
        }
        mAdapter.prepend(data);
        mRecyclerView.scrollToPosition(0);
    }

    private void onLoadMore() {
        List<String> data = new ArrayList<>();
        long id = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            data.add("onLoadMore-" + id + "-" + i);
        }
        mAdapter.append(data);
    }

}


