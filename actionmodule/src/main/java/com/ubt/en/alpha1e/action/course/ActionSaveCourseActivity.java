package com.ubt.en.alpha1e.action.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.BitmapUtil;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.R2;
import com.ubt.en.alpha1e.action.adapter.SelectGridAdapter;
import com.ubt.en.alpha1e.action.contact.SaveActionContact;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.presenter.SaveActionPrenster;
import com.ubt.en.alpha1e.action.util.CourseArrowAminalUtil;
import com.ubt.htslib.base.NewActionInfo;
import com.vise.log.ViseLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActionSaveCourseActivity extends MVPBaseActivity<SaveActionContact.View, SaveActionPrenster> implements SaveActionContact.View, BaseQuickAdapter.OnItemClickListener {

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
        mEdtDisc.setEnabled(false);
        setLeftImageShow();
        mIvSave.setEnabled(false);
        mEdtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 这里可以知道你已经输入的字数，大家可以自己根据需求来自定义文本控件实时的显示已经输入的字符个数
                Log.e("此时你已经输入了", "" + s.length());

                int after_length = s.length();// 输入内容后编辑框所有内容的总长度
                // 如果字符添加后超过了限制的长度，那么就移除后面添加的那一部分，这个很关键
                if (after_length > 1) {
                    mEdtName.setFocusable(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEdtName.getWindowToken(), 0); //强制隐藏键盘
                    mSelectGridAdapter.setisShowArrow(true);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
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
        if (position == 2) {
            CourseArrowAminalUtil.startViewAnimal(true, mIvSaveArrow, 1);
            mIvSave.setEnabled(true);
            mSelectGridAdapter.setisShowArrow(false);
        }
    }


    @OnClick({R2.id.iv_demo1, R2.id.iv_demo2, R2.id.iv_demo3, R2.id.iv_save, R2.id.iv_back})
    public void ClickView(View view) {
        int id = view.getId();
        if (id == R.id.iv_demo1) {
            selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[0]);
            mImgActionLogo.setImageResource(selectModel.getLeftSelectedImage());
        } else if (id == R.id.iv_demo2) {
            selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[1]);
            mImgActionLogo.setImageResource(selectModel.getLeftSelectedImage());
        } else if (id == R.id.iv_demo3) {
            selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[2]);
            mImgActionLogo.setImageResource(selectModel.getLeftSelectedImage());
        } else if (id == R.id.iv_save) {
            CourseArrowAminalUtil.startViewAnimal(false, mIvSaveArrow, 1);
            Intent intent = new Intent();
            intent.putExtra(ActionsEditHelper.SaveActionResult, true);
            setResult(ActionsEditHelper.SaveActionReq, intent);
            finish();
        } else if (id == R.id.iv_back) {
            finish();
        }
    }


    /**
     * 设置左边图片展示
     */
    private void setLeftImageShow() {
        mEdtDisc.setHint(selectModel.getActionDescrion());

        selectModel.setBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getLeftSelectedImage(), 2));

        mIvDemo1.setImageBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getImageTypeArray()[0], 2));
        mIvDemo2.setImageBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getImageTypeArray()[1], 2));
        mIvDemo3.setImageBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getImageTypeArray()[2], 2));

        mImgActionLogo.setImageBitmap(BitmapUtil.compressImage(this.getResources(), selectModel.getImageTypeArray()[0], 2));
        selectModel.setLeftSelectedImage(selectModel.getImageTypeArray()[0]);
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

    }

    @Override
    public void saveActionFailed() {
        BaseLoadingDialog.dismiss(this);
    }


}
