package com.qc.language.ui.question.read.fibrw;

import android.content.Intent;
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

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.common.view.panterdialog.DialogType;
import com.qc.language.common.view.panterdialog.PanterDialog;
import com.qc.language.common.view.panterdialog.interfaces.OnSingleCallbackConfirmListener;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.question.data.OptionData;
import com.qc.language.ui.question.data.QDetail;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.read.fibr.FibrDetailContract;
import com.qc.language.ui.question.read.fibr.FibrDetailPresenter;
import com.qc.language.ui.question.read.fibr.FillAdapter;
import com.qc.language.ui.question.read.fibr.WordAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * 选词填空
 */
public class RfibrwDetailFragment extends CommonFragment implements FibrDetailContract.View{

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


    private RecyclerView checkGv;
    private FillAdapter checkAdapter;
    List<String> check;
    List<String> firstCheck;
    List<String> answer;


    @Inject
    FibrDetailPresenter hsstDetailPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.read_fibrw_detail_frag, container, false);


        //题目区
        typeTv = (TextView) parentView.findViewById(R.id.listener_type);
        quesTv = (TextView) parentView.findViewById(R.id.listener_title);
        askTv = (TextView) parentView.findViewById(R.id.listener_ask);

        vipLl = (LinearLayout) parentView.findViewById(R.id.vip_only);
        answerRightTv = (TextView) parentView.findViewById(R.id.rs_content);
        checkBtn = (Button) parentView.findViewById(R.id.listener_chcek_answer);


        checkGv = (RecyclerView)parentView.findViewById(R.id.fibr_detail_check);
        checkGv.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getCommonActivity(), 4, LinearLayoutManager.VERTICAL,false);
        checkGv.setLayoutManager(layoutManager2);
        checkGv.setNestedScrollingEnabled(false);
        checkAdapter = new FillAdapter(getContext());
        checkGv.setAdapter(checkAdapter);
        checkGv.setFocusable(false);

        check = new ArrayList<>();
        answer = new ArrayList<>();
        firstCheck = new ArrayList<>();

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
                if(answer.size()>0&&answer!=null) {
                    StringBuffer sb = new StringBuffer();
                    for(String str:answer){
                    sb.append(str);
                    sb.append(",");
                    }
                    answerRightTv.setVisibility(View.VISIBLE);
                    answerRightTv.setText("答案："+sb.toString().substring(0,sb.length()-1));
                   }else{
                    ToastUtils.showShort("暂缺答案");
                }
            }
        });


        //弹窗
        checkAdapter.setOnItemClickListener(new FillAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                //弹窗
                if(!StringUtils.isEmpty(check.get(position))){
                    String[] str = check.get(position).split(",");
                    if(str.length>0){
                        new PanterDialog(getCommonActivity())
                                .setDialogType(DialogType.SINGLECHOICE)
                                .isCancelable(false)
                                .items(str, new OnSingleCallbackConfirmListener() {
                                    @Override
                                    public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {
                                        firstCheck.set(position,text);
                                        checkAdapter.notifyDataSetChanged();
                                    }
                                })
                                .show();
                    }
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
        if(hqDetail.getData()!=null) {
            if (hqDetail.getData().getTitle() != null) {
                quesTv.setText(hqDetail.getData().getTitle()); //标题
            }

            if (hqDetail.getData().getContent() != null) {
                String htmlcontent = hqDetail.getData().getContent().replaceAll("font", "androidfont");
                askTv.setText(HtmlUtils.getHtml(getCommonActivity(), askTv, htmlcontent));
            }

           if(hqDetail.getData().getItems()!=null&&hqDetail.getData().getItems().size()>0){
                for(int i=0;i<hqDetail.getData().getItems().size();i++){
                    if(hqDetail.getData().getItems().get(i).getContent()!=null){
                        check.add(hqDetail.getData().getItems().get(i).getContent()); //选项
                    }else{
                        check.add(""); //选项
                    }
                    if(hqDetail.getData().getItems().get(i).getAnswer()!=null){
                        answer.add(hqDetail.getData().getItems().get(i).getAnswer());
                    }else{
                        answer.add("");
                    }

                    int m = i+1;
                    firstCheck.add(String.valueOf(m));
                }

               checkAdapter.setItems(firstCheck);
               checkAdapter.notifyDataSetChanged();
           }
        }
    }
}
