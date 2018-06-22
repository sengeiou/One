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
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.CourseOne1Content;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.HightLightTopCallback;
import zhy.com.highlight.position.OnLeftLocalPosCallback;
import zhy.com.highlight.position.OnRightLocalPosCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;

/**
 * @author：liuhai
 * @date：2018/5/2 15:26
 * @modifier：ubt
 * @modify_date：2018/5/2 15:26
 * [A brief description]
 * version
 */

public class CourseOneActionLayout extends BaseActionEditLayout {
    private String TAG = CourseOneActionLayout.class.getSimpleName();
    private ImageView ivLeftArrow;
    private ImageView ivRightArrow;
    AnimationDrawable animation1;
    AnimationDrawable animation2;
    AnimationDrawable animation3;
    private RelativeLayout rlActionCenter;
    private ImageView ivCenterAction;

    RelativeLayout mRlInstruction;
    private TextView mTextView;
    private boolean isInstruction;

    private ImageView ivBackInStruction;

    private TextView tvKnow;
    private boolean isClicked;

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

    public CourseOneActionLayout(Context context) {
        super(context);
    }

    public CourseOneActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseOneActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            if (course == 1) {
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

    public int getCurrentCourse() {
        return currentCourse;
    }

    /**
     * 根据当前是第几个关卡显示对应的提示
     * 根据当前课时显示界面
     */
    public void setLayoutByCurrentCourse() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setImageViewBg();
                ViseLog.d("currentCourse==" + currentCourse);
                if (currentCourse == 1) {
                    isInstruction = true;
                    mRlInstruction.setVisibility(View.VISIBLE);
                    ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor1.hts");
                } else if (currentCourse == 2) {
                    showAddLight();
                } else if (currentCourse == 3) {
                    showPlayLight();
                }
                if (courseProgressListener != null) {
                    courseProgressListener.completeCurrentCourse(currentCourse);
                }
            }
        }, 200);

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
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
        setImageViewBg();

        mRlInstruction = (RelativeLayout) findViewById(R.id.rl_instruction);
        mTextView = (TextView) findViewById(R.id.tv_all_introduc);
        mTextView.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_guide));
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
    public void setAddButton() {
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
    }

    /**
     * 设置播放按钮高亮
     */
    public void setPlayButton() {
        ivPlay.setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }
        } else if (i == R.id.iv_back_instruction) {
            if (null != courseProgressListener) {
                courseProgressListener.finishActivity();
            }
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
     * 完成播放
     */
    @Override
    public void playComplete() {
        ViseLog.d("播放完成" + currentCourse);
        if (((Activity) mContext).isFinishing()) {
            return;
        }
        if (currentCourse == 1) {
            if (isInstruction) {
                isInstruction = false;
                mRlInstruction.setVisibility(View.GONE);
                showCourseOne();
            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clickKnown();
                    }
                }, 1000);
            }
        } else if (currentCourse == 2) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickKnown();
                }
            }, 1000);
            ViseLog.d("playComplete==" + 2);


        } else if (currentCourse == 3) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickKnown();
                }
            }, 1000);
        }
    }


    /**
     * Activity执行onPause方法
     */
    @Override
    public void onPause() {
        mHandler.removeMessages(1111);
    }


    /**
     * 第一课时界面显示
     */
    private void showCourseOne() {
        showOneCardContent();
    }


    /**
     * 第一个关卡第一个课时
     */
    private void showOneCardContent() {
        setAddButton();
        setPlayButton();
        mHightLight = new HighLight(mContext)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//设置拦截属性为false 高亮布局不影响后面布局
                .enableNext()
                .maskColor(0xAA000000)
                // .anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.ll_frame, R.layout.layout_pop_course_right_level, new HightLightTopCallback(100), new RectLightShape())
                .addHighLight(R.id.rl_musicz_zpne, R.layout.layout_pop_course_right_level, new HightLightTopCallback(100), new RectLightShape())
                .addHighLight(R.id.ll_add_frame, R.layout.layout_pop_course_right_level, new HightLightTopCallback(100), new RectLightShape())
                .addHighLight(R.id.rl_zoom1, R.layout.layout_pop_course_left_level, new OnLeftLocalPosCallback(30), new RectLightShape())
                .setOnNextCallback(new HighLightInterface.OnNextCallback() {
                    @Override
                    public void onNext(HightLightView hightLightView, View targetView, View tipView) {//动态设置文字
                        ViseLog.d(" ======OnNextCallback====currentIndex===" + currentIndex);
                        //  ViseLog.d( "当前的是那个View  onNext====" + currentIndex + "  size===" + mOne1ContentList.size());
                        tv = tipView.findViewById(R.id.tv_content);
                        tvKnow = tipView.findViewById(R.id.tv_know);

                        isClicked = false;
                        if (currentIndex < mOne1ContentList.size()) {
                            CourseOne1Content oneContent = mOne1ContentList.get(currentIndex);
                            tv.setText(oneContent.getTitle());
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isClicked = true;
                                    tvKnow.setTextColor(mContext.getResources().getColor(R.color.white));
                                }
                            }, 3000);
                            ((ActionsEditHelper) mHelper).playAction(oneContent.getActionPath());
                            ViseLog.d(" onNext====" + oneContent.getTitle());
                            ViseLog.d(" oneContent====" + oneContent.toString());
                            currentIndex++;
                        }

                        tvKnow.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clickKnown();
                            }
                        });
                        ViseLog.d("当前的是那个View  onNext====" + currentIndex + "  size===" + mOne1ContentList.size());
                    }
                }).setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow(HightLightView hightLightView) {
                        ViseLog.d(" ======OnShowCallback====currentIndex" + currentIndex);
                    }
                });
        if (courseProgressListener!=null){
            courseProgressListener.showGuide();
        }
        mHightLight.show();
        CourseOne1Content oneContent = mOne1ContentList.get(0);
        //  ((ActionsEditHelper) mHelper).playAction(oneContent.getActionPath());
    }


    /**
     * 添加按钮介绍
     */
    private void showAddLight() {
        setAddButton();
        mHightLight = new HighLight(mContext)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//设置拦截属性为false 高亮布局不影响后面布局
                .enableNext()
                .maskColor(0xAA000000)
                // .anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.iv_add_frame, R.layout.layout_pop_course_left_level, new OnLeftLocalPosCallback(30), new RectLightShape())
                .setOnNextCallback(new HighLightInterface.OnNextCallback() {
                    @Override
                    public void onNext(HightLightView hightLightView, View targetView, View tipView) {//动态设置文字
                        ViseLog.d(" ======OnNextCallback====currentIndex===" + currentIndex);
                        //  ViseLog.d( "当前的是那个View  onNext====" + currentIndex + "  size===" + mOne1ContentList.size());
                        HighLight.ViewPosInfo viewPosInfo = hightLightView.getCurentViewPosInfo();
                        if (null != viewPosInfo) {
                            int layoutId = viewPosInfo.layoutId;

                            tv = tipView.findViewById(R.id.tv_content);
                            tvKnow = tipView.findViewById(R.id.tv_know);
                            isClicked = false;
                            tv.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_add_action));
                            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor6.hts");

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
                        ViseLog.d("当前的是那个View  onNext====" + currentIndex + "  size===" + mOne1ContentList.size());
                    }
                });
        if (courseProgressListener!=null){
            courseProgressListener.showGuide();
        }
        mHightLight.show();
    }


    /**
     * 播放按钮介绍
     */
    private void showPlayLight() {
        setPlayButton();
        mHightLight = new HighLight(mContext)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//设置拦截属性为false 高亮布局不影响后面布局
                .enableNext()
                .maskColor(0xAA000000)
                // .anchor(findViewById(R.id.id_container))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.iv_play_music, R.layout.layout_pop_course_right_level, new OnRightLocalPosCallback(30), new RectLightShape())
                .setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow(HightLightView hightLightView) {
                        HighLight.ViewPosInfo viewPosInfo = hightLightView.getCurentViewPosInfo();
                        if (null != viewPosInfo) {
                            int layoutId = viewPosInfo.layoutId;
                            View tipView = hightLightView.findViewById(layoutId);
                            tv = tipView.findViewById(R.id.tv_content);
                            tv.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_play));
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

        if (courseProgressListener!=null){
            courseProgressListener.showGuide();
        }
        mHightLight.show();
        ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor7.hts");
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
        ((ActionsEditHelper) mHelper).stopAction();
        doReset();
        ViseLog.d("currindex==" + currentIndex);
        if (mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
        {
            mHightLight.next();
        } else {
            remove(null);
            ViseLog.d("=====remove=========");
        }

        if (currentCourse == 1) {
            if (currentIndex == 4) {
                showNextDialog(2);
            }
        } else if (currentCourse == 2) {
            showNextDialog(3);
        } else if (currentCourse == 3) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (courseProgressListener != null) {
                        courseProgressListener.completeSuccess(true);
                    }
                }
            }, 1000);

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
    private void showNextDialog(final int current) {
        currentCourse = current;
        ViseLog.d("进入第二课时，弹出对话框");
        if (courseProgressListener != null) {
            courseProgressListener.showProgressDialog();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHelper.showNextDialog(mContext, 1, current, new ActionsEditHelper.ClickListener() {
                    @Override
                    public void confirm() {
                        currentIndex = 0;
                        setLayoutByCurrentCourse();
                    }
                });
            }
        }, 400);


    }

    @Override
    public void onDestory() {
        courseProgressListener = null;
        mHightLight=null;
    }

}
