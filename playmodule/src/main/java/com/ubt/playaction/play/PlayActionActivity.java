package com.ubt.playaction.play;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.playaction.R;
import com.ubt.playaction.R2;
import com.ubt.playaction.model.ActionData;
import com.vise.log.ViseLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifImageView;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */

@Route(path = ModuleUtils.Playcenter_module)
public class PlayActionActivity extends MVPBaseActivity<PlayActionContract.View, PlayActionPresenter> implements PlayActionContract.View,BaseQuickAdapter.OnItemClickListener {

    @BindView(R2.id.play_iv_back)
    ImageView ivBack;
    @BindView(R2.id.tv_select)
    TextView tvSelect;
    @BindView(R2.id.rv_action_list)
    RecyclerView rvActionList;
    @BindView(R2.id.iv_play_icon)
    ImageView ivPlay;
    @BindView(R2.id.gifPlaying)
    GifImageView gifPlaying;
    @BindView(R2.id.tv_play_name)
    TextView tvPlayName;
    @BindView(R2.id.iv_playlist)
    ImageView ivPlayList;
    @BindView(R2.id.iv_reset)
    ImageView ivReset;
    @BindView(R2.id.iv_play_pause)
    ImageView ivPlayPause;
    @BindView(R2.id.iv_cycle)
    ImageView ivCycleState;
    Unbinder unbinder;

    PlayActionAdapter actionAdapter;
    List<ActionData> actionDataList = new ArrayList<ActionData>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        mPresenter.register(this);
        if(BlueClientUtil.getInstance().getConnectionState() == 3){ //判断蓝牙是否连接
            mPresenter.getActionList();
        }
        init();
    }

    private void init() {
        actionAdapter = new PlayActionAdapter(R.layout.item_play_action, actionDataList,this);
        actionAdapter.setOnItemClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvActionList.setLayoutManager(gridLayoutManager);
        rvActionList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 25;
                outRect.left = 25;
                outRect.top = 20;
            }
        });
        rvActionList.setAdapter(actionAdapter);
    }

    @OnClick({R2.id.play_iv_back,R2.id.tv_select,R2.id.iv_playlist, R2.id.iv_reset, R2.id.iv_play_pause})
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.play_iv_back){
            finish();
        }else if(id == R.id.tv_select){
            //TODO
        }else if(id == R.id.iv_playlist){
            //TODO
        }else if(id == R.id.iv_reset) {

        }else if(id == R.id.iv_play_pause){

        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_play_action;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ToastUtils.showShort("item onItemClick");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void getActionList(List<ActionData> actionList) {
        ViseLog.d("actionDataList:" + actionList.toString());
        actionDataList.clear();
        actionDataList.addAll(actionList);
        actionAdapter.notifyDataSetChanged();
    }
}
