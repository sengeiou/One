package com.ubt.playaction.play;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.customView.BaseLoadingDialog;
import com.ubt.baselib.model1E.PlayEvent;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.playaction.R;
import com.ubt.playaction.R2;
import com.ubt.playaction.model.ActionData;
import com.ubt.playaction.model.PlayConstant;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R2.id.rl_20_tip)
    RelativeLayout rl20Tip;
    @BindView(R2.id.iv_20_tip)
    ImageView iv20Tip;
    @BindView(R2.id.rl_root)
    RelativeLayout mRootRl;
    @BindView(R2.id.rl_playing)
    RelativeLayout rlPlaying;
    Unbinder unbinder;

    CyclePlayActionAdapter cyclePlayActionAdapter;

    PlayActionAdapter actionAdapter;
    List<ActionData> actionDataList = new ArrayList<ActionData>();
    List<ActionData> cycleDataList = new ArrayList<ActionData>();
    List<ActionData> tempDataList = new ArrayList<ActionData>();
    boolean select =false;
    boolean selectAll = false;
    boolean cyclePlay = false;

    private int MSG_CODE = 7;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_CODE){
                mPresenter.playAction((String)msg.obj);
            }
        }
    };


    private long onClickTime; //用于防止用户快速点击


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViseLog.d("onCreate");
        unbinder = ButterKnife.bind(this);
        mPresenter.register(this);
        if(BlueClientUtil.getInstance().getConnectionState() == 3){ //判断蓝牙是否连接
            BaseLoadingDialog.show(this);
            mPresenter.getActionList();
        }
        init();
        EventBus.getDefault().post(new PlayEvent(PlayEvent.Event.PLAYMANAGER_STOP));
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

        cyclePlayActionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

                ActionData actionData = (ActionData) adapter.getItem(position);
                String actionName = actionData.getActionName();
                int mode = SPUtils.getInstance().getInt(PlayConstant.SP_PLAY_MODE, PlayConstant.SP_PLAY_MODE_ORDER);
                ViseLog.d("cyclePlayActionAdapter onItemClick:" + actionName + "--getCurrentPlayActionName:" + PlayActionManger.getInstance().getCurrentPlayActionName() + "--mode:" + mode);


                if(!TextUtils.isEmpty(actionName)) {
                    ViseLog.d("onItemClick:" + actionName);
                    mPresenter.stopAction();
                    Message msg = new Message();
                    msg.what = MSG_CODE;
                    msg.obj = actionName;
                    mHandler.sendMessageDelayed(msg, 400);
//                    mPresenter.playAction(actionName);

                }



            }
        });

        cyclePlayActionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ActionData actionData = (ActionData) adapter.getItem(position);
                final String actionName = actionData.getActionName();
                final int mode = SPUtils.getInstance().getInt(PlayConstant.SP_PLAY_MODE, PlayConstant.SP_PLAY_MODE_ORDER);
                ViseLog.d("cyclePlayActionAdapter OnItemChildClick:" + actionName + "--getCurrentPlayActionName:" + PlayActionManger.getInstance().getCurrentPlayActionName() + "--mode:" + mode);
                if(actionName.equals(PlayActionManger.getInstance().getCurrentPlayActionName())){

                    ViseLog.d("cyclePlayActionAdapter OnItemChildClick mode:" + mode);
                    if(mode == PlayConstant.SP_PLAY_MODE_ORDER || mode == PlayConstant.SP_PLAY_MODE_SINGLE){
                        if(mPresenter != null){
                            mPresenter.stopAction();
                        }
                    }else if(mode == PlayConstant.SP_PLAY_MODE_LSIT /*|| mode == PlayConstant.SP_PLAY_MODE_SINGLE*/){
                        if(PlayActionManger.getInstance().getActionCycleList().size()>1){
                            if(mPresenter != null){
                                if((position+1) == cycleDataList.size()){
                                    ViseLog.d("play name pos size");
                                    mPresenter.playAction(cycleDataList.get(position).getActionName());
                                }else{
                                    ViseLog.d("play name:" + cycleDataList.get(position+1).getActionName());
                                    mPresenter.playAction(cycleDataList.get(position+1).getActionName());
                                }

                            }
                        }else{
                            if(mPresenter != null){
                                mPresenter.stopAction();
                            }
                        }

                    }
                }


                PlayActionManger.getInstance().getActionCycleList().remove(cycleDataList.get(position));
                cycleDataList.remove(position);
                cyclePlayActionAdapter.notifyDataSetChanged();


            }

        });

 /*       if(BlueClientUtil.getInstance().getConnectionState() != BluetoothState.STATE_CONNECTED){
            if (!BaseBTDisconnectDialog.getInstance().isShowing()) {
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
        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        notePlayOrPause();
        onResumePlayMode();

        if(SPUtils.getInstance().getInt(PlayConstant.SP_SHOW_20_TIP, 0)==PlayConstant.TIP_SHOW){
            rl20Tip.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R2.id.play_iv_back,R2.id.tv_select,R2.id.iv_playlist, R2.id.iv_reset, R2.id.iv_play_pause,R2.id.iv_close_list,R2.id.iv_delete_list,R2.id.iv_select_all,R2.id.rl_play_btn, R2.id.iv_20_tip,R2.id.rl_list, R2.id.iv_cycle,R2.id.iv_list_cycle})
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.play_iv_back){
            finish();
        }else if(id == R.id.tv_select){
            if(!select){
                rlPlayCtrl.setVisibility(View.INVISIBLE);
                rlPlaybtn.setVisibility(View.VISIBLE);
                select = true;
                tvSelect.setText(SkinManager.getInstance().getTextById(R.string.base_cancel));
                tvSelect.setTextColor(getResources().getColor(R.color.text_playlist_title));
                tvSelectAll.setVisibility(View.VISIBLE);
                ivSelectAll.setVisibility(View.VISIBLE);
                actionAdapter.setSelect(select);
            }else{
                resetSelectAllState();
            }
            notifyPlayBtnState();
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
            actionAdapter.notifyDataSetChanged();
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
            mPresenter.stopAction();
            PlayActionManger.getInstance().getActionCycleList().clear();
            cycleDataList.clear();
            cyclePlayActionAdapter.notifyDataSetChanged();
        }else if(id == R.id.rl_play_btn){
            showDialog();
        }else if(id == R.id.iv_20_tip){
            SPUtils.getInstance().put(PlayConstant.SP_SHOW_20_TIP, PlayConstant.TIP_CLOSE);
            rl20Tip.setVisibility(View.GONE);
        }else if(id == R.id.rl_list){
            rlPlaylist.setVisibility(View.GONE);
        }else if(id == R.id.iv_list_cycle){
            changePlayMode();
        }else if(id ==  R.id.iv_cycle){
            changePlayMode();
        }
    }

    private void onResumePlayMode(){
        int mode = SPUtils.getInstance().getInt(PlayConstant.SP_PLAY_MODE,PlayConstant.SP_PLAY_MODE_ORDER);
        if(mode == PlayConstant.SP_PLAY_MODE_ORDER){
            ivCycleState.setImageResource(R.drawable.ic_circle_list);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_list);

        }else if(mode == PlayConstant.SP_PLAY_MODE_LSIT) {
            ivCycleState.setImageResource(R.drawable.ic_circle_listloop);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_listloop);
        }else if(mode == PlayConstant.SP_PLAY_MODE_SINGLE){
            ivCycleState.setImageResource(R.drawable.ic_circle_single);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_single);
        }
    }

    private void changePlayMode(){
        int mode = SPUtils.getInstance().getInt(PlayConstant.SP_PLAY_MODE,PlayConstant.SP_PLAY_MODE_ORDER);
        if(mode == PlayConstant.SP_PLAY_MODE_ORDER){
            SPUtils.getInstance().put(PlayConstant.SP_PLAY_MODE, PlayConstant.SP_PLAY_MODE_LSIT);
            ivCycleState.setImageResource(R.drawable.ic_circle_listloop);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_listloop);
            Toast.makeText(PlayActionActivity.this, "List", Toast.LENGTH_SHORT).show();
            if(SPUtils.getInstance().getInt(PlayConstant.SP_SHOW_20_TIP, 0) == 0 /*&& PlayActionManger.getInstance().getPlayState() == PlayActionManger.PLAYING*/){
                rl20Tip.setVisibility(View.VISIBLE);
                SPUtils.getInstance().put(PlayConstant.SP_SHOW_20_TIP, PlayConstant.TIP_SHOW);
            }

        }else if(mode == PlayConstant.SP_PLAY_MODE_LSIT) {
            SPUtils.getInstance().put(PlayConstant.SP_PLAY_MODE, PlayConstant.SP_PLAY_MODE_SINGLE);
            ivCycleState.setImageResource(R.drawable.ic_circle_single);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_single);
            Toast.makeText(PlayActionActivity.this, "Single", Toast.LENGTH_SHORT).show();
            if(SPUtils.getInstance().getInt(PlayConstant.SP_SHOW_20_TIP, 0) == 0 && PlayActionManger.getInstance().getPlayState() == PlayActionManger.PLAYING){
                rl20Tip.setVisibility(View.VISIBLE);
                SPUtils.getInstance().put(PlayConstant.SP_SHOW_20_TIP, PlayConstant.TIP_SHOW);
            }
        }else if(mode == PlayConstant.SP_PLAY_MODE_SINGLE){
            SPUtils.getInstance().put(PlayConstant.SP_PLAY_MODE, PlayConstant.SP_PLAY_MODE_ORDER);
            ivCycleState.setImageResource(R.drawable.ic_circle_list);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_list);
            Toast.makeText(PlayActionActivity.this, "Order", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        new BaseDialog.Builder(this)
                .setMessage(R.string.playlist_play_dialogue).
                setConfirmButtonId(R.string.base_confirm)
                .setCancleButtonID(R.string.base_cancel)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == com.ubt.baselib.R.id.button_confirm){
                            ViseLog.d( "---temp:" + tempDataList.size());
                            if(tempDataList.size()>0){
                                PlayActionManger.getInstance().setActionCycleList(tempDataList);
                            }
                            ViseLog.d("cyclesize:" + PlayActionManger.getInstance().getActionCycleList().size());
                            if(PlayActionManger.getInstance().getActionCycleList().size()>0){
                                PlayActionManger.getInstance().setCurrentCyclePos(0);
                                mPresenter.playAction(PlayActionManger.getInstance().getActionCycleList().get(PlayActionManger.getInstance().getCurrentCyclePos()).getActionName());
//                                PlayActionManger.getInstance().setCycle(true);
                                rlPlaybtn.setVisibility(View.INVISIBLE);
                                rlPlayCtrl.setVisibility(View.VISIBLE);
                                resetSelectAllState();
//                                rl20Tip.setVisibility(View.VISIBLE);
                            }
                            dialog.dismiss();
                        } else if (view.getId() == com.ubt.baselib.R.id.button_cancle) {

                            dialog.dismiss();
                        }

                    }
                }).create().show();
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
        ivSelectAll.setImageResource(R.drawable.ic_row_select);
        handSelectAllData(selectAll);
        actionAdapter.notifyDataSetChanged();
    }

    private void notifyPlayBtnState(){
        if(tempDataList.size() >0){
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

        if ((System.currentTimeMillis() - onClickTime) < 800) {
            return;
        }
        onClickTime = System.currentTimeMillis();

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
            updateSelectAllState();

        }else{
            String actionName = actionData.getActionName();
            if(!TextUtils.isEmpty(actionName)/* && !PlayActionManger.getInstance().isCycle()*/){
                ViseLog.d("DV_STOPPLAY onItemClick:" + actionName);
//                ImageView imageView = view.findViewById(R.id.iv_action_icon);
//                addGoodToCar(imageView);
                PlayActionManger.getInstance().addActionCycleList(actionData);
                mPresenter.stopAction();
                ViseLog.d("notePlay stop");
                Message msg = new Message();
                msg.what = MSG_CODE;
                msg.obj = actionName;
                mHandler.sendMessageDelayed(msg, 400);
//                mPresenter.playAction(actionName);
            }
        }

        notifyPlayBtnState();


    }

    private void updateSelectAllState() {
        if(tempDataList.size() == actionDataList.size()){
            selectAll = true;
            ivSelectAll.setImageResource(R.drawable.ic_row_selected);
        }else{
            selectAll = false;
            ivSelectAll.setImageResource(R.drawable.ic_row_select);
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
        ViseLog.d("notePlayFinish:" + cyclePlay + actionName);

            if(!TextUtils.isEmpty(actionName) && tvPlayName != null){
                tvPlayName.setText(SkinManager.getInstance().getTextById(R.string.playlist_standby));
                gifPlaying.setVisibility(View.INVISIBLE);
                ivPlay.setVisibility(View.VISIBLE);
            }

        notePlayOrPause();
        actionAdapter.setPlayActionName("");
        actionAdapter.notifyDataSetChanged();
        cyclePlayActionAdapter.notifyDataSetChanged();

    }

    @Override
    public void notePlayStart(String actionName) {
        ViseLog.d("DV_STOPPLAY notePlayStart:" + actionName);
        if(!TextUtils.isEmpty(actionName) && tvPlayName != null){
            tvPlayName.setText(actionName);
            tvPlayName.setTextColor(getResources().getColor(R.color.text_blue_color));
        }
        ivPlay.setVisibility(View.INVISIBLE);
        gifPlaying.setVisibility(View.VISIBLE);
        rlPlaying.setBackgroundResource(R.drawable.play_name_rect_playing);
        ivPlayPause.setImageResource(R.drawable.ic_pause);
        actionAdapter.setPlayActionName(actionName);
        actionAdapter.notifyDataSetChanged();
        cyclePlayActionAdapter.notifyDataSetChanged();

    }

    @Override
    public void notePlayOrPause() {
        int playstate = PlayActionManger.getInstance().getPlayState();
        ViseLog.d("playstate:" + playstate);
        if(playstate == PlayActionManger.PAUSE || playstate == PlayActionManger.STOP){
            ivPlayPause.setImageResource(R.drawable.ic_play);
            gifPlaying.setVisibility(View.INVISIBLE);
            ivPlay.setVisibility(View.VISIBLE);
            rlPlaying.setBackgroundResource(R.drawable.play_name_rect);
            tvPlayName.setTextColor(getResources().getColor(R.color.text_playlist_title));
        }else if(playstate == PlayActionManger.PLAYING){
            ivPlayPause.setImageResource(R.drawable.ic_pause);
            gifPlaying.setVisibility(View.VISIBLE);
            ivPlay.setVisibility(View.INVISIBLE);
            rlPlaying.setBackgroundResource(R.drawable.play_name_rect_playing);
            tvPlayName.setTextColor(getResources().getColor(R.color.text_blue_color));
        }

        if(playstate == PlayActionManger.STOP){
            tvPlayName.setText(SkinManager.getInstance().getTextById(R.string.playlist_standby));
            tvPlayName.setTextColor(getResources().getColor(R.color.text_playlist_title));
        }else{
            tvPlayName.setText( PlayActionManger.getInstance().getCurrentPlayActionName());
            if(playstate == PlayActionManger.PLAYING){
                tvPlayName.setTextColor(getResources().getColor(R.color.text_blue_color));
            }else{
                tvPlayName.setTextColor(getResources().getColor(R.color.text_playlist_title));
            }

        }

        actionAdapter.notifyDataSetChanged();

    }

    private void updateCycleModeState(int playstate){
        if(playstate == PlayActionManger.STOP){
            ivCycleState.setImageResource(R.drawable.ic_circle_list);
            ivPlayListCycle.setImageResource(R.drawable.ic_circle_list);
        }else{
            if(PlayActionManger.getInstance().isCycle()){
                if(PlayActionManger.getInstance().getActionCycleList().size()>1){
                    ivCycleState.setImageResource(R.drawable.ic_circle_listloop);
                    ivPlayListCycle.setImageResource(R.drawable.ic_circle_listloop);
                }else{
                    ivCycleState.setImageResource(R.drawable.ic_circle_single);
                    ivPlayListCycle.setImageResource(R.drawable.ic_circle_single);

                }
            }else{
                ivCycleState.setImageResource(R.drawable.ic_circle_list);
                ivPlayListCycle.setImageResource(R.drawable.ic_circle_list);
            }

        }

    }

    @Override
    public void notePlayStop() {
        ViseLog.d("notePlayStop");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivPlayPause.setImageResource(R.drawable.ic_play);
                gifPlaying.setVisibility(View.INVISIBLE);
                ivPlay.setVisibility(View.VISIBLE);
                rlPlaying.setBackgroundResource(R.drawable.play_name_rect);
                if(tvPlayName != null){
                    tvPlayName.setText(SkinManager.getInstance().getTextById(R.string.playlist_standby));
                    tvPlayName.setTextColor(getResources().getColor(R.color.text_playlist_title));
                }
                actionAdapter.notifyDataSetChanged();
            }
        });

//        rl20Tip.setVisibility(View.GONE);
    }

    @Override
    public void noteTapHead() {
        Toast.makeText(PlayActionActivity.this, "stop play!", Toast.LENGTH_SHORT).show();
    }


    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private void addGoodToCar(ImageView imageView){
        final ImageView view = new ImageView(PlayActionActivity.this);
        view.setImageDrawable(imageView.getDrawable());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageView.getWidth(), imageView.getHeight());
        mRootRl.addView(view, layoutParams);

        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        mRootRl.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        imageView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        ivPlayList.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + imageView.getWidth()/2;
        float startY = startLoc[1] - parentLoc[1] + imageView.getHeight()/2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + ivPlayList.getWidth() / 5;
        float toY = endLoc[1] - parentLoc[1];

        //开始绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        mPathMeasure = new PathMeasure(path, false);

        //属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                view.setTranslationX(mCurrentPosition[0]);
                view.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                mRootRl.removeView(view);

                //shopImg 开始一个放大动画
//                Animation scaleAnim = AnimationUtils.loadAnimation(GoodsListActivity.this, R.anim.shop_car_scale);
//                mCarImageView.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

}
