package com.ubt.en.alpha1e;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tencent.ai.tvs.LoginWupManager;
import com.tencent.ai.tvs.env.ELoginEnv;
import com.tencent.bugly.crashreport.CrashReport;
import com.ubt.en.alpha1e.services.GlobalMsgService;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/4 10:21
 * @描述:
 */

public class Application1E extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InitBaseLib.init(this);
        initWXQQ(this);
        //initXG(this);
        CrashReport.initCrashReport(getApplicationContext(), "ff64261bb9", BuildConfig.DEBUG);
        startGlobalMsgService();
    }


    private void initWXQQ(Context context) {
        LoginWupManager manager = LoginWupManager.getInstance(context);
        manager.startup();
        manager.setLoginEnv(ELoginEnv.FORMAL);
    }


    private void initXG(Context context) {
//        String accessId = SPUtils.getInstance().getString(XGConstact.SP_XG_ACCESSID);
//        String accessKey = SPUtils.getInstance().getString(XGConstact.SP_XG_ACCESSKEY);
//        if (!TextUtils.isEmpty(accessId) && !TextUtils.isEmpty(accessKey)) {
//            ViseLog.d("accessId:"+accessId);
//            XGUBTManager.getInstance().initXG(context, Long.parseLong(accessId), accessKey, new XGIOperateCallback() {
//                @Override
//                public void onSuccess(Object data, int flag) {
//                    ViseLog.d("注册成功，设备token为：" + data);
//                }
//
//                /**
//                    错误码：10106,错误信息：TpnsMessage wait for response timeout!
//                 */
//                @Override
//                public void onFail(Object data, int errCode, String msg) {
//                    ViseLog.d( "注册失败，错误码：" + errCode + ",错误信息：" + msg);
//                }
//            });
//            XGUBTManager.getInstance().setXGListener(new XGListener());
//        }
    }

    private void startGlobalMsgService() {
        Intent mIntent = new Intent(this, GlobalMsgService.class);
        startService(mIntent);
    }

}
