package com.qc.language.ui.question.listener.sst;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.common.utils.FileHelper;
import com.qc.language.common.view.textview.html.HtmlUtils;
import com.qc.language.framework.sound.VoiceItem;
import com.qc.language.service.Constant;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.question.CurrentQuesstion;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.question.data.QDetail;
import com.qc.language.ui.question.data.Question;

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
 * 听内容概括段意 Summarize spoken text
 */
public class HsstDetailFragment extends CommonFragment implements HsstDetailContract.View{


    private String name;
    private int role;
    private String type;
    private int currentQues;
    private String id;
    private TextView answerRightTv;  //显示原文


    private int userType;
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
    private EditText myAnswerEt;
    private String rightAnswer;
    private Button checkBtn;

    private String scriptContent;
    private TextView scriptTv;
    private Button scriptBtn;

    @Inject HsstDetailPresenter hsstDetailPresenter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.listener_sst_detail_frag, container, false);

        //题目区
        typeTv = (TextView) parentView.findViewById(R.id.listener_type);
        quesTv = (TextView) parentView.findViewById(R.id.listener_title);
        playTestBtn = (ImageView)parentView.findViewById(R.id.iv_play_bar_play);
        seekBar = (ProgressBar) parentView.findViewById(R.id.pb_play_bar);

        vipLl = (LinearLayout) parentView.findViewById(R.id.vip_only);
        answerRightTv = (TextView) parentView.findViewById(R.id.rs_content);
        myAnswerEt = (EditText) parentView.findViewById(R.id.listener_fill_answer);
        checkBtn = (Button) parentView.findViewById(R.id.listener_chcek_answer);

        scriptBtn = parentView.findViewById(R.id.listener_chcek_script);
        scriptBtn.setVisibility(View.VISIBLE);
        scriptTv = parentView.findViewById(R.id.rs_script);

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
                //普通用户
                userType = 1;
                vipLl.setVisibility(View.VISIBLE);
            }else{
                userType = 0;
                vipLl.setVisibility(View.GONE);
            }
            format = new SimpleDateFormat("mm:ss");

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
                }else{
                    ToastUtils.showShort("暂缺答案");
                }
            }
        });

        scriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!StringUtils.isEmpty(scriptContent)) {
                    scriptTv.setVisibility(View.VISIBLE);
                    String htmlcontent = scriptContent.replaceAll("font","androidfont");
                    scriptTv.setText(HtmlUtils.getHtml(getCommonActivity(), scriptTv, "原文："+htmlcontent));
                }else{
                    ToastUtils.showShort("暂缺原文");
                }
            }
        });

        playTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                quesTv.setText(hqDetail.getData().getTitle());
            }
            if(hqDetail.getData().getAnswer()!=null){
                rightAnswer = hqDetail.getData().getAnswer();
            }

            if(hqDetail.getData().getOrigin()!=null){
                scriptContent = hqDetail.getData().getOrigin();
            }

            if(hqDetail.getData().getFile()!=null&&!StringUtils.isEmpty(hqDetail.getData().getFile())){
                String file = Constant.API_DOWNLOAD_FILE+ hqDetail.getData().getFile();
                final File voiceFile = new File(Environment.getExternalStorageDirectory().getPath() + "/QCYY/AudioRecord", hqDetail.getData().getFile());
                if (!voiceFile.exists()) {
                downloadMusic(file,hqDetail.getData().getFile());
                }else{
                   //文件存在
                    String path = voiceFile.getAbsolutePath();
                    voiceTestItem = new VoiceItem();
                    voiceTestItem.setPath(path);
                    voiceTestItem.setUpdate(false);
                    initMediaPlayer();
               }
            }else{
                voiceTestItem = new VoiceItem();
                voiceTestItem.setPath("");
                initMediaPlayer();
            }
        }
    }

}
