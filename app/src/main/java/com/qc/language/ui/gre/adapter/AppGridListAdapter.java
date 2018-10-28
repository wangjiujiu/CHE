package com.qc.language.ui.gre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.ui.gre.databean.AppData;
import com.qc.language.ui.gre.databean.AppGroupData;
import com.qc.language.common.view.WrapHeightGridView;

import java.util.ArrayList;
import java.util.List;

public class AppGridListAdapter extends BaseAdapter {
   
    private Context mContext;
    private LayoutInflater inflater;
    private List<AppGroupData> items;
    private OnAppClickListener onAppClickListener;

    private int currentGroup;

    public AppGridListAdapter(Context mContext, List<AppGroupData> data) {
        this.mContext =  mContext;
        this.items = data;
        this.inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return items == null ? 0: items.size();
    }

    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final AppViewHolder holder;
             if (view == null) {
                 view = inflater.inflate(R.layout.item_grid_page, parent, false);
                 holder = new AppViewHolder();
                 holder.wrapHeightGridView = (WrapHeightGridView) view.findViewById(R.id.item_grid_page_gv);
                 holder.title = (TextView) view.findViewById(R.id.item_grid_page_title);
                 view.setTag(holder);
             }else{
                 holder = (AppViewHolder) view.getTag();
             }
             if(items.get(position).getName()!=null){
                 holder.title.setText(items.get(position).getName());
             }
            final AppGridAdapter appGridAdapter = new AppGridAdapter(mContext, new ArrayList<AppData>());
             holder.wrapHeightGridView.setAdapter(appGridAdapter);

                if (items.get(position).getData()!= null && items.get(position).getData().size() > 0) {
                    appGridAdapter.setData(items.get(position).getData());
                    appGridAdapter.notifyDataSetChanged();

                }

            holder.wrapHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    if (onAppClickListener != null) {
                        onAppClickListener.onAppClick(items.get(position).getData().get(i));
                    }
                }
            });
        return view;
    }

    public static class AppViewHolder{
        TextView title;
        WrapHeightGridView wrapHeightGridView;
        AppGridAdapter appGridAdapter;
    }

    public void setOnAppClickListener(OnAppClickListener listener){
        this.onAppClickListener = listener;
    }

    public interface OnAppClickListener{
        void onAppClick(AppData appData);
    }

    public void resetData(List<AppGroupData> appDataList){
        this.items.clear();
        this.items.addAll(appDataList);
    }
}
