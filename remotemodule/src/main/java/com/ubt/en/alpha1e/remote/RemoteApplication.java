package com.ubt.en.alpha1e.remote;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ubt.baselib.ConfigureBaseLib;

import static com.ubt.en.alpha1e.action.BuildConfig.DEBUG;


/**
 * @author：liuhai
 * @date：2018/4/11 10:53
 * @modifier：ubt
 * @modify_date：2018/4/11 10:53
 * [A brief description]
 * version
 */

public class RemoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ConfigureBaseLib.getInstance().init(this,  false); //默认为测试环境

        /**ARouter必须放到APP层初始，这是由于库的注解解释器所决定的*/
        if (DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始
    }

}
