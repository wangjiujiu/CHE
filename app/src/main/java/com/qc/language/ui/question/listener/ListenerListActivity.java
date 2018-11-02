package com.qc.language.ui.question.listener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.service.DataCleanManager;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.ui.question.QuestionContainerActivity;
import com.qc.language.ui.question.data.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * 听力部分通用列表
 */
public class ListenerListActivity extends CommonActivity implements ListenerListContract.View{


    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private RecyclerView refreshRecyclerView;
    private ListenerListAdapter rsListAdapter;


    @Inject
    MyApplication application;
    @Inject
    ListenerListPresenter rsListPresenter;

    private int role;  //角色
    private String type;
    private String name;

    private int totalQues=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list);

        name = getIntent().getStringExtra("title"); //名字 SST
        role = getIntent().getIntExtra("role",0); //用户类型 0 是普通用户，1是vip
        type = getIntent().getStringExtra("type");   //题目请求的type参数

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText(name);

        refreshRecyclerView =(RecyclerView) findViewById(R.id.common_list_rv);
        List<Question> datas = new ArrayList<>();
        rsListAdapter = new ListenerListAdapter(this,datas);
        refreshRecyclerView.setAdapter(rsListAdapter);
        refreshRecyclerView.setFocusable(false);
        refreshRecyclerView.setHasFixedSize(true);
        refreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshRecyclerView.setNestedScrollingEnabled(false);

        rsListPresenter.loadList(role,type);

        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListenerListActivity.this.finish();
            }
        });

        rsListAdapter.setOnItemDetailClickListener(new ListenerListAdapter.onItemDetailListener() {
            @Override
            public void onDetailClick(int i, Question data) {
                if(data.getSeq()!=null){
                    //先删掉文件夹下的录音和题目文件，以防出错！
                    if(isInteger(data.getSeq())){
                        Intent intent = new Intent();
                        intent.putExtra("title",name);
                        intent.putExtra("role",role);
                        intent.putExtra("currentQues",i); //题号
                        intent.putExtra("type",type);
                        intent.setClass(ListenerListActivity.this, QuestionContainerActivity.class);
                        startActivity(intent);
                    }
                    }else{
                        ToastUtils.showShort("题库有问题！");
                    }
                }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataCleanManager.cleanVoice(getContext()); //退出前清掉所有下载的音频
    }



        @Override
    public void register() {
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        rsListPresenter.attachView(this);
    }

    @Override
    public void unRegister() {
        rsListPresenter.detachView();
    }


    @Override
    public void loadSuccess(List<Question> dataTObject) {
        if(dataTObject!=null){
            rsListAdapter.resetList(dataTObject);
            rsListAdapter.notifyDataSetChanged();
        }else{
            rsListAdapter.setEmpty();
            totalQues = 0;
            rsListAdapter.notifyDataSetChanged();
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
