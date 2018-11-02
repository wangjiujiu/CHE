package com.qc.language.ui.question.read.fibr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.question.adapter.CheckAdapter;
import com.qc.language.ui.question.adapter.PickAdapter;
import com.qc.language.ui.question.data.OptionData;
import com.qc.language.ui.question.data.QDetail;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.read.mcm.McmDetailContract;
import com.qc.language.ui.question.read.mcm.McmDetailPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * 选词填空
 */
public class RfibrDetailFragment extends CommonFragment implements FibrDetailContract.View{

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
    private RecyclerView adviceGv;
    private WordAdapter adviceAdapter;

    private RecyclerView checkGv;
    private FillAdapter checkAdapter;
    List<String> content;
    List<String> check;

    @Inject
    FibrDetailPresenter hsstDetailPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.read_fibr_detail_frag, container, false);


        //题目区
        typeTv = (TextView) parentView.findViewById(R.id.listener_type);
        quesTv = (TextView) parentView.findViewById(R.id.listener_title);
        askTv = (TextView) parentView.findViewById(R.id.listener_ask);

        vipLl = (LinearLayout) parentView.findViewById(R.id.vip_only);
        answerRightTv = (TextView) parentView.findViewById(R.id.rs_content);
        checkBtn = (Button) parentView.findViewById(R.id.listener_chcek_answer);

        adviceGv = (RecyclerView)parentView.findViewById(R.id.fibr_detail_advice);
        adviceGv.setHasFixedSize(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getCommonActivity(), 4, LinearLayoutManager.VERTICAL,false);
        adviceGv.setLayoutManager(layoutManager1);
        adviceGv.setLayoutManager(layoutManager1);
        adviceGv.setNestedScrollingEnabled(false);
        adviceAdapter = new WordAdapter(getContext());
        adviceGv.setAdapter(adviceAdapter);
        adviceGv.setFocusable(false);


        checkGv = (RecyclerView)parentView.findViewById(R.id.fibr_detail_check);
        checkGv.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getCommonActivity(), 4, LinearLayoutManager.VERTICAL,false);
        checkGv.setLayoutManager(layoutManager2);
        checkGv.setNestedScrollingEnabled(false);
        checkAdapter = new FillAdapter(getContext());
        checkGv.setAdapter(checkAdapter);
        checkGv.setFocusable(false);

        content = new ArrayList<>();
        check = new ArrayList<>();

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


    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    private void setListener() {

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isEmpty(rightAnswer)) {
                    answerRightTv.setVisibility(View.VISIBLE);
                    String htmlcontent = rightAnswer.replaceAll("font","androidfont");
                    answerRightTv.setText(HtmlUtils.getHtml(getCommonActivity(), answerRightTv, "答案："+htmlcontent));
                }else{
                    ToastUtils.showShort("暂缺答案");
                }
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
        checkAdapter.setOnItemClickListener(new FillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                check.set(position,String.valueOf(position+1));
                checkAdapter.notifyDataSetChanged();
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
        if(hqDetail.getData()!=null) {
            if (hqDetail.getData().getTitle() != null) {
                quesTv.setText(hqDetail.getData().getTitle()); //标题
            }

            if (hqDetail.getData().getContent() != null) {
                String htmlcontent = hqDetail.getData().getContent().replaceAll("font", "androidfont");
                askTv.setText(HtmlUtils.getHtml(getCommonActivity(), askTv, htmlcontent));
            }

            //答案
            if (hqDetail.getData().getAnswer() != null) {
                rightAnswer = hqDetail.getData().getAnswer();
            }

           if(hqDetail.getData().getSuggests()!=null&&!StringUtils.isEmpty(hqDetail.getData().getSuggests())){
                String[] str = hqDetail.getData().getSuggests().split(",");
                for(int i=0;i<str.length;i++){
                    content.add(str[i]);
                    int m = i+1;
                    check.add(String.valueOf(m));
                }
               adviceAdapter.setItems(content);
               adviceAdapter.notifyDataSetChanged();

               checkAdapter.setItems(check);
               checkAdapter.notifyDataSetChanged();
           }
        }
    }
}
