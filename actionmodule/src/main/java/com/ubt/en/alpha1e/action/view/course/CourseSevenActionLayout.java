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
import com.ubt.en.alpha1e.action.dialog.ActionCourseTwoUtil;
import com.ubt.en.alpha1e.action.dialog.CourseMusicDialogUtil;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.PrepareDataModel;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
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
 * @date：2018/5/3 16:25
 * @modifier：ubt
 * @modify_date：2018/5/3 16:25
 * [A brief description]
 * version
 */

public class CourseSevenActionLayout extends BaseActionEditLayout implements ActionCourseTwoUtil.OnCourseDialogListener {
    private String TAG = CourseSevenActionLayout.class.getSimpleName();
    private ImageView ivAddLibArrow;
    private ImageView ivLeftArrow1;

    private ImageView ivLeftHandArrow;

    private ImageView ivUpdateArrow;

    private ImageView ivEditArrow;

    private ImageView ivPlayArrow;

    private ImageView ivAddArrow;
    ActionCourseTwoUtil mActionCourseTwoUtil;
    /**
     * 高亮对话框的TextView显示
     */
    TextView tv;
    RelativeLayout mRlInstruction;
    private TextView mTextView;
    CourseProgressListener courseProgressListener;
    private ImageView ivBackInStruction;
    private boolean isCanClickAddFrame = false;

    private RelativeLayout rlCenterAnimal;
    private ImageView ivCenterAnimal;

    /**
     * 当前课时
     */
    private int currentCourse = 1;


    private boolean isPlayAction;

    private CourseMusicDialogUtil mMusicDialogUtil;

    public CourseSevenActionLayout(Context context) {
        super(context);
    }

