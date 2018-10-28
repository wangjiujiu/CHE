package com.qc.language.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.qc.language.R;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.gre.GreFragment;
import com.qc.language.ui.center.CenterFragment;
import com.qc.language.ui.home.HomeFragment;
import com.qc.language.ui.main.login.UserLoginActivity;
import com.qc.language.ui.vip.VipFragment;
import com.qc.language.common.utils.DoubleClickExitHelper;
import com.qc.language.common.view.bottomnavigationviewex.BottomNavigationViewEx;
import com.qc.language.common.view.bottomnavigationviewex.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单界面
 * Created by beckett on 2018/9/19.
 */
public class UserMainActivity extends CommonActivity{

    private DoubleClickExitHelper doubleClickExitHelper;

    private MainVpAdpater adapter;
    private BottomNavigationViewEx bnve;
    private NoScrollViewPager vp;

    // Fragment管理工具
    private SparseIntArray items;
    private List<Fragment> fragments;

    private VipPopWindow tipPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermain_activity);

        bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        vp = (NoScrollViewPager) findViewById(R.id.vp);
        vp.setScanScroll(false);
        vp.setOffscreenPageLimit(3);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);

        initFragments();
        setListener();
        bnve.setCurrentItem(0);
        doubleClickExitHelper = new DoubleClickExitHelper(this); // 注册双击退出事件
    }

    private void setListener() {
        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = 0;
                resetToDefaultIcon();//重置到默认不选中图片
                switch (item.getItemId()) {
                    case R.id.bbn_home_item:
                        id = 0;
                        item.setIcon(R.mipmap.menu_home_clicked);
                        break;
                    case R.id.bbn_gre_item:
                        id = 1;
                        item.setIcon(R.mipmap.menu_gre_clicked);
                        break;
                    case R.id.bbn_vip_item:
                        id = 2;
                        item.setIcon(R.mipmap.menu_vip_clicked);
                        break;
                    case R.id.bbn_center_item:
                        id = 3;
                        item.setIcon(R.mipmap.menu_center_clicked);
                        break;
                }
                if(previousPosition != id) {
                    if(id==2&&CurrentUser.getCurrentUser().hasLogin()){
                        //vip栏目可见
                        vp.setCurrentItem(id, false);
                    }else if(id==2&&!CurrentUser.getCurrentUser().hasLogin()){
                        showPopWin(item.getActionView());  //展示弹窗
                        return false;
                    }else{
                        vp.setCurrentItem(id, false);
                    }
                    previousPosition = id;
                }
                return true;
            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bnve.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化Fragments, Fragments交由FragmentManager进行管理
     */
    private void initFragments() {

        fragments = new ArrayList<>();
        items = new SparseIntArray(5);

        GreFragment greFragment = new GreFragment();
        CenterFragment centerFragment = new CenterFragment();
        HomeFragment homeFragment = new HomeFragment();
        VipFragment vipFragment = new VipFragment();

        fragments.add(homeFragment);
        fragments.add(greFragment);
        fragments.add(vipFragment);
        fragments.add(centerFragment);

        items.put(R.id.bbn_home_item, 0);
        items.put(R.id.bbn_gre_item, 1);
        items.put(R.id.bbn_vip_item, 2);
        items.put(R.id.bbn_center_item, 3);

        adapter = new MainVpAdpater(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        bnve.setupWithViewPager(vp);
    }

    private void resetToDefaultIcon() {
        MenuItem home =  bnve.getMenu().findItem(R.id.bbn_home_item);
        home.setIcon(R.mipmap.menu_home);

        MenuItem gre =  bnve.getMenu().findItem(R.id.bbn_gre_item);
        gre.setIcon(R.mipmap.menu_gre);

        MenuItem vip =  bnve.getMenu().findItem(R.id.bbn_vip_item);
        vip.setIcon(R.mipmap.menu_vip);

        MenuItem center =  bnve.getMenu().findItem(R.id.bbn_center_item);
        center.setIcon(R.mipmap.menu_center);

    }

    public void showPopWin(View view) {
        tipPopWindow = new VipPopWindow(UserMainActivity.this,onClickListener);
        tipPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tipPopWindow.showAtLocation(findViewById(R.id.usermain_main_view), Gravity.CENTER, 0, 0);
    }

    //搜索框里的点击处理
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_tip_sure:
                    //登录操作
                    Intent intent = new Intent();
                    intent.setClass(getContext(), UserLoginActivity.class);
                    startActivity(intent);
                    tipPopWindow.dismiss();
                    break;
                case R.id.dialog_tip_close:
                    tipPopWindow.dismiss();
                    break;
            }
        }
    };

    /**
     * 双击退出APP事件
     *
     * @param keyCode 键盘keyCode
     * @param event   键盘事件
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return doubleClickExitHelper.onKeyDown(keyCode, event);
    }
}
