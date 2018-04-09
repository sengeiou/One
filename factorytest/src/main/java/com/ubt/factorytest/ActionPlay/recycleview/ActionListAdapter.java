package com.ubt.factorytest.ActionPlay.recycleview;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ubt.factorytest.R;

import java.util.List;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2017/11/21 11:38
 * @描述:
 */

public class ActionListAdapter extends BaseQuickAdapter<ActionBean, BaseViewHolder> {

    public ActionListAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, ActionBean item) {
        helper.setText(R.id.tv_action_name, item.getActionName());
    }

}
