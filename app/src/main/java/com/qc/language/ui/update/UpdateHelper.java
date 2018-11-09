package com.qc.language.ui.update;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.view.panterdialog.PanterDialog;
import com.qc.language.common.view.panterdialog.enums.Animation;
import com.qc.language.common.view.panterdialog.interfaces.OnDialogClickListener;
import com.qc.language.service.Constant;
import com.qc.language.ui.update.downloadmanager.DownloadManagerHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 */
public class UpdateHelper {

    private static String UpdateData;
    private static HashMap<String, String> hm;

    private static String filePath;

    private static void deleteFilesByDirectory(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFilesByDirectory(childFiles[i]);
            }
            file.delete();
        }
    }
    /**
     * 检查更新
     *
     * @param showToast 如果不需要更新 ，是否提示
     * @param context 上下文
     * @param activity 当前activity
     */
    public static void doUpdate(final boolean showToast, final Context context, final Activity activity){

        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/che/che.apk";
        File file = new File(filePath);
        if (file.exists()) {
            deleteFilesByDirectory(file);
        }
        OkHttpClient httpCient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.WEB_UPDATE_URL)
                .build();
        Call call = httpCient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtils.showLong("检查更新失败！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseMsg = response.body().string();

                ParseXmlService ps = new ParseXmlService();
                InputStream Is = null;
                hm = new HashMap<String, String>();
                String StartSign = "<version_info>";
                String EndSign = "</version_info>";
                try{
                    UpdateData = responseMsg.substring(responseMsg.indexOf(StartSign),responseMsg.indexOf(EndSign)+EndSign.length());
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (UpdateData != null && !UpdateData.equals("")){
                    Is = new ByteArrayInputStream(UpdateData.getBytes());
                    try{
                        hm = ps.parseXml(Is);
                        int NewVersionCode = Integer.parseInt(hm.get("VersionCode")
                                .toString());
                        int OldVersionCode = VersionUtil.getVersionCode(context);
                        if (NewVersionCode > OldVersionCode) {

                            // 跳转到更新页面
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    final String name = hm.get("VersionName").toString();
                                    final String msg = hm.get("VersionDescription")
                                            .toString();
                                    new PanterDialog(activity)
                                            .setHeaderBackground(R.color.white)
                                            .setTitle("系统更新", 14)
                                            .setTitleColor(R.color.colorBlackText)
                                            .setMessage("发现新版本!版本名称:" + name + ",是否需要更新 ?")
                                            .setNegative("下次再说")
                                            .setPositive("立即更新", new OnDialogClickListener() {
                                                @Override
                                                public void onDialogButtonClicked(PanterDialog dialog) {
                                                    DownloadManagerHelper helper = new DownloadManagerHelper(context);
                                                    helper.updateForNotification(context, Constant.WEB_UPDATE_APK_URL, filePath);
                                                }
                                            }).withAnimation(Animation.SLIDE)
                                            .show();
                                }
                            });

                        }else {
                            if (showToast) {
                                // 跳转到更新页面
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showLong("当前版本已是最新版本");
                                    }
                                });
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
