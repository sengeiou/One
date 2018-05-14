package com.ubt.en.alpha1e.remote.model;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.en.alpha1e.remote.R;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/5/5 14:08
 * @modifier：ubt
 * @modify_date：2018/5/5 14:08
 * [A brief description]
 * version
 */

public class RemoteGridAdapter extends BaseQuickAdapter<RemoteItem, BaseViewHolder> {
    public RemoteGridAdapter(int layoutResId, @Nullable List<RemoteItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteItem item) {
        ImageView ivRemote = helper.getView(R.id.iv_right);

        ivRemote.setImageResource(item.getDrawableId());


    }
}
