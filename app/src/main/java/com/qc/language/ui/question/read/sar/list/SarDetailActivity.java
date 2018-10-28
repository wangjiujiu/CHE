package com.qc.language.ui.question.read.sar.list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.recyclerview.OnRecyclerViewItemClickListener;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.ui.question.adapter.CheckAdapter;
import com.qc.language.ui.question.adapter.PickAdapter;
import com.qc.language.ui.question.adapter.data.AnswerData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class SarDetailActivity extends CommonActivity implements SarDetailContract.View,OnRecyclerViewItemClickListener{

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
    private TextView contentTv;

    //单选
    private RecyclerView recyclerView;
    private PickAdapter adapter;
    List<AnswerData> dataList;
    private List<AnswerData> haschooseitems;


    @Inject
    SarDetailPresenter rsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcs_detail_act);

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
        typeTv.setText("MC-S");
        wordsTv = (TextView) findViewById(R.id.rs_words);
        numTv = (TextView) findViewById(R.id.rs_num);
        dateTv = (TextView) findViewById(R.id.rs_date);
        contentTv = (TextView) findViewById(R.id.rs_content);

        recyclerView = (RecyclerView)findViewById(R.id.mcs_detail_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        initData();

        setListener();
    }

    private  void initData(){

        adapter = new PickAdapter(getContext());
        recyclerView.setAdapter(adapter);

        dataList = new ArrayList<>();
        haschooseitems = new ArrayList<>();

        //假答案
        AnswerData answerData = new AnswerData();
        answerData.setLetter("A");
        answerData.setContent("right");
        answerData.setRight(true);


        AnswerData answerData1 = new AnswerData();
        answerData1.setLetter("B");
        answerData1.setContent("error");
        answerData1.setRight(false);

        AnswerData answerData2 = new AnswerData();
        answerData2.setLetter("C");
        answerData2.setContent("error");
        answerData2.setRight(false);

        AnswerData answerData3 = new AnswerData();
        answerData3.setLetter("D");
        answerData3.setContent("error");
        answerData3.setRight(true);


        dataList.add(answerData);
        dataList.add(answerData1);
        dataList.add(answerData2);
        dataList.add(answerData3);
        adapter.resetList(dataList);

        adapter.setOnItemClickListener(new PickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                haschooseitems.clear();
                AnswerData answerData = new AnswerData();
                answerData = dataList.get(position);
                haschooseitems.add(answerData);
                adapter.resetHasChooseList(haschooseitems);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SarDetailActivity.this.finish();
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
                CheckAdapter checkAdapter = new CheckAdapter(getContext());
                recyclerView.setAdapter(checkAdapter);
                checkAdapter.resetList(dataList);
                checkAdapter.resetHasChooseList(haschooseitems);
                checkAdapter.notifyDataSetChanged();
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
