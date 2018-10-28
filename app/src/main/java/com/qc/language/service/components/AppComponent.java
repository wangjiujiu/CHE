package com.qc.language.service.components;


import com.qc.language.app.MyApplication;
import com.qc.language.service.ApiService;
import com.qc.language.service.modules.ApiServiceModule;
import com.qc.language.service.modules.AppModule;
import com.qc.language.service.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by beckett on 2018/9/11
 */

@ApplicationScope
@Component(modules = {AppModule.class, ApiServiceModule.class})
public interface AppComponent {

    MyApplication getApplication();

    ApiService getApiService();
}
