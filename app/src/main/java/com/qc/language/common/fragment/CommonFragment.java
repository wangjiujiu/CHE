package com.qc.language.common.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.qc.language.common.IRegister;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.contract.CommonContract;
import com.qc.language.common.view.progressdialog.ZLoadingDialog;
import com.qc.language.common.view.progressdialog.Z_TYPE;


/**
 * 封装的基础CommonFragment, 需要和CommonActivity配合使用
 */
public abstract class CommonFragment extends Fragment implements IRegister,CommonContract.CommonView{

    private ZLoadingDialog progressDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register();
    }

    public CommonActivity getCommonActivity() {
        return (CommonActivity) getActivity();
    }

    public Context getContext() {
        return getCommonActivity().getContext();
    }


    @Override
    public void register() {
    }

    @Override
    public void unRegister() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unRegister();
    }

    public void showProgress() {
        if (progressDialog == null) {
//            progressDialog = new MaterialDialog.Builder(getContext())
//                    .content("系统正在处理，请稍后")
//                    .progress(true, 0)
//                    .progressIndeterminateStyle(true)
//                    .cancelable(false)
//                    .build();

            progressDialog = new ZLoadingDialog(getContext());
            progressDialog .setLoadingBuilder(Z_TYPE.valueOf("SINGLE_CIRCLE"))
                    .setHintText("加载中……")
                    .setHintTextColor(Color.WHITE)
                    .setCanceledOnTouchOutside(false)
                    .setDialogBackgroundColor(Color.parseColor("#cc111111")) // 设置背景色
                    .show();
        }
    }

    /**
     * 取消进度显示器显示
     */
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
