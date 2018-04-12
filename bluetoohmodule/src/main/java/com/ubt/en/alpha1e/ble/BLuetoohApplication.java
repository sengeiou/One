package com.ubt.en.alpha1e.ble;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.BlueTooth.BTHeartBeatManager;
import com.ubt.baselib.BlueTooth.BlueToothListenerImpl;
import com.ubt.baselib.utils.ContextUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.loader.LoaderManager;

import static com.ubt.en.alpha1e.ble.BuildConfig.DEBUG;

/**
 * @author：liuhai
 * @date：2018/4/11 10:53
 * @modifier：ubt
 * @modify_date：2018/4/11 10:53
 * [A brief description]
 * version
 */

public class BLuetoohApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }
    public static void init(Application appContext){
        // CrashHandlerAlpha1e.getInstance().register(appContext);
        //初始化HttpEntity 必须在initNet()之前
        ContextUtils.init(appContext);
        initLog();
         LoaderManager.getLoader().init(appContext);
        if (DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(appContext); // 尽可能早，推荐在Application中初始化
        BlueClientUtil.getInstance().init(appContext);
        BlueClientUtil.getInstance().setBlueListener(new BlueToothListenerImpl());
        BTHeartBeatManager.getInstance().init(appContext);
    }

    private static void initLog() {
        ViseLog.getLogConfig()
                .configTagPrefix("alpha1e")
                .configAllowLog(true)//是否输出日志
                .configShowBorders(false);//是否排版显示
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
    }
}
