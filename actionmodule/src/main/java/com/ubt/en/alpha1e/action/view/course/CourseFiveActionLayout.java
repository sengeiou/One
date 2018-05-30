package com.ubt.en.alpha1e.action.view.course;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubt.baselib.model1E.LocalActionRecord;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.course.CourseProgressListener;
import com.ubt.en.alpha1e.action.dialog.CourseMusicDialogUtil;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.CourseOne1Content;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
import com.ubt.en.alpha1e.action.util.ActionConstant;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/5/3 16:18
 * @modifier：ubt
 * @modify_date：2018/5/3 16:18
 * [A brief description]
 * version
 */

public class CourseFiveActionLayout extends BaseActionEditLayout implements CourseMusicDialogUtil.OnMusicDialogListener {
    private String TAG = CourseFiveActionLayout.class.getSimpleName();
    private ImageView ivMusicArror;
    private ImageView ivAddFrameArrow;
    private ImageView playArrow;

    private ImageView ivLeftLegArrow;

    private ImageView resetArrow;

    private ImageView saveArrow;

    RelativeLayout mRlInstruction;
    private TextView mTextView;
    private boolean isInstruction;
    private ImageView ivBackInStruction;

    private RelativeLayout rlCenterAnimal;
    private ImageView ivCenterAnimal;

    /**
     * 高亮对话框的TextView显示
     */
    TextView tv;


    private List<CourseOne1Content> mOne1ContentList = new ArrayList<>();

    CourseProgressListener courseProgressListener;
    /**
     * 当前课时
     */
    private int currentCourse = 1;

    /**
     * 课时一
     */
    private int currentIndex = 0;
    //课时3顺序
    private int secondIndex = 0;

    private boolean firstClick = true;

    public CourseFiveActionLayout(Context context) {
        super(context);
    }

