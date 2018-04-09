package com.ubt.setting.mainuser;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.setting.R;
import com.ubt.setting.R2;
import com.ubt.setting.notice.NoticeFragment;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @author：liuhai
 * @date：2017/10/27 11:52
 * @modifier：ubt
 * @modify_date：2017/10/27 11:52
 * 个人中心主界面
 * version
 */
@Route(path = ModuleUtils.Setting_UserCenterActivity)
public class UserCenterActivity extends MVPBaseActivity<UserCenterContact.UserCenterView, UserCenterImpPresenter> implements UserCenterContact.UserCenterView, NoticeFragment.CallBackListener {
    private static final String TAG = UserCenterActivity.class.getSimpleName();

    @BindView(R2.id.tv_main_title)
    TextView mTvTitle;
    @BindView(R2.id.rl_leftmenu)
    RecyclerView mRecyclerView;
    @BindView(R2.id.fl_main_content)
    FrameLayout mViewPager;
    @BindView(R2.id.iv_main_back)
    ImageView mIvMainBack;
    private List<LeftMenuModel> mMenuModels = new ArrayList<>();//左侧菜单栏信息
    private List<Fragment> mFragmentList = new ArrayList<>();
    private BaseQuickAdapter mBaseQuickAdapter;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mCurrentFragment;

    private int mCurrentPosition = -1;

    String[] mStrings = new String[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_center);
        mPresenter.initData(this);
        mPresenter.getUnReadMessage();
        initUI();
    }

    @Override
    public int getContentViewId() {
        return R.layout.setting_activity_user_center;
    }


    @OnClick(R2.id.iv_main_back)
    public void onBack(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViseLog.d(TAG, "------onPause---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViseLog.d(TAG, "------onDestroy---");
    }

    /**
     * 初始化数据
     */

    protected void initUI() {
        mFragmentManager = this.getSupportFragmentManager();
        mFragmentTransaction = this.mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fl_main_content, mFragmentList.get(0));
        mFragmentTransaction.commit();
        mCurrentFragment = mFragmentList.get(0);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_leftmenu);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCurrentPosition = 0;
        //mTvTitle.setText(mMenuModels.get(0).getNameString());
        mMenuModels.get(0).setChick(true);
        mBaseQuickAdapter = new LeftAdapter(R.layout.setting_layout_usercenter_left_item, mMenuModels);

        mBaseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LeftMenuModel menuModel = mMenuModels.get(position);
                // mTvTitle.setText(menuModel.getNameString());
                loadFragment(mFragmentList.get(position));
                for (int i = 0; i < mMenuModels.size(); i++) {
                    if (mMenuModels.get(i).getNameString().equals(menuModel.getNameString())) {
                        mMenuModels.get(i).setChick(true);
                    } else {
                        mMenuModels.get(i).setChick(false);
                    }
                }
                mBaseQuickAdapter.notifyDataSetChanged();
            }


        });
        mRecyclerView.setAdapter(mBaseQuickAdapter);
    }

    /**
     * 重新加载Fragment
     *
     * @param targetFragment 加载的Fragment
     */
    private void loadFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // UbtLog.d(TAG,"targetFragment.isAdded()->>>"+(!targetFragment.isAdded()));
        if (!targetFragment.isAdded()) {
            mCurrentFragment.onStop();
            transaction
                    .hide(mCurrentFragment)
                    .add(R.id.fl_main_content, targetFragment)
                    .commit();
        } else {
            mCurrentFragment.onStop();
            targetFragment.onResume();

            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment)
                    .commit();
        }
        mCurrentFragment = targetFragment;
    }



    @Override
    public void loadData(List<LeftMenuModel> list, List<Fragment> fragments) {
        mMenuModels.clear();
        mMenuModels.addAll(list);
        mFragmentList.clear();
        mFragmentList.addAll(fragments);
    }

    @Override
    public void getUnReadMessage(boolean isSuccess, int count) {
        if (count > 0) {
            mMenuModels.get(2).setCountUnRead(count);
            mBaseQuickAdapter.notifyDataSetChanged();
        }
    }


    public void onChangeUnReadMessage() {
        ViseLog.d("Notice","onChangeUnReadMessage==");
        int unReadCount = mMenuModels.get(2).getCountUnRead();
        mMenuModels.get(2).setCountUnRead(unReadCount - 1);
        mBaseQuickAdapter.notifyDataSetChanged();
    }


    public class LeftAdapter extends BaseQuickAdapter<LeftMenuModel, BaseViewHolder> {

        public LeftAdapter(@LayoutRes int layoutResId, @Nullable List<LeftMenuModel> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, LeftMenuModel item) {
            helper.setText(R.id.tv_item_name, item.getNameString());
            TextView barView = helper.getView(R.id.bar_num);
            Drawable drawable = getResources().getDrawable(item.getImageId());
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            TextView tv = helper.getView(R.id.tv_item_name);
            tv.setCompoundDrawables(drawable, null, null, null);
            if (item.isChick()) {
                tv.setEnabled(true);
                //  helper.setBackgroundColor(R.id.tv_item_name, getContext().getResources().getColor(R.color.txt_info));
            } else {
                //  helper.setBackgroundColor(R.id.tv_item_name, getContext().getResources().getColor(R.color.white));
                tv.setEnabled(false);
            }
            if (item.getNameString().equals("消息")) {
                if (item.getCountUnRead() > 0) {
                    barView.setVisibility(View.VISIBLE);
                    if (item.getCountUnRead() < 100) {
                        barView.setText(item.getCountUnRead()+"");
                    } else {
                        barView.setText("99+");
                    }
                }
            } else {
                barView.setVisibility(View.GONE);
            }

        }
    }
}
