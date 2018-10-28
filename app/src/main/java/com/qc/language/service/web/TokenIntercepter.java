package com.qc.language.service.web;


import com.blankj.utilcode.util.StringUtils;
import com.qc.language.service.db.user.CurrentUser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Token默认的拦截器
 */
public class TokenIntercepter implements Interceptor {

    private static final String TOKEN_KEY = "token";

    @Override public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        CurrentUser currentUser = CurrentUser.getCurrentUser();
        if (currentUser.hasLogin() && currentUser.getUserDetails().getToken() != null) {
            //如果有TokenHeader的话就不加Token的Header
            if (StringUtils.isSpace(originalRequest.header(TOKEN_KEY))) {
                // 如果已经登录
                Request authorised = originalRequest.newBuilder()
                        .header(TOKEN_KEY, currentUser.getUserDetails().getToken().toString())
                        .build();
                return chain.proceed(authorised);
            }
        }
        return chain.proceed(originalRequest);
    }
}