    public CourseFiveActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseFiveActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mOne1ContentList.clear();
        mOne1ContentList.addAll(ActionCourseDataManager.getCardOneList(mContext));
        int level = 1;// 当前第几个课时
        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
        if (null != record) {
           ViseLog.d( "record===" + record.toString());
            int course = record.getCourseLevel();
            int recordlevel = record.getPeriodLevel();
            if (course == 5) {
                if (recordlevel == 0 || recordlevel == 1) {
                    level = 1;
                } else if (recordlevel == 2) {
                    level = 2;
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
       ViseLog.d( "currentCourse==" + currentCourse);
        if (currentCourse == 1) {
            isInstruction = true;
            mRlInstruction.setVisibility(View.VISIBLE);
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor18.hts");
        } else if (currentCourse == 2) {
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor19.hts");
            CourseArrowAminalUtil.startViewAnimal(true, ivLeftLegArrow, 1);
        } else if (currentCourse == 3) {
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor21.hts");
            CourseArrowAminalUtil.startViewAnimal(true, saveArrow, 1);
            ivSave.setEnabled(true);

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
        playArrow = findViewById(R.id.iv_play_arrow);
        playArrow.setOnClickListener(this);

        ivAddFrameArrow = findViewById(R.id.iv_add_frame_arrow);
        resetArrow = findViewById(R.id.iv_reset_arrow);
        resetArrow.setOnClickListener(this);
        mRlInstruction = (RelativeLayout) findViewById(R.id.rl_instruction);
        mTextView = (TextView) findViewById(R.id.tv_all_introduc);
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_5));

        ivBackInStruction = findViewById(R.id.iv_back_instruction);
        ivBackInStruction.setOnClickListener(this);

        ivLeftLegArrow = findViewById(R.id.iv_left_leg_arrow);
        ivLeftLegArrow.setOnClickListener(this);
        initRightLegArrow();

        saveArrow = findViewById(R.id.iv_save_arrow);
        saveArrow.setOnClickListener(this);

        rlCenterAnimal = findViewById(R.id.rl_center_animal);
        ivCenterAnimal = findViewById(R.id.iv_center_animal);
    }

    /**
     * 初始化箭头图片宽高
     */
    private void initRightLegArrow() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density = dm.density;
       ViseLog.d( "density:" + density);
            ivLeftLegArrow.setLayoutParams(ActionConstant.getIvRobotParams(density, ivLeftLegArrow));
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


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else if (i == R.id.iv_play_music) {
            playAction();

        } else if (i == R.id.iv_play_arrow) {
            playAction();

        } else if (i == R.id.iv_left_leg_arrow || i == R.id.iv_leg_left) {
            if (firstClick) {
                CourseArrowAminalUtil.startViewAnimal(false, ivLeftLegArrow, 1);
                ivAddFrame.setEnabled(true);
                ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
                ivLegLeft.setSelected(true);
                CourseArrowAminalUtil.startViewAnimal(true, ivAddFrameArrow, 1);
            }

        } else if (i == R.id.iv_add_frame_arrow || i == R.id.iv_add_frame) {
            if (firstClick) {
                ((ActionsEditHelper) mHelper).stopAction();
                rlCenterAnimal.setVisibility(View.VISIBLE);
                CourseArrowAminalUtil.startLegViewAnimal(true, ivCenterAnimal, 1);
                firstClick = false;
                startEditRightLeg();
            } else {
                autoRead = false;
                addFrameOnClick();
                lostLeftHand = false;
                lostLeftLeg = false;
                ivLegLeft.setSelected(false);
                isCourseReading = false;
                CourseArrowAminalUtil.startViewAnimal(false, ivAddFrameArrow, 2);
                CourseArrowAminalUtil.startViewAnimal(true, playArrow, 2);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doReset();
                    }
                }, 1000);
            }

        } else if (i == R.id.iv_reset) {
            resetAction();

        } else if (i == R.id.iv_reset_arrow) {
            resetAction();

        } else if (i == R.id.iv_save_arrow) {
            saveAction();


        } else if (i == R.id.iv_save_action) {
            saveAction();

        } else if (i == R.id.iv_back_instruction) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else {
        }
    }

    /**
     * 重置
     */
    private void resetAction() {
        doReset();
        ivReset.setEnabled(false);
        ivReset.setImageResource(R.drawable.ic_reset_disable);
        CourseArrowAminalUtil.startViewAnimal(false, resetArrow, 1);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showNextDialog(3);
            }
        }, 1500);
    }

    /**
     * 保存按钮
     */
    private void saveAction() {
        ((ActionsEditHelper) mHelper).stopAction();
        ivReset.setEnabled(false);
        // CourseArrowAminalUtil.startViewAnimal(false, saveArrow, 1);
        saveNewAction(2);
    }

    /**
     * 播放按钮，过3秒钟结束
     */
    private void playAction() {

        startPlayAction();
        CourseArrowAminalUtil.startViewAnimal(false, playArrow, 2);
        ivPlay.setEnabled(false);
        ivPlay.setImageResource(R.drawable.ic_pause);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pause();
                onPlayMusicComplete();
            }
        }, 10000);
    }


    @Override
    public void onFinishPlay() {
       ViseLog.d( "onFinishPlay=======");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor20.hts");
                ivAddFrame.setEnabled(false);
                ivPlay.setEnabled(false);
                ivPlay.setImageResource(R.drawable.ic_play_disable);
                ivReset.setEnabled(true);
                ivReset.setImageResource(R.drawable.ic_reset);
                CourseArrowAminalUtil.startViewAnimal(true, resetArrow, 1);
            }
        });

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_AUTO_READ) {
                needAdd = true;
               ViseLog.d( "adddddd:" + autoRead);
                if (autoRead) {
                    readEngOneByOne();
                }
            }
        }
    };


    /**
     * 播放音频或者动作结束
     */
    @Override
    public void playComplete() {
       ViseLog.d( "播放完成");
        if (((Activity) mContext).isFinishing()) {
            return;
        }

        if (currentCourse == 1) {
            if (isInstruction) {
                mRlInstruction.setVisibility(View.GONE);
                showNextDialog(2);
            }
        }
    }

    /**
     * 第二关卡摆动机器人手臂
     */
    public void startEditRightLeg() {
        CourseArrowAminalUtil.startViewAnimal(false, ivLeftLegArrow, 2);
        CourseArrowAminalUtil.startViewAnimal(false, ivAddFrameArrow, 2);
        ((ActionsEditHelper) mHelper).stopAction();
        doReset();
        autoRead = true;
        mHandler.sendEmptyMessage(MSG_AUTO_READ);
        isCourseReading = true;
        lostLeftHand = true;
        lostLeftLeg();
        ivAddFrame.setEnabled(false);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_disable);
    }

    private boolean isChangeData;

    @Override
    public void onReacHandData() {
        if (isChangeData) {
            return;
        }
        isChangeData = true;
       ViseLog.d( "机器人角度变化了呢！！");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRead = false;
                ((ActionsEditHelper) mHelper).stopSoundAudio();
                CourseArrowAminalUtil.startViewAnimal(false, ivLeftLegArrow, 2);
                mHandler.removeMessages(MSG_AUTO_READ);
                setButtonEnable(false);
                rlCenterAnimal.setVisibility(View.GONE);
                ivAddFrame.setEnabled(true);
                ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
                CourseArrowAminalUtil.startViewAnimal(true, ivAddFrameArrow, 1);
            }
        }, 2000);

    }


    /**
     * Activity执行onPause方法
     */
    @Override
    public void onPause() {
        mHandler.removeMessages(1111);
        mHandler.removeMessages(1112);
        mHandler.removeMessages(1113);
        mHandler.removeMessages(1115);

    }


    /**
     * 响应所有R.id.iv_known的控件的点击事件
     * <p>
     * 移除高亮布局
     * </p>
     */
    public void clickKnown() {
       ViseLog.d( "currindex==" + currentIndex);

    }


    /**
     * 下一课时对话框
     *
     * @param current 跳转课程
     */
    private void showNextDialog(int current) {

        currentCourse = current;
       ViseLog.d( "进入第二课时，弹出对话框");
        mHelper.showNextDialog(mContext, 5, current, new ActionsEditHelper.ClickListener() {
            @Override
            public void confirm() {
                currentIndex = 0;
                setLayoutByCurrentCourse();
            }
        });

    }


    /**
     * 点击添加音乐按钮
     *
     * @param prepareMusicModel
     */
    @Override
    public void onMusicConfirm(PrepareMusicModel prepareMusicModel) {
        super.onMusicConfirm(prepareMusicModel);

    }

    @Override
    public void onStopRecord(PrepareMusicModel prepareMusicModel, int type) {

    }

}
