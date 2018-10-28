package com.qc.language.ui.question.read.rp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.common.view.itemtouchhelper.ItemTouchActionCallback;
import com.qc.language.ui.question.adapter.data.AnswerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 段落交换的适配器
 */

public class SwapListAdapter extends RecyclerView.Adapter<SwapListAdapter.MessageHolder> implements ItemTouchActionCallback {
    private Context mContext;
    private List<String> mList;

    public SwapListAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageHolder messageHolder = new MessageHolder(LayoutInflater.from(mContext).inflate(R.layout.item_swap, parent, false));
        return messageHolder;
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, final int position) {
        holder.contentTv.setText(mList.get(position));
      }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    //设置滑动，返回null就没有滑动了
    @Override
    public View getContentView(RecyclerView.ViewHolder holder) {
        return null;
    }

    @Override
    public int getMenuWidth(RecyclerView.ViewHolder holder) {
        MessageHolder messageHolder = (MessageHolder) holder;
        return messageHolder.contentTv.getWidth();
    }

    @Override
    public void onMove(int fromPos, int toPos) {
        Collections.swap(mList, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public void onMoved(int fromPos, int toPos) {
       //move action finished
    }

    public static class MessageHolder extends RecyclerView.ViewHolder {
        public TextView contentTv;
        public MessageHolder(View itemView) {
            super(itemView);
            contentTv = (TextView) itemView.findViewById(R.id.item_swap_content);
        }
    }

    public void resetList(List<String> list) {
        this.mList.clear();
        this.mList.addAll(list);
    }


    public List<String> getNewList(){
        if(mList!=null){
            return mList;
        }
        return null;
    }
}
