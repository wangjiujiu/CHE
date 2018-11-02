package com.qc.language.ui.gre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qc.language.R;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.gre.adapter.AppGridListAdapter;
import com.qc.language.ui.gre.databean.AppData;
import com.qc.language.ui.gre.databean.AppGroupData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 用listview打造分组菜单
 * 菜单数据从json文件中取出
 * Created by beckett on 2018/9/19.
 */
public class GreFragment extends CommonFragment {

    private ListView listView;//外层listview

    private List<AppGroupData> datalist = new ArrayList<>();
    private AppGridListAdapter appGridAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.app_frag_main, container, false);

        listView = (ListView) root.findViewById(R.id.app_frag_rv);
        appGridAdapter = new AppGridListAdapter(getContext(),datalist);
        listView.setAdapter(appGridAdapter);

        datalist = getAppGroup(getCommonActivity());
        appGridAdapter.resetData(datalist);
        appGridAdapter.notifyDataSetChanged();


        appGridAdapter.setOnAppClickListener(new AppGridListAdapter.OnAppClickListener() {
            @Override
            public void onAppClick(AppData appData) {
                if(!StringUtils.isEmpty(appData.getBundlename())){
                Intent intent = new Intent();
                intent.putExtra("title",appData.getName());
                intent.putExtra("role",0);
                intent.putExtra("type",appData.getType());
                intent.setClassName(getContext(),appData.getBundlename());
                startActivity(intent);
                }
            }
        });
        return root;
    }


    //从json文件里获取菜单信息，方便
    public static List<AppGroupData> getAppGroup(Context context) {
        String result = readFileFromRaw(context, R.raw.main_app);
        if (result != null) {
            Gson gson = new Gson();
            return gson.fromJson(result, new TypeToken<List<AppGroupData>>() {
            }.getType());
        }
        return null;
    }

    /**
     * 读取Raw文件夹下文本类型文件
     *
     * @param context    上下文
     * @param resourceId 资源id
     * @return 返回的读取完成的数据 string格式
     */
    public static String readFileFromRaw(Context context, int resourceId) {
        if (null == context || resourceId < 0) {
            return null;
        }

        String result = null;
        try {
            InputStream input = context.getResources().openRawResource(resourceId);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.close();
            input.close();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
