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


public class CyclePlayActionAdapter extends BaseQuickAdapter<ActionData, BaseViewHolder> {

    private Context context;


    public CyclePlayActionAdapter(@LayoutRes int layoutResId, @Nullable List<ActionData> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, ActionData item) {

        ViseLog.d("item:" + item.toString());

        GifImageView gifPlaying = helper.getView(R.id.iv_play_state);
        TextView tvActionName = helper.getView(R.id.tv_action_name);
        ImageView ivDelete = helper.getView(R.id.iv_delete_item);

        if(PlayActionManger.getInstance().getCurrentPlayActionName().equals(item.getActionName())  && PlayActionManger.getInstance().getPlayState() == PlayActionManger.PLAYING){
            gifPlaying.setVisibility(View.VISIBLE);
            tvActionName.setTextColor(context.getResources().getColor(R.color.text_blue_color));
        }else{
            gifPlaying.setVisibility(View.INVISIBLE);
            tvActionName.setTextColor(context.getResources().getColor(R.color.text_playlist_title));
        }

        tvActionName.setText(item.getActionName());



    }





}
