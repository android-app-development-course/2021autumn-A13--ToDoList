package com.android.minefragment.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.librarydb.bean__.TagBean;
import com.android.minefragment.R;
import com.qmuiteam.qmui.recyclerView.QMUISwipeAction;
import com.qmuiteam.qmui.recyclerView.QMUISwipeViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<QMUISwipeViewHolder>{

    private List<TagBean> mData = new ArrayList<>();
    final private Context mContext;
    final public QMUISwipeAction mDeleteAction;

    public TagsAdapter(Context context){
        mContext = context;
        QMUISwipeAction.ActionBuilder builder = new QMUISwipeAction.ActionBuilder()
                .textColor(Color.WHITE)
                .paddingStartEnd(QMUIDisplayHelper.dp2px(mContext, 16));

        mDeleteAction = builder.icon(mContext.getDrawable(R.drawable.delete)).build();
    }

    public void setData(@Nullable List<TagBean> list) {
        mData.clear();
        if(list != null){
            mData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void remove(int pos){
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void add(int pos, TagBean item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void prepend(@NonNull List<TagBean> items){
        mData.addAll(0, items);
        notifyDataSetChanged();
    }

    public void append(@NonNull List<TagBean> items){
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QMUISwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        final QMUISwipeViewHolder vh = new QMUISwipeViewHolder(view);
        vh.addSwipeAction(mDeleteAction);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,
//                        "click position=" + vh.getAdapterPosition(),
//                        Toast.LENGTH_SHORT).show();
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull QMUISwipeViewHolder holder, int position) {
        TextView set_tag_name = holder.itemView.findViewById(R.id.set_tag_name);
        set_tag_name.setText(mData.get(position).getTag_name());

        View tag_color = holder.itemView.findViewById(R.id.tag_color);
        tag_color.setBackgroundColor(mContext.getResources().getColor(mData.get(position).getTag_color()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
