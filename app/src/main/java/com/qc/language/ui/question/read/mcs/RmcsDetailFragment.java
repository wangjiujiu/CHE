package com.qc.language.ui.question.read.mcs;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.common.utils.FileHelper;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.framework.sound.VoiceItem;
import com.qc.language.service.Constant;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.question.adapter.CheckAdapter;
import com.qc.language.ui.question.adapter.PickAdapter;
import com.qc.language.ui.question.data.OptionData;
import com.qc.language.ui.question.data.QDetail;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.listener.mcs.HmcsDetailContract;
import com.qc.language.ui.question.listener.mcs.HmcsDetailPresenter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 阅读单选
 */
public class RmcsDetailFragment extends CommonFragment implements HmcsDetailContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;


    private String name;
    private int role;
    private String type;
    private int currentQues;
    private String id;
    private List<Question> list = new ArrayList<>();
    private TextView answerRightTv;  //显示原文


    private int userType;
    //考题内容
    private TextView quesTv;
    private TextView askTv;
    private TextView typeTv;

    //vip区域
    private LinearLayout vipLl;

    private String rightAnswer;
    private Button checkBtn;

    //单选
    private RecyclerView recyclerView;
    private PickAdapter adapter;
    List<OptionData> dataList;
    private List<OptionData> haschooseitems=new ArrayList<>();

    @Inject
    HmcsDetailPresenter hsstDetailPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.read_mcs_detail_frag, container, false);


        //题目区
        typeTv = (TextView) parentView.findViewById(R.id.listener_type);
        quesTv = (TextView) parentView.findViewById(R.id.listener_title);
        askTv = (TextView) parentView.findViewById(R.id.listener_ask);

        vipLl = (LinearLayout) parentView.findViewById(R.id.vip_only);
        answerRightTv = (TextView) parentView.findViewById(R.id.rs_content);
        checkBtn = (Button) parentView.findViewById(R.id.listener_chcek_answer);

        recyclerView = (RecyclerView)parentView.findViewById(R.id.mcs_detail_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new PickAdapter(getContext());
        recyclerView.setAdapter(adapter);

        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载一级部门列表
        if(getArguments() != null ){
            Bundle bundle = getArguments();
            //返回的
            name = bundle.getString("title"); //名字 SST
            role = bundle.getInt("role",0); //用户类型 0 是普通用户，1是vip
            type = bundle.getString("type");   //题目请求的type参数
            currentQues =bundle.getInt("currentQues",0);
            id = bundle.getString("id");


            typeTv.setText(name);

            //判断是不是vip
            if(CurrentUser.getCurrentUser()!=null&&CurrentUser.getCurrentUser().hasLogin()){
                userType = 1;
                vipLl.setVisibility(View.VISIBLE);

            }else{
                userType = 0;
                vipLl.setVisibility(View.GONE);
            }

            hsstDetailPresenter.loadDetail(id,type);

            setListener();
        }

    }


    private void setListener() {

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isEmpty(rightAnswer)) {
                    answerRightTv.setVisibility(View.VISIBLE);
                    String htmlcontent = rightAnswer.replaceAll("font","androidfont");
                    answerRightTv.setText(HtmlUtils.getHtml(getCommonActivity(), answerRightTv, "答案："+htmlcontent));

                    if(dataList!=null&&dataList.size()>0){
                    CheckAdapter checkAdapter = new CheckAdapter(getContext());
                    recyclerView.setAdapter(checkAdapter);
                    checkAdapter.resetList(dataList);
                    checkAdapter.resetRightAnswer(htmlcontent);
                    checkAdapter.resetHasChooseList(haschooseitems);
                    checkAdapter.notifyDataSetChanged();
                    }
                }else{
                    ToastUtils.showShort("暂缺答案");
                }
            }
        });

    }


    @Override
    public void unRegister() {
        super.unRegister();
        hsstDetailPresenter.detachView();
    }

    @Override
    public void register() {
        super.register();
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        hsstDetailPresenter.attachView(this);
    }

    @Override
    public void loadSuccess(QDetail hqDetail) {
        if(hqDetail.getData()!=null){
            if(hqDetail.getData().getTitle()!=null)
            {
                quesTv.setText(hqDetail.getData().getTitle()); //标题
            }

            if(hqDetail.getData().getContent()!=null){
                String htmlcontent = hqDetail.getData().getContent().replaceAll("font","androidfont");
                askTv.setText(HtmlUtils.getHtml(getCommonActivity(), askTv, htmlcontent));
            }

            //答案
            if(hqDetail.getData().getAnswer()!=null){
                rightAnswer = hqDetail.getData().getAnswer();
            }

            //选择题,以及选择的监听
            if(hqDetail.getData().getItems()!=null&&hqDetail.getData().getItems().size()>0){
                 Collections.sort(hqDetail.getData().getItems());
                 dataList = hqDetail.getData().getItems();
                 adapter.resetList(dataList);
                 adapter.notifyDataSetChanged();

                adapter.setOnItemClickListener(new PickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        haschooseitems.clear();
                        OptionData answerData = new OptionData();
                        answerData = dataList.get(position);
                        haschooseitems.add(answerData);
                        adapter.resetHasChooseList(haschooseitems);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
