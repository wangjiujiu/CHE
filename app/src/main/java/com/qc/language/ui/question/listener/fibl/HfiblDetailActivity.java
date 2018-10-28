package com.qc.language.ui.question.listener.fibl;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.utils.FileHelper;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.framework.sound.VoiceItem;
import com.qc.language.service.Constant;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.question.CurrentQuesstion;
import com.qc.language.ui.question.listener.data.HQDetail;
import com.qc.language.ui.question.listener.data.HQuestion;
import com.qc.language.ui.question.listener.mcs.HmcsDetailContract;
import com.qc.language.ui.question.listener.mcs.HmcsDetailPresenter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * 听录音，填空题
 */
public class HfiblDetailActivity extends CommonActivity implements HmcsDetailContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;


    private String name;
    private int role;
    private String type;
    private int currentQues;
    private int totalQues;
    private List<HQuestion> list = new ArrayList<>();
    private TextView answerRightTv;  //显示原文


    //考题内容
    private TextView quesTv;
    private TextView typeTv;
    //是否在播放题目
    private ImageView playTestBtn;
    private ProgressBar seekBar;  //进度条
    private VoiceItem voiceTestItem ;
    private Timer timer;
    private boolean isSeekBarChanging=false;//互斥变量，防止进度条与定时器冲突
    private int currentPosition=0;//当前音乐播放的进度
    SimpleDateFormat format;
    MediaPlayer testPlayer = new MediaPlayer();

    //vip区域
    private LinearLayout vipLl;

    private String rightAnswer;
    private Button checkBtn;

    private Button lastBtn;
    private Button nextBtn;

    //播放、录音相关
    private Handler voiceHandler = new Handler();

    @Inject HmcsDetailPresenter hsstDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listener_sst_detail);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        name = getIntent().getStringExtra("title"); //名字 SST
        role = getIntent().getIntExtra("role",0); //用户类型 0 是普通用户，1是vip
        type = getIntent().getStringExtra("type");   //题目请求的type参数
        currentQues =getIntent().getIntExtra("currentQues",0);


        //题目区
        typeTv = (TextView) findViewById(R.id.listener_type);
        quesTv = (TextView) findViewById(R.id.listener_title);
        playTestBtn = (ImageView)findViewById(R.id.iv_play_bar_play);
        seekBar = (ProgressBar) findViewById(R.id.pb_play_bar);

        lastBtn = (Button) findViewById(R.id.answer_btn_previous);
        nextBtn = (Button) findViewById(R.id.answer_btn_next);

        vipLl = (LinearLayout) findViewById(R.id.vip_only);
        answerRightTv = (TextView) findViewById(R.id.rs_content);

        checkBtn = (Button) findViewById(R.id.listener_chcek_answer);

        typeTv.setText(name);

         //判断是不是vip
        if(role==0){
            //普通用户
            vipLl.setVisibility(View.GONE);
        }else{
            //vip用户
            vipLl.setVisibility(View.VISIBLE);
        }
        format = new SimpleDateFormat("mm:ss");

        final Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.obj == "DB") {
                    initData();
                }
            }
        };
        // 在这里开一个线程执行读数据耗时操作
        new Thread() {
            public void run() {
                if(CurrentQuesstion.getCurrentQuesstion()!=null){
                    list = CurrentQuesstion.getCurrentQuesstion().getQuestionIdFromDB();
                    totalQues = list.size();
                }
                Message msg = handler.obtainMessage();
                msg.obj = "DB";
                handler.sendMessage(msg);
            }
        }.start();
        setListener();
    }

    private void initData(){

        answerRightTv.setText(""); //清空答案
        answerRightTv.setVisibility(View.GONE);
        toolbarTitleTextView.setText("第"+(currentQues+1)+"题");
        voiceTestItem = new VoiceItem();
        isSeekBarChanging=false;//互斥变量，防止进度条与定时器冲突
        currentPosition=0;//当前音乐播放的进度
        testPlayer.reset();//停止播放
        initMediaPlayer(); //初始化媒体播放器
        if(currentQues<list.size()){
            String id = list.get(currentQues).getId();
            hsstDetailPresenter.loadDetail(id,type); //加载当前题目
        }
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HfiblDetailActivity.this.finish();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前音乐在不在播放
                if(currentQues==0){
                    ToastUtils.showShort("已经是第一题了");
                }else{
                    currentQues--;
                    initData();
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
                    initData();
                }
            }
        });

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isEmpty(rightAnswer)) {
                    answerRightTv.setVisibility(View.VISIBLE);
                    String htmlcontent = rightAnswer.replaceAll("font","androidfont");
                    answerRightTv.setText(HtmlUtils.getHtml(getApplicationContext(), answerRightTv, "答案："+rightAnswer));
                }else{
                    ToastUtils.showShort("暂缺答案");
                }
            }
        });

        playTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(voiceTestItem!=null&&!StringUtils.isEmpty(voiceTestItem.getPath())){
                        if(!testPlayer.isPlaying()){  //播放中
                                playTestBtn.setImageResource(R.mipmap.ic_play_bar_btn_pause);
                                    testPlayer.start();
                                    testPlayer.seekTo(currentPosition);
                                    //监听播放时回调函数
                                    timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if (!isSeekBarChanging) {
                                                try{
                                                seekBar.setProgress(testPlayer.getCurrentPosition());
                                                }catch(Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }, 0, 50);
                    }else if(testPlayer.isPlaying()){
                            testPlayer.pause();
                            currentPosition = testPlayer.getCurrentPosition();
                            playTestBtn.setImageResource(R.mipmap.ic_play_bar_btn_play);
                    }
                }else{
                        ToastUtils.showShort("稍等，正在缓存音频！");
                    }
               }
        });

        testPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //播放完毕回调
                playTestBtn.setImageResource(R.mipmap.ic_play_bar_btn_play);
                currentPosition = 0;
                testPlayer.reset();//停止播放
                initMediaPlayer();
            }
        });

    }

    private void initMediaPlayer() {
        try {
            testPlayer.setDataSource(voiceTestItem.getPath());//指定音频文件的路径
            testPlayer.prepare();//让mediaplayer进入准备状态
            testPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                  seekBar.setMax(testPlayer.getDuration());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Runnable mPollTask = new Runnable() {
        public void run() {
            voiceHandler.postDelayed(mPollTask, 300);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSeekBarChanging = false;
        if (testPlayer != null) {
            testPlayer.stop();
            try{
                testPlayer.release();
            }catch(Exception e){
                e.printStackTrace();
            }
            testPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private void downloadMusic(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .addHeader("Accept", "application/json")
                .get()//传参数、文件或者混合，改一下就行请求体就行
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showLong("音频试题获取失败！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    InputStream is = response.body().byteStream();
                    File file = FileHelper.createFile("题目.mp3");
                    String path = file.getAbsolutePath();
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                    voiceTestItem = new VoiceItem();
                    voiceTestItem.setPath(path);
                    voiceTestItem.setUpdate(false);
                    initMediaPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
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
    public void loadSuccess(HQDetail hqDetail) {
        if(hqDetail.getData()!=null){
            if(hqDetail.getData().getTitle()!=null)
            {
                quesTv.setText(hqDetail.getData().getTitle());
            }
            if(hqDetail.getData().getAnswer()!=null){
                rightAnswer = hqDetail.getData().getAnswer();
            }
            if(hqDetail.getData().getFile()!=null){
                String file = Constant.API_DOWNLOAD_FILE+ hqDetail.getData().getFile();
                downloadMusic(file);
            }
        }
    }
}
