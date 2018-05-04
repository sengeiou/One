package com.ubt.en.alpha1e.action.view.course;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ubt.baselib.model1E.LocalActionRecord;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.course.CourseProgressListener;
import com.ubt.en.alpha1e.action.dialog.ActionCourseTwoUtil;
import com.ubt.en.alpha1e.action.dialog.CourseMusicDialogUtil;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.PrepareDataModel;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

/**
 * @author：liuhai
 * @date：2018/5/3 16:22
 * @modifier：ubt
 * @modify_date：2018/5/3 16:22
 * [A brief description]
 * version
 */

public class CourseSixActionLayout extends BaseActionEditLayout implements ActionCourseTwoUtil.OnCourseDialogListener, CourseMusicDialogUtil.OnMusicDialogListener {
    private String TAG = CourseSixActionLayout.class.getSimpleName();
    private ImageView ivAddLibArrow;
    private ImageView ivLeftArrow1;

    private ImageView ivMusiArrow;

    private ImageView ivPlayArrow;
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


    private boolean isPlayAction;

    private CourseMusicDialogUtil mMusicDialogUtil;

    public CourseSixActionLayout(Context context) {
        super(context);
    }

    public CourseSixActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseSixActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            ViseLog.d("record===" + record.toString());
            int course = record.getCourseLevel();
            int recordlevel = record.getPeriodLevel();
            if (course == 6) {
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
        ViseLog.d("currentCourse==" + currentCourse);
        if (currentCourse == 1) {
            mRlInstruction.setVisibility(View.VISIBLE);
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor22.hts");
            //showOneCardContent();
        } else if (currentCourse == 2) {
            ivActionLib.setEnabled(true);
            ivActionLibMore.setEnabled(false);
        } else if (currentCourse == 3) {
            ivActionBgm.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true, ivMusiArrow, 2);
        } else if (currentCourse == 4) {
            ivPlay.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true, ivPlayArrow, 2);
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor25.hts");
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
        ivAddLibArrow = findViewById(R.id.iv_add_action_arrow);
        ivAddLibArrow.setOnClickListener(this);
        ivLeftArrow1 = findViewById(R.id.iv_add_action_arrow1);
        ivLeftArrow1.setOnClickListener(this);
        mRlInstruction = findViewById(R.id.rl_instruction);
        mTextView = findViewById(R.id.tv_all_introduc);
        mTextView.setText("现在给阿尔法的踢腿动作配一个声音吧！");

        ivMusiArrow = findViewById(R.id.iv_music_arrow);

        ivPlayArrow = findViewById(R.id.iv_play_arrow);

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

        } else if (i == R.id.iv_action_bgm) {
            showMusicDialog();

        } else if (i == R.id.iv_music_arrow) {
            showMusicDialog();

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
        CourseArrowAminalUtil.startViewAnimal(false, ivPlayArrow, 2);
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
        ViseLog.d("onFinishPlay=======");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor20.hts");
                ivAddFrame.setEnabled(false);
                ivPlay.setEnabled(false);
                ivPlay.setEnabled(true);
                ivPlay.setImageResource(R.drawable.ic_play_disable);
                ivReset.setEnabled(true);
                ivReset.setImageResource(R.drawable.ic_reset);
                if (courseProgressListener != null) {
                    courseProgressListener.completeSuccess(true);
                }
            }
        });

    }

    /**
     * 点击弹出音乐对话框
     */
    private void showMusicDialog() {
        CourseArrowAminalUtil.startViewAnimal(false, ivMusiArrow, 2);
        ((ActionsEditHelper) mHelper).stopAction();

        if (null == mMusicDialogUtil) {
            mMusicDialogUtil = new CourseMusicDialogUtil(mContext);
        }
        if (currentCourse == 1) {
            mMusicDialogUtil.showMusicDialog(0, this);
        } else if (currentCourse == 3) {
            mMusicDialogUtil.showMusicDialog(1000, this);
        }
    }

    /**
     * 显示动作对话框
     */
    public void showActionDialog() {
        CourseArrowAminalUtil.startViewAnimal(false, ivAddLibArrow, 1);
        ((ActionsEditHelper) mHelper).stopSoundAudio();
        if (null == mActionCourseTwoUtil) {
            mActionCourseTwoUtil = new ActionCourseTwoUtil(mContext);
        }

        mActionCourseTwoUtil.showActionDialog(1, this);

    }

    @Override
    public void playComplete() {
        ViseLog.d("播放完成");
        if (((Activity) mContext).isFinishing()) {
            return;
        }
        if (currentCourse == 1) {
            mRlInstruction.setVisibility(View.GONE);
            ivActionBgm.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true, ivMusiArrow, 2);
        } else if (currentCourse == 2) {

        } else if (currentCourse == 3) {

        }
        ViseLog.d("isPlayAction==" + isPlayAction);

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

    }


    /**
     * 下一课时对话框
     *
     * @param current 跳转课程
     */
    private void showNextDialog(int current) {

        currentCourse = current;
        ViseLog.d("进入第6课时，弹出对话框");

        mHelper.showNextDialog(mContext, 6, current, new ActionsEditHelper.ClickListener() {
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
        ViseLog.d("onCourseConfirm====" + currentCourse);
        onActionConfirm(prepareDataModel);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showNextDialog(4);

            }
        }, 1000);
    }

    @Override
    public void playCourseAction(PrepareDataModel prepareDataModel, int type) {
        isPlayAction = true;
        //  playAction(prepareDataModel);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != mActionCourseTwoUtil) {
                    mActionCourseTwoUtil.showAddAnimal();
                }
            }
        }, 1000);
    }

    @Override
    public void onMusicConfirm(PrepareMusicModel prepareMusicModel) {
        super.onMusicConfirm(prepareMusicModel);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ivActionLib.setEnabled(true);
                ivActionBgm.setEnabled(false);
                CourseArrowAminalUtil.startViewAnimal(true, ivAddLibArrow, 2);

            }
        });
    }


    @Override
    public void onStopRecord(PrepareMusicModel prepareMusicModel, int type) {
        ViseLog.d("onStopRecord===========type==" + type);

        if (type == 1) {
            if (courseProgressListener != null) {
                courseProgressListener.completeCurrentCourse(1);
            }
            currentCourse = 2;
        } else if (type == 2) {
            showNextDialog(3);
            ((ActionsEditHelper) mHelper).stopAction();
            doReset();
        } else if (type == 3) {
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor23.hts");
        } else if (type == 4) {
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor24.hts");
        }
    }
}
