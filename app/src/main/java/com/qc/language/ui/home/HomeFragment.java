package com.qc.language.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.qc.language.R;
import com.qc.language.common.fragment.CommonFragment;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.center.updatepwd.UpdatePwdActivity;
import com.qc.language.ui.main.UserMainActivity;
import com.qc.language.ui.main.VipPopWindow;
import com.qc.language.ui.main.login.UserLoginActivity;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by beckett on 2018/9/19.
 */
public class HomeFragment extends CommonFragment {

    private LinearLayout classRl;
    private LinearLayout qrRl;
    private LinearLayout contactRl;
    private LinearLayout timeRl;

    private SaoyisaoPopWindow saoyisaoPopWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_frag_main, container, false);

        classRl = (LinearLayout) root.findViewById(R.id.home_class);
        qrRl = (LinearLayout) root.findViewById(R.id.home_scan);
        contactRl = (LinearLayout) root.findViewById(R.id.home_contact_us);
        timeRl = root.findViewById(R.id.home_frag_main_time);

        RxView.clicks(this.classRl).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(),IntroduceActivity.class);
                        startActivity(intent);
                    }
                });


        RxView.clicks(this.qrRl).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
//                        Intent intent = new Intent();
//                        intent.setClass(getContext(),SaoyisaoActivity.class);
//                        startActivity(intent);
                        showSaoyisaoPopWin(qrRl);
                    }
                });

        RxView.clicks(this.contactRl).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(),TeacherActivity.class);
                        startActivity(intent);
                    }
                });

        RxView.clicks(this.timeRl).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(),TimeActivity.class);
                        startActivity(intent);
                    }
                });

        return root;
    }

//    public void showClassPopWin(View view) {
//        classPopWindow = new ClassPopWindow(getCommonActivity(),onClickListener);
//        classPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        classPopWindow.showAtLocation(getCommonActivity().findViewById(R.id.home_class), Gravity.CENTER, 0, 0);
//    }
//
    public void showSaoyisaoPopWin(View view) {
        saoyisaoPopWindow = new SaoyisaoPopWindow(getCommonActivity(),onClickListener);
        saoyisaoPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        saoyisaoPopWindow.showAtLocation(getCommonActivity().findViewById(R.id.home_scan), Gravity.CENTER, 0, 0);
    }
//
//    //团队简介
//    public void showContactPopWin(View view) {
//        contactPopWindow = new ContactPopWindow(getCommonActivity(),onClickListener);
//        contactPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        contactPopWindow.showAtLocation(getCommonActivity().findViewById(R.id.home_contact_us), Gravity.CENTER, 0, 0);
//    }
//
//
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.dialog_class_close:
//                    classPopWindow.dismiss();
//                    break;
                case R.id.dialog_saoyisao_close:
                    saoyisaoPopWindow.dismiss();
                    break;
//                case R.id.dialog_contact_close:
//                    contactPopWindow.dismiss();
//                    break;
            }
        }
    };
}
