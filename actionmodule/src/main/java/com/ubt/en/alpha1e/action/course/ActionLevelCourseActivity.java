package com.ubt.en.alpha1e.action.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseBTDisconnectDialog;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseLowBattaryDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.contact.CourseContract;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.IEditActionUI;
import com.ubt.en.alpha1e.action.presenter.CoursePrenster;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.en.alpha1e.action.view.BaseActionEditLayout;
import com.vise.log.ViseLog;

public class ActionLevelCourseActivity extends MVPBaseActivity<CourseContract.View, CoursePrenster> implements CourseContract.View, IEditActionUI, ActionsEditHelper.PlayCompleteListener, CourseProgressListener, BaseActionEditLayout.OnSaveSucessListener {


    BaseActionEditLayout mActionEdit;
    private ActionsEditHelper mHelper;
    public static final String COURSE_LEVEL = "course_level";
    private int level;
    /**
     * 当前课时
     */
    private int currentCourse;
    private boolean isFinishCourse = false;
    private DialogPlus exitDialog;

    public static void launchActivity(Activity context, int position) {
        Intent intent = new Intent(context, ActionLevelCourseActivity.class);
        intent.putExtra(COURSE_LEVEL, position);
        context.startActivityForResult(intent, 10000);
    }


    @Override
    public int getContentViewId() {
        level = getIntent().getIntExtra(COURSE_LEVEL, 0);
        int layoutId = R.layout.action_activity_course_one;
        if (level == 1) {
            layoutId = R.layout.action_activity_course_one;
        } else if (level == 2) {
            layoutId = R.layout.action_activity_course_two;
        } else if (level == 3) {
            layoutId = R.layout.action_activity_course_three;
        } else if (level == 4) {
            layoutId = R.layout.action_activity_course_four;
        } else if (level == 5) {
            layoutId = R.layout.action_activity_course_five;
        } else if (level == 6) {
            layoutId = R.layout.action_activity_course_six;
        } else if (level == 7) {
            layoutId = R.layout.action_activity_course_seven;
        } else if (level == 8) {
            layoutId = R.layout.action_activity_course_eight;
        } else if (level == 9) {
            layoutId = R.layout.action_activity_course_nine;
        } else if (level == 10) {
            layoutId = R.layout.action_activity_course_ten;
        }
        return layoutId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new ActionsEditHelper(this, this);
        ((ActionsEditHelper) mHelper).setListener(this);
        mActionEdit = (BaseActionEditLayout) findViewById(R.id.action_layout_course_one);
        mActionEdit.setUp(mHelper);

        //行为习惯流程未开始，该干啥干啥
        ((ActionsEditHelper) mHelper).doEnterCourse((byte) 1);
        mActionEdit.setData(this);
        mActionEdit.setOnSaveSucessListener(this);
        AppStatusUtils.setBtBussiness(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViseLog.d("------------onPause___________");
        mActionEdit.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViseLog.d("onDestroy");
        mHelper.doEnterCourse((byte) 0);
        if (mActionEdit != null) {
            mActionEdit.doReset();
            mActionEdit.onDestory();
        }
        mHelper.unRegister();
        AppStatusUtils.setBtBussiness(false);
        mActionEdit = null;
        mHelper = null;
    }

    //监听手机屏幕上的按键
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            ViseLog.d("返回键");
//            showExitDialog();
//            return true;
//        }
//        return true;
//        //return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressedSupport() {
        // super.onBackPressedSupport();
        showExitDialog();
    }

