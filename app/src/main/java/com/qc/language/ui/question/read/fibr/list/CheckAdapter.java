package com.qc.language.ui.question.read.fibr.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qc.language.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beckett_W
 */
public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {

    private List<String> items;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }



    public CheckAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fill_word, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final  ViewHolder viewHolder, int position) {

        if (position < items.size() && items.get(position) != null) {

            viewHolder.textView.setText(items.get(position).toString());
            viewHolder.textView.setBackgroundResource(R.drawable.bg_fill_shape);
        }
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    if(position == (items.size()-1)) {
                        return false;
                    }else{
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView, position);
                    }
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return items.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  textView;
        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById( R.id.item_fill_word_tv);
        }
    }

    /**
     * 获取items
     *
     * @param items
     */
    public void setItems(List<String> items) {
        this.items.clear();
        this.items = items;
    }

}


