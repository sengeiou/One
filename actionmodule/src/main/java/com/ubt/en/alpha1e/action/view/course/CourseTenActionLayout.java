package com.ubt.en.alpha1e.action.view.course;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.course.CourseProgressListener;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;


/**
 * @author：liuhai
 * @date：2017/11/20 15:08
 * @modifier：ubt
 * @modify_date：2017/11/20 15:08
 * [A brief description]
 * version
 */

public class CourseTenActionLayout extends BaseActionEditLayout {
    private String TAG = CourseTenActionLayout.class.getSimpleName();


    RelativeLayout mRlInstruction;
    private TextView mTextView;
    CourseProgressListener courseProgressListener;
    private ImageView ivBackInStruction;
    /**
     * 当前课时
     */
    private int currentCourse = 1;
    private boolean isInstruction;


    public CourseTenActionLayout(Context context) {
        super(context);
    }

    public CourseTenActionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseTenActionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        this.currentCourse = 1;
        this.courseProgressListener = courseProgressListener;
        setLayoutByCurrentCourse();

    }

    /**
     * 根据当前是第几个关卡显示对应的提示
     * 根据当前课时显示界面
     */
    public void setLayoutByCurrentCourse() {
        ViseLog.d( "currentCourse==" + currentCourse);
        if (currentCourse == 1) {
            isInstruction = true;
            mRlInstruction.setVisibility(View.VISIBLE);
            ((ActionsEditHelper) mHelper).playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_action editor32.hts");
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

        mRlInstruction = (RelativeLayout) findViewById(R.id.rl_instruction);
        mTextView = (TextView) findViewById(R.id.tv_all_introduc);
        mTextView.setText("现在，你来独立设计一个动作给阿尔法吧。记得按保存才算通关哦。");
        ivBackInStruction = findViewById(R.id.iv_back_instruction);
        ivBackInStruction.setOnClickListener(this);

    }

    private void showExitDialog() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.view_comfirmdialog, null);
        TextView title = contentView.findViewById(R.id.txt_msg);
        title.setText("成功就在眼前，放弃闯关吗？");
        Button button = contentView.findViewById(R.id.btn_pos);
        button.setText("放弃闯关");
        Button button1 = contentView.findViewById(R.id.btn_neg);
        button1.setText("继续闯关");
        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.6); //设置宽度
        DialogPlus.newDialog(mContext)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                // .setContentBackgroundResource(R.drawable.action_dialog_filter_rect)
                .setOnClickListener(new com.orhanobut.dialogplus.OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_pos) {
                            ((ActionsEditHelper) mHelper).doEnterCourse((byte) 0);
                            if (null != courseProgressListener) {
                                courseProgressListener.finishActivity();
                            }
                        } else if (view.getId() == R.id.btn_pos) {

                        }
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.iv_back) {
            super.onClick(v);
        }
        int i = v.getId();
        if (i == R.id.iv_back) {
            showExitDialog();

        } else if (i == R.id.iv_save_action) {
            if (musicTimes == 0) {
                if (list_frames.size() < 1) {
                    new BaseDialog.Builder(mContext)
                            .setMessage(R.string.action_ui_readback_not_null).
                            setConfirmButtonId(R.string.action_ui_common_confirm)
                            .setCancleButtonID(R.string.action_ui_common_cancel)
                            .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    if (view.getId() == com.ubt.baselib.R.id.button_confirm) {
                                        dialog.dismiss();
                                    } else if (view.getId() == com.ubt.baselib.R.id.button_cancle) {
                                        dialog.dismiss();
                                    }

                                }
                            }).create().show();
                    return;
                }
            } else {
                if (list_frames.size() < 2) {
                    new BaseDialog.Builder(mContext)
                            .setMessage(R.string.action_ui_readback_not_null).
                            setConfirmButtonId(R.string.action_ui_common_confirm)
                            .setCancleButtonID(R.string.action_ui_common_cancel)
                            .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    if (view.getId() == com.ubt.baselib.R.id.button_confirm) {
                                        dialog.dismiss();
                                    } else if (view.getId() == com.ubt.baselib.R.id.button_cancle) {
                                        dialog.dismiss();
                                    }

                                }
                            }).create().show();
                    return;
                }
            }
            if (null != courseProgressListener) {
                courseProgressListener.completeCurrentCourse(1);
            }

        } else if (i == R.id.iv_back_instruction) {
            showExitDialog();

        } else {
        }
    }

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
            }
        }
    }

    @Override
    public void onPause() {
        
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        showExitDialog();
        return false;
    }
}