package com.qc.language.ui.question.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.common.view.recyclerview.OnRecyclerViewItemClickListener;
import com.qc.language.ui.question.data.Question;

import java.util.List;

/**
 * 收藏列表
 */
public class ListenerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private OnRecyclerViewItemClickListener listener;
    private List<Question> items;
    private LayoutInflater mLayoutInflater;
    private Context context;

    private OnEmptyClickListener emptylistener;
    /** * 空数据时，显示空布局类型 */
    private final int EMPTY_VIEW = 1;
    /** * 控制空布局的显隐 */
    private int mEmptyType = 0;

    public ListenerListAdapter(Context context, List<Question> items) {
        this.items = items;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(EMPTY_VIEW!=viewType){
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_listener, parent, false));

        }
        return new EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_no_data, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder  holder, final int position) {
        int itemViewType = getItemViewType(position);
        if(EMPTY_VIEW!=itemViewType){
            final Question item = items.get(position);

            if(item.getTitle()!=null){
                ((ItemViewHolder) holder).titleTv.setText(item.getTitle());
            }

            if(item.getSeq()!=null){
                ((ItemViewHolder) holder).numTv.setText("#"+item.getSeq());
            }


            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemDetailListener != null){
                        mOnItemDetailListener.onDetailClick(position, items.get(position));
                    }
                }
            });
            ((ItemViewHolder) holder).itemView.setTag(item);
        }else{
            ((EmptyViewHolder) holder).mEmptyTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (emptylistener != null)
                    {
                        emptylistener.onEmptyClick();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() + mEmptyType : mEmptyType;
    }

    @Override
    public int getItemViewType(int position) {
        if(mEmptyType==EMPTY_VIEW){
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    public interface OnEmptyClickListener {
        void onEmptyClick();
    }

    public void setOnEmptyClickListener(OnEmptyClickListener listener) {
        this.emptylistener = listener;
    }

        @Override
        public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, v.getTag());
        }
    }

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public  class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTv;
        public TextView numTv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            numTv = (TextView) itemView.findViewById(R.id.item_rs_num_status);
            titleTv = (TextView)itemView.findViewById(R.id.item_rs_title);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {

        private TextView mEmptyTextView;
        public EmptyViewHolder(View itemView)
        {
            super(itemView);

            mEmptyTextView = (TextView) itemView.findViewById(R.id.empty_iv);
        }
    }


    public void resetList(List<Question> list) {
        mEmptyType = 0;
        items.clear();
        items.addAll(list);
    }

    public void addList(List<Question> list) {
        mEmptyType = 0;
        items.addAll(list);
    }

    public void setEmpty() {
        if (!items.isEmpty())
        { //如果在设置空布局之前，已经显示了子条目类型的数据，那么需要清空还原
            int size = items.size();
            items.clear();
            notifyItemRangeRemoved(0, size);
            /*notifyDataSetChanged();*/
        }
        if (mEmptyType != EMPTY_VIEW)
        { //当前布局不是空布局，则刷新显示空布局
             mEmptyType = EMPTY_VIEW;
             notifyItemInserted(0);
        }
    }

    public interface onItemDetailListener {
        void onDetailClick(int i, Question data);
    }

    private onItemDetailListener mOnItemDetailListener;

    public void setOnItemDetailClickListener(onItemDetailListener onItemDetailListener) {
        this.mOnItemDetailListener = onItemDetailListener;
    }

}
