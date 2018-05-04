package com.ubt.baselib.model1E;

import android.content.Context;

/**
 * @author：liuhai
 * @date：2018/4/19 16:06
 * @modifier：ubt
 * @modify_date：2018/4/19 16:06
 * [A brief description]
 * version
 */

public class ManualEvent {

    private boolean isManual;

    private Event mEvent;

    private Context mContext;
    public enum Event {
        MANUAL_ENTER,
        MANUAL_DISCONNECT,
        START_AUTOSERVICE,
        CONNECT_ROBOT_SUCCESS
    }



    public ManualEvent(Event event) {
        this.mEvent = event;
    }

    public ManualEvent( Context context,Event event) {
        mEvent = event;
        mContext = context;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
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