    private void showExitDialog() {
        exitDialog = new BaseDialog.Builder(this)
                .setCancleable(true)
                .setMessage(SkinManager.getInstance().getTextById(R.string.actions_lesson_quit))
                .setConfirmButtonId(R.string.base_cancel)
                .setConfirmButtonColor(R.color.black)
                .setCancleButtonID(R.string.base_confirm)
                .setCancleButtonColor(R.color.base_blue)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.button_confirm) {

                            dialog.dismiss();
                        } else if (view.getId() == R.id.button_cancle) {
                            mHelper.doEnterCourse((byte) 0);
                            finish();
                            dialog.dismiss();
                        }

                    }
                }).create();
        exitDialog.show();
    }


    @Override
    public void onPlaying() {
        if (mActionEdit != null) {
            mActionEdit.onPlaying();
        }
    }

    @Override
    public void onPausePlay() {
        if (mActionEdit != null) {
            mActionEdit.onPausePlay();
        }
    }

    @Override
    public void onFinishPlay() {
        if (mActionEdit != null) {
            mActionEdit.onFinishPlay();
        }
    }

    @Override
    public void onFrameDo(int index) {
        if (mActionEdit != null) {
            mActionEdit.onFrameDo(index);
        }
    }

    @Override
    public void notePlayChargingError() {

    }


    @Override
    public void onReadEng(byte[] eng_angle) {
        if (mActionEdit != null) {
            mActionEdit.onReadEng(eng_angle);
        }
    }


    @Override
    public void playComplete() {
        ViseLog.d("播放完成");
        if (!isFinishCourse) {
            mHandler.sendEmptyMessage(1111);
        }
    }

    @Override
    public void onDisconnect() {
        if (!BaseBTDisconnectDialog.getInstance().isShowing()) {
            BaseBTDisconnectDialog.getInstance().show(new BaseBTDisconnectDialog.IDialogClick() {
                @Override
                public void onConnect() {
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).navigation();
                    BaseBTDisconnectDialog.getInstance().dismiss();
                    finish();
                }

                @Override
                public void onCancel() {
                    BaseBTDisconnectDialog.getInstance().dismiss();
                    finish();
                }
            });
        }
    }

    @Override
    public void tapHead() {
        ViseLog.d("拍头打断" + "isFinishIng==" + !isFinishing() + "   isHowHeadDialog==" + isHowHeadDialog);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isHowHeadDialog) {
                    ViseLog.d("显示拍头打断对话框");
                    showTapHeadDialog();
                }
            }
        });
    }

    @Override
    public void lowerPower() {
        BaseLowBattaryDialog.getInstance().showLow5Dialog(new BaseLowBattaryDialog.IDialog5Click() {
            @Override
            public void onOK() {
                finish();
            }
        });
    }

    private boolean isHowHeadDialog;
    private DialogPlus mTapDialogPlus;

    private void showTapHeadDialog() {
        isHowHeadDialog = true;

        mTapDialogPlus = new BaseDialog.Builder(this)
                .setCancleable(true)
                .setMessage(R.string.actions_not_finish_course)
                .setConfirmButtonId(R.string.actions_lesson_not_quit)
                .setConfirmButtonColor(R.color.base_blue)
                .setCancleButtonID(R.string.actions_lesson_quit_confirm)
                .setCancleButtonColor(R.color.black)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.button_confirm) {
                            isHowHeadDialog = false;
                            if (mTapDialogPlus != null) {
                                mTapDialogPlus.dismiss();
                            }
                        } else if (view.getId() == R.id.button_cancle) {
                            ((ActionsEditHelper) mHelper).doEnterCourse((byte) 0);
                            ActionLevelCourseActivity.this.finish();
                            isHowHeadDialog = false;
                            if (mTapDialogPlus != null) {
                                mTapDialogPlus.dismiss();
                            }
                        }

                    }
                }).setOnDissmissListener(new BaseDialog.OnDissmissListener() {
                    @Override
                    public void onDissmiss() {
                        ViseLog.d("拍头对话框消失");
                        isHowHeadDialog = false;
                    }
                }).create();
        mTapDialogPlus.show();


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1111) {
                mActionEdit.playComplete();
            }
        }
    };

    @Override
    public void completeCurrentCourse(int current) {
        currentCourse = current;
        mPresenter.savaCourseDataToDB(level, current);
        if (level == 10) {
            if (isHowHeadDialog) {
                if (mTapDialogPlus != null && mTapDialogPlus.isShowing()) {
                    mTapDialogPlus.dismiss();
                    mTapDialogPlus = null;
                }

                if (exitDialog != null && exitDialog.isShowing()) {
                    exitDialog.dismiss();
                    exitDialog = null;
                }
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    returnCardActivity();
                }
            }, isHowHeadDialog ? 400 : 0);
        }
    }

    @Override
    public void finishActivity() {
        showExitDialog();
    }

    /**
     * 课程结束的时候如果显示拍头打断对话框则隐藏拍头对话框
     *
     * @param isSuccess
     */
    @Override
    public void completeSuccess(boolean isSuccess) {
        if (isHowHeadDialog) {
            if (mTapDialogPlus != null && mTapDialogPlus.isShowing()) {
                mTapDialogPlus.dismiss();
                mTapDialogPlus = null;
            }
        }
        if (exitDialog != null && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                returnCardActivity();
            }
        }, 400);
        mPresenter.savaCourseDataToDB(level + 1, 1);
    }

    @Override
    public void showProgressDialog() {
        if (mTapDialogPlus != null && mTapDialogPlus.isShowing()) {
            mTapDialogPlus.dismiss();
            mTapDialogPlus = null;
        }
        if (exitDialog != null && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }
    }

    /**
     * 显示提示框的时候拍头打断对话框显示在后面
     */
    @Override
    public void showGuide() {
        if (isHowHeadDialog && mTapDialogPlus != null && mTapDialogPlus.isShowing()) {
            mTapDialogPlus.dismiss();
            mTapDialogPlus = null;
        }
        if (exitDialog != null && exitDialog.isShowing()) {
            exitDialog.dismiss();
            exitDialog = null;
        }
    }

    /**
     * 返回关卡页面
     */
    public void returnCardActivity() {
        isFinishCourse = true;
        mHelper.playAction(ActionCourseDataManager.COURSE_ACTION_PATH + "AE_victory editor.hts");
        View contentView = LayoutInflater.from(this).inflate(R.layout.action_dialog_course_result, null);
        TextView tvResult = contentView.findViewById(R.id.tv_result);
        tvResult.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_pass));
        TextView title = contentView.findViewById(R.id.tv_card_name);
        title.setText(ActionsEditHelper.getCourseDialogTitle(level));
        ((ImageView) contentView.findViewById(R.id.iv_result)).setImageResource(R.drawable.action_img_level_success);
        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = (int) ((display.getWidth()) * 0.6); //设置宽度

        DialogPlus.newDialog(this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setContentBackgroundResource(android.R.color.transparent)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_retry) {//点击确定以后刷新列表并解锁下一关
                            mHelper.doEnterCourse((byte) 0);
                            Intent intent = new Intent();
                            intent.putExtra("course", level);//第几关
                            intent.putExtra("leavel", currentCourse);//第几个课时
                            intent.putExtra("isComplete", true);
                            intent.putExtra("score", 1);

                            setResult(1, intent);
                            finish();
                            dialog.dismiss();
                        }
                    }
                })
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                    }
                })
                .setCancelable(false)
                .create().show();


    }

    @Override
    public void startSave(Intent intent) {
        if (level != 10) {
            startActivityForResult(intent, ActionsEditHelper.SaveActionReq);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActionsEditHelper.SaveActionReq) {
            if (null != data) {
                boolean isSaveSuccess = (Boolean) data.getExtras().get(ActionsEditHelper.SaveActionResult);
                if (isSaveSuccess) {
                    completeSuccess(true);
                }
            }
        }

    }
}
