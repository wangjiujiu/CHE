package com.qc.language.service.db.user;


import com.blankj.utilcode.util.SPUtils;
import com.qc.language.service.db.DBConstants;
import com.qc.language.service.db.data.UserDetails;

/**
 * 代表当前登录用户
 */
public final class CurrentUser {

    /**
     * 代表当前用户
     */
    private static CurrentUser currentUser;

    private CurrentUser(Build build) {
        this.build = build;
    }

    /**
     * 获得当前用户的单例对象
     *
     * @return
     */
    public static CurrentUser getCurrentUser() {
        if (currentUser == null) {
            currentUser = new CurrentUser.Build().userRepository(new DefaultUserRepository(DBConstants.getBriteDatabase())).build();
        }
        return currentUser;
    }

    private boolean hasLogin = false;  // 是否已经登录

    // 用户对象
    private UserDetails userDetails;

    //是否需要刷新首页菜单
    private boolean refreshCenter = false; //默认false

    public boolean isRefreshSetting() {
        return refreshCenter;
    }

    public void updateRefreshSetting(boolean refreshCenter) {
        this.refreshCenter = refreshCenter;
    }

    /**
     * 设置登录
     * @param userDetails
     */
    public void updateCurrentUser(UserDetails userDetails) {
        this.userDetails = userDetails;

        this.build.sharePrefsUtil.put(LAST_LOGIN_USERNAME, userDetails.getUsername());

        // 记录用户名和密码
        if (this.build.userRepository != null) {
            this.build.userRepository.saveUserDetails(userDetails);
        }
        this.hasLogin = true;
    }



    /**
     * 清空登录信息
     */
    public void removeCurrentUser() {
        this.build.sharePrefsUtil.remove(LAST_LOGIN_USERNAME);

        // 从数据库中清除当前用户信息
        if (this.build.userRepository != null) {
            this.build.userRepository.removeUserDetailsByUsername(currentUser.getUserDetails().getUsername());
        }
        this.hasLogin = false;
    }



    /**
     * 获得上一次的登录用户名
     *
     * @return
     */
    public String getLastLoginUsername() {
        return this.build.sharePrefsUtil.getString(LAST_LOGIN_USERNAME);
    }


    public boolean hasLogin() {
        return hasLogin;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    /**
     * 获取用户详细信息
     */
    public UserDetails getUserDetails(String userName) {
        return this.build.userRepository.getUserDetailsByUsername(userName);
    }

    // 用户生成者
    private Build build;

    private static final String USER_REPOSITORY = "USER_REPOSITORY";
    private static final String LAST_LOGIN_USERNAME = "LAST_LOGIN_USERNAME";

    public static class Build {

        // 用户仓储
        private UserRepository userRepository;

        // 临时的SP仓储
        private SPUtils sharePrefsUtil;

        public Build userRepository(UserRepository userRepository) {
            this.userRepository = userRepository;
            return this;
        }

        public Build sharePrefsUtil(SPUtils sharePrefsUtil) {
            this.sharePrefsUtil = sharePrefsUtil;
            return this;
        }

        public CurrentUser build() {
            if (sharePrefsUtil == null) {
                sharePrefsUtil = SPUtils.getInstance(USER_REPOSITORY);
            }
            return new CurrentUser(this);
        }

    }
}
