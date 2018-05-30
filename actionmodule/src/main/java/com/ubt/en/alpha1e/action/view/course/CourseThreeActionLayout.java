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
import com.ubt.en.alpha1e.action.dialog.CourseMusicDialogUtil;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.CourseOne1Content;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnRightLocal1PosCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;

/**
 * @author：liuhai
 * @date：2018/5/3 15:56
 * @modifier：ubt
 * @modify_date：2018/5/3 15:56
 * [A brief description]
 * version
 */

public class CourseThreeActionLayout extends BaseActionEditLayout implements CourseMusicDialogUtil.OnMusicDialogListener {
    private String TAG = CourseThreeActionLayout.class.getSimpleName();
    private ImageView ivMusicArror;
    private ImageView playArrow;


    AnimationDrawable animation1;
    AnimationDrawable animation2;
    AnimationDrawable animation3;


    RelativeLayout mRlInstruction;
    private TextView mTextView;
    private boolean isInstruction;
    private ImageView ivBackInStruction;
    CourseMusicDialogUtil mMusicDialogUtil;

    /**
     * 高亮对话框的TextView显示
     */
    TextView tv;
    private TextView tvKnow;
    private boolean isClicked;

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

    public CourseThreeActionLayout(Context context) {
        super(context);
    }

    public CourseThreeActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseThreeActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            if (course == 3) {
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
        ViseLog.d("currentCourse==" + currentCourse);
        if (currentCourse == 1) {
            isInstruction = true;
            mRlInstruction.setVisibility(View.VISIBLE);
            mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor14.hts");
        } else if (currentCourse == 2) {
            CourseArrowAminalUtil.startViewAnimal(true, ivMusicArror, 2);
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

        mRlInstruction = (RelativeLayout) findViewById(R.id.rl_instruction);
        mTextView = (TextView) findViewById(R.id.tv_all_introduc);
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_3_intro));
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
     * 设置添加按钮高亮
     */
    public void setActionMusicButton() {
        setImageViewBg();
        ivActionBgm.setEnabled(true);
        ivActionBgm.setImageResource(R.drawable.ic_add_music);
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
        ivActionBgm.setImageResource(R.drawable.ic_add_music_disable);
        CourseArrowAminalUtil.startViewAnimal(false, ivMusicArror, 2);
        mHelper.stopAction();
        if (null == mMusicDialogUtil) {
            mMusicDialogUtil = new CourseMusicDialogUtil(mContext);
        }
        mMusicDialogUtil.showMusicDialog(1, this);
    }


    /**
     * 播放按钮，过3秒钟结束
     */
    private void playAction() {
        startPlayAction();
        CourseArrowAminalUtil.startViewAnimal(false, playArrow, 1);
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
     * 播放音频或者动作结束
     */
    @Override
    public void playComplete() {
        ViseLog.d("播放完成");
        if (((Activity) mContext).isFinishing()) {
            return;
        }

        if (currentCourse == 1) {
            if (isInstruction) {
                isInstruction = false;
                mRlInstruction.setVisibility(View.GONE);
                showMusicLight();
            }
        } else if (currentCourse == 2) {
            ViseLog.d("playComplete==" + 2);
            if (secondIndex == 1) {
                lostRightLeg = true;
                lostLeft();
                CourseArrowAminalUtil.startViewAnimal(false, ivMusicArror, 2);
            }

        }
    }


    /**
     * Activity执行onPause方法
     */
    @Override
    public void onPause() {
        if (null != mMusicDialogUtil) {
            mMusicDialogUtil.pause();
        }
        pause();

    }


    /**
     * 音乐库介绍
     */
    private void showMusicLight() {
        setActionMusicButton();
        mHightLight = new HighLight(mContext)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//设置拦截属性为false 高亮布局不影响后面布局
                .enableNext()
                .maskColor(0xAA000000)
                // .anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.iv_action_bgm, R.layout.layout_pop_course_right_level, new OnRightLocal1PosCallback(30), new RectLightShape())
                .setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow(HightLightView hightLightView) {
                        HighLight.ViewPosInfo viewPosInfo = hightLightView.getCurentViewPosInfo();
                        if (null != viewPosInfo) {
                            int layoutId = viewPosInfo.layoutId;
                            View tipView = hightLightView.findViewById(layoutId);
                            tv = tipView.findViewById(R.id.tv_content);
                            tv.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_3_audio_library));
                            tvKnow = tipView.findViewById(R.id.tv_know);
                            isClicked = false;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isClicked = true;
                                    tvKnow.setTextColor(mContext.getResources().getColor(R.color.white));
                                }
                            }, 3000);
                            tvKnow.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    clickKnown();
                                }
                            });
                            ViseLog.d("======onShow====showMusicLight1");
                        }
                    }
                });

        mHightLight.show();
     }


    /**
     * 响应所有R.id.iv_known的控件的点击事件
     * <p>
     * 移除高亮布局
     * </p>
     */
    public void clickKnown() {
        if (!isClicked) {
            return;
        }
        ViseLog.d("currindex==" + currentIndex);
        if (mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
        {
            mHightLight.next();
        } else {
            remove(null);
            ViseLog.d("=====remove=========");
        }
        mHelper.stopAction();
        doReset();

        if (currentCourse == 1) {
            showNextDialog(2);
        } else if (currentCourse == 2) {
            showNextDialog(3);

        }
    }


    public void remove(View view) {
        mHightLight.remove();
    }


    /**
     * 下一课时对话框
     *
     * @param current 跳转课程
     */
    private void showNextDialog(int current) {
        currentCourse = current;
        ViseLog.d("进入第二课时，弹出对话框");

        mHelper.showNextDialog(mContext, 3, current, new ActionsEditHelper.ClickListener() {
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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pause();
                mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor15.hts");
                CourseArrowAminalUtil.startViewAnimal(true, playArrow, 2);
                ivPlay.setEnabled(true);
                ivActionBgm.setEnabled(false);
            }
        }, 1000);
    }

    @Override
    public void onStopRecord(PrepareMusicModel prepareMusicModel, int type) {

    }
}