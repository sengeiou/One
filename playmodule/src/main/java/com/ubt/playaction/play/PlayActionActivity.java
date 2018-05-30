package com.ubt.playaction.play;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
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
    @BindView(R2.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R2.id.iv_select_all)
    ImageView ivSelectAll;
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
    @BindView(R2.id.rl_list)
    RelativeLayout rlPlaylist;
    @BindView(R2.id.iv_close_list)
    ImageView ivCloseList;
    @BindView(R2.id.iv_delete_list)
    ImageView ivDeleteList;
    @BindView(R2.id.iv_list_cycle)
    ImageView ivPlayListCycle;
    @BindView(R2.id.rl_play_btn)
    RelativeLayout rlPlaybtn;
    @BindView(R2.id.iv_play_btn)
    ImageView ivPlayBtn;
    @BindView(R2.id.rl_play_ctrl)
    RelativeLayout rlPlayCtrl;
    @BindView(R2.id.recycleview_playlist)
    RecyclerView rvCycleList;
    Unbinder unbinder;

    CyclePlayActionAdapter cyclePlayActionAdapter;

    PlayActionAdapter actionAdapter;
    List<ActionData> actionDataList = new ArrayList<ActionData>();
//    List<ActionData> actionSelectList = new ArrayList<ActionData>();
    List<ActionData> cycleDataList = new ArrayList<ActionData>();
    List<ActionData> tempDataList = new ArrayList<ActionData>();
    boolean select =false;
    boolean selectAll = false;
    boolean cyclePlay = false;
    int currentPlayPos = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        mPresenter.register(this);
        if(BlueClientUtil.getInstance().getConnectionState() == 3){ //判断蓝牙是否连接
            BaseLoadingDialog.show(this);
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

        cyclePlayActionAdapter = new CyclePlayActionAdapter(R.layout.item_cycle_play_action, cycleDataList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCycleList.setLayoutManager(linearLayoutManager);
        rvCycleList.setAdapter(cyclePlayActionAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        notePlayOrPause();


    }

    @OnClick({R2.id.play_iv_back,R2.id.tv_select,R2.id.iv_playlist, R2.id.iv_reset, R2.id.iv_play_pause,R2.id.iv_close_list,R2.id.iv_delete_list,R2.id.iv_select_all,R2.id.rl_play_btn})
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.play_iv_back){
            finish();
        }else if(id == R.id.tv_select){
            if(!select){
                rlPlayCtrl.setVisibility(View.INVISIBLE);
                rlPlaybtn.setVisibility(View.VISIBLE);
                select = true;
                tvSelect.setText(SkinManager.getInstance().getTextById(R.string.select_cancel));
                tvSelect.setTextColor(getResources().getColor(R.color.text_playlist_title));
                tvSelectAll.setVisibility(View.VISIBLE);
                ivSelectAll.setVisibility(View.VISIBLE);
                actionAdapter.setSelect(select);
            }else{
/*                tempDataList.clear();
                rlPlayCtrl.setVisibility(View.VISIBLE);
                rlPlaybtn.setVisibility(View.INVISIBLE);
                select = false;
                tvSelect.setText(SkinManager.getInstance().getTextById(R.string.select_list));
                tvSelect.setTextColor(getResources().getColor(R.color.text_blue_color));
                tvSelectAll.setVisibility(View.INVISIBLE);
                ivSelectAll.setVisibility(View.INVISIBLE);
                actionAdapter.setSelect(select);*/
                resetSelectAllState();
            }
            actionAdapter.notifyDataSetChanged();

        }else if(id == R.id.iv_select_all){
            if(selectAll){
                selectAll = false;
                ivSelectAll.setImageResource(R.drawable.ic_row_select);
                tempDataList.clear();
            }else{
                selectAll = true;
                ivSelectAll.setImageResource(R.drawable.ic_row_selected);
                tempDataList.clear();
                tempDataList.addAll(actionDataList);
            }
            handSelectAllData(selectAll);
            notifyPlayBtnState();
//            actionAdapter.setSelectAll(selectAll);
            actionAdapter.notifyDataSetChanged();
            //TODO add actionList  to cycle playlist
        }else if(id == R.id.iv_playlist){
            rlPlaylist.setVisibility(View.VISIBLE);
            cycleDataList.clear();
            cycleDataList.addAll(PlayActionManger.getInstance().getActionCycleList());
            ViseLog.d("cycleDataList:" + cycleDataList.size());
            cyclePlayActionAdapter.notifyDataSetChanged();
        }else if(id == R.id.iv_reset) {
            mPresenter.stopAction();
            PlayActionManger.getInstance().setCycle(false);
        }else if(id == R.id.iv_play_pause){
            mPresenter.playPauseAction();
        }else if(id == R.id.iv_close_list){
            rlPlaylist.setVisibility(View.GONE);
        }else if(id == R.id.iv_delete_list){
            PlayActionManger.getInstance().getActionCycleList().clear();
            cycleDataList.clear();
            cyclePlayActionAdapter.notifyDataSetChanged();
        }else if(id == R.id.rl_play_btn){
            ViseLog.d( "---temp:" + tempDataList.size());
            if(tempDataList.size()>0){
//                PlayActionManger.getInstance().getActionCycleList().clear();
                PlayActionManger.getInstance().setActionCycleList(tempDataList);
            }
            ViseLog.d("cyclesize:" + PlayActionManger.getInstance().getActionCycleList().size());
            if(PlayActionManger.getInstance().getActionCycleList().size()>0){
                mPresenter.playAction(PlayActionManger.getInstance().getActionCycleList().get(PlayActionManger.getInstance().getCurrentCyclePos()).getActionName());
//                cyclePlay = true;
                PlayActionManger.getInstance().setCycle(true);
                rlPlaybtn.setVisibility(View.INVISIBLE);
                rlPlayCtrl.setVisibility(View.VISIBLE);
                resetSelectAllState();
            }
        }
    }


    private void handSelectAllData(boolean selectAll){
        if(selectAll){
            for(int i = 0; i<actionDataList.size(); i++){
                actionDataList.get(i).setSelect(true);
            }
        }else{
            for(int i = 0; i<actionDataList.size(); i++){
                actionDataList.get(i).setSelect(false);
            }
        }
    }

    private void resetSelectAllState(){
        tempDataList.clear();
        rlPlayCtrl.setVisibility(View.VISIBLE);
        rlPlaybtn.setVisibility(View.INVISIBLE);
        select = false;
        tvSelect.setText(SkinManager.getInstance().getTextById(R.string.playlist_select));
        tvSelect.setTextColor(getResources().getColor(R.color.text_blue_color));
        tvSelectAll.setVisibility(View.INVISIBLE);
        ivSelectAll.setVisibility(View.INVISIBLE);
        actionAdapter.setSelect(select);
        selectAll = false;
        handSelectAllData(selectAll);
        actionAdapter.notifyDataSetChanged();
    }

    private void notifyPlayBtnState(){
        if(/*PlayActionManger.getInstance().getActionCycleList().size()*/tempDataList.size() >0){
           rlPlaybtn.setEnabled(true);
           ivPlayBtn.setEnabled(true);
        }else{
            rlPlaybtn.setEnabled(false);
            ivPlayBtn.setEnabled(false);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_play_action;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ToastUtils.showShort("item onItemClick");
        ActionData actionData = (ActionData) adapter.getItem(position);
        if(select){
            if(actionData.isSelect()){
                actionData.setSelect(false);
                tempDataList.remove(actionData);
            }else{
                actionData.setSelect(true);
                tempDataList.add(actionData);
            }
            actionAdapter.notifyItemChanged(position);

        }else{
            String actionName = actionData.getActionName();
            if(!TextUtils.isEmpty(actionName) && !PlayActionManger.getInstance().isCycle()){
                ViseLog.d("onItemClick:" + actionName);
                mPresenter.playAction(actionName);
            }
        }


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
        BaseLoadingDialog.dismiss(this);
    }

    @Override
    public void notePlayFinish(String actionName) {
        ViseLog.d("notePlayFinish:" + cyclePlay);
        if(cyclePlay){

        }else{
            if(!TextUtils.isEmpty(actionName) && tvPlayName != null){
                tvPlayName.setText("");
                gifPlaying.setVisibility(View.INVISIBLE);
                ivPlay.setVisibility(View.VISIBLE);
            }
        }
        notePlayOrPause();
        actionAdapter.setPlayActionName("");
        actionAdapter.notifyDataSetChanged();

    }

    @Override
    public void notePlayStart(String actionName) {
        if(!TextUtils.isEmpty(actionName) && tvPlayName != null){
            tvPlayName.setText(actionName);
        }
        ivPlay.setVisibility(View.INVISIBLE);
        gifPlaying.setVisibility(View.VISIBLE);
        ivPlayPause.setImageResource(R.drawable.ic_pause);
        actionAdapter.setPlayActionName(actionName);
        actionAdapter.notifyDataSetChanged();

    }

    @Override
    public void notePlayOrPause() {
        int playstate = PlayActionManger.getInstance().getPlayState();
        ViseLog.d("playstate:" + playstate);
        if(playstate == PlayActionManger.PAUSE || playstate == PlayActionManger.STOP){
            ivPlayPause.setImageResource(R.drawable.ic_play);
            gifPlaying.setVisibility(View.INVISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
        }else if(playstate == PlayActionManger.PLAYING){
            ivPlayPause.setImageResource(R.drawable.ic_pause);
            gifPlaying.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.INVISIBLE);
        }
        tvPlayName.setText( PlayActionManger.getInstance().getCurrentPlayActionName());
        actionAdapter.notifyDataSetChanged();

    }

    @Override
    public void notePlayStop() {
        ivPlayPause.setImageResource(R.drawable.ic_play);
        gifPlaying.setVisibility(View.INVISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        if(tvPlayName != null){
            tvPlayName.setText("");
        }
        actionAdapter.notifyDataSetChanged();
    }
}