    public CourseSevenActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseSevenActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            if (course == 7) {
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
            mRlInstruction.setVisibility(View.VISIBLE);
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor26.hts");
        } else if (currentCourse == 2) {
            ivActionLib.setEnabled(false);
            CourseArrowAminalUtil.startViewAnimal(true, ivUpdateArrow, 2);
            isOnCourse = false;
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
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_7_guide));

        ivLeftHandArrow = findViewById(R.id.iv_left_arrow);
        ivLeftHandArrow.setOnClickListener(this);
        ivLeftHandArrow();
        ivPlayArrow = findViewById(R.id.iv_play_arrow);
        ivUpdateArrow = findViewById(R.id.iv_update_arrow);

        ivEditArrow = findViewById(R.id.iv_edit_arrow);
        ivEditArrow.setOnClickListener(this);

        ivAddArrow = findViewById(R.id.iv_add_frame_arrow);
        ivAddArrow.setOnClickListener(this);

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
        ViseLog.d("i==="+i+"     R.id.iv_hand_left=="+R.id.iv_hand_left+"    i == R.id.iv_left_arrow=="+ R.id.iv_left_arrow);
        if (i == R.id.iv_back) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else if (i == R.id.iv_action_lib) {
            showActionDialog();

        } else if (i == R.id.iv_add_action_arrow) {
            showActionDialog();

        } else if (i == R.id.iv_play_music) {
            playAction();

        } else if (i == R.id.iv_play_arrow) {
            playAction();

        } else if (i == R.id.iv_hand_left || i == R.id.iv_left_arrow) {
            rlCenterAnimal.setVisibility(View.VISIBLE);
            lostRightLeg = true;
            lostLeft();
            CourseArrowAminalUtil.startViewAnimal(false, ivLeftHandArrow, 1);
            CourseArrowAminalUtil.startLegViewAnimal(true, ivCenterAnimal, 2);
            ivAddFrame.setEnabled(false);
            isOnCourse = false;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ActionsEditHelper) mHelper).stopAction();
                    doChangeItem();
                    autoRead = true;
                    mHandler.sendEmptyMessage(MSG_AUTO_READ);
                    isCourseReading = true;
                    ivAddFrame.setEnabled(false);
                    ivAddFrame.setImageResource(R.drawable.ic_confirm);
                }
            }, 1500);


        } else if (i == R.id.iv_edit_arrow) {
            editUpdate();

        } else if (i == R.id.iv_change) {
            editUpdate();

        } else if (i == R.id.iv_add_frame_arrow || i == R.id.iv_add_frame) {
            if (isCanClickAddFrame) {
                addFrameOnClick();
                isOnCourse = true;
                ivPlay.setEnabled(true);
                ivPlay.setImageResource(R.drawable.ic_play_enable);
                CourseArrowAminalUtil.startViewAnimal(true, ivPlayArrow, 2);
                CourseArrowAminalUtil.startViewAnimal(false, ivAddArrow, 1);
            }

        } else if (i == R.id.iv_back_instruction) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }

        } else {
        }
    }

    /**
     * 点击修改按钮，先复原机器人
     */
    private void editUpdate() {
        ((ActionsEditHelper) mHelper).stopAction();

       ViseLog.d("doReset");

        String angles = "93#20#66#86#156#127#90#74#95#104#89#89#104#81#76#90";

//        String angles = "90#90#90#90#90#90#90#60#76#110#90#90#120#104#70#90";
        FrameActionInfo info = new FrameActionInfo();
        info.eng_angles = angles;

        info.eng_time = 600;
        info.totle_time = 600;

        Map map = new HashMap<String, Object>();
        map.put(ActionsEditHelper.MAP_FRAME, info);
        //String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
       // item_name = item_name.replace("#", 1 + "");
        //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
        map.put(ActionsEditHelper.MAP_FRAME_NAME, 1 + "");
        map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);

        ((ActionsEditHelper) mHelper)
                .doCtrlAllEng(((FrameActionInfo) map
                        .get(ActionsEditHelper.MAP_FRAME)).getData());

        CourseArrowAminalUtil.startViewAnimal(false, ivEditArrow, 1);

        rlEditFrame.setVisibility(View.GONE);
        CourseArrowAminalUtil.startViewAnimal(true, ivLeftHandArrow, 1);
        ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor27.hts");

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
                ivAddFrame.setImageResource(R.drawable.ic_confirm);
            }
        }
    };
    private String autoAng = "";

    /**
     * 重写父类的方法判断角度变化了
     */
    @Override
    public void addFrame() {
        if (isChangeData) {
            super.addFrame();
            return;
        }
       ViseLog.d("addFrame:================");
        String angles = "";
        for (int i = 0; i < init.length; i++) {
            angles += init[i] + "#";
        }
       ViseLog.d("angles:" + angles);

       ViseLog.d("autoAng:" + autoAng + "angles:" + angles);
        if (autoAng.equals(angles)) {
            mHandler.sendEmptyMessage(MSG_AUTO_READ);

        } else {
           ViseLog.d("autoAng:" + autoAng + "angles:" + angles);
            if (autoAng.equals("")) {
                autoAng = angles;
                mHandler.sendEmptyMessage(MSG_AUTO_READ);
            } else {
                String[] auto = autoAng.split("#");
                String[] ang = angles.split("#");
                boolean isNeedAdd = false;
                for (int i = 0; i < auto.length; i++) {
                    int abs = Integer.valueOf(auto[i]) - Integer.valueOf(ang[i]);
                    if (Math.abs(abs) > 5) {
                        autoAng = angles;
                        isNeedAdd = true;
                        break;
                    }
                }
                if (!isNeedAdd) {
                   ViseLog.d("no need");
                    mHandler.sendEmptyMessage(MSG_AUTO_READ);
                } else {
                    onReacHandData();
                }
            }
        }
    }

    private boolean isChangeData;

    @Override
    public void onReacHandData() {
        super.onReacHandData();
        isChangeData = true;
       ViseLog.d("机器人角度变化了呢！！");
        autoRead = false;
        mHandler.removeMessages(MSG_AUTO_READ);
        setButtonEnable(false);
        isCanClickAddFrame = true;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlCenterAnimal.setVisibility(View.GONE);
                CourseArrowAminalUtil.startLegViewAnimal(false, ivCenterAnimal, 2);
                ivAddFrame.setEnabled(true);
                ivAddFrame.setImageResource(R.drawable.ic_confirm);
                CourseArrowAminalUtil.startViewAnimal(true, ivAddArrow, 1);
            }
        }, 2000);

    }

    @Override
    public void showEditFrameLayout() {
        if (currentCourse == 2) {
            isOnCourse = false;
            ivAddFrame.setEnabled(false);
            ivAddFrame.setImageResource(R.drawable.ic_confirm);
            rlEditFrame.setVisibility(View.VISIBLE);
            CourseArrowAminalUtil.startViewAnimal(true, ivEditArrow, 2);
            CourseArrowAminalUtil.startViewAnimal(false, ivUpdateArrow, 1);
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
            ivActionLib.setEnabled(true);
            CourseArrowAminalUtil.startViewAnimal(true, ivAddLibArrow, 2);
        } else if (currentCourse == 2) {
            autoRead = false;
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
       ViseLog.d("进入第七课时，弹出对话框");
        mHelper.showNextDialog(mContext, 7, current, new ActionsEditHelper.ClickListener() {
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
                showNextDialog(2);
                ivSave.setEnabled(false);
            }
        }, 1000);
    }

    @Override
    public void playCourseAction(PrepareDataModel prepareDataModel, int type) {
        isPlayAction = true;
        //  playAction(prepareDataModel);
       ViseLog.d("playCourseAction===" + currentCourse);
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

}
