package com.qc.language.ui.question.read.fibr.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.recyclerview.OnNextPageListener;
import com.qc.language.common.view.recyclerview.OnRecyclerViewItemClickListener;
import com.qc.language.common.view.recyclerview.RefreshRecyclerView;
import com.qc.language.common.view.recyclerview.RefreshRecyclerViewUtil;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.ui.question.read.fibr.data.FibrData;
import com.qc.language.ui.question.speak.rs.data.RSListData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 阅读填空
 * Created by beckett on 2018/10/11.
 */
public class FibrListActivity extends CommonActivity implements OnRecyclerViewItemClickListener, FibrListContract.View{

    private static final int PAGE_STARTING = 0; // 代表起始页

    private int pageNo;
    private int pageSize = 10;
    private int totalRecords;
    private boolean isPullRefresh = false;

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private RefreshRecyclerView refreshRecyclerView;
    private FibrListAdapter rsListAdapter;


    @Inject
    MyApplication application;
    @Inject
    FibrListPresenter rsListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_list_act);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleTextView.setText("Fill in the blanks");

        refreshRecyclerView =(RefreshRecyclerView)findViewById(R.id.common_list_act_rv);
        List<FibrData> datas = new ArrayList<>();
        rsListAdapter = new FibrListAdapter(this,datas);
        refreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshRecyclerView.setAdapter(rsListAdapter);
        refreshRecyclerView.setRefreshEnable(true);
        rsListAdapter.setListener(this);
        rsListAdapter.notifyDataSetChanged();
        RefreshRecyclerViewUtil.initRefreshViewColorSchemeColors(refreshRecyclerView,getResources());

        //假数据
        List<FibrData> rsDataList = new ArrayList<>();
       for(int i=0;i<5;i++){
           FibrData rsData = new FibrData();
           rsData.setTitle("第"+i+"题");
           rsDataList.add(rsData);
       }

       rsListAdapter.resetList(rsDataList);
       rsListAdapter.notifyDataSetChanged();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FibrListActivity.this.finish();
            }
        });


       // setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FibrListActivity.this.finish();
            }
        });

        // 刷新监听
        refreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onDoRefresh();
            }
        });

        // 请求下一页监听
        refreshRecyclerView.setOnNextPageListener(new OnNextPageListener() {
            @Override
            public void onNextPage() {
                onDoNextPage();
            }
        });

        refreshRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                refreshRecyclerView.setRefreshing(true);
                onDoRefresh();
            }
        });

    }

    protected void onDoRefresh() {
        if (!isPullRefresh) {
            pageNo = PAGE_STARTING;
            isPullRefresh = true;

        }
    }

    protected void onDoNextPage() {
        if (!isPullRefresh) {
            pageNo++;
            isPullRefresh = true;

        }
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
    public void loadSuccess(RSListData dataTObject) {

    }

    @Override
    public void completeLoading() {
        refreshRecyclerView.refreshComplete();
        isPullRefresh = false;
    }

    @Override
    public void onItemClick(View view, Object data) {

        Intent intent = new Intent();
        intent.putExtra("currentQues",3);
        intent.putExtra("totalQues",150);
        intent.setClass(getContext(),FibrDetailActivity.class);
        startActivity(intent);
    }
}