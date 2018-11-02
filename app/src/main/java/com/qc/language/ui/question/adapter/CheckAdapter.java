package com.qc.language.ui.question.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.qc.language.R;
import com.qc.language.ui.question.data.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 *选择adapter
 */
public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder>{

    private List<OptionData> items;
    private List<String> haschooseitems;
    private String answer;
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
        super();
        this.items = new ArrayList<>();
        this.haschooseitems = new ArrayList<>();
        this.answer = "";
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pick, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final  ViewHolder viewHolder, final int position) {
        if (position < items.size() && items.get(position) != null) {
            if(items.get(position).getContent()!=null&& !StringUtils.isEmpty(items.get(position).getContent())){
                viewHolder.answer.setText(items.get(position).getContent());
            }

            if(items.get(position).getSeq()!=null&& !StringUtils.isEmpty(items.get(position).getSeq())){
                viewHolder.num.setText(items.get(position).getSeq());
            }

            if (haschooseitems.contains(items.get(position).getSeq())&&answer.contains(items.get(position).getSeq())) {
                //选中是对的
                viewHolder.pickIv.setImageResource(R.mipmap.ic_right);
                viewHolder.rootview.setBackgroundColor(context.getResources().getColor(R.color.pick_right));
            } else if(haschooseitems.contains(items.get(position).getSeq())&&!answer.contains(items.get(position).getSeq())){
                //选中，但不是对的，选错了
                viewHolder.pickIv.setImageResource(R.mipmap.ic_error);
                viewHolder.rootview.setBackgroundColor(context.getResources().getColor(R.color.pick_error));
            }else if(answer.contains(items.get(position).getSeq())){
                viewHolder.pickIv.setImageResource(R.mipmap.ic_unpick);
                viewHolder.rootview.setBackgroundColor(context.getResources().getColor(R.color.picked_unpicked));
            } else{
                viewHolder.rootview.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }
        //  判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView,position);
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
        public ImageView pickIv;
        public TextView answer;
        public TextView num;
        public LinearLayout rootview;
        public ViewHolder(View view) {
            super(view);
            pickIv = (ImageView) view.findViewById(R.id.check_iv);
            num = (TextView) view.findViewById(R.id.item_pick_num);
            answer = (TextView) view.findViewById(R.id.item_pick_content);
            rootview = (LinearLayout) view.findViewById(R.id.item_pick_rootview);
        }
    }

    public void resetList(List<OptionData> list) {
        items.clear();
        items.addAll(list);
    }

    public void resetHasChooseList(List<OptionData> list) {
        haschooseitems.clear();
        if (list != null && list.size() > 0) {
            for (OptionData ob : list) {
                haschooseitems.add(ob.getSeq());
            }
        }
    }

    public void resetRightAnswer(String  list) {
      answer = list;
    }
}



