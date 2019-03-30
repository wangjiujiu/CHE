package com.qc.language.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.service.db.question.CurrentQuesstion;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.listener.fibl.HfiblDetailFragment;
import com.qc.language.ui.question.listener.hcs.HhcsDetailFragment;
import com.qc.language.ui.question.listener.hiw.HhiwDetailFragment;
import com.qc.language.ui.question.listener.mcm.HmcmDetailFragment;
import com.qc.language.ui.question.listener.mcs.HmcsDetailFragment;
import com.qc.language.ui.question.listener.smw.HsmwDetailFragment;
import com.qc.language.ui.question.listener.sst.HsstDetailFragment;
import com.qc.language.ui.question.listener.wfd.HwfdDetailFragment;
import com.qc.language.ui.question.read.fibr.RfibrDetailFragment;
import com.qc.language.ui.question.read.fibrw.RfibrwDetailFragment;
import com.qc.language.ui.question.read.mcm.RmcmDetailFragment;
import com.qc.language.ui.question.read.mcs.RmcsDetailFragment;
import com.qc.language.ui.question.read.rp.RrpDetailFragment;
import com.qc.language.ui.question.speak.asq.SAsqDetailFragment;
import com.qc.language.ui.question.speak.di.SdiDetailFragment;
import com.qc.language.ui.question.speak.ra.SraDetailFragment;
import com.qc.language.ui.question.speak.rl.SrlDetailFragment;
import com.qc.language.ui.question.speak.rs.SrsDetailFragment;
import com.qc.language.ui.question.write.swt.WswtDetailFragment;
import com.qc.language.ui.question.write.we.WwfdDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckett on 2018/10/29.
 */
public class ClassSettingActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private TextView oneTv;
    private TextView secondTv;
    private TextView thirdTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_clasee_setting);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        toolbarTitleTextView.setText("课程设置");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        oneTv = (TextView) findViewById(R.id.home_class_one);
        secondTv = (TextView) findViewById(R.id.home_class_two);
        thirdTv = (TextView) findViewById(R.id.home_class_three);

        String one = "<font color=\"#2c2b2b\">1.特色小班：</font>没有嘈杂的人群，每班最多4人，12小时的干货讲解+12小时的习题巩固。";
        String second="<font color=\"#2c2b2b\">2.精品一对一：</font>我们专业的老师会全方位解读每个学员的个人情况，挑出其无法7炸，8炸的原因，并且量身定制一套学习方案。";
        String third = "<font color=\"#2c2b2b\">3.一对一口语单项强化</font>：由native English speaker为学员强化提高口语的发音技巧，纠正发音错误，帮助学员在熟练掌握口语答题技巧的同时全方位综合提高口语水平";

        oneTv.setText(Html.fromHtml(one));
        secondTv.setText(Html.fromHtml(second));
        thirdTv.setText(Html.fromHtml(third));
        setListener();

    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClassSettingActivity.this.finish();
            }
        });



    }

}
