package com.ubt.en.alpha1e.BlueTooth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/2/6 13:46
 * @描述: 蓝牙心跳管理
 */

public class BTHeartBeatManager {

    private Context mContext;
    private static BTHeartBeatManager instance;

    private static AtomicInteger mHeartCnt = new AtomicInteger(0);
    private static byte[] heartDatas;
    private static int repeatTime = 5000;

    private static PendingIntent pi;
    private static AlarmManager am;

    private static BlueClientUtil mBlueClientUtil;

    public BTHeartBeatManager() {

    }

    public void init(Context context) {
        mContext = context;
        mBlueClientUtil = BlueClientUtil.getInstance();
    }

    public static BTHeartBeatManager getInstance() {
        if (instance == null) {
            synchronized (BTHeartBeatManager.class) {
                if (instance == null) {
                    instance = new BTHeartBeatManager();
                }
            }
        }
        return instance;
    }

    public boolean startHeart(@NonNull byte[] heart, int repeatTime) {
        if (mContext == null) {
            ViseLog.e("请先初始化BTHeartBeatManager");
            return false;
        }

        this.heartDatas = heart;
        this.repeatTime = repeatTime;
        if (mBlueClientUtil != null) {
            mBlueClientUtil.sendData(heartDatas);
        }

        startAlarm();
        EventBus.getDefault().register(BTHeartBeatManager.this);
        return true;
    }

    public void stopHeart() {
        if (am != null && pi != null) {
            am.cancel(pi);
            pi = null;
            am = null;
            EventBus.getDefault().unregister(BTHeartBeatManager.this);
        }
    }

    private void resetHeartCnt() {
        mHeartCnt.set(0);
    }

    @Subscribe
    public void onReadData(BTReadData readData) {
        resetHeartCnt();
    }

    private void startAlarm() {
        //创建Intent对象，action为ELITOR_CLOCK，附加信息为字符串“你该打酱油了”
        Intent intent = new Intent("BT_HEARTBEAT");

    //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
    //也就是发送了action 为"ELITOR_CLOCK"的intent
        pi = PendingIntent.getBroadcast(mContext, 0, intent, 0);

    //AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        am = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);

    //设置闹钟从当前时间开始，每隔5s执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
    // 5秒后通过PendingIntent pi对象发送广播
    //am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),repeatTime,pi);
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + repeatTime, pi);
    }

    static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mHeartCnt.incrementAndGet() < 3 && am != null && pi != null) {
                if (mBlueClientUtil != null) {
                    mBlueClientUtil.sendData(heartDatas);
                }
                am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + repeatTime, pi);
            } else {
                ViseLog.e("心跳超时!!!");
                BTHeartBeatManager.getInstance().stopHeart();
            }
        }
    }
}