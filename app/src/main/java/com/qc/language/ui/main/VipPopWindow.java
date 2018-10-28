package com.qc.language.ui.main;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qc.language.R;

/**
 * vip模块需要登录才可以
 * Created by beckett on 2018/9/24.
 */

public class VipPopWindow extends PopupWindow {

    private Context mContext;

    private View view;

    public Button btn_sure;

    public ImageView btn_cancel;

    public VipPopWindow(Activity mContext, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tip, null);


        btn_sure =  (Button) view.findViewById(R.id.dialog_tip_sure);
        btn_cancel =  (ImageView) view.findViewById(R.id.dialog_tip_close);

        btn_cancel.setOnClickListener(itemsOnClick) ;

        // 设置按钮监听
        btn_sure.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(false);


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);

        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

    }
}

