package com.ubt.en.alpha1e.action.view.course;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubt.baselib.model1E.LocalActionRecord;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.course.CourseProgressListener;
import com.ubt.en.alpha1e.action.dialog.ActionCourseTwoUtil;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.PrepareDataModel;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

/**
 * @author：liuhai
 * @date：2018/5/2 15:26
 * @modifier：ubt
 * @modify_date：2018/5/2 15:26
 * [A brief description]
 * version
 */

public class CourseTwoActionLayout extends BaseActionEditLayout implements ActionCourseTwoUtil.OnCourseDialogListener {
    private String TAG = CourseTwoActionLayout.class.getSimpleName();
    private ImageView ivLeftArrow;
    private ImageView ivLeftArrow1;
    private ImageView playArrow;

    AnimationDrawable animation1;
    AnimationDrawable animation2;
    AnimationDrawable animation3;
    ActionCourseTwoUtil mActionCourseTwoUtil;
    /**
     * 高亮对话框的TextView显示
     */
    TextView tv;
    RelativeLayout mRlInstruction;
    private TextView mTextView;
    CourseProgressListener courseProgressListener;

    private ImageView ivBackInStruction;
    /**
     * 当前课时
     */
    private int currentCourse = 1;
    private int oneIndex = 0;
    private int secondIndex = 0;
    private int threeIndex = 0;

    private boolean isPlayAction;


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

    /**
     * 设置课程数据,开始播放数据
     *
     * @param courseProgressListener 回调监听
     */
    @Override
    public void setData(CourseProgressListener courseProgressListener) {

        int level = 1;// 当前第几个课时
        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
        if (null != record) {
            ViseLog.d(TAG, "record===" + record.toString());
            int course = record.getCourseLevel();
            int recordlevel = record.getPeriodLevel();
            if (course == 2) {
                if (recordlevel == 0) {
                    level = 1;
                } else if (recordlevel == 1) {
                    level = 1;
                } else if (recordlevel == 2) {
                    level = 2;
                } else if (recordlevel == 3) {
                    level = 3;
                }
            }

        }
        this.currentCourse = 1;
        this.courseProgressListener = courseProgressListener;
        setLayoutByCurrentCourse();
        isSaveAction = true;
    }

    /**
     * 根据当前是第几个关卡显示对应的提示
     * 根据当前课时显示界面
     */
    public void setLayoutByCurrentCourse() {
        setImageViewBg();
        ViseLog.d(TAG, "currentCourse==" + currentCourse);
        if (currentCourse == 1) {
            mRlInstruction.setVisibility(View.VISIBLE);
            mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor8.hts");
            //showOneCardContent();
        } else if (currentCourse == 2) {
            ivActionLib.setEnabled(true);
            ivActionLibMore.setEnabled(false);
            showLeftArrow(true);
            showPlayArrow1(false);
            secondIndex = 1;
            mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor9.hts");
            // mHelper.playSoundAudio("{\"filename\":\"AE_action editor9.mp3\",\"playcount\":1}");
        } else if (currentCourse == 3) {
            ivActionLib.setEnabled(false);
            ivActionLibMore.setEnabled(true);
            showLeftArrow1(true);
            mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor12.hts");
            // mHelper.playSoundAudio("{\"filename\":\"AE_action editor12.mp3\",\"playcount\":1}");
            threeIndex = 1;
        }

        if (courseProgressListener != null) {
            courseProgressListener.completeCurrentCourse(currentCourse);
        }
    }

    /**
     * 初始化数据
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        super.init(context);
        isOnCourse = true;
        ivAddFrame.setEnabled(false);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_disable);
        setImageViewBg();
        ivLeftArrow = findViewById(R.id.iv_add_action_arrow);
        ivLeftArrow.setOnClickListener(this);
        ivLeftArrow1 = findViewById(R.id.iv_add_action_arrow1);
        ivLeftArrow1.setOnClickListener(this);
        playArrow = findViewById(R.id.iv_play_arrow);
        playArrow.setOnClickListener(this);
        mRlInstruction = findViewById(R.id.rl_instruction);
        mTextView = findViewById(R.id.tv_all_introduc);
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_2_items));
        ivBackInStruction = findViewById(R.id.iv_back_instruction);
        ivBackInStruction.setOnClickListener(this);
    }

    /**
     * 设置按钮不可点击
     */
    public void setImageViewBg() {
        ivReset.setEnabled(false);
        ivAutoRead.setEnabled(false);
        ivSave.setEnabled(false);
        ivActionLib.setEnabled(false);
        ivActionLibMore.setEnabled(false);
        ivActionBgm.setEnabled(false);
        ivPlay.setEnabled(false);
        ivHelp.setEnabled(false);
        ivAddFrame.setEnabled(false);
    }

    /**
     * 左边箭头动效
     *
     * @param flag true 播放 false 结束
     */
    private void showLeftArrow(boolean flag) {
        if (flag) {
            ivLeftArrow.setVisibility(View.VISIBLE);
            ivLeftArrow.setImageResource(R.drawable.animal_left_arrow);
            animation1 = (AnimationDrawable) ivLeftArrow.getDrawable();
            animation1.start();
            playArrow.setVisibility(View.GONE);
        } else {
            ivLeftArrow.setVisibility(View.GONE);
            if (null != animation1) {
                animation1.stop();
            }
        }
    }

