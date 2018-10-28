package com.qc.language.ui.question.speak.asq.list;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.framework.sound.SoundMeter;
import com.qc.language.framework.sound.VoiceItem;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.common.utils.FileHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 短问答给出一个简短的答案
 * Created by beckett on 2018/10/11.
 */
public class AsqDetailActivity extends CommonActivity implements AsqDetailContract.View{

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

    private TextView originalContentBtn;  //显示原文


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

    //是否在播放题目
    private LinearLayout playTestLl;
    private ImageView playTestBtn;
    private ProgressBar seekBar;  //进度条
    private VoiceItem voiceTestItem ;
    private Timer timer;
    private boolean isSeekBarChanging=false;//互斥变量，防止进度条与定时器冲突
    private int currentPosition=0;//当前音乐播放的进度
    SimpleDateFormat format;
    MediaPlayer testPlayer = new MediaPlayer();
    @Inject
    AsqDetailPresenter rsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rs_detail_act);

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

        answerRl = (RelativeLayout) findViewById(R.id.rs_vip_btn);
        recordBtn = (ImageView) findViewById(R.id.rs_vip_recorded);
        playBtn = (ImageView) findViewById(R.id.rs_vip_player);
        originalContentBtn = (TextView) findViewById(R.id.rs_vip_article);
        playTestLl = (LinearLayout) findViewById(R.id.layout_play_voice);
        playTestBtn = (ImageView)findViewById(R.id.iv_play_bar_play);
        seekBar = (ProgressBar) findViewById(R.id.pb_play_bar);
        if(IsVip){
            answerRl.setVisibility(View.VISIBLE);
            recordBtn.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.VISIBLE);
            originalContentBtn.setVisibility(View.VISIBLE);
        }else{
            answerRl.setVisibility(View.VISIBLE);
            recordBtn.setVisibility(View.GONE);
            playBtn.setVisibility(View.GONE);
            originalContentBtn.setVisibility(View.VISIBLE);
        }
        titleTv = (TextView) findViewById(R.id.rs_title);
        typeTv = (TextView) findViewById(R.id.rs_type);
        typeTv.setText("ASQ");
        wordsTv = (TextView) findViewById(R.id.rs_words);
        numTv = (TextView) findViewById(R.id.rs_num);
        dateTv = (TextView) findViewById(R.id.rs_date);
        contentTv = (TextView) findViewById(R.id.rs_content);

        format = new SimpleDateFormat("mm:ss");
        downloadMusic("http://ws.stream.qqmusic.qq.com/M500001VfvsJ21xFqb.mp3?guid=ffffffff82def4af4b12b3cd9337d5e7&uin=346897220&vkey=6292F51E1E384E06DCBDC9AB7C49FD713D632D313AC4858BACB8DDD29067D3C601481D36E62053BF8DFEAF74C0A5CCFADD6471160CAF3E6A&fromtag=46");

        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsqDetailActivity.this.finish();
            }
        });

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentQues<2){
                }else{
                    currentQues--;
                    toolbarTitleTextView.setText("第"+currentQues+"题");
                    voiceItem = new VoiceItem();
                    voiceTestItem = new VoiceItem();
                    testPlayer.reset();//停止播放
                    initMediaPlayer();
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
                    voiceTestItem = new VoiceItem();
                    testPlayer.reset();//停止播放
                    initMediaPlayer();
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


        playTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlayPressing&&!isVoicePressing){
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
                                                seekBar.setProgress(testPlayer.getCurrentPosition());
                                            }
                                        }
                                    }, 0, 50);
                    }else if(testPlayer.isPlaying()){
                            testPlayer.pause();
                            currentPosition = testPlayer.getCurrentPosition();
                            playTestBtn.setImageResource(R.mipmap.ic_play_bar_btn_play);
                    }
                }else{
                        ToastUtils.showShort("正在缓存音频！");
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSeekBarChanging = true;
        if (testPlayer != null) {
            testPlayer.stop();
            testPlayer.release();
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
                ToastUtils.showLong("音频文件获取失败！");
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
}
