package com.ubt.en.alpha1e.action.view.course;

import android.app.Activity;
import android.content.Context;
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
import com.ubt.en.alpha1e.action.dialog.ActionCourseTwoUtil;
import com.ubt.en.alpha1e.action.dialog.DialogMusic;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.util.ActionConstant;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.ubt.htslib.base.FrameActionInfo;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author：liuhai
 * @date：2018/5/3 16:32
 * @modifier：ubt
 * @modify_date：2018/5/3 16:32
 * [A brief description]
 * version
 */

public class CourseNineActionLayout extends BaseActionEditLayout {
    private String TAG = CourseNineActionLayout.class.getSimpleName();
    private ImageView ivAddLibArrow;
    private ImageView ivLeftArrow1;

    private ImageView ivLeftHandArrow;

    private ImageView ivRightHandArrow;
    private ImageView ivUpdateArrow;

    private ImageView ivAutoReadArrow;

    private ImageView ivPlayArrow;

    private ImageView ivAddArrow;
    ActionCourseTwoUtil mActionCourseTwoUtil;
    private boolean isRlInstruction;

    RelativeLayout mRlInstruction;
    private TextView mTextView;
    CourseProgressListener courseProgressListener;
    private ImageView ivBackInStruction;
    /**
     * 当前课时
     */
    private int currentCourse = 1;

    private boolean isShowAddArrow;
    private RelativeLayout rlCenterAnimal;
    private ImageView ivCenterAnimal;

    public CourseNineActionLayout(Context context) {
        super(context);
    }

    public CourseNineActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseNineActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            if (course == 9) {
                if (recordlevel == 0) {
                    level = 1;
                } else if (recordlevel == 1) {
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
        ViseLog.d("currentCourse==" + currentCourse);
        if (currentCourse == 1) {
            isRlInstruction = true;
            mRlInstruction.setVisibility(View.VISIBLE);
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor30.hts");
        } else if (currentCourse == 2) {
            ivPlay.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true, ivPlayArrow, 2);
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
        mRlInstruction = findViewById(R.id.rl_instruction);
        mTextView = findViewById(R.id.tv_all_introduc);
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_9_intro));

        ivLeftHandArrow = findViewById(R.id.iv_left_arrow);
        ivLeftHandArrow.setOnClickListener(this);

        ivRightHandArrow = findViewById(R.id.iv_right_arrow);
        ivRightHandArrow.setOnClickListener(this);
        ivLeftHandArrow();
        ivPlayArrow = findViewById(R.id.iv_play_arrow);
        ivUpdateArrow = findViewById(R.id.iv_update_arrow);

        ivAutoReadArrow = findViewById(R.id.iv_auto_read_arrow);
        ivAutoReadArrow.setOnClickListener(this);

        ivAddArrow = findViewById(R.id.iv_add_frame_arrow);
        ivAddArrow.setOnClickListener(this);

        ivHandLeft.setEnabled(false);
        ivHandRight.setEnabled(false);

