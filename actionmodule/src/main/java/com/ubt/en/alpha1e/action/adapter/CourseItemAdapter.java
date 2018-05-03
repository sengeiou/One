package com.ubt.en.alpha1e.action.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.model.CourseActionModel;

import java.util.List;

/**
 * @author：liuhai
 * @date：2018/5/2 20:15
 * @modifier：ubt
 * @modify_date：2018/5/2 20:15
 * [A brief description]
 * version
 */

public class CourseItemAdapter extends BaseQuickAdapter<CourseActionModel, BaseViewHolder> {

    public CourseItemAdapter(@LayoutRes int layoutResId, @Nullable List<CourseActionModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseActionModel item) {
        helper.setText(R.id.tv_action_course_item, item.getCourseName());
        ImageView ivDone = helper.getView(R.id.iv_done);
        ivDone.setVisibility(item.getStatu() == 1 ? View.VISIBLE : View.INVISIBLE);
        TextView textView = (TextView) helper.getView(R.id.tv_action_course_item);
        if (item.getStatu() == 1) {
            textView.setTextColor(mContext.getResources().getColor(R.color.tv_user_edit_color));
        } else if (item.getStatu() == 2) {
            textView.setTextColor(mContext.getResources().getColor(R.color.base_blue));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.base_tv_ble_gray));
        }

    }
}
