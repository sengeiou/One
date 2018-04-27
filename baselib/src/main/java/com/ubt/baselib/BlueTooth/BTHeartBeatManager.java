package com.ubt.baselib.BlueTooth;

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

    public void init(Context context,@NonNull byte[] heart, int repeatTime) {
        mContext = context;
        mBlueClientUtil = BlueClientUtil.getInstance();
        BTHeartBeatManager.heartDatas = heart;
        BTHeartBeatManager.repeatTime = repeatTime;
        EventBus.getDefault().register(BTHeartBeatManager.this);
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

    private boolean startHeart() {
        if (mContext == null) {
            ViseLog.e("请先初始化BTHeartBeatManager");
            return false;
        }

        if (mBlueClientUtil != null) {
            mBlueClientUtil.sendData(heartDatas);
        }
        stopHeart();  //防止未关闭又重新发送心跳
        startAlarm();

        return true;
    }

    private void stopHeart() {
        if (am != null && pi != null) {
            am.cancel(pi);
            pi = null;
            am = null;
        }
    }

    private void resetHeartCnt() {
        mHeartCnt.set(0);
    }

    /**
     * 释放掉心跳所有资源
     */
    public void release(){
        EventBus.getDefault().unregister(BTHeartBeatManager.this);
        stopHeart();
    }

    @Subscribe
    public void onReadData(BTReadData readData) {
        resetHeartCnt();
    }

    @Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged){
        if(serviceStateChanged.getState() == BTServiceStateChanged.STATE_DISCONNECTED){
            stopHeart();
        }else if(serviceStateChanged.getState() == BTServiceStateChanged.STATE_CONNECTED){
            startHeart();
        }
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

   public static class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mHeartCnt.incrementAndGet() < 6 && am != null && pi != null) {
                 if (mBlueClientUtil != null) {
                     ViseLog.i("发送蓝牙心跳");
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
