package com.qc.language.service.modules;


import com.qc.language.app.MyApplication;
import com.qc.language.service.scope.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MyApplication application;

    public AppModule(MyApplication application) {
        this.application = application;
    }

    @Provides @ApplicationScope
    public MyApplication provideApplication() {
        return application;
    }
}
