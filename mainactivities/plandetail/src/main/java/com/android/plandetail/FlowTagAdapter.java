package com.android.plandetail;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.librarybase.basemethod.Color;
import com.android.librarydb.SQLite.SQLiteTools;
import com.xuexiang.xui.widget.flowlayout.BaseTagAdapter;

public class FlowTagAdapter extends BaseTagAdapter<String, TextView> {

    Context mContext;
    public FlowTagAdapter(Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected TextView newViewHolder(View convertView) {
        return (TextView) convertView.findViewById(R.id.tv_tag);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.adapter_item_tag;
    }

    @Override
    protected void convert(TextView textView, String item, int position) {
        int color= SQLiteTools.getTagColorDB(mContext,item);
        textView.setBackground(mContext.getResources().getDrawable(Color.getTagDrawableNormalColor(color)));
        textView.setTextColor(mContext.getResources().getColorStateList(Color.getTagDrawableSelectedColor(color)));
        textView.setText(item);
    }
}