package com.qc.language.ui.question.read.fibrw.list;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.panterdialog.DialogType;
import com.qc.language.common.view.panterdialog.PanterDialog;
import com.qc.language.common.view.panterdialog.interfaces.OnSingleCallbackConfirmListener;
import com.qc.language.common.view.recyclerview.OnRecyclerViewItemClickListener;
import com.qc.language.common.view.textview.ReplaceSpan;
import com.qc.language.common.view.textview.SpanController;
import com.qc.language.service.components.DaggerActivityComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * 拖动重新排序，两个gridview，
 * Created by beckett on 2018/10/11.
 */
public class FibrwDetailActivity extends CommonActivity implements FibrwDetailContract.View,ReplaceSpan.OnClick{

    private Toolbar toolbar;
    private TextView checkBtn;
    private TextView toolbarTitleTextView;

    private int currentQues;
    private int totalQues;

    private Button lastBtn;
    private Button nextBtn;

    private boolean IsVip = true;

    //考题内容+答案显示
    private TextView titleTv;
    private TextView typeTv;
    private TextView wordsTv;
    private TextView numTv;
    private TextView dateTv;

    private TextView mTvContent;
    private String mStr = "Today the weather was [fine], we are going to climb the [mountain].";
    private List<String> strings;
    private SpanController mSpc;

    @Inject
    FibrwDetailPresenter rsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fibrw_detail_act);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        checkBtn = (TextView) findViewById(R.id.common_toolbar_finish);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        checkBtn.setText("验证答案");

        currentQues = getIntent().getIntExtra("currentQues",0);
        totalQues = getIntent().getIntExtra("totalQues",0);
        toolbarTitleTextView.setText("第"+currentQues+"题");

        lastBtn = (Button) findViewById(R.id.answer_btn_previous);
        nextBtn = (Button) findViewById(R.id.answer_btn_next);


        titleTv = (TextView) findViewById(R.id.rs_title);
        typeTv = (TextView) findViewById(R.id.rs_type);
        typeTv.setText("FIB_RW");
        wordsTv = (TextView) findViewById(R.id.rs_words);
        numTv = (TextView) findViewById(R.id.rs_num);
        dateTv = (TextView) findViewById(R.id.rs_date);

        mTvContent = (TextView) findViewById(R.id.rs_content);
        mSpc = new SpanController();
        mSpc.makeData(this,mTvContent,mStr);
        
        initData();
        setListener();
    }


    private  void initData(){
        strings = new ArrayList<>();
        strings.add("air");
        strings.add("boy");
        strings.add("coco");
        strings.add("dog");
    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FibrwDetailActivity.this.finish();
            }
        });


        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues<2){
                }else{
                    currentQues--;
                    toolbarTitleTextView.setText("第"+currentQues+"题");
                    initData();
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues<totalQues){
                    currentQues++;
                    toolbarTitleTextView.setText("第"+currentQues+"题");
                    //
                    initData();
                }
            }
        });


        //验证答案的按钮
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得答案
                ToastUtils.showShort(mSpc.getAllAnswer());
            }
        });


    }


    @Override
    public void register() {
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        rsDetailPresenter.attachView(this);
    }

    @Override
    public void unRegister() {
        rsDetailPresenter.detachView();
    }


    @Override
    public void OnClick(final TextView v, final int id, final ReplaceSpan span) {
//        if (span.mObject == null)return;
//        if (span.mObject instanceof View){
//            View view = (View)span.mObject;
//        }
//        span.mText = ""; //删除
//        span.mObject = null;
//        mTvContent.invalidate();
        new PanterDialog(this)
                .setDialogType(DialogType.SINGLECHOICE)
                .isCancelable(false)
                .items(strings.toArray(new String[strings.size()]), new OnSingleCallbackConfirmListener() {
                    @Override
                    public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {
                        String key = text;
                        if (TextUtils.isEmpty(key)) return;
                         mSpc.setData(key, v);  //新增
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
