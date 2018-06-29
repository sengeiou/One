package com.ubt.baselib.model1E;

import android.content.Context;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class PlayEvent {

    private PlayEvent.Event mEvent;

    private Context mContext;
    public enum Event {
        STOP,
        PLAY,
        PAUSE,
        PLAYMANAGER_STOP,
        ACTION_STOP
    }



    public PlayEvent(Event event) {
        this.mEvent = event;
    }

    public PlayEvent( Context context,Event event) {
        mEvent = event;
        mContext = context;
    }


    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }

    public Context getContext() {
        return mContext;
    }

}
