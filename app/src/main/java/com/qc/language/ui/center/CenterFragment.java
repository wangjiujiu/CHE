package com.qc.language.ui.center;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.qc.language.R;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.common.view.panterdialog.PanterDialog;
import com.qc.language.common.view.panterdialog.enums.Animation;
import com.qc.language.common.view.panterdialog.interfaces.OnDialogClickListener;
import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.center.updatepwd.UpdatePwdActivity;
import com.qc.language.ui.main.UserMainActivity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by beckett on 2018/9/19.
 */
public class CenterFragment extends CommonFragment {

    private TextView statusTv;
    private TextView cStatusTv;
    private TextView userNameTv;
    private TextView nameTv;
    private TextView phoneTv;
    private RelativeLayout changeRl;
    private TextView endTime;

    private boolean isfirst = true;
    private boolean isforward = false;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.center_frag_main, container, false);

        statusTv = (TextView) root.findViewById(R.id.setting_status);
        cStatusTv = (TextView) root.findViewById(R.id.setting_chinese_status);
        userNameTv = (TextView) root.findViewById(R.id.setting_username);
        nameTv = (TextView) root.findViewById(R.id.setting_name);
        phoneTv = (TextView) root.findViewById(R.id.setting_cellphone);
        changeRl = (RelativeLayout) root.findViewById(R.id.center_frag_pwd);
        endTime = (TextView) root.findViewById(R.id.setting_endTime);

        //当前状态登录了
        initView();
        return root;
    }

    private void initView(){
        if(CurrentUser.getCurrentUser().hasLogin()&&CurrentUser.getCurrentUser().getUserDetails()!=null){
            statusTv.setText("Hi,Vip");
            UserDetails userDetails = CurrentUser.getCurrentUser().getUserDetails();
            if(userDetails.getUsername()!=null){
                userNameTv.setText(userDetails.getUsername());
            }

            if(userDetails.getName()!=null){
                cStatusTv.setText(userDetails.getName());
                nameTv.setText(userDetails.getName());
            }

            if(userDetails.getCellphone()!=null){
                phoneTv.setText(userDetails.getCellphone());
            }

            if(userDetails.getEndTime()!=null){
                endTime.setText(userDetails.getEndTime());
            }
        }

        RxView.clicks(this.changeRl).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(getCommonActivity(), UpdatePwdActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        isforward = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isforward = true;
        if (isfirst) {
            isfirst = false;
        } else {
            if(CurrentUser.getCurrentUser().isRefreshSetting()){
                 CurrentUser.getCurrentUser().updateRefreshSetting(false);
                 initView();
            }
        }
    }


}
