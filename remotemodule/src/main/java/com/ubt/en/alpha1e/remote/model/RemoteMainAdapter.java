package com.ubt.en.alpha1e.remote.model;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.en.alpha1e.action.R;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/5/5 14:08
 * @modifier：ubt
 * @modify_date：2018/5/5 14:08
 * [A brief description]
 * version
 */

public class RemoteMainAdapter extends BaseQuickAdapter<RemoteRoleInfo, BaseViewHolder> {
    public RemoteMainAdapter(int layoutResId, @Nullable List<RemoteRoleInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteRoleInfo item) {
        ImageView ivRemote = helper.getView(R.id.iv_remote);
        ImageView ivLock = helper.getView(R.id.iv_lock);
        TextView tvName = helper.getView(R.id.tv_remote_name);
        ivRemote.setImageResource(item.getRoleIcon());
        if (item.getBz() == 1) {
            ivRemote.setAlpha(0.7f);
            ivLock.setVisibility(View.VISIBLE);
            tvName.setTextColor(mContext.getResources().getColorStateList(R.color.base_tv_ble_gray));
        } else {
            ivRemote.setAlpha(1f);
            ivLock.setVisibility(View.GONE);
            tvName.setTextColor(mContext.getResources().getColorStateList(R.color.black));
        }

        tvName.setText(item.getRoleName());

    }
}
