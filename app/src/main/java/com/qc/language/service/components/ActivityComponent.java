package com.qc.language.service.components;

import com.qc.language.service.scope.ActivityScope;
import com.qc.language.ui.question.listener.fibl.HfiblDetailFragment;
import com.qc.language.ui.question.listener.hcs.HhcsDetailFragment;
import com.qc.language.ui.question.listener.hiw.HhiwDetailFragment;
import com.qc.language.ui.question.listener.mcm.HmcmDetailFragment;
import com.qc.language.ui.question.listener.mcs.HmcsDetailFragment;
import com.qc.language.ui.question.listener.smw.HsmwDetailFragment;
import com.qc.language.ui.question.listener.sst.HsstDetailFragment;
import com.qc.language.ui.question.listener.wfd.HwfdDetailFragment;
import com.qc.language.ui.question.read.fibr.RfibrDetailFragment;
import com.qc.language.ui.question.read.fibrw.RfibrwDetailFragment;
import com.qc.language.ui.question.read.mcm.RmcmDetailFragment;
import com.qc.language.ui.question.read.mcs.RmcsDetailFragment;
import com.qc.language.ui.question.read.rp.RrpDetailFragment;
import com.qc.language.ui.question.speak.asq.SAsqDetailFragment;
import com.qc.language.ui.question.speak.di.SdiDetailFragment;
import com.qc.language.ui.question.speak.ra.SraDetailFragment;
import com.qc.language.ui.question.speak.rl.SrlDetailFragment;
import com.qc.language.ui.question.speak.rs.SrsDetailFragment;
import com.qc.language.ui.question.write.swt.WswtDetailFragment;
import com.qc.language.ui.question.write.we.WwfdDetailFragment;
import com.qc.language.ui.splash.SplashActivity;
import com.qc.language.ui.center.updatepwd.UpdatePwdActivity;
import com.qc.language.ui.main.login.UserLoginActivity;
import com.qc.language.ui.question.listener.ListenerListActivity;

import dagger.Component;

/**
 * Created by beckett on 2018/9/22.
 */
@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent {

    SplashActivity inject(SplashActivity splashActivity);
    UpdatePwdActivity inject(UpdatePwdActivity updatePwdActivity);
    UserLoginActivity inject(UserLoginActivity userLoginActivity);


    ListenerListActivity inject(ListenerListActivity listenerListActivity);

    //听
    HsstDetailFragment inject(HsstDetailFragment hsstDetailFragment);
    HmcsDetailFragment inject(HmcsDetailFragment hmcsDetailFragment);
    HmcmDetailFragment inject(HmcmDetailFragment hmcmDetailFragment);
    HfiblDetailFragment inject(HfiblDetailFragment hfiblDetailFragment);
    HhcsDetailFragment inject(HhcsDetailFragment hhcsDetailFragment);
    HsmwDetailFragment inject(HsmwDetailFragment hsmwDetailFragment);
    HwfdDetailFragment inject(HwfdDetailFragment hwfdDetailFragment);
    HhiwDetailFragment inject(HhiwDetailFragment hhiwDetailFragment);

    //说
    SraDetailFragment inject(SraDetailFragment sraDetailFragment);
    SrsDetailFragment inject(SrsDetailFragment srsDetailFragment);
    SdiDetailFragment inject(SdiDetailFragment sdiDetailFragment);
    SrlDetailFragment inject(SrlDetailFragment srlDetailFragment);
    SAsqDetailFragment inject(SAsqDetailFragment sAsqDetailFragment);

    //读
    RmcsDetailFragment inject(RmcsDetailFragment rmcsDetailFragment);
    RmcmDetailFragment inject(RmcmDetailFragment rmcmDetailFragment);
    RrpDetailFragment  inject(RrpDetailFragment rrpDetailFragment);
    RfibrDetailFragment inject(RfibrDetailFragment rfibrDetailFragment);
    RfibrwDetailFragment inject(RfibrwDetailFragment rfibrwDetailFragment);


    //写
    WswtDetailFragment inject(WswtDetailFragment wswtDetailFragment);
    WwfdDetailFragment inject(WwfdDetailFragment wwfdDetailFragment);
}
