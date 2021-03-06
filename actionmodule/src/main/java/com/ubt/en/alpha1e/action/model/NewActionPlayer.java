package com.ubt.en.alpha1e.action.model;

import android.content.Context;

import com.ubt.baselib.btCmd1E.cmd.BTCmdCtrlAllEngine;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.htslib.base.NewActionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author：liuhai
 * @date：2018/4/25 10:17
 * @modifier：ubt
 * @modify_date：2018/4/25 10:17
 * [A brief description]
 * version
 */

public class NewActionPlayer {

    public enum PlayerState {
        PLAYING, PAUSING, STOPING
    }


    // 发送命令相关参数
    private static String mCurrentMac;
    private static Context mContext;
    // 播放相关参数
    private static PlayerState mState;
    private static NewActionPlayer mPlayer;
    private static NewActionInfo mAction;
    private static NewActionPlayerListener mlistener_;
    private static Boolean isPauseing = false;
    private static Boolean isRunning = false;
    private List<NewActionPlayerListener> mListeners;
    // 播放线程相关
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    private BlueClientUtil mBlueClientUtil = null;

    // 重置播放器
    private void initPlayer() {
        mBlueClientUtil = BlueClientUtil.getInstance();
        mState = PlayerState.STOPING;
        mAction = null;
        mContext = null;
    }

    private NewActionPlayer() {
        initPlayer();
    }

    public void addListener(NewActionPlayerListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<NewActionPlayerListener>();
        }
        if (!mListeners.contains(listener)) {
            {
                mListeners.add(listener);
            }
        }
    }

    public void removeListener(NewActionPlayerListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<NewActionPlayerListener>();
        }
        if (mListeners.contains(listener))
            mListeners.remove(listener);
    }

    public static NewActionPlayer getPlayer(String mac) {
        if (mPlayer == null)
            mPlayer = new NewActionPlayer();
        mCurrentMac = mac;
        return mPlayer;
    }

    public static NewActionPlayer getPlayer() {
        return mPlayer;
    }

//    public static void reset() {
//        if(mPlayer.mBtManager != null){
//            mPlayer.mBtManager.removeBlueToothInteraction(mPlayer);
//        }
//        mPlayer = null;
//    }


    public String getBtMac() {
        return mCurrentMac;
    }

    // 获取播放器状态---------------------------------start
    public PlayerState getState() {
        return mState;
    }

    public String getPlayActonName() {
        if (mAction != null) {
            return mAction.actionName;
        }
        return "";
    }

    public long getPlayActonId() {
        if (mAction != null) {
            return mAction.actionId;
        }
        return -1;
    }

    // 获取播放器状态---------------------------------end
    // 播放函数
    public void PlayAction(NewActionInfo action, Context context) {

        final NewActionInfo _action = action;
        final Context _context = context;

        threadPool.execute(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                // 如果不是停止状态（播放或者暂停状态），停止原来的播放器
                if (mState != PlayerState.STOPING)
                    StopPlayer();
                // 判断上一个动作状态是否改变完成
                synchronized (isRunning) {
                    if (isRunning) {
                        try {
                            isRunning.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                // 在上一个动作状态改变完成的基础上，执行本次变更
                mState = PlayerState.PLAYING;
                mAction = _action;
                mContext = _context;

                if (mListeners != null) {
                    for (int i = 0; i < mListeners.size(); i++) {
                        mListeners.get(i).onPlaying();
                    }
                }

                /*if (mlistener != null){
                    mlistener.onPlaying();
                }*/
                new SendThread().start();
            }
        });

    }

    // 只能在播放状态暂停
    public void PausePalyer() {
        if (mState != PlayerState.PLAYING)
            return;
        mState = PlayerState.PAUSING;
        if (mListeners != null) {
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onPausePlay();
            }
        }

        /*if (mlistener != null)
            mlistener.onPausePlay();*/
        // 获取等待信号
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (isPauseing) {
                    isPauseing = true;
                }
            }
        });

    }

    public void ContinuePlayer() {

        if (mState != PlayerState.PAUSING)
            return;

        mState = PlayerState.PLAYING;
        if (mListeners != null) {
            for (int i = 0; i < mListeners.size(); i++) {
                mListeners.get(i).onPlaying();
            }
        }

        /*if (mlistener != null)
            mlistener.onPlaying();*/
        // 释放等待信号量
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (isPauseing) {
                    try {
                        isPauseing.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isPauseing = false;
                }
            }
        });

    }

    public void StopPlayer() {
        // 如果是在播放时停止
        if (mState == PlayerState.PLAYING) {
            mState = PlayerState.STOPING;
        }
        // 如果是在暂停时停止
        else {
            mState = PlayerState.STOPING;
            // 释放等待信号
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (isPauseing) {
                        try {
                            isPauseing.notify();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isPauseing = false;
                    }
                }
            });
        }

    }

    // 将Player的状态置成STOPING即可打断线程
    private class SendThread extends Thread {
        @Override
        public void run() {

            synchronized (isRunning) {
                isRunning = true;
            }
            for (int x = 0; mState != PlayerState.STOPING; x++) {
                if (x >= mAction.frameActions.size()) {
                    mState = PlayerState.STOPING;
                    break;
                }
                if (mListeners != null) {
                    for (int i = 0; i < mListeners.size(); i++) {
                        mListeners.get(i).onFrameDo(x);
                    }
                }

                /*if (mlistener != null)
                    mlistener.onFrameDo(x);*/
                sendCmd(mAction.frameActions.get(x).getData());
                if (isPauseing) {
                    synchronized (isPauseing) {
                        try {
                            isPauseing.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (mListeners != null) {
                for (int i = 0; i < mListeners.size(); i++) {
                    mListeners.get(i).onFinishPlay();
                }
                mAction = null;
            }
            /*if (mlistener != null) {
                mlistener.onFinishPlay();
                mAction = null;
            }*/

            synchronized (isRunning) {
                try {
                    isRunning.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isRunning = false;
            }
        }
    }

    public void sendCmd(byte[] datas) {

        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdCtrlAllEngine(datas).toByteArray());
        }

//        if (mPlayer.mBtManager == null) {
//            mBtManager = ((AlphaApplication) mContext.getApplicationContext())
//                    .getBlueToothManager();
//            mBtManager.addBlueToothInteraction(this);
//        }
//
//        mBtManager.sendCommand(mCurrentMac,
//                ConstValue.CTRL_ALL_ENGINE, datas, datas.length, false);
        try {
            byte t = datas[16];
            int t_int = t & 0xff;
            Thread.sleep(t_int * 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
