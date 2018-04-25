package com.ubt.en.alpha1e.action.model;

public interface NewActionPlayerListener {

    void onPlaying();

    void onPausePlay();

    void onFinishPlay();

    void onFrameDo(int index);

    void notePlayChargingError();
}
