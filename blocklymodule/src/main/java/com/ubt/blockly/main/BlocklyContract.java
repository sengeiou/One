package com.ubt.blockly.main;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;

import java.util.List;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class BlocklyContract {

    interface View extends BaseView {
        void getActionList(List<String> actionList);
        void notePlayFinish(String actionName);
        void playEmojiFinish();
        void playSoundFinish();
        void doWalkFinish();
        void infraredSensor(int state);
        void read6DState(int state);
        void tempState(String state);
        void updatePower(int power);
        void lostBT();
        void noteTapHead();
        void noteRobotFallDown(int state);
    }

    interface  Presenter extends BasePresenter<View> {
        void  register(Context context);
        void getActionList();
        void unRegister();

        void playAction(String actionName);
        void stopAction();
        void playEmoji(String emojiName);
        void playSound(String soundName);
        void playLedLight(byte[] params);
        void doWalk(byte direct, byte speed, byte[] step);
        void stopPlayAudio();
        void stopLedLight();
        void stopPlayEmoji();
        void doReadInfraredSensor(byte cmd);
        void doRead6Dstate();
        void doReadTemperature(byte cmd);
        void startOrStopRun(byte cmd);
        void doReadRobotFallState();

    }
}
