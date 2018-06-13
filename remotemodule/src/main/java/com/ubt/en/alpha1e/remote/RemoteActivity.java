package com.ubt.en.alpha1e.remote;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.AppStatusUtils;
import com.ubt.en.alpha1e.remote.contract.RemoteContact;
import com.ubt.en.alpha1e.remote.model.RemoteGridAdapter;
import com.ubt.en.alpha1e.remote.presenster.RemotePrenster;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RemoteActivity extends MVPBaseActivity<RemoteContact.View, RemotePrenster> implements RemoteContact.View, BaseQuickAdapter.OnItemClickListener {


    @BindView(R2.id.remote_iv_back)
    ImageView mRemoteIvBack;
    @BindView(R2.id.remote_title)
    TextView mRemoteTitle;
    @BindView(R2.id.iv_top)
    ImageView mIvTop;
    @BindView(R2.id.iv_left)
    ImageView mIvLeft;
    @BindView(R2.id.iv_right)
    ImageView mIvRight;
    @BindView(R2.id.iv_bottom)
    ImageView mIvBottom;
    @BindView(R2.id.rl_rock)
    RelativeLayout mRlRock;
    @BindView(R2.id.iv_bottom_left)
    ImageView mIvBottomLeft;
    @BindView(R2.id.iv_bottom_right)
    ImageView mIvBottomRight;
    @BindView(R2.id.iv_center_stop)
    ImageView mIvCenterStop;
    @BindView(R2.id.remote_recyleview)
    RecyclerView mRemoteRecyleview;
    private Unbinder mUnbinder;

    RemoteGridAdapter mGridAdapter;

    private boolean keepExec = false;
    private View longClickItem = null;
    private static final int EXEC_STOP_ACTION = 106;


    private List<View> mViewList = new ArrayList<>();
    private int remoteType = 0;

    @Override
    public int getContentViewId() {
        return R.layout.remote_activity;
    }


    public static void launch(Context context, int remoteType) {
        Intent intent = new Intent(context, RemoteActivity.class);
        intent.putExtra("remote_type", remoteType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        remoteType = getIntent().getIntExtra("remote_type", 1);
        mPresenter.init(this, remoteType);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.startOrStopRun((byte) 0x05);
        AppStatusUtils.setBtBussiness(true);
    }

    private void initView() {

        mViewList.clear();
        mViewList.add(mIvTop);
        mViewList.add(mIvLeft);
        mViewList.add(mIvRight);
        mViewList.add(mIvBottom);

        mViewList.add(mIvBottomLeft);
        mViewList.add(mIvBottomRight);

        //方向键可以长按一直执行
        mIvTop.setOnTouchListener(viewOnTouchListener);
        mIvTop.setOnLongClickListener(viewOnLongTouchListener);
        mIvLeft.setOnTouchListener(viewOnTouchListener);
        mIvLeft.setOnLongClickListener(viewOnLongTouchListener);
        mIvRight.setOnTouchListener(viewOnTouchListener);
        mIvRight.setOnLongClickListener(viewOnLongTouchListener);
        mIvBottom.setOnTouchListener(viewOnTouchListener);
        mIvBottom.setOnLongClickListener(viewOnLongTouchListener);

        mIvBottomLeft.setOnTouchListener(viewOnTouchListener);
        mIvBottomLeft.setOnLongClickListener(viewOnLongTouchListener);
        mIvBottomRight.setOnTouchListener(viewOnTouchListener);

        mIvBottomRight.setOnLongClickListener(viewOnLongTouchListener);
        mIvCenterStop.setOnClickListener(controlListener);
//        mIvTop.setOnClickListener(controlListener);
//        mIvLeft.setOnClickListener(controlListener);
//        mIvRight.setOnClickListener(controlListener);
//        mIvBottom.setOnClickListener(controlListener);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRemoteRecyleview.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator animator = mRemoteRecyleview.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mRemoteRecyleview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 30;
                outRect.left = 30;
            }
        });
        mGridAdapter = new RemoteGridAdapter(R.layout.remote_item_grid, mPresenter.getRemoteItems().subList(6, mPresenter.getRemoteItems().size()));
        mRemoteRecyleview.setAdapter(mGridAdapter);
        mGridAdapter.setOnItemClickListener(this);
        mRemoteIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startOrStopRun((byte) 0x06);
                handler.sendEmptyMessage(EXEC_STOP_ACTION);
                RemoteActivity.this.finish();
            }
        });
    }


    private View.OnTouchListener viewOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ViseLog.d("viewOnTouchListener===  ACTION_DOWN" + keepExec);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                //松开stop
                ViseLog.d("viewOnTouchListener  ACTION_UP===" + keepExec);
                if (!keepExec) {
                    return false;
                }
                keepExec = false;
                longClickItem = null;
                handler.sendEmptyMessageDelayed(EXEC_STOP_ACTION, 200);
                lastClickTime = System.currentTimeMillis();
            }
            return false;
        }
    };


    private View.OnLongClickListener viewOnLongTouchListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            longClickItem = view;
            execActions(longClickItem);
            keepExec = true;
            ViseLog.d("viewOnLongTouchListener===" + keepExec);
            return false;
        }
    };
    public int playIndex = -1;

    /**
     * 执行动作
     *
     * @param view
     */
    private void execActions(View view) {
        playIndex = -1;
        for (int i = 0; i < mViewList.size(); i++) {
            if (mViewList.get(i).getId() == view.getId()) {
                playIndex = i;
                break;
            }
        }

        mPresenter.playAction(playIndex);

    }

    long lastClickTime = System.currentTimeMillis();
    private View.OnClickListener controlListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
//            arg0.setClickable(false);
            if (System.currentTimeMillis() - lastClickTime < 1000) {
                ViseLog.d("1000ms才能点击");
                return;
            }
            lastClickTime = System.currentTimeMillis();
            ViseLog.d("controlListener===" + keepExec);
            execActions(arg0);
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case EXEC_STOP_ACTION:
                    mPresenter.playAction(-1);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        AppStatusUtils.setBtBussiness(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.playAction(position + 6);
    }

    @Override
    public void playFinish() {
        ViseLog.d("playFinish===" + keepExec);
        if (keepExec) {
            execActions(longClickItem);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPresenter.startOrStopRun((byte) 0x06);
            handler.sendEmptyMessage(EXEC_STOP_ACTION);
        }
        return super.onKeyDown(keyCode, event);
    }
}
