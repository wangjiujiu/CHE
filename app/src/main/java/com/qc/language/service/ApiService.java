package com.qc.language.service;

import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.web.WebDataTObject;
import com.qc.language.ui.center.updatepwd.UpdatePwdBody;
import com.qc.language.ui.main.login.LoginBody;
import com.qc.language.ui.question.data.QListData;
import com.qc.language.ui.question.data.QDetail;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * HTTP服务接口,负责与网络层交互
 * Created by beckett on 2018/9/11.
 */
public interface ApiService {


    @POST("common/login")
    Observable<UserDetails> authentication(@HeaderMap Map<String, String> headers, @Body LoginBody loginBody);

    @POST("account/save")
    Observable<WebDataTObject> updatePwd(@HeaderMap Map<String, String> headers, @Body UpdatePwdBody updatePwdBody);

    @GET("exam/loadNums")
    Observable<QListData> hearList(@Query("role") int role, @Query("type") String type);

    @GET("exam/loadDetail")
    Observable<QDetail> hearDetail(@Query("id") String id, @Query("type") String type);
}
