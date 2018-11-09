package com.qc.language.ui.question;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.service.db.question.CurrentQuesstion;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.listener.fibl.HfiblDetailFragment;
import com.qc.language.ui.question.listener.hcs.HhcsDetailFragment;
import com.qc.language.ui.question.listener.hiw.HhiwDetailFragment;
import com.qc.language.ui.question.listener.mcm.HmcmDetailFragment;
import com.qc.language.ui.question.listener.mcs.HmcsDetailFragment;
import com.qc.language.ui.question.listener.smw.HsmwDetailFragment;
import com.qc.language.ui.question.listener.sst.HsstDetailFragment;
import com.qc.language.ui.question.listener.wfd.HwfdDetailFragment;
import com.qc.language.ui.question.read.fibr.RfibrDetailFragment;
import com.qc.language.ui.question.read.fibrw.RfibrwDetailFragment;
import com.qc.language.ui.question.read.mcm.RmcmDetailFragment;
import com.qc.language.ui.question.read.mcs.RmcsDetailFragment;
import com.qc.language.ui.question.read.rp.RrpDetailFragment;
import com.qc.language.ui.question.speak.asq.SAsqDetailFragment;
import com.qc.language.ui.question.speak.di.SdiDetailFragment;
import com.qc.language.ui.question.speak.ra.SraDetailFragment;
import com.qc.language.ui.question.speak.rl.SrlDetailFragment;
import com.qc.language.ui.question.speak.rs.SrsDetailFragment;
import com.qc.language.ui.question.write.swt.WswtDetailFragment;
import com.qc.language.ui.question.write.we.WwfdDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckett on 2018/10/29.
 */
public class QuestionContainerActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private Button lastBtn;
    private Button nextBtn;


    private String name;
    private int role;
    private String type;
    private int currentQues;
    private int totalQues;
    private List<Question> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        lastBtn = (Button) findViewById(R.id.answer_btn_previous);
        nextBtn = (Button) findViewById(R.id.answer_btn_next);

        name = getIntent().getStringExtra("title"); //名字 SST
        role = getIntent().getIntExtra("role",0); //用户类型 0 是普通用户，1是vip
        type = getIntent().getStringExtra("type");   //题目请求的type参数
        currentQues =getIntent().getIntExtra("currentQues",0);


        final Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.obj == "DB") {
                    toolbarTitleTextView.setText(name+"-"+(currentQues+1)+"/"+totalQues);
                    setListener();
                }
            }
        };
        // 在这里开一个线程执行读数据耗时操作
        new Thread() {
            public void run() {
                if(CurrentQuesstion.getCurrentQuesstion()!=null){
                    list = CurrentQuesstion.getCurrentQuesstion().getQuestionIdFromDB();
                    totalQues = list.size();
                    initFragment();
                }
                Message msg = handler.obtainMessage();
                msg.obj = "DB";
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void initFragment(){
        if(type.equals("H-SST")) {
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HsstDetailFragment hsstDetailFragment = new HsstDetailFragment();
            hsstDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hsstDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-MC-S")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HmcsDetailFragment hsstDetailFragment = new HmcsDetailFragment();
            hsstDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hsstDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-MC-M")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HmcmDetailFragment hmcmDetailFragment = new HmcmDetailFragment();
            hmcmDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hmcmDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-FIB-L")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HfiblDetailFragment hfiblDetailFragment = new HfiblDetailFragment();
            hfiblDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hfiblDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-HCS")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HhcsDetailFragment hhcsDetailFragment = new HhcsDetailFragment();
            hhcsDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hhcsDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-SMW")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HsmwDetailFragment hsmwDetailFragment = new HsmwDetailFragment();
            hsmwDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hsmwDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-WFD")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HwfdDetailFragment hwfdDetailFragment = new HwfdDetailFragment();
            hwfdDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hwfdDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("H-HIW")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            HhiwDetailFragment hwfdDetailFragment = new HhiwDetailFragment();
            hwfdDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, hwfdDetailFragment).addToBackStack(null).commit();
        }

        //写作
        else if(type.equals("W-WE")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            WwfdDetailFragment wwfdDetailFragment = new WwfdDetailFragment();
            wwfdDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, wwfdDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("W-SWT")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            WswtDetailFragment wswtDetailFragment = new WswtDetailFragment();
            wswtDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, wswtDetailFragment).addToBackStack(null).commit();
        }

        //说
        else if(type.equals("S-RA")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            SraDetailFragment sraDetailFragment = new SraDetailFragment();
            sraDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, sraDetailFragment).addToBackStack(null).commit();
        }   else if(type.equals("S-RS")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            SrsDetailFragment srsDetailFragment = new SrsDetailFragment();
            srsDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, srsDetailFragment).addToBackStack(null).commit();
        }  else if(type.equals("S-DI")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            SdiDetailFragment sdiDetailFragment = new SdiDetailFragment();
            sdiDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, sdiDetailFragment).addToBackStack(null).commit();
        } else if(type.equals("S-RL")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name); //一级
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            SrlDetailFragment srlDetailFragment = new SrlDetailFragment();
            srlDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, srlDetailFragment).addToBackStack(null).commit();
        } else if(type.equals("S-ASQ")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            SAsqDetailFragment sAsqDetailFragment = new SAsqDetailFragment();
            sAsqDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, sAsqDetailFragment).addToBackStack(null).commit();
        }

        //读
        else if(type.equals("R-MC-S")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            RmcsDetailFragment rmcsDetailFragment = new RmcsDetailFragment();
            rmcsDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, rmcsDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("R-MC-M")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            RmcmDetailFragment rmcmDetailFragment = new RmcmDetailFragment();
            rmcmDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, rmcmDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("R-RO")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            RrpDetailFragment rmcmDetailFragment = new RrpDetailFragment();
            rmcmDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, rmcmDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("R-RO")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            RrpDetailFragment rmcmDetailFragment = new RrpDetailFragment();
            rmcmDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, rmcmDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("R-FIB-R")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            RfibrDetailFragment rfibrDetailFragment = new RfibrDetailFragment();
            rfibrDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, rfibrDetailFragment).addToBackStack(null).commit();
        }else if(type.equals("R-FIB-RW")){
            Bundle bundle = new Bundle();
            bundle.putString("title", name);
            bundle.putInt("role", role);
            bundle.putString("type", type);
            bundle.putInt("currentQues", currentQues);
            bundle.putString("id", list.get(currentQues).getId());
            RfibrwDetailFragment rfibrwDetailFragment = new RfibrwDetailFragment();
            rfibrwDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_container, rfibrwDetailFragment).addToBackStack(null).commit();
        }
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionContainerActivity.this.finish();
            }
        });


        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues==0){
                    ToastUtils.showShort("已经是第一题了");
                }else{
                    currentQues--;
                    toolbarTitleTextView.setText(name+"-"+(currentQues+1)+"/"+totalQues);
                    //替换fragment
                    initFragment();
                }

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues==(totalQues-1)){
                    ToastUtils.showShort("已经是最后一题了");
                }else{
                    currentQues++;
                    toolbarTitleTextView.setText(name+"-"+(currentQues+1)+"/"+totalQues);
                    //替换fragment
                    initFragment();
                }
            }
        });


    }

}
