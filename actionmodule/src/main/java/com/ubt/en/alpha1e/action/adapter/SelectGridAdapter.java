package com.ubt.en.alpha1e.action.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.model.ActionTypeModel;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/4/26 20:22
 * @modifier：ubt
 * @modify_date：2018/4/26 20:22
 * [A brief description]
 * version
 */

public class SelectGridAdapter extends BaseQuickAdapter<ActionTypeModel, BaseViewHolder> {


    private boolean isShow;

    public void setisShowArrow(boolean isShow) {
        this.isShow = isShow;
        notifyDataSetChanged();
    }

    public SelectGridAdapter(int layoutResId, @Nullable List<ActionTypeModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionTypeModel item) {
        helper.setText(R.id.txt_action_select_type, item.getActionName());
        helper.setImageResource(R.id.iv_action, item.getDrawableId());
        ImageView ivSelected = helper.getView(R.id.img_select_item);
        if (item.isSelected()) {
            ivSelected.setImageResource(R.drawable.mynew_publish_choose);
            ivSelected.setVisibility(View.VISIBLE);
        } else {
            ivSelected.setVisibility(View.GONE);
        }

        ImageView ivArrow = helper.getView(R.id.iv_show_arrow);
        if (isShow && item.getActionName().equals(SkinManager.getInstance().getTextById(R.string.ui_square_sport))) {
            ivArrow.setVisibility(View.VISIBLE);
        } else {
            ivArrow.setVisibility(View.GONE);
        }
    }
}
