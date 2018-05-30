package com.ubt.playaction.play;

import android.content.Context;

import com.ubt.baselib.mvp.BasePresenter;
import com.ubt.baselib.mvp.BaseView;
import com.ubt.playaction.model.ActionData;

import java.util.List;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class PlayActionContract {

    public interface View extends BaseView{
        void getActionList(List<ActionData> actionDataList);
        void notePlayFinish(String actionName);
        void notePlayStart(String actionName);
        void notePlayOrPause();
        void notePlayStop();
    }

    public interface Presenter extends BasePresenter<View>{
        void getActionList();
        void register(Context context);
        void unRegister();
        void playAction(String actionName);
        void stopAction();
        void playPauseAction();
    }
}
