package com.ubt.en.alpha1e.action.view.course;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;

/**
 * @author：liuhai
 * @date：2018/5/2 15:26
 * @modifier：ubt
 * @modify_date：2018/5/2 15:26
 * [A brief description]
 * version
 */

public class CourseTwoActionLayout extends BaseActionEditLayout {
    public CourseTwoActionLayout(Context context) {
        super(context);
    }

    public CourseTwoActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseTwoActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getLayoutId() {
        return R.layout.action_create_course_layout;
    }



}
