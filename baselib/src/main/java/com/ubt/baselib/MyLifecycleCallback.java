package com.ubt.baselib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;

/**
 * @author：liuhai
 * @date：2018/4/19 14:22
 * @modifier：ubt
 * @modify_date：2018/4/19 14:22
 * [A brief description]
 * version
 */

public class MyLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    //Activity前台个数，用来判断app是否进入后台
    public static int mStateActivityCount = 0;


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        mStateActivityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        EventBus.getDefault().post(new EnterBackgroundEvent(EnterBackgroundEvent.Event.ENTER_RESUME));
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mStateActivityCount--;
        ViseLog.d("mStateActivityCount stop = " + mStateActivityCount);
        if (mStateActivityCount == 0) {
            EventBus.getDefault().post(new EnterBackgroundEvent(EnterBackgroundEvent.Event.ENTER_BACKGROUND));
            // AutoScanConnectService.doEntryBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 判断app是否进入后台
     *
     * @return
     */
    public static boolean isBackground() {
        if (mStateActivityCount == 0) {
            return true;
        } else {
            return false;
        }
    }
}
