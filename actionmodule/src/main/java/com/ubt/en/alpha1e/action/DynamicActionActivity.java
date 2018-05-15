package com.ubt.en.alpha1e.action;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.adapter.DynamicActionAdapter;
import com.ubt.en.alpha1e.action.contact.DynamicActionContract;
import com.ubt.en.alpha1e.action.model.DownloadProgressInfo;
import com.ubt.en.alpha1e.action.model.DynamicActionModel;
import com.ubt.en.alpha1e.action.presenter.DynamicActionPrenster;
import com.ubt.en.alpha1e.action.util.DownLoadActionManager;
import com.ubt.globaldialog.customDialog.ConfirmDialog;
import com.ubt.globaldialog.customDialog.loading.LoadingDialog;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DynamicActionActivity extends MVPBaseActivity<DynamicActionContract.View, DynamicActionPrenster> implements DynamicActionContract.View, BaseQuickAdapter.OnItemChildClickListener, DownLoadActionManager.DownLoadActionListener, BaseQuickAdapter.OnItemClickListener {
    private static String TAG = DynamicActionActivity.class.getSimpleName();

    /**
     * 获取机器人动作列表超时
     */
    private static int HANDLE_GET_ROBOTACTIONLIST_TIMEOUT = 1115;

    public static int REQUEST_CODE = 10001;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R2.id.recyclerview_dynamic)
    RecyclerView mRecyclerviewDynamic;
    DynamicActionAdapter mDynamicActionAdapter;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    @BindView(R2.id.iv_back)
    ImageView mIvBack;
    private List<DynamicActionModel> mDynamicActionModels = new ArrayList<>();//原创列表
    private List<String> robotActionList = new ArrayList<>();//机器人下载列表

    private View emptyView;
    private TextView tvEmpty;
    private TextView tvRetry;
    private LinearLayout llError;
    private ImageView ivStatu;
    private int playingPosition = -1;
    private String mParam1;
    private String mParam2;

    private int currentType = 0;//上拉下拉类型
    private boolean isNoneFinishLoadMore = false;//是否可以上拉加载 true不能上拉 false 可以上拉

    private int page = 1;
    private int offset = 8;

    private boolean isShowHibitsDialog = false;
    private boolean isShowLowBarry = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_dynamic_action;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        DownLoadActionManager.getInstance(this).addDownLoadActionListener(this);
        initUI();
    }


    protected void initUI() {
        mDynamicActionAdapter = new DynamicActionAdapter(R.layout.action_dynamic_item, mDynamicActionModels);
        mDynamicActionAdapter.setOnItemChildClickListener(this);
        mDynamicActionAdapter.setOnItemClickListener(this);
        mDynamicActionAdapter.setHasStableIds(true);
        mRecyclerviewDynamic.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerviewDynamic.setAdapter(mDynamicActionAdapter);
        emptyView = LayoutInflater.from(this).inflate(R.layout.action_layout_empty, null);
        tvEmpty = (TextView) emptyView.findViewById(R.id.tv_no_data);
        tvRetry = emptyView.findViewById(R.id.tv_retry);
        llError = emptyView.findViewById(R.id.ll_error_layout);
        ivStatu = emptyView.findViewById(R.id.iv_no_data);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                mPresenter.getDynamicData(0, page, offset);
                ViseLog.d("tvRetry", "重试一次");
                LoadingDialog.show(DynamicActionActivity.this);

            }
        });
        //触发自动刷新
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setEnableAutoLoadmore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setEnableScrollContentWhenLoaded(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                mPresenter.getDynamicData(0, page, offset);

            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ++page;
                mPresenter.getDynamicData(1, page, offset);
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    /**
     * 刷新获取数据
     *
     * @param statu 是否刷新成功
     * @param type
     * @param list
     */
    @Override
    public void setDynamicData(boolean statu, int type, List<DynamicActionModel> list) {
        currentType = type;
        if (statu) {
            refreshData(type, list);
        } else {//请求失败
            if (mDynamicActionModels.size() == 0) {//如果请求失败切列表数据为0，则显示错误页面
                showStatuLayout(2);
            } else {
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadmore();
            }
        }
        LoadingDialog.dismiss(this);
    }

    @Override
    public void deleteActionResult(boolean isSuccess) {

    }

    /**
     * 刷新数据后，如果机器人联网则从机器人拿取已下载的动作列表
     *
     * @param type 0下拉刷新 1上拉加载
     * @param list
     */
    private void refreshData(int type, List<DynamicActionModel> list) {
        if (null != list) {
            if (type == 0) {
                mDynamicActionModels.clear();
                mDynamicActionModels.addAll(list);
            } else if (type == 1) {
                mDynamicActionModels.addAll(list);
            }
            if (list.size() < 8) {
                isNoneFinishLoadMore = true;
            } else {
                isNoneFinishLoadMore = false;
            }
            if (mPresenter.isBlueConnected() && type == 0) {//蓝牙连接成功则从机器人获取列表动作
                DownLoadActionManager.getInstance(this).getRobotAction();
                mHandler.sendEmptyMessageDelayed(HANDLE_GET_ROBOTACTIONLIST_TIMEOUT, 3000);
            } else {
                mPresenter.praseGetRobotData(this, robotActionList, mDynamicActionModels);
                finishRefresh();
            }

        }
        ViseLog.d(TAG, "size====" + mDynamicActionModels.size());
    }


    /**
     * 显示空View还是Error View 1表示Empty 2表示Error
     *
     * @param status
     */
    private void showStatuLayout(int status) {
        if (status == 1) {
            tvEmpty.setVisibility(View.VISIBLE);
            llError.setVisibility(View.GONE);
            tvEmpty.setText(SkinManager.getInstance().getTextById(R.string.action_empty_no_dynamiaction));
            ivStatu.setImageResource(R.drawable.ic_setting_action_deafult);
        } else if (status == 2) {
            tvRetry.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tvRetry.getPaint().setAntiAlias(true);//抗锯齿
            tvEmpty.setVisibility(View.GONE);
            llError.setVisibility(View.VISIBLE);
            ivStatu.setImageResource(R.drawable.ic_setting_action_deafult);
            mRefreshLayout.setEnableRefresh(false);
            mRefreshLayout.setEnableLoadmore(false);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadmore();
        }
        mDynamicActionAdapter.setEmptyView(emptyView);
    }

    /**
     * 按钮点击事件
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        int i = view.getId();
        if (i == R.id.rl_play_action) {
            playAction(position);

        } else {
        }
    }

    /**
     * Item点击播放事件
     *
     * @param position
     */
    private void playAction(int position) {
        if (!mPresenter.isBlueConnected()) {
            showBluetoothConnectDialog();
            return;
        }

//        if (BaseHelper.isLowBatteryNotExecuteAction) {
//            showLowBatteryDialog();
//            return;
//        }

        mPresenter.playAction(this, position, mDynamicActionModels);
        mDynamicActionAdapter.notifyDataSetChanged();

    }

    private void showLowBatteryDialog() {
        isShowLowBarry = true;
        new ConfirmDialog(this).builder()
                .setTitle("提示")
                .setMsg("机器人电量低动作不能执行，请充电！")
                .setCancelable(true)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isShowLowBarry = false;
                        //调到主界面
                        ViseLog.d(TAG, "确定 ");
                    }
                }).show();
    }


    /**
     * Item点击事件
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ActionDetailDynamicActivity.launch(this, mDynamicActionModels.get(position));

    }

    /**
     * 详情页删除actionId回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ViseLog.d("onActivityResult=======================");
        if (requestCode == 1000) {
            if (resultCode == ActionDetailDynamicActivity.REQUEST_CODE) {
                boolean isDelete = data.getBooleanExtra(ActionDetailDynamicActivity.DELETE_RESULT, false);
                int actionId = data.getIntExtra(ActionDetailDynamicActivity.DELETE_ACTIONID, 0);
                ViseLog.d("onActivityResult=======================actionId===" + actionId + "   isDelete==" + isDelete
                        + "   mDynamicActionModels.size()===" + mDynamicActionModels.size());

                if (isDelete && actionId != 0) {
                    for (int i = 0; i < mDynamicActionModels.size(); i++) {
                        if (mDynamicActionModels.get(i).getActionId() == actionId) {
                            mDynamicActionModels.remove(i);
                            break;
                        }
                    }
                    if (mDynamicActionModels.size() == 0) {
                        mRefreshLayout.setEnableLoadmore(false);
                        showStatuLayout(1);
                    }
                    mDynamicActionAdapter.notifyDataSetChanged();
                }
            } else if (resultCode == REQUEST_CODE) {

            }
        }
    }

    /**
     * 结束刷新事件
     */
    private void finishRefresh() {
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadmore(true);
        if (currentType == 0) {
            mRefreshLayout.finishRefresh();
            if (isNoneFinishLoadMore) {
                mRefreshLayout.setLoadmoreFinished(true);//将不会再次触发加载更多事件
            } else {
                mRefreshLayout.resetNoMoreData();
            }
        } else if (currentType == 1) {
            if (isNoneFinishLoadMore) {
                mRefreshLayout.finishLoadmoreWithNoMoreData();//将不会再次触发加载更多事件
            } else {
                mRefreshLayout.finishLoadmore();
            }
        }
        if (null == mDynamicActionModels || mDynamicActionModels.size() == 0) {//数据为空
            mRefreshLayout.setEnableLoadmore(false);
            showStatuLayout(1);
        }
        mDynamicActionAdapter.notifyDataSetChanged();
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLE_GET_ROBOTACTIONLIST_TIMEOUT) {
                finishRefresh();
            }
        }
    };


    /**
     * 获取机器人动作列表
     *
     * @param list
     */
    @Override
    public void getRobotActionLists(List<String> list) {
        ViseLog.d(TAG, "获取机器人列表==" + list.toString());
        this.robotActionList = list;
        mHandler.removeMessages(HANDLE_GET_ROBOTACTIONLIST_TIMEOUT);
        mPresenter.praseGetRobotData(this, list, mDynamicActionModels);
        finishRefresh();
    }

    /**
     * 下载进度
     *
     * @param info
     * @param progressInfo
     */
    @Override
    public void getDownLoadProgress(DynamicActionModel info, DownloadProgressInfo progressInfo) {
        mPresenter.praseDownloadData(this, progressInfo, mDynamicActionModels);
        mDynamicActionAdapter.notifyDataSetChanged();
    }

    /**
     * 动作播放结束
     *
     * @param actionName
     */
    @Override
    public void playActionFinish(String actionName) {
        for (int i = 0; i < mDynamicActionModels.size(); i++) {
            if (actionName.contains(mDynamicActionModels.get(i).getActionOriginalId())) {
                mDynamicActionModels.get(i).setActionStatu(0);
                break;
            }
        }
        mDynamicActionAdapter.notifyDataSetChanged();
    }

    /**
     * 蓝牙掉线
     */
    @Override
    public void onBlutheDisconnected() {//机器人掉线
        ViseLog.d(TAG, "机器人掉线");
        for (int i = 0; i < mDynamicActionModels.size(); i++) {
            mDynamicActionModels.get(i).setActionStatu(0);
        }
        mDynamicActionAdapter.notifyDataSetChanged();
    }

    /**
     * 在详情页播放或者暂停，可以同步到列表页面
     *
     * @param actionid
     * @param statu    0结束动作 1播放动作
     */
    @Override
    public void doActionPlay(long actionid, int statu) {
        for (int i = 0; i < mDynamicActionModels.size(); i++) {
            if (mDynamicActionModels.get(i).getActionId() == actionid) {
                ViseLog.d(TAG, "actionName==" + mDynamicActionModels.get(i));
                mDynamicActionModels.get(i).setActionStatu(statu);
                if (statu == 1) {
                    DynamicActionModel model = DownLoadActionManager.getInstance(this).getPlayingInfo();
                    if (null != model && model.getActionId() != actionid) {
                        int postion = mPresenter.getPositionById(model.getActionId(), mDynamicActionModels);
                        mDynamicActionModels.get(postion).setActionStatu(0);
                    }
                }
                break;
            }

        }
        mDynamicActionAdapter.notifyDataSetChanged();
    }

    /**
     * 拍头打断
     */
    @Override
    public void doTapHead() {
        for (int i = 0; i < mDynamicActionModels.size(); i++) {
            if (mDynamicActionModels.get(i).getActionStatu() == 1) {
                ViseLog.d(TAG, "actionName==" + mDynamicActionModels.get(i));
                mDynamicActionModels.get(i).setActionStatu(0);
            }
        }
        mDynamicActionAdapter.notifyDataSetChanged();
    }

    @Override
    public void isAlpha1EConnectNet(boolean statu) {
        if (!statu) {
            for (int i = 0; i < mDynamicActionModels.size(); i++) {
                if (mDynamicActionModels.get(i).getActionStatu() == 2) {
                    ViseLog.d(TAG, "actionName==" + mDynamicActionModels.get(i));
                    mDynamicActionModels.get(i).setActionStatu(0);
                }
            }
            showNetWorkConnectDialog();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ViseLog.d(TAG, "--------------onDestory-----------");
        DownLoadActionManager.getInstance(this).removeDownLoadActionListener(this);
    }

    //显示蓝牙连接对话框
    private void showBluetoothConnectDialog() {
        new ConfirmDialog(this).builder()
                .setTitle("提示")
                .setMsg("请先连接蓝牙和Wi-Fi")
                .setCancelable(true)
                .setPositiveButton("去连接", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViseLog.d(TAG, "去连接蓝牙 ");
//                        Intent intent = new Intent();
//                        intent.putExtra(com.ubt.alpha1e.base.Constant.BLUETOOTH_REQUEST, true);
//                        intent.setClass(this, BluetoothconnectActivity.class);
//                        startActivityForResult(intent, REQUEST_CODE);
                    }
                }).show();
    }


    //显示网络连接对话框
    private void showNetWorkConnectDialog() {
        new ConfirmDialog(this).builder()
                .setTitle("提示")
                .setMsg("请先连接机器人Wi-Fi")
                .setCancelable(true)
                .setPositiveButton("去连接", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViseLog.d(TAG, "去连接Wifi ");
//                        Intent intent = new Intent();
//                        intent.setClass(this, NetconnectActivity.class);
//                        startActivity(intent);
                    }
                }).show();
    }

}
