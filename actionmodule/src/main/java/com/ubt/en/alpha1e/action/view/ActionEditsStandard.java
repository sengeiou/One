package com.ubt.en.alpha1e.action.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.course.CourseProgressListener;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.vise.log.ViseLog;


/**
 * @author：liuhai
 * @date：2017/11/15 15:36
 * @modifier：ubt
 * @modify_date：2017/11/15 15:36
 * [A brief description]
 * version
 */

public class ActionEditsStandard extends BaseActionEditLayout {

    public ActionEditsStandard(Context context) {
        super(context);
    }

    public ActionEditsStandard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionEditsStandard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(CourseProgressListener courseProgressListener) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.action_create_layout;

    }

    @Override
    public void playComplete() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void init(Context context) {
        super.init(context);
        ViseLog.d("ActionEditsStandard", "执行init方法 111");

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((ActionsEditHelper) mHelper).doEnterOrExitActionEdit((byte) 0x03);
                doReset();

            }
        }, 1000);
    }

    @Override
    public void onDestory() {
    }

}
