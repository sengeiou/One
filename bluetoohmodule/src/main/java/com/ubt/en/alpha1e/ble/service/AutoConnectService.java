package com.ubt.en.alpha1e.ble.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.ble.presenter.AutoConnectPrenster;
import com.vise.log.ViseLog;

import java.util.Date;

public class AutoConnectService extends Service {

    private BlueClientUtil mBlueClient;

    private Date lastTime_onConnectState = null;

    private Date lastTime_DV_HANDSHAKE = null;//握手次数时间

    AutoConnectPrenster mPresenter;



    public AutoConnectService() {
    }


    public static void startAutoConnectService(Context context) {
        Intent intent = new Intent(context, AutoConnectService.class);
        context.startService(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new AutoConnectPrenster();
        mPresenter.register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * 停止服务
     */
    public void doStopSelf(){
             stopSelf();
    }

    @Override
    public void onDestroy() {
        mPresenter.unRegister();
        ViseLog.d("结束服务");
        super.onDestroy();
    }
}
