package com.qc.language.ui.question.read.rp.list;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.itemtouchhelper.ItemTouchHelper;
import com.qc.language.common.view.itemtouchhelper.ItemTouchHelperCallback;
import com.qc.language.common.view.itemtouchhelper.RecycleViewDivider;
import com.qc.language.common.view.itemtouchhelper.SpacesItemDecoration;
import com.qc.language.common.view.panterdialog.PanterDialog;
import com.qc.language.common.view.recyclerview.OnRecyclerViewItemClickListener;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.ui.question.adapter.CheckAdapter;
import com.qc.language.ui.question.adapter.PickAdapter;
import com.qc.language.ui.question.adapter.data.AnswerData;
import com.qc.language.ui.question.read.rp.SwapListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 拖动重新排序
 * Created by beckett on 2018/10/11.
 */
public class RpDetailActivity extends CommonActivity implements RpDetailContract.View,OnRecyclerViewItemClickListener{

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

    RecyclerView recyclerView;
    List<String> list;
    SwapListAdapter adapter;

    @Inject
    RpDetailPresenter rsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rp_detail_act);

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
        typeTv.setText("RP");
        wordsTv = (TextView) findViewById(R.id.rs_words);
        numTv = (TextView) findViewById(R.id.rs_num);
        dateTv = (TextView) findViewById(R.id.rs_date);

        recyclerView = (RecyclerView) findViewById(R.id.mcs_detail_rv);
        adapter = new SwapListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.color.colorDivider));
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(adapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(ItemTouchHelperCallback.DRAG_FLAGS_VERTICAL,adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        initData(3);
        setListener();
    }

    private  void initData(int m){
        list = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            list.add("段落" + i);
        }

        adapter.resetList(list);
        adapter.notifyDataSetChanged();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RpDetailActivity.this.finish();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues<2){
                }else{
                    currentQues--;
                    toolbarTitleTextView.setText("第"+currentQues+"题");
                    initData(2);
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
                    initData(6);
                }
            }
        });


        //验证答案的按钮
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得答案
                new PanterDialog(RpDetailActivity.this)
                        .setHeaderBackground(R.color.colorPrimary)
                        .setHeaderLogo(R.mipmap.ic_launcher)
                        .setPositive("确定")// You can pass also View.OnClickListener as second param
                        .setMessage(adapter.getNewList().toString())
                        .isCancelable(false)
                        .show();
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
    public void onItemClick(View view, Object data) {

    }
}
