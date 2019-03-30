package com.qc.language.ui.home;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
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
 * Created by beckett on 2018/9/24.
 */

public class ClassPopWindow extends PopupWindow {

    private Context mContext;


    private TextView oneTv;
    private TextView secondTv;
    private TextView thirdTv;
    private View view;

    public Button btn_cancel;

    public ClassPopWindow(Activity mContext, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.dialog_class, null);
        oneTv = (TextView) view.findViewById(R.id.home_class_one);
        secondTv = (TextView) view.findViewById(R.id.home_class_two);
        thirdTv = (TextView) view.findViewById(R.id.home_class_three);
        btn_cancel =  (Button) view.findViewById(R.id.dialog_class_close);

        String one = "<font color=\"#2c2b2b\">1.特色小班：</font>没有嘈杂的人群，每班最多4人，12小时的干货讲解+12小时的习题巩固。";
        String second="<font color=\"#2c2b2b\">2.精品一对一：</font>我们专业的老师会全方位解读每个学员的个人情况，挑出其无法7炸，8炸的原因，并且量身定制一套学习方案。";
        String third = "<font color=\"#2c2b2b\">3.一对一口语单项强化</font>：由native English speaker为学员强化提高口语的发音技巧，纠正发音错误，帮助学员在熟练掌握口语答题技巧的同时全方位综合提高口语水平";

        oneTv.setText(Html.fromHtml(one));
        secondTv.setText(Html.fromHtml(second));
        thirdTv.setText(Html.fromHtml(third));

        btn_cancel.setOnClickListener(itemsOnClick) ;

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

