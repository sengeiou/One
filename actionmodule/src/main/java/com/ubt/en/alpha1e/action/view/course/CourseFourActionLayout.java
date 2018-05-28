package com.ubt.en.alpha1e.action.view.course;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
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
import com.ubt.en.alpha1e.action.dialog.CourseMusicDialogUtil;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.CourseOne1Content;
import com.ubt.en.alpha1e.action.model.PrepareDataModel;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import zhy.com.highlight.HighLight;

/**
 * @author：liuhai
 * @date：2018/5/3 16:03
 * @modifier：ubt
 * @modify_date：2018/5/3 16:03
 * [A brief description]
 * version
 */

public class CourseFourActionLayout  extends BaseActionEditLayout implements ActionCourseTwoUtil.OnCourseDialogListener, CourseMusicDialogUtil.OnMusicDialogListener {
    private String TAG = CourseFourActionLayout.class.getSimpleName();
    private ImageView ivMusicArror;
    private ImageView ivRightArrow;
    private ImageView playArrow;

    private ImageView ivActionMore;
    private ImageView ivMoreArrow;
    AnimationDrawable animation1;
    AnimationDrawable animation2;
    AnimationDrawable animation3;
    private RelativeLayout rlActionCenter;
    private ImageView ivCenterAction;

    RelativeLayout mRlInstruction;
    private TextView mTextView;
    private boolean isInstruction;
    private ImageView ivBackInStruction;
    CourseMusicDialogUtil mMusicDialogUtil;//音乐对话框

    ActionCourseTwoUtil mActionCourseTwoUtil;//动作对话框

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
    private HighLight mHightLight;

    public CourseFourActionLayout(Context context) {
        super(context);
    }

    public CourseFourActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseFourActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            ViseLog.d("record===" + record.toString());
            int course = record.getCourseLevel();
            int recordlevel = record.getPeriodLevel();
            if (course == 4) {
                if (recordlevel == 0 || recordlevel == 1) {
                    level = 1;
                } else if (recordlevel == 2) {
                    level = 2;
                } else if (recordlevel==3){
                    level=3;
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
            isInstruction = true;
            mRlInstruction.setVisibility(View.VISIBLE);
            mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor16.hts");
        } else if (currentCourse == 2) {
            ivActionBgm.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true,ivMusicArror,2);
        } else if (currentCourse == 3) {
            ivPlay.setEnabled(true);
            ivAddFrame.setEnabled(false);
            CourseArrowAminalUtil.startViewAnimal(true,playArrow,2);
            mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor17.hts");
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

        ivMusicArror = findViewById(R.id.iv_music_arrow);
        ivMusicArror.setOnClickListener(this);
        ivRightArrow = findViewById(R.id.iv_add_frame_arrow);
        rlActionCenter = findViewById(R.id.rl_action_animal);
        ivCenterAction = findViewById(R.id.iv_center_action);

        ivActionMore = findViewById(R.id.iv_action_lib_more);
        ivActionMore.setOnClickListener(this);
        ivMoreArrow = findViewById(R.id.iv_add_action_arrow1);
        ivMoreArrow.setOnClickListener(this);

        mRlInstruction = (RelativeLayout) findViewById(R.id.rl_instruction);
        mTextView = (TextView) findViewById(R.id.tv_all_introduc);
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_4_add));
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

        } else if (i == R.id.iv_action_bgm) {
            showMusicDialog();

        } else if (i == R.id.iv_music_arrow) {
            showMusicDialog();

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
     * 点击弹出音乐对话框
     */
    private void showMusicDialog() {
        ivActionBgm.setEnabled(false);
        CourseArrowAminalUtil.startViewAnimal(false,ivMusicArror,2);
        mHelper.stopSoundAudio();
        if (null == mMusicDialogUtil) {
            mMusicDialogUtil = new CourseMusicDialogUtil(mContext);
        }
        mMusicDialogUtil.showMusicDialog(3, this);
    }


    /**
     * 显示动作对话框
     */
    public void showActionDialog() {
        mHelper.stopSoundAudio();
        if (null == mActionCourseTwoUtil) {
            mActionCourseTwoUtil = new ActionCourseTwoUtil(mContext);
        }
        ivActionLibMore.setEnabled(false);
        CourseArrowAminalUtil.startViewAnimal(false,ivMoreArrow,2);
        mActionCourseTwoUtil.showActionDialog(2, this);

    }

    /**
     * 播放按钮，过3秒钟结束
     */
    private void playAction() {
        mHelper.stopAction();
        startPlayAction();
        ivAddFrame.setEnabled(false);
        CourseArrowAminalUtil.startViewAnimal(false,playArrow,2);
        ivPlay.setEnabled(false);
        ivPlay.setImageResource(R.drawable.ic_pause);
    }


    /**
     * 音乐播放结束,并结束课程
     */
    @Override
    public void onPlayMusicComplete() {
        if (courseProgressListener != null) {
            courseProgressListener.completeSuccess(true);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_AUTO_READ) {
                needAdd = true;
                ViseLog.d("adddddd:" + autoRead);
                if (autoRead) {
                    readEngOneByOne();
                }
            }
        }
    };


    /**
     * hts播放完成
     */
    @Override
    public void playComplete() {
        ViseLog.d("播放完成");
        if (((Activity) mContext).isFinishing()) {
            return;
        }

        if (currentCourse == 1) {
            if (isInstruction) {//第一课程
                isInstruction = false;
                mRlInstruction.setVisibility(View.GONE);
                ivActionLibMore.setEnabled(true);
                CourseArrowAminalUtil.startViewAnimal(true,ivMoreArrow,2);

            }
        } else if (currentCourse == 2) {
            ViseLog.d("playComplete==" + 2);
        }
    }


    /**
     * Activity执行onPause方法
     */
    @Override
    public void onPause() {
        pause();
    }


     
    /**
     * 下一课时对话框
     *
     * @param current 跳转课程
     */
    private void showNextDialog(int current) {
        currentCourse = current;
        ViseLog.d("进入第二课时，弹出对话框");


        mHelper.showNextDialog(mContext, 4, current, new ActionsEditHelper.ClickListener() {
            @Override
            public void confirm() {
                currentIndex = 0;
                setLayoutByCurrentCourse();
            }
        });

    }


    @Override
    public void onCourseConfirm(PrepareDataModel prepareDataModel) {
        super.onActionConfirm(prepareDataModel);
        ivPlay.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                showNextDialog(2);
            }
        }, 1000);

    }

    @Override
    public void playCourseAction(PrepareDataModel prepareDataModel, int type) {

    }

    /**
     * 点击添加音乐按钮
     *
     * @param prepareMusicModel
     */
    @Override
    public void onMusicConfirm(PrepareMusicModel prepareMusicModel) {
        super.onMusicConfirm(prepareMusicModel);
        ivPlay.setEnabled(false);
        mHelper.stopSoundAudio();
        mHandler.postDelayed(new Runnable() {//延迟一秒播放语音
            @Override
            public void run() {
                showNextDialog(3);
            }
        }, 1000);
    }

    @Override
    public void onStopRecord(PrepareMusicModel prepareMusicModel,int type) {
        ViseLog.d("onStopRecord====================================");
    }

}
