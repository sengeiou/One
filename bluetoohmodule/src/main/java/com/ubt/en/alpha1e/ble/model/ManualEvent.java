package com.ubt.en.alpha1e.ble.model;

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

    public enum Event {
        MANUAL_ENTER,
        MANUAL_DISCONNECT,

    }

    public ManualEvent(Event event) {
        this.mEvent = event;
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
}
