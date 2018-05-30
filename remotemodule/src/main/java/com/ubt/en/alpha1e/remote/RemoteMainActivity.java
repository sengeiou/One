package com.ubt.en.alpha1e.remote;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.en.alpha1e.remote.contract.RemoteMainContact;
import com.ubt.en.alpha1e.remote.model.RemoteMainAdapter;
import com.ubt.en.alpha1e.remote.presenster.RemoteMainPrenster;

/**
 * 遥控器列表页面
 */

@Route(path = ModuleUtils.Joystick_ActionProgram)
public class RemoteMainActivity extends MVPBaseActivity<RemoteMainContact.View, RemoteMainPrenster> implements RemoteMainContact.View, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private ImageView imgBack;
    private ImageView imgClose;

    private RelativeLayout rlTip;

    private RecyclerView mRecyclerView;

    private RemoteMainAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_remote_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   DBManager dbManager = new DBManager(this);
        mPresenter.init(this);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppStatusUtils.setBtBussiness(true);
    }

    private void initView() {
        imgBack = findViewById(R.id.remote_iv_back);
        imgClose = findViewById(R.id.iv_close_publish);
        rlTip = findViewById(R.id.rl_head_tip);
        imgBack.setOnClickListener(this);
        imgClose.setOnClickListener(this);
        boolean isHowTip = SPUtils.getInstance().getBoolean(Constant1E.REMOTE_SHOW_TIP, false);
        if (!isHowTip) {
            rlTip.setVisibility(View.VISIBLE);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_my_roles);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 30;
                outRect.left = 30;
            }
        });
        mAdapter = new RemoteMainAdapter(R.layout.remote_adapter_main_item, mPresenter.getRoleInfos());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.remote_iv_back) {
            finish();

        } else if (i == R.id.iv_close_publish) {
            rlTip.setVisibility(View.GONE);
            SPUtils.getInstance().put(Constant1E.REMOTE_SHOW_TIP, true);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unRegister();
        AppStatusUtils.setBtBussiness(false);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mPresenter.getRoleInfos().get(position).getBz() == 0) {
            //startActivity(new Intent(this,RemoteActivity.class));
            RemoteActivity.launch(this, position + 1);
        }
    }
}
