package com.ubt.en.alpha1e.action;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseBTDisconnectDialog;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseLowBattaryDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.TimeTools;
import com.ubt.en.alpha1e.action.contact.DynamicActionContract;
import com.ubt.en.alpha1e.action.model.DownloadProgressInfo;
import com.ubt.en.alpha1e.action.model.DynamicActionModel;
import com.ubt.en.alpha1e.action.presenter.DynamicActionPrenster;
import com.ubt.en.alpha1e.action.util.DownLoadActionManager;
import com.ubt.en.alpha1e.action.util.GlideRoundTransform;
import com.ubt.globaldialog.customDialog.loading.LoadingDialog;
import com.vise.log.ViseLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActionDetailDynamicActivity extends MVPBaseActivity<DynamicActionContract.View, DynamicActionPrenster> implements DynamicActionContract.View, DownLoadActionManager.DownLoadActionListener {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.iv_delete)
    TextView mIvDelete;
    @BindView(R2.id.iv_cover)
    ImageView mIvCover;
    @BindView(R2.id.tv_action_name)
    TextView mTvActionName;
    @BindView(R2.id.tv_action_create_time)
    TextView mTvActionCreateTime;
    @BindView(R2.id.tv_action_time)
    TextView mTvActionTime;
    @BindView(R2.id.iv_action_type1)
    ImageView mIvActionType1;
    @BindView(R2.id.tv_action_type)
    TextView mTvActionType;
    @BindView(R2.id.ll_type)
    LinearLayout mLlType;
    @BindView(R2.id.tv_flag)
    TextView mTvFlag;
    @BindView(R2.id.tv_content)
    TextView mTvContent;
    @BindView(R2.id.btn_publish)
    Button mBtnPublish;
    @BindView(R2.id.tv_play)
    TextView mTvPlay;

    DynamicActionModel mDynamicActionModel;
    public static String dynamicModel = "dynamicModel";
    @BindView(R2.id.rl_play_action)
    RelativeLayout mRlPlayAction;
    @BindView(R2.id.view_line)
    View mViewLine;

    @BindView(R2.id.progress_download)
    ProgressBar mProgressDownload;

    public static int REQUEST_CODE = 1000;
    public static String DELETE_RESULT = "delete_action";
    public static String DELETE_ACTIONID = "delete_action_id";
    private boolean isShowHibitsDialog = false;

    Unbinder mUnbinder;

    public static void launch(Activity context, DynamicActionModel mDynamicActionModel) {
        Intent intent = new Intent(context, ActionDetailDynamicActivity.class);
        intent.putExtra(dynamicModel, mDynamicActionModel);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mDynamicActionModel = (DynamicActionModel) getIntent().getSerializableExtra(dynamicModel);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DownLoadActionManager.getInstance(this).addDownLoadActionListener(this);
        AppStatusUtils.setBtBussiness(true);
    }

    /**
     * 初始化UI
     */

    protected void initUI() {
        mTvActionName.setText(mDynamicActionModel.getActionName());
        mTvActionCreateTime.setText(SkinManager.getInstance().getTextById(R.string.actions_my_works_details_time) + " " + TimeTools.format(mDynamicActionModel.getActionDate()));
        mTvActionTime.setText(TimeTools.getMMTime(mDynamicActionModel.getActionTime() * 1000));
        mTvContent.setText(mDynamicActionModel.getActionDesciber());
        Glide.with(this).load(mDynamicActionModel.getActionHeadUrl()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.action_sport_1b)
                .transform(new GlideRoundTransform(this, 10))
                .into(mIvCover);
        if (mDynamicActionModel.getActionStatu() == 1) {
            setPlaBtnAction(2);
        } else if (mDynamicActionModel.getActionStatu() == 2) {
            setPlaBtnAction(3);
        } else if (mDynamicActionModel.getActionStatu() == 0) {
            setPlaBtnAction(1);
        }

        int actionType = mDynamicActionModel.getActionType();
        if (actionType == 1) {//舞蹈
            mTvActionType.setText(SkinManager.getInstance().getTextById(R.string.actions_save_dance));
            mIvActionType1.setImageResource(R.drawable.ic_type_dance);

        } else if (actionType == 2) {//故事
            mTvActionType.setText(SkinManager.getInstance().getTextById(R.string.actions_save_story));
            mIvActionType1.setImageResource(R.drawable.ic_type_story);

        } else if (actionType == 3) {//运动
            mTvActionType.setText(SkinManager.getInstance().getTextById(R.string.actions_save_sport));
            mIvActionType1.setImageResource(R.drawable.ic_type_action);

        } else if (actionType == 4) {//儿歌
            mTvActionType.setText(SkinManager.getInstance().getTextById(R.string.actions_save_child));
            mIvActionType1.setImageResource(R.drawable.ic_type_song);

        } else if (actionType == 5) {//科普
            mTvActionType.setText(SkinManager.getInstance().getTextById(R.string.actions_save_science));
            mIvActionType1.setImageResource(R.drawable.ic_type_science);
        } else {
            mTvActionType.setText(SkinManager.getInstance().getTextById(R.string.actions_save_child));
            mIvActionType1.setImageResource(R.drawable.ic_type_song);
        }

    }

    @OnClick({R2.id.iv_back, R2.id.rl_play_action, R2.id.iv_delete})
    public void onClickView(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finish();

        } else if (i == R.id.rl_play_action) {
            playAction();

        } else if (i == R.id.iv_delete) {
            showDeleteDialog();


        } else {
        }
    }

    /**
     * 播放按钮
     */
    private void playAction() {
        if (!mPresenter.isBlueConnected()) {
            showBluetoothConnectDialog();
            return;
        }

        if (AppStatusUtils.isLowPower()) {
            BaseLowBattaryDialog.getInstance().showLow5Dialog(new BaseLowBattaryDialog.IDialog5Click() {
                @Override
                public void onOK() {

                }
            });
            return;
        }
        int actionStatu = mDynamicActionModel.getActionStatu();
        if (actionStatu == 0) {
            if (mDynamicActionModel.isDownload()) {//已经下载
                setPlaBtnAction(2);
                DownLoadActionManager.getInstance(this).playAction(true, mDynamicActionModel);
                mDynamicActionModel.setActionStatu(1);
            } else {//没有下载，需要下载
                DownLoadActionManager.getInstance(this).readNetworkStatus();
                DownLoadActionManager.getInstance(this).downRobotAction(mDynamicActionModel);
            }
        } else if (actionStatu == 1) {//正在播放
            DownLoadActionManager.getInstance(this).stopAction(true);
            mDynamicActionModel.setActionStatu(0);
            setPlaBtnAction(1);
        } else if (actionStatu == 2) {//正在下载
            setPlaBtnAction(3);
        }
    }


    /**
     * 设置播放按钮状态 1播放状态 2暂停状态 3下载状态
     *
     * @param type
     */
    public void setPlaBtnAction(int type) {
        if (type == 1) {
            mProgressDownload.setVisibility(View.GONE);
            mTvPlay.setVisibility(View.VISIBLE);
            mTvPlay.setText(SkinManager.getInstance().getTextById(R.string.actions_my_works_details_play));
            Drawable drawable = getResources().getDrawable(R.drawable.ic_btn_play2);
            mTvPlay.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (type == 2) {
            mProgressDownload.setVisibility(View.GONE);
            mTvPlay.setVisibility(View.VISIBLE);
            mTvPlay.setText(SkinManager.getInstance().getTextById(R.string.actions_download_details_stop));
            Drawable drawable = getResources().getDrawable(R.drawable.ic_btn_stop2);
            mTvPlay.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (type == 3) {
            mProgressDownload.setVisibility(View.VISIBLE);
            mTvPlay.setVisibility(View.GONE);
            if ((int) mDynamicActionModel.getDownloadProgress() == 0) {
                mTvPlay.setVisibility(View.VISIBLE);
                mTvPlay.setText(SkinManager.getInstance().getTextById(R.string.actions_my_works_details_loading));
                mTvPlay.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                mTvPlay.setVisibility(View.GONE);
                mProgressDownload.setProgress((int) mDynamicActionModel.getDownloadProgress());
            }

        }
    }


    @Override
    public int getContentViewId() {
        return R.layout.activity_action_detail_dynamic;
    }

    @Override
    public void setDynamicData(boolean statu, int type, List<DynamicActionModel> list) {

    }

    /**
     * 删除结果
     *
     * @param isSuccess
     */
    @Override
    public void deleteActionResult(boolean isSuccess) {
        LoadingDialog.dismiss(this);
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra(DELETE_RESULT, true);
            intent.putExtra(DELETE_ACTIONID, mDynamicActionModel.getActionId());
            setResult(REQUEST_CODE, intent);
            finish();
        }
    }

    @Override
    public void getRobotActionLists(List<String> list) {

    }

    @Override
    public void getDownLoadProgress(DynamicActionModel info, DownloadProgressInfo downloadProgressInfo) {
        if (info.getActionId() == mDynamicActionModel.getActionId()) {
            if (downloadProgressInfo.status == 1) {//正在下载
                String progress = downloadProgressInfo.progress;
                mDynamicActionModel.setDownloadProgress(Double.parseDouble(downloadProgressInfo.progress));
                setPlaBtnAction(3);
                ViseLog.d("praseDownloadData", "progress=====" + progress);
            } else if (downloadProgressInfo.status == 2) {//下载成功后立即播放
                mDynamicActionModel.setActionStatu(1);
                DownLoadActionManager.getInstance(this).playAction(true, mDynamicActionModel);
                setPlaBtnAction(2);
            } else if (downloadProgressInfo.status == 3) {//机器人未联网
                //ToastUtils.showShort("机器人未联网");
                mDynamicActionModel.setActionStatu(0);
                setPlaBtnAction(1);
            } else {//下载失败
               // ToastUtils.showShort("下载失败");
                mDynamicActionModel.setActionStatu(0);
                setPlaBtnAction(1);
            }
        }
    }

    @Override
    public void playActionFinish(String actionName) {
        if (actionName.contains(mDynamicActionModel.getActionOriginalId())) {
            mDynamicActionModel.setActionStatu(0);
            setPlaBtnAction(1);
        }
    }

    @Override
    public void onBlutheDisconnected() {
        showBluetoothConnectDialog();
    }

    @Override
    public void doActionPlay(long actionId, int statu) {

    }

    @Override
    public void doTapHead() {
        mDynamicActionModel.setActionStatu(0);
        setPlaBtnAction(1);
    }

    @Override
    public void isAlpha1EConnectNet(boolean statu) {
        if (!statu) {
            mDynamicActionModel.setActionStatu(0);
            setPlaBtnAction(1);
            mPresenter.showNetWorkConnectDialog(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        DownLoadActionManager.getInstance(this).removeDownLoadActionListener(this);
        AppStatusUtils.setBtBussiness(true);
    }


    //显示蓝牙连接对话框
    private void showDeleteDialog() {
        new BaseDialog.Builder(this)
                .setMessage(SkinManager.getInstance().getTextById(R.string.actions_download_details_delete))
                .setConfirmButtonId(R.string.base_cancel)
                .setConfirmButtonColor(R.color.black)
                .setCancleButtonID(R.string.base_delete)
                .setCancleButtonColor(R.color.base_color_red)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.button_confirm) {
                            dialog.dismiss();
                        } else if (view.getId() == R.id.button_cancle) {
                            mPresenter.deleteActionById(mDynamicActionModel.getActionId());
                            LoadingDialog.show(ActionDetailDynamicActivity.this);
                            dialog.dismiss();
                        }

                    }
                }).create().show();

    }

    //显示蓝牙连接对话框
    private void showBluetoothConnectDialog() {
        BaseBTDisconnectDialog.getInstance().show(new BaseBTDisconnectDialog.IDialogClick() {
            @Override
            public void onConnect() {
                ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).navigation();
                BaseBTDisconnectDialog.getInstance().dismiss();
            }

            @Override
            public void onCancel() {
                BaseBTDisconnectDialog.getInstance().dismiss();
            }
        });
    }

}
