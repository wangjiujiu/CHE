package com.qc.language.ui.question.speak.di.list;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.framework.sound.SoundMeter;
import com.qc.language.framework.sound.VoiceItem;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.common.utils.FileHelper;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

/**
 * 朗读句子，免费版只显示题目，收费版显示答题区，没有全文和音频播放区域
 * Created by beckett on 2018/10/11.
 */
public class DIDetailActivity extends CommonActivity implements DIDetailContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private int currentQues;
    private int totalQues;

    private Button lastBtn;
    private Button nextBtn;

    private boolean IsVip = true;
    private RelativeLayout answerRl;

    private ImageView recordBtn;
    private String recordState="录音"; //录音，不能录音，结束
    private boolean isVoicePressing = false;

    private ImageView playBtn;
    private String playState="播放";  //播放，停止，不能点击状态
    private boolean isPlayPressing = false;

    private ImageView subjectIv;


    //考题内容+答案显示
    private TextView titleTv;
    private TextView typeTv;
    private TextView wordsTv;
    private TextView numTv;
    private TextView dateTv;
    private TextView contentTv;

    private Handler voiceHandler = new Handler();
    private SoundMeter voiceSensor = new SoundMeter();

    private String voiceName;
    private VoiceItem voiceItem;

    @Inject
    DIDetailPresenter rsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.di_detail_act);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        currentQues = getIntent().getIntExtra("currentQues",0);
        totalQues = getIntent().getIntExtra("totalQues",0);
        toolbarTitleTextView.setText("第"+currentQues+"题");

        lastBtn = (Button) findViewById(R.id.answer_btn_previous);
        nextBtn = (Button) findViewById(R.id.answer_btn_next);
        
        //答題
        answerRl = (RelativeLayout) findViewById(R.id.rs_vip_btn);
        recordBtn = (ImageView) findViewById(R.id.rs_vip_recorded);
        playBtn = (ImageView) findViewById(R.id.rs_vip_player);
        if(IsVip){
            answerRl.setVisibility(View.VISIBLE);
        }else{
            answerRl.setVisibility(View.GONE); //没有答题区
        }
        titleTv = (TextView) findViewById(R.id.rs_title);
        typeTv = (TextView) findViewById(R.id.rs_type);
        typeTv.setText("DI");
        wordsTv = (TextView) findViewById(R.id.rs_words);
        numTv = (TextView) findViewById(R.id.rs_num);
        dateTv = (TextView) findViewById(R.id.rs_date);
        contentTv = (TextView) findViewById(R.id.rs_content);
        subjectIv = (ImageView) findViewById(R.id.di_detail_image);
        //假数据
        contentTv.setText("");
        subjectIv.setImageResource(R.mipmap.ic_launcher);

        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DIDetailActivity.this.finish();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues<2){
                }else{
                    currentQues--;
                    toolbarTitleTextView.setText("第"+currentQues+"题");
                    voiceItem = new VoiceItem();  //刷新整个答题部分的UI
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues<totalQues){
                    currentQues++;
                    toolbarTitleTextView.setText("第"+currentQues+"题");
                    voiceItem = new VoiceItem();
                }
            }
        });

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //是否按了播放
                if(!isPlayPressing){
                    String voicestate=recordState;
                    if (voicestate.equals("录音")) {
                        final File recodFile = FileHelper.createFile("录音.amr");
                        voiceName = recodFile.getAbsolutePath();
                        start(voiceName);
                        isVoicePressing = true;
                        recordState = "结束录音";
                        recordBtn.setImageResource(R.mipmap.ic_micorphone_press);
                        playBtn.setImageResource(R.mipmap.ic_player_disabale);
                    } else if (voicestate.equals("结束录音")){
                        stop();  // 停止录音
                        voiceItem = new VoiceItem();
                        voiceItem.setPath(voiceName);
                        voiceItem.setUpdate(false);
                        isVoicePressing = false;
                        recordState = "录音";
                        recordBtn.setImageResource(R.mipmap.ic_micorphone);
                        playBtn.setImageResource(R.mipmap.ic_player);
                    }
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVoicePressing){
                    return;
                }else{
                    if(playState.equals("播放")){  //播放中
                        if(voiceItem!=null&&!StringUtils.isEmpty(voiceItem.getPath())){
                            playBtn.setImageResource(R.mipmap.ic_player_press);
                            recordBtn.setImageResource(R.mipmap.ic_micorphone_disable);
                            isPlayPressing = true;
                            playState = "停止";
                            MediaPlayer player = new MediaPlayer();
                            try {
                                player.setDataSource(voiceItem.getPath());
                                player.prepare();
                                player.start();
                                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        //播放完毕回调
                                        playBtn.setImageResource(R.mipmap.ic_player);
                                        recordBtn.setImageResource(R.mipmap.ic_micorphone);
                                        playState = "播放";
                                        isPlayPressing = false;
                                    }
                                });
                            } catch (IOException e) {
                                playBtn.setImageResource(R.mipmap.ic_player);
                                playState = "播放";
                                recordBtn.setImageResource(R.mipmap.ic_micorphone);
                                isPlayPressing = false;
                            }
                        }
                    }
                }
            }
        });
    }

    private void start(String name) {
        voiceSensor.start(name);
        voiceHandler.postDelayed(mPollTask, 300);
    }

    private void stop() {
        voiceHandler.removeCallbacks(mPollTask);
        voiceSensor.stop();
    }

    private Runnable mPollTask = new Runnable() {
        public void run() {
            voiceHandler.postDelayed(mPollTask, 300);
        }
    };


    @Override
    public void register() {
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        rsDetailPresenter.attachView(this);
    }

    @Override
    public void unRegister() {
        rsDetailPresenter.detachView();
    }
}
