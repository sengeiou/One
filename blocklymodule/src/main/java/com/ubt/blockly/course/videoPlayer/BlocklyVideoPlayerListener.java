package com.ubt.blockly.course.videoPlayer;

import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.vise.log.ViseLog;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class BlocklyVideoPlayerListener implements StandardVideoAllCallBack {

    private static String TAG = "BlocklyVideoPlayerListener";

    @Override
    public void onPrepared(String s, Object... objects) {
        ViseLog.d(TAG, "onPrepared");
    }

    @Override
    public void onClickStartIcon(String s, Object... objects) {
        ViseLog.d(TAG, "onClickStartIcon");
    }

    @Override
    public void onClickStartError(String s, Object... objects) {
        ViseLog.d(TAG, "onClickStartError");
    }

    @Override
    public void onClickStop(String s, Object... objects) {
        ViseLog.d(TAG, "onClickStop");
    }

    @Override
    public void onClickStopFullscreen(String s, Object... objects) {
        ViseLog.d(TAG, "onClickStopFullscreen");
    }

    @Override
    public void onClickResume(String s, Object... objects) {
        ViseLog.d(TAG, "onClickResume");
    }

    @Override
    public void onClickResumeFullscreen(String s, Object... objects) {
        ViseLog.d(TAG, "onClickResumeFullscreen");
    }

    @Override
    public void onClickSeekbar(String s, Object... objects) {
        ViseLog.d(TAG, "onClickSeekbar");
    }

    @Override
    public void onClickSeekbarFullscreen(String s, Object... objects) {
        ViseLog.d(TAG, "onClickSeekbarFullscreen");
    }

    @Override
    public void onAutoComplete(String s, Object... objects) {
        ViseLog.d(TAG, "onAutoComplete");
    }

    @Override
    public void onEnterFullscreen(String s, Object... objects) {
        ViseLog.d(TAG, "onEnterFullscreen");
    }

    @Override
    public void onQuitFullscreen(String s, Object... objects) {
        ViseLog.d(TAG, "onQuitFullscreen");
    }

    @Override
    public void onQuitSmallWidget(String s, Object... objects) {
        ViseLog.d(TAG, "onQuitSmallWidget");
    }

    @Override
    public void onEnterSmallWidget(String s, Object... objects) {
        ViseLog.d(TAG, "onEnterSmallWidget");
    }

    @Override
    public void onTouchScreenSeekVolume(String s, Object... objects) {
        ViseLog.d(TAG, "onTouchScreenSeekVolume");
    }

    @Override
    public void onTouchScreenSeekPosition(String s, Object... objects) {
        ViseLog.d(TAG, "onTouchScreenSeekPosition");
    }

    @Override
    public void onTouchScreenSeekLight(String s, Object... objects) {
        ViseLog.d(TAG, "onTouchScreenSeekLight");
    }

    @Override
    public void onPlayError(String s, Object... objects) {
        ViseLog.d(TAG, "onPlayError");
    }

    @Override
    public void onClickStartThumb(String s, Object... objects) {
        ViseLog.d(TAG, "onClickStartThumb");
    }

    @Override
    public void onClickBlank(String s, Object... objects) {
        ViseLog.d(TAG, "onClickBlank");
    }

    @Override
    public void onClickBlankFullscreen(String s, Object... objects) {
        ViseLog.d(TAG, "onClickBlankFullscreen");
    }
}
