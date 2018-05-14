package com.ubt.en.alpha1e.remote;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.mvp.MVPBaseActivity;
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

    @Override
    public int getContentViewId() {
        return R.layout.remote_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
        mPresenter.init(this, 1);
        initView();
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
        mIvTop.setOnClickListener(controlListener);
        mIvLeft.setOnClickListener(controlListener);
        mIvRight.setOnClickListener(controlListener);
        mIvBottom.setOnClickListener(controlListener);
        mIvBottomLeft.setOnClickListener(controlListener);
        mIvBottomRight.setOnClickListener(controlListener);

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
    }


    private View.OnTouchListener viewOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            } else if (event.getAction() == MotionEvent.ACTION_UP) {

                //松开stop

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
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mPresenter.playAction(position + 6);
    }

    @Override
    public void playFinish() {
        if (keepExec) {
            execActions(longClickItem);
        }
    }
}