        ivBackInStruction = findViewById(R.id.iv_back_instruction);
        ivBackInStruction.setOnClickListener(this);
        rlCenterAnimal = findViewById(R.id.rl_center_animal);
        ivCenterAnimal = findViewById(R.id.iv_center_animal);
    }

    /**
     * 初始化箭头图片宽高
     */
    private void ivLeftHandArrow() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density = dm.density;
        ViseLog.d("density:" + density);

        ivLeftHandArrow.setLayoutParams(ActionConstant.getIvRobotParams(density, ivLeftHandArrow));
        ViseLog.d("ivLeftArrow:" + ivLeftHandArrow.getWidth() + "/" + ivLeftHandArrow.getHeight());

        ivRightHandArrow.setLayoutParams(ActionConstant.getIvRobotParams(density, ivRightHandArrow));

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

        } else if (i == R.id.iv_left_arrow || i == R.id.iv_hand_left) {
            lostRightLeg = true;
            lostLeft();
            CourseArrowAminalUtil.startNineViewAnimal(false, ivLeftHandArrow, 1);
            CourseArrowAminalUtil.startNineViewAnimal(true, ivRightHandArrow, 2);
            ivAddFrame.setEnabled(false);
            ivAddFrame.setImageResource(R.drawable.ic_addaction_disable);
            ivHandRight.setEnabled(true);

        } else if (i == R.id.iv_right_arrow || i == R.id.iv_hand_right) {
            lostLeftLeg = true;
            lostRight();
            CourseArrowAminalUtil.startNineViewAnimal(false, ivRightHandArrow, 1);
            showAutoRead();
            ivAddFrame.setEnabled(false);
            ivAddFrame.setImageResource(R.drawable.ic_addaction_disable);

        } else if (i == R.id.iv_add_frame_arrow) {
            addFrameClick();

        } else if (i == R.id.iv_add_frame) {
            addFrameClick();

        } else if (i == R.id.iv_auto_read) {
            DialogMusic dialogMusic = new DialogMusic(mContext, this, 3);
            dialogMusic.show();
            CourseArrowAminalUtil.startViewAnimal(false, ivAutoReadArrow, 1);
            ivAddFrame.setEnabled(false);
            ivAddFrame.setImageResource(R.drawable.ic_stop);

        } else if (i == R.id.iv_auto_read_arrow) {
            DialogMusic dialogMusic1 = new DialogMusic(mContext, this, 3);
            dialogMusic1.show();
            CourseArrowAminalUtil.startViewAnimal(false, ivAutoReadArrow, 1);
            ivAddFrame.setEnabled(false);
            ivAddFrame.setImageResource(R.drawable.ic_stop);

        } else if (i == R.id.iv_back_instruction) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else {
        }
    }

    /**
     * 点击添加按钮事件
     */
    private void addFrameClick() {
        mHandler.removeMessages(MSG_AUTO_READ);
        autoRead = false;
        needAdd = false;
        autoAng = "";
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
        CourseArrowAminalUtil.startViewAnimal(false, ivAddArrow, 1);
        showNextDialog(2);
    }


    private void showAutoRead() {
        if (lostLeftLeg && lostRightLeg) {
            ivAutoRead.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true, ivAutoReadArrow, 1);
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

    }

    @Override
    public void onFinishPlay() {
        ViseLog.d("onFinishPlay=======");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ivPlay.setEnabled(false);
                if (courseProgressListener != null) {
                    courseProgressListener.completeSuccess(true);
                }
            }
        });

    }

    /**
     * 添加自动回读数据
     */
    @Override
    public void addFrame() {
        super.addFrame();
        ViseLog.d("addframe==========" + list_autoFrames.size());
        ivAddFrame.setEnabled(false);
        ivAddFrame.setImageResource(R.drawable.ic_stop);
        if (list_autoFrames.size() > 4 && !isShowAddArrow) {
            isShowAddArrow = true;
            CourseArrowAminalUtil.startViewAnimal(true, ivAddArrow, 1);
            ivAddFrame.setEnabled(true);
            ivAddFrame.setImageResource(R.drawable.ic_stop);
            rlCenterAnimal.setVisibility(View.GONE);
            CourseArrowAminalUtil.startTwoLegViewAnimal(false, ivCenterAnimal, 1);
        }
    }

    @Override
    public void playComplete() {
        ViseLog.d("播放完成");
        if (((Activity) mContext).isFinishing()) {
            return;
        }
        if (currentCourse == 1) {
            if (isRlInstruction) {
                isRlInstruction = false;
                mRlInstruction.setVisibility(View.GONE);
                ivActionLib.setEnabled(true);
                ivHandLeft.setEnabled(true);
                CourseArrowAminalUtil.startNineViewAnimal(true, ivLeftHandArrow, 1);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor31.hts");
                    }
                }, 1000);
            }
            //  CourseArrowAminalUtil.startViewAnimal(true, ivRightHandArrow, 2);
        } else if (currentCourse == 2) {
            autoRead = false;
        }

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
    private void showNextDialog(final int current) {

        currentCourse = current;
        ViseLog.d("进入第九课时，弹出对话框");
        if (courseProgressListener != null) {
            courseProgressListener.showProgressDialog();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHelper.showNextDialog(mContext, 9, current, new ActionsEditHelper.ClickListener() {
                    @Override
                    public void confirm() {
                        setLayoutByCurrentCourse();
                    }
                });

            }
        },400);



    }


    @Override
    public void startAutoRead() {
        ((ActionsEditHelper) mHelper).stopAction();
        doReset1();
        setButtonEnable(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRead = true;
                ivAddFrame.setImageResource(R.drawable.ic_stop);
                mHandler.sendEmptyMessage(MSG_AUTO_READ);
                rlCenterAnimal.setVisibility(View.VISIBLE);
                CourseArrowAminalUtil.startTwoLegViewAnimal(true, ivCenterAnimal, 1);


            }
        }, 1000);
    }

    public void doReset1() {
        ViseLog.d("doReset");

        String angles = "93#20#66#86#156#127#90#74#95#104#89#89#104#81#76#90";

//        String angles = "90#90#90#90#90#90#90#60#76#110#90#90#120#104#70#90";
        FrameActionInfo info = new FrameActionInfo();
        info.eng_angles = angles;

        info.eng_time = 600;
        info.totle_time = 600;

        Map map = new HashMap<String, Object>();
        map.put(ActionsEditHelper.MAP_FRAME, info);
       // String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
      //  item_name = item_name.replace("#", 1 + "");
        //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
        map.put(ActionsEditHelper.MAP_FRAME_NAME, 1 + "");
        map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);

        ((ActionsEditHelper) mHelper)
                .doCtrlAllEng(((FrameActionInfo) map
                        .get(ActionsEditHelper.MAP_FRAME)).getData());


    }
    @Override
    public void onDestory() {
        courseProgressListener=null;
    }

}
