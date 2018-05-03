package com.ubt.en.alpha1e.action.course;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.CourseContract;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.IEditActionUI;
import com.ubt.en.alpha1e.action.presenter.CoursePrenster;
import com.ubt.en.alpha1e.action.view.course.CourseOneActionLayout;
import com.vise.log.ViseLog;

public class ActionLevelOneActivity extends MVPBaseActivity<CourseContract.View, CoursePrenster> implements CourseContract.View, IEditActionUI, ActionsEditHelper.PlayCompleteListener, CourseProgressListener {


    CourseOneActionLayout mActionEdit;
    private ActionsEditHelper mHelper;
    public static final String COURSE_LEVEL = "course_level";
    private int level;

    @Override
    public int getContentViewId() {
         return R.layout.action_activity_course_one;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        mHelper = new ActionsEditHelper(this, this);
        ((ActionsEditHelper) mHelper).setListener(this);
        mActionEdit = findViewById(R.id.action_layout_course_one);
        mActionEdit.setUp(mHelper);

        //行为习惯流程未开始，该干啥干啥
        ((ActionsEditHelper) mHelper).doEnterCourse((byte) 1);
        mActionEdit.setData(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViseLog.d("onDestroy");
        if (mActionEdit != null) {
            mActionEdit.doReset();
        }
        mHelper.unRegister();
    }

    @Override
    public void onPlaying() {
        mActionEdit.onPlaying();
    }

    @Override
    public void onPausePlay() {
        mActionEdit.onPausePlay();
    }

    @Override
    public void onFinishPlay() {
        mActionEdit.onFinishPlay();
    }

    @Override
    public void onFrameDo(int index) {
        mActionEdit.onFrameDo(index);
    }

    @Override
    public void notePlayChargingError() {

    }


    @Override
    public void onReadEng(byte[] eng_angle) {
        mActionEdit.onReadEng(eng_angle);
    }


    @Override
    public void playComplete() {
        ViseLog.d( "播放完成");

        mHandler.sendEmptyMessage(1111);
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void tapHead() {

    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1111) {
                mActionEdit.playComplete();
            }
        }
    };

    @Override
    public void completeCurrentCourse(int current) {

    }

    @Override
    public void finishActivity() {

    }

    @Override
    public void completeSuccess(boolean isSuccess) {

    }
}
