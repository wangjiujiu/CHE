package com.qc.language.service.modules;

import com.qc.language.app.MyApplication;
import com.qc.language.service.ApiService;
import com.qc.language.service.Constant;
import com.qc.language.service.scope.ApplicationScope;
import com.qc.language.service.web.TokenIntercepter;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiServiceModule {

    @Provides @ApplicationScope
    public static OkHttpClient provideOkHttpClient(final MyApplication application) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new TokenIntercepter());
        return builder.build();
    }

    @Provides @ApplicationScope public static ApiService provideApiService(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE_URI)//基本Url
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        return retrofit.create(ApiService.class);
    }



}
