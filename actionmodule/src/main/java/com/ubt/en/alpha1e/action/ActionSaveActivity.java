package com.ubt.en.alpha1e.action;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.BitmapUtil;
import com.ubt.baselib.utils.FileUtils;
import com.ubt.baselib.utils.LQRPhotoSelectUtils;
import com.ubt.baselib.utils.PermissionUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.action.adapter.SelectGridAdapter;
import com.ubt.en.alpha1e.action.contact.SaveActionContact;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.presenter.SaveActionPrenster;
import com.ubt.htslib.base.NewActionInfo;
import com.vise.log.ViseLog;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActionSaveActivity extends MVPBaseActivity<SaveActionContact.View, SaveActionPrenster> implements SaveActionContact.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    @BindView(R2.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R2.id.iv_save)
    ImageView mIvSave;
    @BindView(R2.id.iv_save_arrow)
    ImageView mIvSaveArrow;
    @BindView(R2.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R2.id.img_action_logo)
    ImageView mImgActionLogo;
    @BindView(R2.id.iv_demo1)
    ImageView mIvDemo1;
    @BindView(R2.id.iv_demo2)
    ImageView mIvDemo2;
    @BindView(R2.id.iv_demo3)
    ImageView mIvDemo3;
    @BindView(R2.id.txt_action_type)
    TextView mTxtActionType;
    @BindView(R2.id.txt_action_type_des)
    TextView mTxtActionTypeDes;
    @BindView(R2.id.edt_name)
    EditText mEdtName;
    @BindView(R2.id.txt_select_type)
    TextView mTxtSelectType;
    @BindView(R2.id.grid_actions_type)
    RecyclerView mGridActionsType;
    @BindView(R2.id.edt_disc)
    EditText mEdtDisc;
    @BindView(R2.id.lay_action_info)
    LinearLayout mLayActionInfo;
    RelativeLayout mLayHeadSel;
    private Unbinder mUnbinder;


    private SelectGridAdapter mSelectGridAdapter;

    private ActionTypeModel selectModel;

    private NewActionInfo mCurrentAction;//动作文件
    private String musicDir = "";
    public static String MUSIC_DIR = "music_dir";
    private Uri mImageUri;
    File tempFile;
    /**
     * 拍照获取照片
     */
    public static final int GetUserHeadRequestCodeByShoot = 1001;
    /**
     * 相册获取
     */
    public static final int GetUserHeadRequestCodeByFile = 1002;

    LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    @Override
    public int getContentViewId() {
        return R.layout.activity_action_save;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.init(this);
        mCurrentAction = getIntent().getParcelableExtra(ActionsEditHelper.New_ActionInfo);//get parcelable object
        musicDir = getIntent().getStringExtra(MUSIC_DIR);
        initView();
        ViseLog.d(mCurrentAction.toString());
        AppStatusUtils.setBtBussiness(true);
        AppStatusUtils.setBussiness(true);
    }


    /**
     * 初始化RecyleView Adapter
     */
    private void initView() {
        mGridActionsType.setLayoutManager(new GridLayoutManager(this, 5));
        mSelectGridAdapter = new SelectGridAdapter(R.layout.action_action_type_item, mPresenter.getTypeModelList());
        mGridActionsType.setAdapter(mSelectGridAdapter);
        mSelectGridAdapter.setOnItemClickListener(this);
        selectModel = mPresenter.getTypeModelList().get(0);
        setLeftImageShow();
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
//                // 4、当拍照或从图库选取图片成功后回调

                Bitmap bitmap = null;
                try {
                    bitmap = FileUtils.getBitmapFormUri(ActionSaveActivity.this, outputUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mImgActionLogo.setImageBitmap(bitmap);
                selectModel.setBitmap(bitmap);
            }
        }, false);//true裁剪，false不裁剪
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        AppStatusUtils.setBtBussiness(false);
        AppStatusUtils.setBussiness(false);
    }

    /**
     * 点击动作类型事件
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.selectActionMode(position);
        selectModel = mPresenter.getTypeModelList().get(position);
        setLeftImageShow();
    }


    @OnClick({R2.id.iv_demo1, R2.id.iv_demo2, R2.id.iv_demo3, R2.id.iv_save, R2.id.iv_back, R2.id.img_action_logo})
    public void ClickView(View view) {
        int id = view.getId();
        if (id == R.id.iv_demo1) {
            selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[0]);
            selectModel.setBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getLeftSelectedImage(), 2));
            mImgActionLogo.setImageResource(selectModel.getLeftSelectedImage());
        } else if (id == R.id.iv_demo2) {
            selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[1]);
            selectModel.setBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getLeftSelectedImage(), 2));
            mImgActionLogo.setImageResource(selectModel.getLeftSelectedImage());
        } else if (id == R.id.iv_demo3) {
            selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[2]);
            selectModel.setBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getLeftSelectedImage(), 2));
            mImgActionLogo.setImageResource(selectModel.getLeftSelectedImage());
        } else if (id == R.id.iv_save) {
            saveAction();
        } else if (id == R.id.iv_back) {
            finish();
        } else if (id == R.id.img_action_logo) {
            showBottomDialog();
        }
    }

    private void showBottomDialog() {

        ViewHolder viewHolder = new ViewHolder(R.layout.action_dialog_bottom_photo);

        DialogPlus.newDialog(this)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.txt_shooting) {
                            getShootCamera();
                            dialog.dismiss();
                        } else if (view.getId() == R.id.txt_from_file) {
                            takeImageFromAblum();
                            dialog.dismiss();
                        } else if (view.getId() == R.id.txt_del) {
                            dialog.dismiss();
                        }


                    }
                })
                .create().show();

    }

    public void getShootCamera() {
        PermissionUtils.getInstance().request(new PermissionUtils.PermissionLocationCallback() {
            @Override
            public void onSuccessful() {
                mLqrPhotoSelectUtils.takePhoto();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onRationSetting() {

            }

            @Override
            public void onCancelRationSetting() {

            }
        }, PermissionUtils.PermissionEnum.CAMERA, this);

    }

    /**
     * 从相册获取照片
     */

    public void takeImageFromAblum() {
        mLqrPhotoSelectUtils.selectPhoto();
    }


    /**
     * 修改相片结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    /**
     * 设置左边图片展示
     */
    private void setLeftImageShow() {
        mEdtDisc.setHint(selectModel.getActionDescrion());
        mIvDemo1.setImageResource(selectModel.getImageTypeArray()[0]);
        mIvDemo2.setImageResource(selectModel.getImageTypeArray()[1]);
        mIvDemo3.setImageResource(selectModel.getImageTypeArray()[2]);
        mImgActionLogo.setImageResource(selectModel.getImageTypeArray()[0]);
        selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[0]);
        selectModel.setBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getLeftSelectedImage(), 2));
    }

    /**
     * 刷新RecyleView
     */
    @Override
    public void notifyDataSetChanged() {
        mSelectGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void saveActionSuccess() {
        BaseLoadingDialog.dismiss(this);
//        Intent intent = new Intent();
//        intent.putExtra(ActionsEditHelper.SaveActionResult,true);
//        setResult(ActionsEditHelper.SaveActionReq, intent);
//        finish();
        Intent intent = new Intent(this, SaveSuccessActivity.class);
        startActivity(intent);
        ActionCreateActivity.finishSelf();
        finish();
    }

    @Override
    public void saveActionFailed() {
        BaseLoadingDialog.dismiss(this);
    }


    private void saveAction() {
        if (mEdtName.getText().toString().equals("")) {
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_action_name_empty));
            return;
        }

        if (!mPresenter.isRightName(mEdtName.getText().toString(), -1, false, "")) {
            return;
        }

        int length = mEdtDisc.getText().toString().length();

        if (length > 100) {
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_about_feedback_input_too_long));
            return;
        }

        if (TextUtils.isEmpty(mEdtDisc.getText().toString())) {
            mCurrentAction.actionDesciber = mEdtDisc.getHint().toString();
        } else {
            mCurrentAction.actionDesciber = mEdtDisc.getText().toString();
        }
        mCurrentAction.actionName = mEdtName.getText().toString().replace("\n", "");
        mCurrentAction.actionSonType = selectModel.getActionType();
        mCurrentAction.actionType = selectModel.getActionType();
        mCurrentAction.actionTime = mCurrentAction.getTitleTime() / 1000;
        BaseLoadingDialog.show(this);
        mPresenter.saveNewAction(selectModel, mCurrentAction, musicDir);
    }
}
