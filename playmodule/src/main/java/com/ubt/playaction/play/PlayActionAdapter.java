package com.ubt.playaction.play;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.playaction.R;
import com.ubt.playaction.model.ActionData;
import com.vise.log.ViseLog;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class PlayActionAdapter extends BaseQuickAdapter<ActionData, BaseViewHolder> {

    private Context context;
    private boolean select = false;
    private boolean selectAll = false;
    private String playActionName="";

    public PlayActionAdapter(@LayoutRes int layoutResId, @Nullable List<ActionData> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, ActionData item) {

        ViseLog.d("item:" + item.toString());
        ImageView iv_action_icon = helper.getView(R.id.iv_action_icon);
        GifImageView gifImageView = helper.getView(R.id.iv_play_state);
        ImageView ivSelectState = helper.getView(R.id.iv_select_state);
        TextView tvActionName = helper.getView(R.id.tv_action_name);
        TextView tvActionTime = helper.getView(R.id.tv_action_time);
        ImageView ivWhite = helper.getView(R.id.iv_white);

        if(select){
            ivSelectState.setVisibility(View.VISIBLE);
        }else{
            ivSelectState.setVisibility(View.INVISIBLE);
        }

    /*    if(selectAll){
            ivSelectState.setImageResource(R.drawable.ic_pic_selected);
        }else{
            ivSelectState.setImageResource(R.drawable.ic_pic_disselect);
        }*/


        if(PlayActionManger.getInstance().getCurrentPlayActionName().equals(item.getActionName()) && PlayActionManger.getInstance().getPlayState() == PlayActionManger.PLAYING){
            gifImageView.setVisibility(View.VISIBLE);
            ivWhite.setVisibility(View.VISIBLE);
        }else{
            gifImageView.setVisibility(View.INVISIBLE);
            ivWhite.setVisibility(View.INVISIBLE);
        }

        if(item.isSelect()){
            ivSelectState.setImageResource(R.drawable.ic_pic_selected);
        }else{
            ivSelectState.setImageResource(R.drawable.ic_pic_disselect);
        }


        iv_action_icon.setImageResource(item.getActionIcon());
        tvActionName.setText(item.getActionName());
        tvActionTime.setText(item.getActionTime());
    }

    public void setSelect(boolean select){
        this.select = select;
    }
    public void setSelectAll(boolean selectAll){
        this.selectAll = selectAll;
    }

    public void setPlayActionName(String actionName){
        playActionName = actionName;
    }



}
