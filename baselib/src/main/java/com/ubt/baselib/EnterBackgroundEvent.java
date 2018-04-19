package com.ubt.baselib;

/**
 * @author：liuhai
 * @date：2018/4/19 16:06
 * @modifier：ubt
 * @modify_date：2018/4/19 16:06
 * [A brief description]
 * version
 */

public class EnterBackgroundEvent {

    private boolean isEnterBackground;

    private Event mEvent;

    public enum Event {
        ENTER_BACKGROUND,

        ENTER_RESUME
    }

    public EnterBackgroundEvent(Event event) {
        this.mEvent = event;
    }

    public boolean isEnterBackground() {
        return isEnterBackground;
    }

    public void setEnterBackground(boolean enterBackground) {
        isEnterBackground = enterBackground;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }
}
