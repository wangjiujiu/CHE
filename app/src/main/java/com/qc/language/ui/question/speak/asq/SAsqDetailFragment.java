package com.qc.language.ui.question.speak.asq;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.common.utils.FileHelper;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.framework.sound.SoundMeter;
import com.qc.language.framework.sound.VoiceItem;
import com.qc.language.service.Constant;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.question.data.QDetail;
import com.qc.language.ui.question.data.Question;
import com.qc.language.ui.question.speak.ra.RADetailContract;
import com.qc.language.ui.question.speak.ra.RADetailPresenter;
import com.squareup.picasso.Picasso;

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
 * 说，题目音频 答题是能录音 能播放给自己听 答案是原文
 */
public class SAsqDetailFragment extends CommonFragment implements AsqDetailContract.View{


    private String name;
    private int role;
    private String type;
    private int currentQues;
    private String id;
    private List<Question> list = new ArrayList<>();
    private TextView answerRightTv;  //显示原文


    //考题内容
    private TextView quesTv;
    private TextView typeTv;
    //是否在播放题目
    private ImageView playTestBtn;
    private ProgressBar seekBar;  //进度条
    private VoiceItem voiceTestItem ;
    private String attachmentFile="";
    private Timer timer;
    private boolean isSeekBarChanging=false;//互斥变量，防止进度条与定时器冲突
    private int currentPosition=0;//当前音乐播放的进度
    SimpleDateFormat format;
    MediaPlayer testPlayer = new MediaPlayer();

    //vip区域
    private int userType;
    private LinearLayout vipLl;
    private String rightAnswer;
    private Button checkBtn;

    private RelativeLayout answerRl;
    private ImageView recordBtn;
    private String recordState="录音"; //录音，不能录音，结束
    private boolean isVoicePressing = false;

    private ImageView playBtn;
    private String playState="播放";  //播放，停止，不能点击状态
    private boolean isPlayPressing = false;

    private String voiceName;
    private VoiceItem voiceItem;

    private Handler voiceHandler;
    private SoundMeter voiceSensor;


    @Inject
    AsqDetailPresenter hsstDetailPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.speak_asq_detail, container, false);


        //题目区
        typeTv = (TextView) parentView.findViewById(R.id.listener_type);
        quesTv = (TextView) parentView.findViewById(R.id.listener_title);

        playTestBtn = (ImageView)parentView.findViewById(R.id.iv_play_bar_play);
        seekBar = (ProgressBar) parentView.findViewById(R.id.pb_play_bar);

        vipLl = (LinearLayout) parentView.findViewById(R.id.vip_only);
        answerRightTv = (TextView) parentView.findViewById(R.id.rs_content);
        checkBtn = (Button) parentView.findViewById(R.id.listener_chcek_answer);

        answerRl = (RelativeLayout) parentView.findViewById(R.id.rs_vip_btn);
        recordBtn = (ImageView) parentView.findViewById(R.id.rs_vip_recorded);
        playBtn = (ImageView) parentView.findViewById(R.id.rs_vip_player);

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
                answerRl.setVisibility(View.VISIBLE);

            }else{
                userType = 0;
                vipLl.setVisibility(View.GONE);
                answerRl.setVisibility(View.GONE);

            }
            format = new SimpleDateFormat("mm:ss");
            voiceHandler = new Handler();
            voiceSensor = new SoundMeter();

            hsstDetailPresenter.loadDetail(id,type);

            setListener();
        }

    }


    private void setListener() {

        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //显示原文
                if(!StringUtils.isEmpty(rightAnswer)) {
                    answerRightTv.setVisibility(View.VISIBLE);
                    String htmlcontent = rightAnswer.replaceAll("font","androidfont");
                    answerRightTv.setText(HtmlUtils.getHtml(getCommonActivity(), answerRightTv, "原文："+htmlcontent));
                }else{
                    ToastUtils.showShort("暂缺答案");
                }
            }
        });

        playTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPlayPressing&&!isVoicePressing){
                if(voiceTestItem!=null){
                    if(!StringUtils.isEmpty(voiceTestItem.getPath())){
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
                        ToastUtils.showShort("没有音频文件！");
                    }
                }else{
                        ToastUtils.showShort("稍等，正在缓存音频！");
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

    @Override
    public void onDestroy() {
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


    private void downloadMusic(final String url, final String fileName){
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
                    File file = FileHelper.createFile(fileName);
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
    public void loadSuccess(QDetail hqDetail) {
        if(hqDetail.getData()!=null){
            if(hqDetail.getData().getTitle()!=null)
            {
                quesTv.setText(hqDetail.getData().getTitle()); //标题
            }

            if(hqDetail.getData().getAnswer()!=null){
                rightAnswer = hqDetail.getData().getAnswer();
            }
                if (hqDetail.getData().getFile() != null && !StringUtils.isEmpty(hqDetail.getData().getFile())) {
                    String file = Constant.API_DOWNLOAD_FILE + hqDetail.getData().getFile();
                    final File voiceFile = new File(Environment.getExternalStorageDirectory().getPath() + "/QCYY/AudioRecord", hqDetail.getData().getFile());
                    if (!voiceFile.exists()) {
                        downloadMusic(file, hqDetail.getData().getFile());
                    } else {
                        //文件存在
                        String path = voiceFile.getAbsolutePath();
                        voiceTestItem = new VoiceItem();
                        voiceTestItem.setPath(path);
                        voiceTestItem.setUpdate(false);
                        initMediaPlayer();
                    }
                } else {
                    voiceTestItem = new VoiceItem();
                    voiceTestItem.setPath("");
                    initMediaPlayer();
                }
        }
    }
}
