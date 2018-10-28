package com.qc.language.ui.gre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.ui.gre.databean.AppData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckett on 2018/9/19.
 */
public class AppGridAdapter extends BaseAdapter {

    protected Context mContext;
    private List<AppData> appData=new ArrayList<>();

    public AppGridAdapter(Context context, List<AppData> mAppData) {
        mContext = context;
        this.appData  = mAppData;
    }

    @Override
    public int getCount() {
        return appData == null ? 0 : appData.size();
    }

    @Override
    public AppData getItem(int position) {
        return appData == null ? null : appData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HotCityViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homeapp_gridview, parent, false);
            holder = new HotCityViewHolder();
            holder.name =  (TextView) view.findViewById(R.id.tv_item_homeapp);
            holder.imageView = (ImageView)view.findViewById(R.id.iv_item_homeapp);
            view.setTag(holder);
        }else{
            holder = (HotCityViewHolder) view.getTag();
        }
        if(appData.get(position).getName()!=null){
        holder.name.setText(appData.get(position).getName());
        }
        if(appData.get(position).getImageurl()!=null){
            int mipmapId = mContext.getResources().getIdentifier(appData.get(position).getImageurl(), "mipmap", MyApplication.getInstance().getPackageName());
            if (mipmapId != 0x0) {
                holder.imageView.setImageResource(mipmapId);  //设置图片
            }
        }
        return view;
    }

    public static class HotCityViewHolder{
        TextView name;
        ImageView imageView;
    }

    public void setData(List<AppData> appDataList){
        this.appData.clear();
        this.appData.addAll(appDataList);
    }
}