    /**
     * 左边箭头动效
     *
     * @param flag true 播放 false 结束
     */
    private void showLeftArrow1(boolean flag) {
        if (flag) {
            ivLeftArrow1.setVisibility(View.VISIBLE);
            ivLeftArrow1.setImageResource(R.drawable.animal_left_arrow);
            animation2 = (AnimationDrawable) ivLeftArrow1.getDrawable();
            animation2.start();
        } else {
            ivLeftArrow1.setVisibility(View.GONE);
            if (null != animation2) {
                animation2.stop();
            }
        }
    }

    /**
     * 播放按钮箭头动效
     *
     * @param flag true 播放 false 结束
     */
    private void showPlayArrow1(boolean flag) {
        ivPlay.setEnabled(true);
        // ivBack.setEnabled(false);
        ivActionLibMore.setEnabled(false);
        ivAddFrame.setEnabled(false);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_disable);
        if (flag) {
            playArrow.setVisibility(View.VISIBLE);
            playArrow.setImageResource(R.drawable.animal_left_arrow);
            animation3 = (AnimationDrawable) playArrow.getDrawable();
            animation3.start();
        } else {
            playArrow.setVisibility(View.GONE);
            if (null != animation3) {
                animation3.stop();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else if (i == R.id.iv_action_lib) {
            showActionDialog();

        } else if (i == R.id.iv_add_action_arrow) {
            showActionDialog();

        } else if (i == R.id.iv_action_lib_more) {
            showActionDialog();

        } else if (i == R.id.iv_add_action_arrow1) {
            showActionDialog();

        } else if (i == R.id.iv_play_music) {
            playAction();

        } else if (i == R.id.iv_play_arrow) {
            playAction();

        } else if (i == R.id.iv_back_instruction) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else {
        }
    }

    /**
     * 播放按钮，过3秒钟结束
     */
    private void playAction() {
        startPlayAction();
        showPlayArrow1(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (courseProgressListener != null) {
                    courseProgressListener.completeSuccess(true);
                }
            }
        }, 4000);

    }

    /**
     * 显示动作对话框
     */
    public void showActionDialog() {
        mHelper.stopSoundAudio();
        if (null == mActionCourseTwoUtil) {
            mActionCourseTwoUtil = new ActionCourseTwoUtil(mContext);
        }
        if (currentCourse == 2) {
            secondIndex = 0;
            showLeftArrow(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor10.hts");
                    // mHelper.playSoundAudio("{\"filename\":\"AE_action editor10.mp3\",\"playcount\":1}");
                }
            }, 1000);
            mActionCourseTwoUtil.showActionDialog(1, this);
        } else if (currentCourse == 3) {
            threeIndex = 0;
            showLeftArrow1(false);
            mActionCourseTwoUtil.showActionDialog(2, this);

        }
    }

    /**
     * 机器人播放音乐和动作回调
     */
    @Override
    public void playComplete() {
        ViseLog.d(TAG, "播放完成");
        if (((Activity) mContext).isFinishing()) {
            return;
        }
        if (currentCourse == 1) {
            mRlInstruction.setVisibility(View.GONE);
            showNextDialog(2);
        } else if (currentCourse == 2) {
            ViseLog.d(TAG, "secondIndex==" + secondIndex);
            if (secondIndex == 1) {
                secondIndex = 2;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 500);
            }
        } else if (currentCourse == 3) {
            if (threeIndex == 1) {
                threeIndex = 2;
            }
        }
        ViseLog.d(TAG, "isPlayAction==" + isPlayAction);

    }


    /**
     * Activity执行onPause方法
     */
    @Override
    public void onPause() {
        mHandler.removeMessages(1111);
 

    }


    /**
     * 下一课时对话框
     *
     * @param current 跳转课程
     */
    private void showNextDialog(int current) {
        currentCourse = current;
        ViseLog.d(TAG, "进入第二课时，弹出对话框");

        mHelper.showNextDialog(mContext, 2, current, new ActionsEditHelper.ClickListener() {
            @Override
            public void confirm() {
                setLayoutByCurrentCourse();
            }
        });

    }

    /**
     * 点击添加按钮
     *
     * @param prepareDataModel
     */
    @Override
    public void onCourseConfirm(PrepareDataModel prepareDataModel) {
        ViseLog.d(TAG, "onCourseConfirm====" + currentCourse);
        onActionConfirm(prepareDataModel);
        mHelper.stopSoundAudio();
        isSaveAction = true;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentCourse == 2) {
                    showNextDialog(3);
                    ViseLog.d(TAG, "showNextDialog====");
                } else if (currentCourse == 3) {
                    showPlayArrow1(true);

                }
            }
        }, 1000);

    }

    @Override
    public void playCourseAction(PrepareDataModel prepareDataModel, int type) {
        isPlayAction = true;
        playAction(prepareDataModel);
        ViseLog.d(TAG, "playCourseAction===" + currentCourse);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != mActionCourseTwoUtil) {
                    mActionCourseTwoUtil.showAddAnimal();
                }
                mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor11.hts");
            }
        }, 1200);

    }


}
