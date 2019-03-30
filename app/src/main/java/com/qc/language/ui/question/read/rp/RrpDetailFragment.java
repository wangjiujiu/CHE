package com.qc.language.ui.question.read.rp;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.qc.language.common.view.itemtouchhelper.ItemTouchHelper;
import com.qc.language.common.view.itemtouchhelper.ItemTouchHelperCallback;
import com.qc.language.common.view.itemtouchhelper.RecycleViewDivider;
import com.qc.language.common.view.itemtouchhelper.SpacesItemDecoration;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.question.adapter.CheckAdapter;
import com.qc.language.ui.question.adapter.PickAdapter;
import com.qc.language.ui.question.data.OptionData;
import com.qc.language.ui.question.data.QDetail;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.listener.mcs.HmcsDetailContract;
import com.qc.language.ui.question.listener.mcs.HmcsDetailPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * 排序
 */
public class RrpDetailFragment extends CommonFragment implements HmcsDetailContract.View{

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
    private LinearLayout answerLl;
    private RecyclerView recyclerViewRight;
    private RightAdapter adapterRight;
    List<OptionData> dataListRight;


    //单选
    private RecyclerView recyclerView;
    private SwapListAdapter adapter;
    List<OptionData> dataList;
    private List<OptionData> haschooseitems=new ArrayList<>();

    @Inject
    HmcsDetailPresenter hsstDetailPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.read_ro_detail_frag, container, false);


        //题目区
        typeTv = (TextView) parentView.findViewById(R.id.listener_type);
        quesTv = (TextView) parentView.findViewById(R.id.listener_title);
        askTv = (TextView) parentView.findViewById(R.id.listener_ask);

        vipLl = (LinearLayout) parentView.findViewById(R.id.vip_only);
        answerRightTv = (TextView) parentView.findViewById(R.id.rs_content);
        checkBtn = (Button) parentView.findViewById(R.id.listener_chcek_answer);

        recyclerView = (RecyclerView)parentView.findViewById(R.id.mcs_detail_rv);
        adapter = new SwapListAdapter(getCommonActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getCommonActivity()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getCommonActivity(), LinearLayoutManager.VERTICAL, R.color.colorDivider));
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(adapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(ItemTouchHelperCallback.DRAG_FLAGS_VERTICAL,adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        answerLl = parentView.findViewById(R.id.rs_content_ll);
        recyclerViewRight = (RecyclerView)parentView.findViewById(R.id.mcs_detail_right);
        adapterRight = new RightAdapter(getCommonActivity());
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(getCommonActivity()));
        recyclerViewRight.addItemDecoration(new RecycleViewDivider(getCommonActivity(), LinearLayoutManager.VERTICAL, R.color.colorDivider));
        recyclerViewRight.addItemDecoration(new SpacesItemDecoration(5));
        recyclerViewRight.setAdapter(adapterRight);
        ItemTouchHelperCallback callback1 = new ItemTouchHelperCallback(ItemTouchHelperCallback.DRAG_FLAGS_VERTICAL,adapter);
        ItemTouchHelper helper1 = new ItemTouchHelper(callback1);
        helper1.attachToRecyclerView(recyclerViewRight);

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

    public static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }

    public static String letterToNum(String input)	{
        String reg = "[a-zA-Z]";
        StringBuffer strBuf = new StringBuffer();
        input = input.toLowerCase();
        if (null != input && !"".equals(input))
        {
            for (char c : input.toCharArray())
            {
                if (String.valueOf(c).matches(reg))
                {
                    strBuf.append(c - 96);
                } else
                    {
                        strBuf.append(c);
                    }
            }
            return strBuf.toString();
        } else {
            return input;
        }
    }

    private void setListener() {

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isEmpty(rightAnswer)) {
                    answerLl.setVisibility(View.VISIBLE);
                    answerRightTv.setVisibility(View.VISIBLE);
                    String htmlcontent = rightAnswer.replaceAll("font","androidfont");
                    answerRightTv.setText(HtmlUtils.getHtml(getCommonActivity(), answerRightTv, "答案："+htmlcontent));

                    recyclerViewRight.setVisibility(View.VISIBLE);

                    String s = htmlcontent.replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , ""); //去除所有標點

                    //321   ABC排成  CBA
                    List<OptionData> optionData = new ArrayList<>();
                    List<String> list1 = new ArrayList<>();
                    for(int i=0;i<dataListRight.size();i++){
                        if(i<s.length()){
                            String word = s.substring(i,i+1);
                            String n = letterToNum(word);  //
                            if(isNumeric(n)) {
                               list1.add(n);
                            }
                        }
                    }
                    for(int i=0;i<list1.size();i++) {
                       for(int j=0;j<dataListRight.size();j++){
                           if(list1.get(i).equals(dataListRight.get(j).getSeq())){
                               optionData.add(dataListRight.get(j));
                           }
                       }
                    }
                    adapterRight.setItems(optionData);
                    adapterRight.notifyDataSetChanged();
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
                 dataListRight = hqDetail.getData().getItems();  //沒有排序的
                 adapter.resetList(dataList);
                 adapter.notifyDataSetChanged();
            }
        }
    }
}
