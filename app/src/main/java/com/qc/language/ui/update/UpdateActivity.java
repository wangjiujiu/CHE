package com.qc.language.ui.update;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.service.Constant;

import java.io.File;

public class UpdateActivity extends Activity {

    private TextView upgrade_downloading_version;
    private ProgressBar upgrade_pb;
    private TextView upgrade_progress_tv;

    private String versionName;
    private String versionUrl;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_act);

        versionName = "";
        versionUrl = "";
        filePath = "";

        upgrade_downloading_version = (TextView) findViewById(R.id.upgrade_downloading_version);
        upgrade_pb = (ProgressBar) findViewById(R.id.upgrade_pb);
        upgrade_progress_tv = (TextView) findViewById(R.id.upgrade_progress_tv);

        versionName = getIntent().getStringExtra("VersionName");
        versionUrl = Constant.WEB_UPDATE_APK_URL;

        upgrade_pb.setMax(100);
        upgrade_downloading_version.setText("正在下载安装包:");

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + versionName + ".apk";
        }

        FileAsyncTask task = new FileAsyncTask(getApplicationContext(), this, filePath, versionUrl,
                upgrade_progress_tv, upgrade_pb, null);
        task.execute(versionUrl);
    }


    public void success() {
        try {
            File file = new File(filePath);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);

            Uri uri = Uri.fromFile(file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");

            getApplicationContext().startActivity(intent);
        } catch (Exception e) {
        } finally {
            finish();
        }
    }

    public void failure() {
        ToastUtils.showLong("更新版本出错,请稍后再试!");
        finish();
    }
}
