package com.qc.language.service.components;

import com.qc.language.service.scope.ActivityScope;
import com.qc.language.ui.question.listener.fibl.HfiblDetailActivity;
import com.qc.language.ui.question.listener.hcs.HhcsDetailActivity;
import com.qc.language.ui.question.listener.hiw.HhiwDetailActivity;
import com.qc.language.ui.question.listener.mcm.HmcmDetailActivity;
import com.qc.language.ui.question.listener.mcs.HmcsDetailActivity;
import com.qc.language.ui.question.listener.smw.HsmwDetailActivity;
import com.qc.language.ui.question.listener.wfd.HwfdDetailActivity;
import com.qc.language.ui.question.write.swt.WswtDetailActivity;
import com.qc.language.ui.splash.SplashActivity;
import com.qc.language.ui.center.updatepwd.UpdatePwdActivity;
import com.qc.language.ui.main.login.UserLoginActivity;
import com.qc.language.ui.question.listener.ListenerListActivity;
import com.qc.language.ui.question.listener.sst.HsstDetailActivity;
import com.qc.language.ui.question.read.fibr.list.FibrDetailActivity;
import com.qc.language.ui.question.read.fibr.list.FibrListActivity;
import com.qc.language.ui.question.read.fibrw.list.FibrwDetailActivity;
import com.qc.language.ui.question.read.fibrw.list.FibrwListActivity;
import com.qc.language.ui.question.read.mar.list.MarDetailActivity;
import com.qc.language.ui.question.read.mar.list.MarListActivity;
import com.qc.language.ui.question.read.rp.list.RpDetailActivity;
import com.qc.language.ui.question.read.rp.list.RpListActivity;
import com.qc.language.ui.question.read.sar.list.SarDetailActivity;
import com.qc.language.ui.question.read.sar.list.SarListActivity;
import com.qc.language.ui.question.speak.asq.list.AsqDetailActivity;
import com.qc.language.ui.question.speak.asq.list.AsqListActivity;
import com.qc.language.ui.question.speak.di.list.DIDetailActivity;
import com.qc.language.ui.question.speak.di.list.DIListActivity;
import com.qc.language.ui.question.speak.ra.list.RADetailActivity;
import com.qc.language.ui.question.speak.ra.list.RAListActivity;
import com.qc.language.ui.question.speak.rl.list.RlDetailActivity;
import com.qc.language.ui.question.speak.rl.list.RlListActivity;
import com.qc.language.ui.question.speak.rs.list.RSDetailActivity;
import com.qc.language.ui.question.speak.rs.list.RSListActivity;

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

    RSListActivity inject(RSListActivity rsListActivity);
    RSDetailActivity inject(RSDetailActivity rsDetailActivity);

    RAListActivity inject(RAListActivity raListActivity);
    RADetailActivity inject(RADetailActivity raDetailActivity);

    DIListActivity inject(DIListActivity diListActivity);
    DIDetailActivity inject(DIDetailActivity diDetailActivity);

    RlDetailActivity inject(RlDetailActivity rlDetailActivity);
    RlListActivity inject(RlListActivity rlListActivity);

    AsqListActivity inject(AsqListActivity asqListActivity);
    AsqDetailActivity inject(AsqDetailActivity asqDetailActivity);

    SarListActivity inject(SarListActivity sarListActivity);
    SarDetailActivity inject(SarDetailActivity sarDetailActivity);

    MarListActivity inject(MarListActivity marListActivity);
    MarDetailActivity inject(MarDetailActivity marDetailActivity);

    RpDetailActivity inject(RpDetailActivity rpDetailActivity);
    RpListActivity inject(RpListActivity rpListActivity);

    FibrDetailActivity inject(FibrDetailActivity fibrDetailActivity);
    FibrListActivity inject(FibrListActivity fibrListActivity);

    FibrwDetailActivity inject(FibrwDetailActivity fibrwDetailActivity);
    FibrwListActivity inject(FibrwListActivity fibrwListActivity);

    //听
    ListenerListActivity inject(ListenerListActivity listenerListActivity);
    HsstDetailActivity inject(HsstDetailActivity hsstDetailActivity);
    HmcsDetailActivity inject(HmcsDetailActivity hmcsDetailActivity);
    HmcmDetailActivity inject(HmcmDetailActivity hmcmDetailActivity);
    HfiblDetailActivity inject(HfiblDetailActivity hfiblDetailActivity);
    HsmwDetailActivity inject(HsmwDetailActivity hsmwDetailActivity);
    HhcsDetailActivity inject(HhcsDetailActivity hhcsDetailActivity);
    HhiwDetailActivity inject(HhiwDetailActivity hhiwDetailActivity);
    HwfdDetailActivity inject(HwfdDetailActivity hwfdDetailActivity);

    //写
    WswtDetailActivity inject(WswtDetailActivity wswtDetailActivity);

}
