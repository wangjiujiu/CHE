package com.qc.language.ui.question.read.fibr.list;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * 拖动重新排序，两个gridview，
 * Created by beckett on 2018/10/11.
 */
public class FibrDetailActivity extends CommonActivity implements FibrDetailContract.View,OnRecyclerViewItemClickListener{

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

    private RecyclerView adviceGv;
    private WordAdapter adviceAdapter;

    private RecyclerView checkGv;
    private CheckAdapter checkAdapter;
    List<String> content;
    List<String> check;


    @Inject
    FibrDetailPresenter rsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fibr_detail_act);

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

        adviceGv = (RecyclerView)findViewById(R.id.fibr_detail_advice);
        adviceGv.setHasFixedSize(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL,false);
        adviceGv.setLayoutManager(layoutManager1);
        adviceGv.setLayoutManager(layoutManager1);
        adviceGv.setNestedScrollingEnabled(false);
        adviceAdapter = new WordAdapter(getContext());
        adviceGv.setAdapter(adviceAdapter);
        adviceGv.setFocusable(false);


        checkGv = (RecyclerView)findViewById(R.id.fibr_detail_check);
        checkGv.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL,false);
        checkGv.setLayoutManager(layoutManager2);
        checkGv.setNestedScrollingEnabled(false);
        checkAdapter = new CheckAdapter(getContext());
        checkGv.setAdapter(checkAdapter);
        checkGv.setFocusable(false);

        initData();
        setListener();
    }

    private  void initData(){
        //建议里放入答案
        content = new ArrayList<>();
        content.add("advice");
        content.add("born");
        content.add("carry");
        content.add("unbrella");
        content.add("advice");
        content.add("born");
        content.add("carry");
        content.add("abcdefghijklm");

        adviceAdapter.setItems(content);
        adviceAdapter.notifyDataSetChanged();

        check = new ArrayList<>();
        check.add("1");
        check.add("2");
        check.add("3");
        check.add("4");
        check.add("5");
        check.add("6");
        check.add("7");
        check.add("8");

        checkAdapter.setItems(check);
        checkAdapter.notifyDataSetChanged();
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FibrDetailActivity.this.finish();
            }
        });

        adviceAdapter.setOnItemClickListener(new WordAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for(int i=0;i<check.size();i++){
                    if(isInteger(check.get(i))){  //是标号的地方能添加单词
                        check.set(i,content.get(position));
                        checkAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        //删除
        checkAdapter.setOnItemClickListener(new CheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                check.set(position,String.valueOf(position+1));
                checkAdapter.notifyDataSetChanged();
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
