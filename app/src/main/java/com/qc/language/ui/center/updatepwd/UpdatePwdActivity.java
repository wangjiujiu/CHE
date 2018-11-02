package com.qc.language.ui.center.updatepwd;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.edittext.ClearEditText;
import com.qc.language.common.view.panterdialog.PanterDialog;
import com.qc.language.common.view.panterdialog.enums.Animation;
import com.qc.language.common.view.panterdialog.interfaces.OnDialogClickListener;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.service.web.WebDataTObject;

import javax.inject.Inject;


public class UpdatePwdActivity extends CommonActivity implements UpdatePwdContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private ClearEditText originPwd;
    private ClearEditText newPwd;
    private ClearEditText confirmPwd;

    private Button submitButton;

    @Inject
    MyApplication application;
    @Inject UpdatePwdPresenter updatePwdPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        toolbarTitleTextView.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("修改密码");

        originPwd = (ClearEditText) findViewById(R.id.updatepwd_origin_edittext);
        newPwd = (ClearEditText) findViewById(R.id.updatepwd_new_edittext);
        confirmPwd = (ClearEditText) findViewById(R.id.updatepwd_confirm_edittext);

        originPwd.setFilters(new InputFilter[]{new MaxTextLengthFilter(16)});
        newPwd.setFilters(new InputFilter[]{new MaxTextLengthFilter(16)});
        confirmPwd.setFilters(new InputFilter[]{new MaxTextLengthFilter(16)});
        submitButton = (Button)findViewById(R.id.updatepwd_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePwd();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePwdActivity.this.finish();
            }
        });

    }

    private void updatePwd() {

        String originpwd = this.originPwd.getText().toString();
        String newpwd = this.newPwd.getText().toString();
        String confirmpwd = this.confirmPwd.getText().toString();

        if(newpwd.length()<6||confirmpwd.length()<6){
            ToastUtils.showLong("输入的密码不少于6位");
        }else{
        updatePwdPresenter.updatePwd(originpwd,newpwd,confirmpwd);
        }

    }

    public class MaxTextLengthFilter implements InputFilter {

        private int mMaxLength;
        //构造方法中传入最多能输入的字数
        public MaxTextLengthFilter(int max) {
            mMaxLength = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            int keep = mMaxLength - (dest.length() - (dend - dstart));
            if (keep < (end - start)) {
                ToastUtils.showShort("最多只能输入" + mMaxLength + "个字");
            }
            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null;
            } else {
                return source.subSequence(start, start + keep);
            }
        }
    }


    @Override
    public void register() {
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        updatePwdPresenter.attachView(this);
    }

    @Override
    public void unRegister() {
        updatePwdPresenter.detachView();
    }

    @Override
    public void updatePwdSuccess(final WebDataTObject updateData) {
//        new PanterDialog(getContext())
//                .setHeaderBackground(R.color.white)
//                .setTitle("提示", 14)
//                .setTitleColor(R.color.colorBlackText)
//                .setMessage("密码修改成功，请点击关闭此页面")
//                .setNegative("取消")
//                .setPositive("确定", new OnDialogClickListener() {
//                    @Override
//                    public void onDialogButtonClicked(PanterDialog dialog) {
//                        //密码修改成功,自动替换存储的密码，下次自动登录
//                        UserDetails userDetails = CurrentUser.getCurrentUser().getUserDetails();
//                        userDetails.setPassword(newPwd.getText().toString());
//                        CurrentUser.getCurrentUser().updateCurrentUser(userDetails);  //新密码
//                        finish();
//                    }
//                })
//                .withAnimation(Animation.SLIDE)
//                .isCancelable(false)
//                .show();
        ToastUtils.showShort("密码修改成功!");
        finish();
    }

}
